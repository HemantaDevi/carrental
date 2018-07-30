package com.accenture.carrental.hemanta.devi.huril.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.accenture.carrental.hemanta.devi.huril.entities.CarRental;
import com.accenture.carrental.hemanta.devi.huril.entities.User;
import com.accenture.carrental.hemanta.devi.huril.entities.enums.RoleType;
import com.accenture.carrental.hemanta.devi.huril.exceptions.NotInsertedException;
import com.accenture.carrental.hemanta.devi.huril.repositories.CarRentalRepositories;
import com.accenture.carrental.hemanta.devi.huril.repositories.UserRepository;

@Transactional
@Service
public class UserService {
	private final UserRepository userRepository;
	private final CarRentalRepositories carRentalRepositories;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, CarRentalRepositories carRentalRepositories,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.carRentalRepositories = carRentalRepositories;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	/**
	 * 
	 * @param role
	 * @return
	 * List of all Customers
	 */
	public List<User> findAllCustomers(RoleType role) {
		return userRepository.findByRole(role);
	}
	
	/**
	 * 
	 * @return
	 * List of all Users
	 */
	public List<User> findAllUsers() {
		List<User> user = new ArrayList<>();
		userRepository.findAll().forEach(user::add);
		return user;
	}

	/**
	 * 
	 * @param nationalId
	 * @return
	 * Find a User By its national Id
	 */
	public User findUserByNationalId(String nationalId) {
		return userRepository.findOneByNationalId(nationalId);
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws NotInsertedException
	 * Insert a new Customer
	 */
	public User createCustomer(User user) throws NotInsertedException {
		if (userRepository.findOneByNationalId(user.getNationalId()) != null) {
			throw new NotInsertedException("Customer already exist");
		} else {
			user.setRole(RoleType.CUSTOMER);
			return userRepository.save(user);
		}
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws NotInsertedException
	 * Insert a new Admin
	 */
	public User CreateAdmin(User user) throws NotInsertedException {
		if (userRepository.findOneByNationalId(user.getNationalId()) != null) {
			throw new NotInsertedException("Admin already exist");
		} else {
			user.setRole(RoleType.ADMIN);
			return userRepository.save(user);
		}
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * Update a User by its national Id
	 */
	public User updateUser(User user) {
		if (userRepository.findOneByNationalId(user.getNationalId()) != null) {
			User user1 = userRepository.findOneByNationalId(user.getNationalId());
			user1.setName(user.getName());
			return userRepository.save(user1);
		}
		return null;
	}

	private boolean checkIfFound(String nationalId) {
		List<CarRental> carRentals = carRentalRepositories.findAll();
		boolean found = false;
		for (CarRental carRental : carRentals) {
			if (carRental.getUser().getNationalId().equals(nationalId)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * 
	 * @param nationalId
	 * @return
	 * Delete a User by its national Id
	 */
	public int deleteUser(String nationalId) {
		if (checkIfFound(nationalId)) {
			return -1;
		} else {
			return userRepository.deleteByNationalId(nationalId);
		}
	}

	/**
	 * Initializing two users
	 * 1.Admin
	 * 2.Customer
	 */
	@PostConstruct
	public void initialise() {
		if (findAllCustomers(RoleType.ADMIN).isEmpty()) {

			User user2 = new User();
			user2.setNationalId("admin");
			user2.setName("admin");
			user2.setPassword(bCryptPasswordEncoder.encode("admin"));
			user2.setRole(RoleType.ADMIN);
			user2.setSex("Male");
			user2.setDateOfBirth(LocalDate.of(1993, 03, 22));

			try {
				CreateAdmin(user2);
			} catch (NotInsertedException e) {
				e.printStackTrace();
			}

		}

		if (findAllCustomers(RoleType.CUSTOMER).isEmpty()) {

			User user1 = new User();
			user1.setNationalId("customer");
			user1.setName("customer");
			user1.setPassword(bCryptPasswordEncoder.encode("customer"));
			user1.setRole(RoleType.CUSTOMER);
			user1.setSex("Male");
			user1.setDateOfBirth(LocalDate.of(1990, 12, 22));

			try {
				createCustomer(user1);
			} catch (NotInsertedException e) {
				e.printStackTrace();
			}
		}
	}
}
