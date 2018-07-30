package com.accenture.carrental.hemanta.devi.huril.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.accenture.carrental.hemanta.devi.huril.api.dtos.CarDTO;
import com.accenture.carrental.hemanta.devi.huril.entities.Car;
import com.accenture.carrental.hemanta.devi.huril.entities.CarRental;
import com.accenture.carrental.hemanta.devi.huril.exceptions.CSVNotFound;
import com.accenture.carrental.hemanta.devi.huril.exceptions.CarRegNumAlreadyExistException;
import com.accenture.carrental.hemanta.devi.huril.exceptions.GeneralIOException;
import com.accenture.carrental.hemanta.devi.huril.exceptions.NotAllCarsInCSVInserted;
import com.accenture.carrental.hemanta.devi.huril.exceptions.NotInsertedException;
import com.accenture.carrental.hemanta.devi.huril.repositories.CarRentalRepositories;
import com.accenture.carrental.hemanta.devi.huril.repositories.CarRepository;

@Transactional
@Service
public class CarService {
	private CarRentalRepositories carRentalRepository;

	private CarRepository carRepository;

	public CarService(CarRentalRepositories carRentalRepository, CarRepository carRepository) {
		this.carRentalRepository = carRentalRepository;
		this.carRepository = carRepository;
	}

	public List<Car> ListAllCars() {
		return carRepository.findAll();

	}
	
	/**
	 * 
	 * @param carDTO
	 * @throws CarRegNumAlreadyExistException
	 * @throws NotInsertedException
	 * Method to add a new car
	 */
	public void addCar(CarDTO carDTO) throws CarRegNumAlreadyExistException,NotInsertedException {
		Car found = carRepository.findOneByRegistrationNumber(carDTO.getRegistrationNumber());
		if (found != null) { // exception name is not good
			throw new CarRegNumAlreadyExistException("similar registration numbers");
		}
		Car car = new Car(carDTO.getRegistrationNumber(), carDTO.getModel(), carDTO.getPricePerDay());
		car.setYear(carDTO.getYear());
		carRepository.save(car);
	}
	
	/**
	 * 
	 * @param car
	 * @return
	 * Update Car Details
	 */
	public Car updateCar(Car car) {
		if (carRepository.findOneByRegistrationNumber(car.getRegistrationNumber()) != null) {
			Car car1 = carRepository.findOneByRegistrationNumber(car.getRegistrationNumber());
			car1.setPricePerDay(car.getPricePerDay());
			return carRepository.save(car1);
		}
		return null;
	}

	private boolean checkIfFound(String registrationNumber) {
		List<CarRental> carRentals = carRentalRepository.findAll();
		boolean found = false;
		for (CarRental carRental : carRentals) {
			if (carRental.getCar().getRegistrationNumber().equals(registrationNumber)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * 
	 * @param registrationNumber
	 * @return
	 * Delete a Car by its registration Number
	 */
	public int deleteCar(String registrationNumber) {
		if (checkIfFound(registrationNumber)) {
			return -1;
		} else {
			return carRepository.deleteByRegistrationNumber(registrationNumber);
		}
	}

	public Car findByRegistrationNumber(String registrationNumber) {
		return carRepository.findOneByRegistrationNumber(registrationNumber);
	}

	/**
	 * 
	 * @return
	 * List of Cars Available for rent
	 */
	public List<Car> findByAvailability() {
		// return carRepository.findByAvailability(true);
		List<CarRental> carRentals = carRentalRepository.findByReturned(false);
		List<Car> setOfUnreturned = new ArrayList<>();
		for (CarRental carRental : carRentals) {
			setOfUnreturned.add(carRental.getCar());
		}
		List<Car> allCarsAvailable = carRepository.findAll();
		allCarsAvailable.removeAll(setOfUnreturned);
		List<Car> available = new ArrayList<>();
		for (Car car : allCarsAvailable) {
			available.add(new Car(car.getId(), car.getRegistrationNumber(), car.getModel(), car.getPricePerDay(),
					car.getYear()));
		}
		return available;
	}

	/**
	 * 
	 * @param path
	 * @throws Exception
	 * Import CSV of Car Details
	 */
	public void importCSVforCars(String path) throws Exception {
		BufferedReader br;
		boolean allCarsInsertedWithoutError = true;
		Set<String> errorMessage = new HashSet<>();
		try {
			br = new BufferedReader(new FileReader(path));
			br.readLine(); // skips first line which should be titles
			String str;
			while ((str = br.readLine()) != null) {
				String[] split = str.split(",");
				try {
					addCar(new CarDTO(split[0], split[1], Double.parseDouble(split[2]), Integer.parseInt(split[3]),
							Double.parseDouble(split[4])));
				} catch (Exception e) {
					errorMessage.add(e.getMessage());
					allCarsInsertedWithoutError = false;
					continue;
				}
			}
			br.close();
			if (!allCarsInsertedWithoutError) {
				String errorMsg = "Not all cars imported for the following reasons: ";
				int counter = 1;
				for (String error : errorMessage) {
					errorMsg += error;
					if (counter == errorMessage.size()) {
						errorMsg += ".";
					} else {
						errorMsg += ",";
					}

					counter++;
				}
				throw new NotAllCarsInCSVInserted(errorMsg);
			}
		} catch (FileNotFoundException e) {
			throw new CSVNotFound(e.getMessage());
		} catch (IOException e) {
			throw new GeneralIOException(e.getMessage());
		}
	}
	
	/**
	 * Initializing two Cars
	 */
	@PostConstruct
	public void initialise(){
		if (carRepository.findAll().isEmpty()) {
			CarDTO car1 = new CarDTO();
			car1.setRegistrationNumber("12 Mar 2018");
			car1.setModel("Porsche");
			car1.setPricePerDay(1000.0);
			car1.setYear(2018);
			
			CarDTO car2 = new CarDTO();
			car2.setRegistrationNumber("22 Mar 2018");
			car2.setModel("Mercedes");
			car2.setPricePerDay(2000.0);
			car2.setYear(2016);
			try {
				addCar(car1);
				addCar(car2);
			} catch (CarRegNumAlreadyExistException e) {
				e.printStackTrace();
			} catch (NotInsertedException e) {
				e.printStackTrace();
			}
		}
	}
}
