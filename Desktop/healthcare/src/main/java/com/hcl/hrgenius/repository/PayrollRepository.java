package com.hcl.hrgenius.repository;

import com.hcl.hrgenius.entity.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PayrollRepository extends JpaRepository<PayrollRecord, Long> {
  List<PayrollRecord> findByEmployeeId(Long employeeId);
}