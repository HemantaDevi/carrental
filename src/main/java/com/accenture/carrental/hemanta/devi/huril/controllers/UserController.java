package com.accenture.carrental.hemanta.devi.huril.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.accenture.carrental.hemanta.devi.huril.entities.User;
import com.accenture.carrental.hemanta.devi.huril.entities.enums.RoleType;
import com.accenture.carrental.hemanta.devi.huril.exceptions.NotInsertedException;
import com.accenture.carrental.hemanta.devi.huril.services.UserService;

@Controller
public class UserController {
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@GetMapping("/")
	public String showLoginPage() {
		return "redirect:/processLogin";
	}

	@GetMapping("/processLogin")
	public String redirectUserBasedOnRole() {

		// Get currently logged in user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nationalId = authentication.getName();

		// Get user
		User user = userService.findUserByNationalId(nationalId);

		// Get role
		String role = user.getRole().toString();

		if (role.equalsIgnoreCase("ADMIN")) {
			return "admin";
		}

		if (role.equalsIgnoreCase("CUSTOMER")) {
			return "customer";
		}

		return "redirect:/";
	}
	
	/**
	 * 
	 * @param model
	 * @param request
	 * @return
	 * Show all user
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/user")
	public String User(Model model,HttpServletRequest request) {
		List<User> user = userService.findAllUsers();
		model.addAttribute("users", user);
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		return "user";
	}

	/**
	 * 
	 * @return
	 * Create an Admin
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/createAdmin")
	public String createAdmin() {
		return "createAdmin";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/creatingAdmin")
	public String creatingAdmin(@RequestParam("nationalId") String nationalId,
			@RequestParam("password") String password, @RequestParam("name") String name,
			@RequestParam("sex") String sex, @RequestParam("dateOfBirth") String Date, Model model,HttpServletRequest request) {
		User user = new User();
		user.setNationalId(nationalId);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		user.setName(name);
		user.setSex(sex);
		LocalDate dob = LocalDate.parse(Date);
		user.setDateOfBirth(dob);
		try {
			userService.CreateAdmin(user);
		} catch (NotInsertedException e) {
			model.addAttribute("error", e.getMessage());
			return "createAdmin";
		}
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		return "user";
	}
	
	/**
	 * 
	 * @return
	 * Create a Customer
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/createCustomer")
	public String createCustomer() {
		return "createCustomer";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/creatingCustomer")
	public String creatingCustomer(@RequestParam("nationalId") String nationalId,
			@RequestParam("password") String password, @RequestParam("name") String name,
			@RequestParam("sex") String sex, @RequestParam("dateOfBirth") String Date, Model model,HttpServletRequest request) {
		User user = new User();
		user.setNationalId(nationalId);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		user.setName(name);
		user.setSex(sex);
		LocalDate dob = LocalDate.parse(Date);
		user.setDateOfBirth(dob);
		try {
			userService.createCustomer(user);
		} catch (NotInsertedException e) {
			model.addAttribute("error", e.getMessage());
			return "createCustomer";
		}
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		return "user";
	}
	
	/**
	 * 
	 * @param model
	 * @param request
	 * @return
	 * Show all admin
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/showAdmin")
	public String showAdmin(Model model, HttpServletRequest request) {
		List<User> user = userService.findAllCustomers(RoleType.ADMIN);
		model.addAttribute("users", user);
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		return "user";
	}
	
	/**
	 * 
	 * @param model
	 * @param request
	 * @return
	 * Show all Customer
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/showCustomer")
	public String showCustomer(Model model,HttpServletRequest request) {
		List<User> users = userService.findAllCustomers(RoleType.CUSTOMER);
		model.addAttribute("users", users);
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		return "user";
	}
	
	/**
	 * 
	 * @param nationalId
	 * @param model
	 * @return
	 * Update a User according to its role by its national Id
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/updateUser/{nationalId}")
	public String updateCar(@PathVariable String nationalId, Model model) {
		User user = userService.findUserByNationalId(nationalId);
		model.addAttribute("user", user);
		return "updateUser";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/updatingUser")
	public String updating(@RequestParam("nationalId") String nationalId, @RequestParam("name") String name,
			Model model,HttpServletRequest request) {
		User user = new User();
		user.setNationalId(nationalId);
		user.setName(name);
		userService.updateUser(user);
		model.addAttribute("users", userService.findAllUsers());
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		return "user";
	}
	
	/**
	 * 
	 * @param nationalId
	 * @param model
	 * @param request
	 * @return
	 * Delete a user by its national id
	 */
	@GetMapping("/deleteUser/{nationalId}")
	public String deleteUser(@PathVariable String nationalId, Model model,HttpServletRequest request) {
		if(userService.deleteUser(nationalId)==-1) {
			model.addAttribute("errMsg","Car is rented. Cannot be deleted");
		}
		String hostNport = request.getServerName() + ":" + request.getServerPort();
		model.addAttribute("hostNport", hostNport);
		model.addAttribute("users", userService.findAllUsers());
		return "user";
	}

	/**
	 * 
	 * @return
	 * Search for a customer by its national Id
	 */
	@GetMapping("/searchCustomer")
	public String getACarByItsRegistrationNumber() {
		return "searchCustomer";
	}

	@PostMapping("/searchingCustomer")
	public String searchingTheCar(@RequestParam("nationalId") String nationalId, Model model,HttpServletRequest request) {
		User user = userService.findUserByNationalId(nationalId);
		if (user != null) {
			List<User> users = new ArrayList<>();
			users.add(user);
			model.addAttribute("users", users);
			String hostNport = request.getServerName() + ":" + request.getServerPort();
			model.addAttribute("hostNport", hostNport);
			return "user";
		} else {
			model.addAttribute("error", "User Not Found. Invalid National Id");
			return "searchCustomer";
		}
	}

}
