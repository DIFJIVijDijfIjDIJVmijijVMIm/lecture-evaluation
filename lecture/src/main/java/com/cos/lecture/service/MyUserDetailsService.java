package com.cos.lecture.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.lecture.model.User;
import com.cos.lecture.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository mUserRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = mUserRepository.findByUserName(userName);
		
		MyUserDetails userDetails = null;
		if(user != null) {
			userDetails = new MyUserDetails();
			userDetails.setUser(user);
		}else {
			throw new UsernameNotFoundException("Not Found 'username'");
		}
		return userDetails;
	}
	
	
}
