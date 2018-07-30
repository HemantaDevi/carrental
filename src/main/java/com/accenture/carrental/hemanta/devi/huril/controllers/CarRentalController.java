package com.accenture.carrental.hemanta.devi.huril.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.accenture.carrental.hemanta.devi.huril.api.dtos.CarDTO;
import com.accenture.carrental.hemanta.devi.huril.api.dtos.CarRentalDto;
import com.accenture.carrental.hemanta.devi.huril.entities.Car;
import com.accenture.carrental.hemanta.devi.huril.entities.CarRental;
import com.accenture.carrental.hemanta.devi.huril.entities.User;
import com.accenture.carrental.hemanta.devi.huril.exceptions.CarRentalEntryNotFoundException;
import com.accenture.carrental.hemanta.devi.huril.exceptions.CarNotReturnedException;
import com.accenture.carrental.hemanta.devi.huril.services.CarService;
import com.accenture.carrental.hemanta.devi.huril.services.FileSystemStorageService;
import com.accenture.carrental.hemanta.devi.huril.services.RentalService;
import com.accenture.carrental.hemanta.devi.huril.services.UserService;

@Controller
public class CarRentalController {
	
	private UserService userService;
	private RentalService rentalService;
	private CarService carService;
	private final Path path = Paths.get("upload-dir");
	private final FileSystemStorageService storageService;

	public CarRentalController(UserService userService, RentalService rentalService, CarService carService,
			FileSystemStorageService storageService) {
		this.userService = userService;
		this.rentalService = rentalService;
		this.carService = carService;
		this.storageService = storageService;
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 * Total Amount for Returned Rented Cars
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/amount")
	public String amountRented(Model model) {
		List<Car> cars = carService.ListAllCars();
		List<CarDTO> dto = new ArrayList<>();
		for (Car car : cars) {
			dto.add(new CarDTO(car.getRegistrationNumber(), car.getModel(), car.getPricePerDay(), car.getYear(),
					rentalService.amountForCar(car.getRegistrationNumber())));
		}
		model.addAttribute("cars", dto);
		model.addAttribute("total", rentalService.totalAmount());
		return "amountRented";

	}
	
	
	/**
	 * 
	 * @param model
	 * @return
	 * Import a CSV containing Car Details
	 */
	  @Secured("ROLE_ADMIN")
	  @GetMapping("/importcsv")
		public String importCsv(Model model) {
		  model.addAttribute("error", "");
			return "importCsv";

		}
	  
	  
	  @Secured("ROLE_ADMIN")
	 @PostMapping("/importCsv")
	 public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request) {
		 storageService.init(); 
		 storageService.store(file);
		try {
			carService.importCSVforCars(path + "//" + file.getOriginalFilename());
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "importCsv";
		}
		List<Car> cars =carService.ListAllCars();
		model.addAttribute("cars", cars);
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		return "allCars";
	}
	 
	 /**
	  * 
	  * @param model
	  * @return
	  * List of Rented Cars
	  */
	  @Secured("ROLE_ADMIN")
	 @GetMapping("/rentedBy")
		public String redirectToRented(Model model) {
		 List<User> users = userService.findAllUsers();
		 if(users.isEmpty())
		 {
			 users= new ArrayList<>();
		 }
			model.addAttribute("users", users);
			return "rentedBy";

		}
	 @Secured("ROLE_ADMIN")
	 @PostMapping("/rentedBy")
	 public String rentedBy(@RequestParam String nationalId, Model model,HttpServletRequest request) {
		 model.addAttribute("cars", rentalService.carRentedByCustomer(nationalId));
		 String hostNport = request.getServerName() + ":" + request.getServerPort();
		 model.addAttribute("hostNport", hostNport);
			return "allCars";
	}
	 
	 /**
	  * 
	  * @param model
	  * @return
	  * Release the car when the customer returned it
	  */
	 @Secured("ROLE_ADMIN")
	 @GetMapping("/returned")
	 public String releaseCar(Model model) {
		 List<CarRentalDto> list=rentalService.getAllRentalCars();
		 model.addAttribute("availabilityList", list);
		return "allRentedCars";
	 }
	 
	 @Secured("ROLE_ADMIN")
	 @GetMapping("/release/{registrationNumber}")
	 public String release(Model model,@PathVariable String registrationNumber) throws CarRentalEntryNotFoundException {
	 rentalService.releaseCar(registrationNumber);
	 List<CarRentalDto> list=rentalService.getAllRentalCars();
	 model.addAttribute("availabilityList", list);
	 return "allRentedCars";
	} 
	 
	 /**
	  * 
	  * @param model
	  * @return
	  * Renting a Car
	  */
	 @Secured("ROLE_CUSTOMER")
	 @GetMapping("/rentCarCustomer")
	 public String customerRentCar(Model model) {
		 List<Car> list=carService.findByAvailability();
		 model.addAttribute("availabilityList", list);
		 return "rentCarCustomer";
		 
	 }
	
	 @Secured("ROLE_CUSTOMER")
	 @GetMapping("/rentingCar/{registrationNumber}")
	 public String customerRentCarAvailable(Model model,@PathVariable("registrationNumber")String registrationNumber) {
		try {
			rentalService.customerRentingCar(registrationNumber);
		} catch (CarNotReturnedException e) {
			model.addAttribute("errorMsg", e.getMessage());
		} 
		List<Car> list=carService.findByAvailability();
		 model.addAttribute("availabilityList", list);
		 return "rentCarCustomer"; 
	 }
	 
	 /**
	  * 
	  * @param model
	  * @return
	  * Total Amount of Cars Rented by a specific Customer
	  */
	 @Secured("ROLE_CUSTOMER")
	 @GetMapping("/amountCars")
	 public String amountCarsRentedByCustomer(Model model) {
		List<CarRental> carRentals = rentalService.getCarRentalsForSpecificUserByNationalId();
		model.addAttribute("carRentals", carRentals);
		return "amountRentedByCustomer";
	 }
}

