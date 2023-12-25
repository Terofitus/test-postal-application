package com.example.testpostalapplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "post_office")
public class PostOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 100)
    private String name;
    @Column(unique = true, nullable = false, length = 200)
    private String address;
    @Column(nullable = false, length = 10)
    private String index;
}
