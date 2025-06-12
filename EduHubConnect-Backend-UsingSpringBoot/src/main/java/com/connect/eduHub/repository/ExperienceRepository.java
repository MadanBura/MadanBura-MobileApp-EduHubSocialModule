package com.connect.eduHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.connect.eduHub.model.Experience;


public interface ExperienceRepository extends JpaRepository<Experience, Long> {
	  List<Experience> findByUserId(Long userId);
}
