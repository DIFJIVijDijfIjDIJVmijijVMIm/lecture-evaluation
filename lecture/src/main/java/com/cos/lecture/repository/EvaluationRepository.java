package com.cos.lecture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.lecture.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer>{
	
	
	@Query(value = "SELECT * FROM evaluation ORDER BY id DESC LIMIT ?1, 4", nativeQuery = true)
	List<Evaluation> findAllByOrderByIdDesc(int page);
	
	//No Limit
	@Query(value = "SELECT * FROM evaluation WHERE lectureDivide LIKE ?1 AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?2% ORDER BY likeCount DESC", nativeQuery = true)
	List<Evaluation> findBySearchOrderByLikeCount(String lectureDivide, String searchText);
	
	@Query(value = "SELECT * FROM evaluation WHERE lectureDivide LIKE ?1 AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?2% ORDER BY id DESC", nativeQuery = true)
	List<Evaluation> findBySearchOrderById(String lectureDivide, String searchText);
	
	@Query(value = "SELECT * FROM evaluation WHERE CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?1% ORDER BY id DESC", nativeQuery = true)
	List<Evaluation> findAllBySearchOrderById(String searchText);
	
	@Query(value = "SELECT * FROM evaluation WHERE CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?1% ORDER BY likeCount DESC", nativeQuery = true)
	List<Evaluation> findAllBySearchOrderByLikeCount(String searchText);
	
	@Query(value = "SELECT * FROM evaluation WHERE CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?1%", nativeQuery = true)
	List<Evaluation> findAllBySearch(String searchText);
	
	
	//Limit
	@Query(value = "SELECT * FROM evaluation WHERE lectureDivide LIKE ?1 AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?2% ORDER BY likeCount DESC LIMIT ?3, 4", nativeQuery = true)
	List<Evaluation> findBySearchOrderByLikeCountLimit(String lectureDivide, String searchText, int page);
	
	@Query(value = "SELECT * FROM evaluation WHERE lectureDivide LIKE ?1 AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?2% ORDER BY id DESC LIMIT ?3, 4", nativeQuery = true)
	List<Evaluation> findBySearchOrderByIdLimit(String lectureDivide, String searchText, int page);
	
	@Query(value = "SELECT * FROM evaluation WHERE CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?1% ORDER BY id DESC LIMIT ?2, 4", nativeQuery = true)
	List<Evaluation> findAllBySearchOrderByIdLimit(String searchText, int page);
	
	@Query(value = "SELECT * FROM evaluation WHERE CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?1% ORDER BY likeCount DESC LIMIT ?2, 4", nativeQuery = true)
	List<Evaluation> findAllBySearchOrderByLikeCountLimit(String searchText, int page);
	
	@Query(value = "SELECT * FROM evaluation WHERE CONCAT(lectureName, professorName, evaluationTitle, evaluationContent)"
			+ " LIKE %?1% LIMIT ?2, 4", nativeQuery = true)
	List<Evaluation> findAllBySearchLimit(String searchText, int page);
	
}
