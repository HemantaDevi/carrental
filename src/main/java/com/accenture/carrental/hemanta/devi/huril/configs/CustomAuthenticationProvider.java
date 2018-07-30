package com.accenture.carrental.hemanta.devi.huril.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.accenture.carrental.hemanta.devi.huril.entities.User;
import com.accenture.carrental.hemanta.devi.huril.repositories.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	} 
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    		String nationalId = authentication.getName();
    		        String password = authentication.getCredentials().toString();
    		       User user = userRepository.findOneByNationalId(nationalId);
    		        if (user!=null) {
    		            List<GrantedAuthority> authorities = getAuthorities(user);
    		            String encodedPassword = user.getPassword();
    		            if (bCryptPasswordEncoder.matches(password, encodedPassword)) {
    		                return new UsernamePasswordAuthenticationToken(
    		                		nationalId, encodedPassword, authorities);
    		            }
    		        }
    		        throw new BadCredentialsException("Invalid credentials");
    		    }


    private List<GrantedAuthority> getAuthorities(User user) {
    	List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    	grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    	if (user.getRole().name().equalsIgnoreCase("ADMIN")) {
    		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
    		return grantedAuthorities;
    } 
    
    @Override
    public boolean supports(Class<?> authentication) {
    	return authentication.equals(
    			UsernamePasswordAuthenticationToken.class);
    }

}