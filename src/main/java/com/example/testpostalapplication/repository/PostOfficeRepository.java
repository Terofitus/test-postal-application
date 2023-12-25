package com.example.testpostalapplication.repository;

import com.example.testpostalapplication.model.PostOffice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostOfficeRepository extends CrudRepository<PostOffice, Long> {
}
