package com.example.testpostalapplication.repository;

import com.example.testpostalapplication.model.PostalDispatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchRepository extends CrudRepository<PostalDispatch, Long> {
}
