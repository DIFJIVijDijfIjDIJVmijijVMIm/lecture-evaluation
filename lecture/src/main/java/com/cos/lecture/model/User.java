package com.cos.lecture.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "userName", unique = true)
	@NotBlank(message = "Username cannot be blank")
	private String userName;	
	@NotBlank(message = "UserPassword cannot be blank")
	private String userPassword;
	private String userEmail;
	private String userEmailHash;
	@Column(name = "userEmailChecked", nullable = false, columnDefinition = "boolean default false")
	private Boolean userEmailChecked;
	
}
