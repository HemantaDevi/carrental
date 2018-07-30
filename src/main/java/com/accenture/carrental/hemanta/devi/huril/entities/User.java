package com.accenture.carrental.hemanta.devi.huril.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.accenture.carrental.hemanta.devi.huril.entities.enums.RoleType;


@Entity
@Table(name="USER")
public class User implements Serializable{
	
	private static final long serialVersionUID = 2870964171558097949L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="USER_ID")
	private long id;
	
	@Column(name="NATIONAL_ID",nullable=false,unique=true)
	private String nationalId;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ROLE")
	private RoleType role;
	
	@Column(name="NAME",nullable=false)
	private String name;
	
	@Column(name="SEX")
	private String sex;
	
	@Column(name="DATE_OF_BIRTH")
	private LocalDate dateOfBirth;
	
	 @OneToMany(mappedBy = "user")
	 private Set<CarRental> rentals;
	
	public User() {
	}
	
	public User(long id, String nationalId, String password, RoleType role, String name, String sex, LocalDate dateOfBirth) {
		this.id = id;
		this.nationalId = nationalId;
		this.password = password;
		this.role = role;
		this.name = name;
		this.sex = sex;
		this.dateOfBirth = dateOfBirth;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public RoleType getRole() {
		return role;
	}
	public void setRole(RoleType role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	
	
}
