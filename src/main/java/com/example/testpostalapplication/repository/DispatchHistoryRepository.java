package com.example.testpostalapplication.repository;

import com.example.testpostalapplication.model.PostalDispatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DispatchHistoryRepository extends JpaRepository<PostalDispatchHistory, Long> {
    @Query("select h from PostalDispatchHistory h where h.postalDispatch.id=:dispatchId order by h.dateTime desc limit 1")
    Optional<PostalDispatchHistory> lastTransfer(@Param("dispatchId") Long dispatchId);

    Page<PostalDispatchHistory> findAllByPostalDispatchIdOrderByDateTime(Long id, Pageable pageable);
}
