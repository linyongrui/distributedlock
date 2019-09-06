package com.example.distributedlock.repository;

import com.example.distributedlock.po.LockTestPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockTestRepository extends JpaRepository<LockTestPo, Long> {
}
