package com.alpaca.mrc.domain.record.repository;

import com.alpaca.mrc.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = RecordRepository.class)
public interface RecordRepository extends JpaRepository<Record, Long> {
}
