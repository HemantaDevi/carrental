package com.accenture.carrental.hemanta.devi.huril.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="CAR_RENTAL")
public class CarRental implements Serializable{
	
	private static final long serialVersionUID = -7613561149053209334L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="RENTAL_ID")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@ManyToOne
	@JoinColumn(name = "CAR_ID")
	private Car car;
	
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private LocalDate startDate;
	
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private LocalDate endDate;

	@Column(name = "RETURNED")
	private boolean returned;
	
	@Column(name="PRICE_PER_DAY")
	private Double pricePerDay;
	
	public CarRental() {
	}

	public CarRental(int id, User user, Car car, LocalDate startDate, LocalDate endDate, boolean returned,
			Double pricePerDay) {
		this.id = id;
		this.user = user;
		this.car = car;
		this.startDate = startDate;
		this.endDate = endDate;
		this.returned = returned;
		this.pricePerDay = pricePerDay;
	}
	
	public CarRental(User user, Car car, LocalDate startDate, LocalDate endDate, boolean returned, Double pricePerDay) {
		this.user = user;
		this.car = car;
		this.startDate = startDate;
		this.endDate = endDate;
		this.returned = returned;
		this.pricePerDay = pricePerDay;
	}

	public CarRental(User user, Car car, LocalDate startDate, boolean returned, Double pricePerDay) {
		this.user = user;
		this.car = car;
		this.startDate = startDate;
		this.returned = returned;
		this.pricePerDay = pricePerDay;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(Double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}

}
