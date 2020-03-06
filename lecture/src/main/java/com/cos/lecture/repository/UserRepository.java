package com.cos.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.lecture.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUserName(String userName);
	
	User findByUserNameAndUserPassword(String userName, String userPassword);
	
}
