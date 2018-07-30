package com.accenture.carrental.hemanta.devi.huril.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.accenture.carrental.hemanta.devi.huril.entities.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>{
	
	public Car findOneByRegistrationNumber(String registrationNumber);
	
	@Modifying
	@Query("DELETE FROM Car WHERE registrationNumber=?1")
	public int deleteByRegistrationNumber(String registrationNumber);
}
