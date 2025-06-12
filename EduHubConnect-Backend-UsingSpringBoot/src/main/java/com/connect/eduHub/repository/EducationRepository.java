package com.connect.eduHub.repository;


import com.connect.eduHub.model.Education;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByUserId(Long userId);
}