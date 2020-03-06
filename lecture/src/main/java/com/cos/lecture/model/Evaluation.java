package com.cos.lecture.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
public class Evaluation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	private String lectureName;
	private String professorName;
	private int lectureYear;
	private String semesterDivide;
	private String lectureDivide;
	private String evaluationTitle;
	private String evaluationContent;
	private String totalScore;
	private String creditScore;
	private String comfortableScore;
	private String lectureScore;
	
	private int likeCount;
	
	@Transient
	private int page;
	
}
