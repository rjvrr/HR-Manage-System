package com.hcl.hrgenius.repository;

import com.hcl.hrgenius.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
  List<Job> findByStatusIgnoreCase(String status);
  List<Job> findByDepartmentIgnoreCase(String department);
}