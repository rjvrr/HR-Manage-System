package com.hcl.hrgenius.repository;

import com.hcl.hrgenius.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
  List<Candidate> findByStatusIgnoreCase(String status);
}