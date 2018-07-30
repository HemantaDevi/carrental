package com.accenture.carrental.hemanta.devi.huril.api.dtos;

public class CarDTO {
	private String registrationNumber;

	private String carModel;

	private Double pricePerDay;
	
	private int year;
	
	private Double amount;


	public CarDTO(String registrationNumber, String carModel, Double pricePerDay, int year, Double amount) {
		this.registrationNumber = registrationNumber;
		this.carModel = carModel;
		this.pricePerDay = pricePerDay;
		this.year = year;
		this.amount = amount;
	}
	
	public CarDTO(String registrationNumber) {
		super();
		this.registrationNumber = registrationNumber;
	}



	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public CarDTO() {
		super();
	}

	public CarDTO(String registrationNumber, String model, Double pricePerDay, int year) {
		super();
		this.registrationNumber = registrationNumber;
		this.carModel = model;
		this.pricePerDay = pricePerDay;
		this.year = year;
	}
	
}
