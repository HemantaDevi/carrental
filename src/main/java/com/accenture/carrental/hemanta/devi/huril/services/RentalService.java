package com.accenture.carrental.hemanta.devi.huril.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.accenture.carrental.hemanta.devi.huril.api.dtos.CarRentalDto;
import com.accenture.carrental.hemanta.devi.huril.entities.Car;
import com.accenture.carrental.hemanta.devi.huril.entities.CarRental;
import com.accenture.carrental.hemanta.devi.huril.entities.User;
import com.accenture.carrental.hemanta.devi.huril.exceptions.CarRentalEntryNotFoundException;
import com.accenture.carrental.hemanta.devi.huril.exceptions.CarNotReturnedException;
import com.accenture.carrental.hemanta.devi.huril.repositories.CarRentalRepositories;
import com.accenture.carrental.hemanta.devi.huril.repositories.CarRepository;
import com.accenture.carrental.hemanta.devi.huril.repositories.UserRepository;

@Transactional
@Service
public class RentalService {
	private final CarRentalRepositories carRentalRepositories;
	private final UserRepository userRepository;
	private final CarRepository carRepository;

	@Autowired
	public RentalService(CarRentalRepositories carRentalRepositories, UserRepository userRepository,
			CarRepository carRepository) {
		this.carRentalRepositories = carRentalRepositories;
		this.userRepository = userRepository;
		this.carRepository = carRepository;
	}

	public CarRental allocateCar(CarRental rental) {
		return carRentalRepositories.save(rental);
	}

		/**
		 * 
		 * @return
		 * Car Rented For a specific Period of time
		 */
		public List<CarRental> RentedForSpecificPeriod() {
			List<CarRental> carRental = new ArrayList<>();
			List<CarRental> carRental2 = carRentalRepositories.findByReturned(true);
			if(carRental2.isEmpty()) {
				return carRental;
			}
			return carRental2;
		} 

	/**
	 * 
	 * @param registrationNumber
	 * @return
	 * Total Amount Rented for each car
	 * number of days * price per day of each car
	 */
	public Double amountForCar(String registrationNumber) {
		Double amount = 0D;
		List<CarRental> rental = new ArrayList<>();
		rental = carRentalRepositories.findByCarRegistrationNumberAndReturned(registrationNumber, true);
		//Period period;
		for (CarRental carRental : rental) {
			
			System.out.println(carRental.getStartDate());
			System.out.println(carRental.getEndDate());
			
			Period period = Period.between(carRental.getStartDate(), carRental.getEndDate());
			amount += (period.getDays() + 1) * carRental.getPricePerDay();
			
			
		}
		return amount;

	}

	/**
	 * 
	 * @return
	 * Total amount of rent for all cars
	 */
	public Double totalAmount() {
		Double amount = 0D;
		List<CarRental> rental = new ArrayList<>();
		carRentalRepositories.findAll().forEach(rental::add);
		
		Period period;
		for (CarRental carRental : rental) {
			if (carRental.isReturned()) {
				period = Period.between(carRental.getStartDate(), carRental.getEndDate());
				amount += (period.getDays() + 1) * carRental.getPricePerDay();
			}
		}
		return amount;

	}

	/**
	 * 
	 * @param nationalId
	 * @return
	 * Cars rented to customer and admin
	 */
	public Set<Car> carRentedByCustomer(String nationalId) {
		userRepository.findOneByNationalId(nationalId);
		Set<Car> cars = new HashSet<>();
		List<CarRental> rental = carRentalRepositories.findByUserNationalId(nationalId);
		for (CarRental carRental : rental) {
			cars.add(carRental.getCar());
		}
		return cars;
	}

	// Release the car when the customer return it , end date unchanged
	/*
	 * public void releaseCar(String registrationNumber) throws
	 * CarRentalEntryNotFoundException { List<CarRental> rentals =
	 * carRentalRepositories.findByCarRegistrationNumber(registrationNumber);
	 * boolean done = false; for(CarRental rental : rentals) {
	 * if(rental.isReturned()==false) { rental.setReturned(true);
	 * carRentalRepositories.save(rental); done = true; } } if(!done) { throw new
	 * CarRentalEntryNotFoundException("Car Entry not found in database!"); } }
	 */

	/**
	 * 
	 * @return
	 * Display All Rental Cars
	 */
	public List<CarRentalDto> getAllRentalCars() {
		List<CarRental> rentals = carRentalRepositories.findAll();
		List<CarRentalDto> rentals2 = new ArrayList<>();
		for (CarRental carRental : rentals) {
			rentals2.add(new CarRentalDto(carRental.getId(), carRental.getUser().getNationalId(),
					carRental.getCar().getRegistrationNumber(), carRental.getCar().getModel(),
					carRental.getPricePerDay(), carRental.getCar().getYear(), carRental.getStartDate(),
					carRental.getEndDate(), carRental.isReturned()));
		}
		return rentals2;
	}

	/**
	 * 
	 * @param registrationNumber
	 * @throws CarNotReturnedException
	 * Customer Renting available Cars
	 */
	public void customerRentingCar(String registrationNumber) throws CarNotReturnedException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nationalId = authentication.getName();
		User user = userRepository.findOneByNationalId(nationalId);
		if(carNotReturnedByUserId(user)) {
			throw new CarNotReturnedException("Customer has not returned car");
		}
		Car car = carRepository.findOneByRegistrationNumber(registrationNumber);
		CarRental rental = new CarRental(user, car, LocalDate.now(), false, car.getPricePerDay());
		carRentalRepositories.save(rental);
	}

	private boolean carNotReturnedByUserId(User customer) {
		boolean unreturnedCar = false;
		List<CarRental> listOfPending = carRentalRepositories.findByReturned(false);
		for (CarRental rental : listOfPending) {
			if (rental.getUser() == customer) {
				unreturnedCar = true;
			}
		}
		return unreturnedCar;
	}

	/**
	 * rented car returned by customer,end date change to now
	 */
	public void releaseCar(String registrationNumber) throws CarRentalEntryNotFoundException {
		List<CarRental> rentals = carRentalRepositories.findByCarRegistrationNumber(registrationNumber);
		boolean done = false;
		for (CarRental rental : rentals) {
			if (rental.isReturned() == false) {
				rental.setReturned(true);
				rental.setEndDate(LocalDate.now());
				carRentalRepositories.save(rental);
				done = true;
			}
		}
		if (!done) {
			throw new CarRentalEntryNotFoundException("Car Entry not found in database!");
		}
	}
	
	/**
	 * 
	 * @return
	 * Total amount of cars customer has rented 
	 */
	public Set<Car> UserRentedCar() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nationalId = authentication.getName();
		List<CarRental> rental = carRentalRepositories.findByUserNationalId(nationalId);
		Set<Car> cars = new HashSet<>();
		for (CarRental carRental : rental) {
			cars.add(carRental.getCar());
		}
		return cars;
	}
	
	public Double calculateAmountOfCars(Car car) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nationalId = authentication.getName();
		List<CarRental> rental = carRentalRepositories.findByUserNationalIdAndCar(nationalId, car);
		return (double) rental.size();
	}
	
	public List<CarRental> getCarRentalsForSpecificUserByNationalId() {
		
		// Get currently logged in user national id
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nationalId = authentication.getName();
		
		// Get all car rentals for that user
		return carRentalRepositories.findByUserNationalId(nationalId);
	}
}
