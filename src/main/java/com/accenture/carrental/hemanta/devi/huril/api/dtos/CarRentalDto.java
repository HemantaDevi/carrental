package com.accenture.carrental.hemanta.devi.huril.api.dtos;

import java.time.LocalDate;


public class CarRentalDto {
	private int id;
	private String nationalId;
	private String registrationNumber;
	private String carModel;
	private Double pricePerDay;
	private int year;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double amount;
	private boolean returned;
	


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	public boolean isReturned() {
		return returned;
	}
	public void setReturned(boolean returned) {
		this.returned = returned;
	}
	public CarRentalDto(int id, String nationalId, String registrationNumber, String carModel, Double pricePerDay, int year,
			LocalDate startDate, LocalDate endDate, Double amount, boolean returned) {
		super();
		this.id = id;
		this.nationalId = nationalId;
		this.registrationNumber = registrationNumber;
		this.carModel = carModel;
		this.pricePerDay = pricePerDay;
		this.year = year;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.returned = returned;
	}
	public CarRentalDto(int id, String nationalId, String registrationNumber, String carModel, Double pricePerDay, int year,
			LocalDate startDate, LocalDate endDate, boolean returned) {
		super();
		this.id = id;
		this.nationalId = nationalId;
		this.registrationNumber = registrationNumber;
		this.carModel = carModel;
		this.pricePerDay = pricePerDay;
		this.year = year;
		this.startDate = startDate;
		this.endDate = endDate;
		this.returned = returned;
	}
	public CarRentalDto() {
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
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
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
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	
}
