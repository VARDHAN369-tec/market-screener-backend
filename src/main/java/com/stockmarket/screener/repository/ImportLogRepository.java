package com.stockmarket.screener.repository;

import com.stockmarket.screener.entity.ImportLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportLogRepository extends JpaRepository<ImportLog, Long> {
    List<ImportLog> findTop50ByOrderByTimestampDesc();
    Page<ImportLog> findAllByOrderByTimestampDesc(Pageable pageable);
}
