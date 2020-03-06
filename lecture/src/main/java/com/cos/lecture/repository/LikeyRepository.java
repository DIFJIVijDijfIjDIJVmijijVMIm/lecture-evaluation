package com.cos.lecture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.lecture.model.Likey;

public interface LikeyRepository extends JpaRepository<Likey, Integer>{
	
	int countByEvaluationId(int evaluationId);
	
	List<Likey> findByUserId(int userId);
}
