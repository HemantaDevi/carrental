package com.accenture.carrental.hemanta.devi.huril.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="CAR")
public class Car implements Serializable {
	
	private static final long serialVersionUID = 7958090416994266792L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CAR_ID")
	private long id;
	
	@Column(name="REGISTRATION_NUMBER",unique=true)
	private String registrationNumber;
	
	@Column(name="MODEL")
	private String carModel;
	
	@Column(name="PRICE_PER_DAY")
	private Double pricePerDay;
	
	@Column(name="YEAR", length = 4)
	private int year;
	
	 @OneToMany(mappedBy = "car")
	 private Set<CarRental> rentals;
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Set<CarRental> getRentals() {
		return rentals;
	}
	public void setRentals(Set<CarRental> rentals) {
		this.rentals = rentals;
	}
	public Car() {
		
	}
	public Car(String registrationNumber, String model, Double pricePerDay) {
		this.registrationNumber = registrationNumber;
		this.carModel = model;
		this.pricePerDay = pricePerDay;
	}
	
	public Car(String registrationNumber, String carModel, Double pricePerDay, int year) {
		this(registrationNumber,carModel,pricePerDay);
		this.year = year;
	}
	
	public Car(long id, String registrationNumber, String carModel, Double pricePerDay, int year) {
		this(registrationNumber,carModel,pricePerDay,year);
		this.id = id;
	
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getModel() {
		return carModel;
	}
	public void setModel(String model) {
		this.carModel = model;
	}
	public Double getPricePerDay() {
		return pricePerDay;
	}
	public void setPricePerDay(Double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((registrationNumber == null) ? 0 : registrationNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (registrationNumber == null) {
			if (other.registrationNumber != null)
				return false;
		} else if (!registrationNumber.equals(other.registrationNumber))
			return false;
		return true;
	}
	
	
	
}
