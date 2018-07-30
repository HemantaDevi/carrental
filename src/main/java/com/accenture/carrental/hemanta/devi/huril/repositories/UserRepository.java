package com.accenture.carrental.hemanta.devi.huril.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.accenture.carrental.hemanta.devi.huril.entities.User;
import com.accenture.carrental.hemanta.devi.huril.entities.enums.RoleType;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findOneByNationalId(String nationalId);
	
	public List<User> findByRole(RoleType role);
	
	@Modifying
	@Query("DELETE FROM User WHERE nationalId=?1")
	public int deleteByNationalId(String nationalId);
}
