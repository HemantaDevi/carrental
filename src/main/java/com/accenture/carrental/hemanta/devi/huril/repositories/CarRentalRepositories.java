package com.accenture.carrental.hemanta.devi.huril.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.carrental.hemanta.devi.huril.entities.Car;
import com.accenture.carrental.hemanta.devi.huril.entities.CarRental;

@Repository
public interface CarRentalRepositories extends JpaRepository <CarRental, Integer>{
	
	public List<CarRental> findByReturned(Boolean returned);
	
	public List<CarRental> findByCarRegistrationNumber(String registrationNumber);
	
	public List<CarRental> findByCarRegistrationNumberAndReturned(String registrationNumber, Boolean returned);
	
	public List<CarRental> findByUserNationalId(String NationalId);

	public List<CarRental> findByUserId(Long userId);
	
	public List<CarRental> findByCarId(Long carId);
	
	public List<CarRental> findByUserNationalIdAndCar(String nationalId, Car car);

}
