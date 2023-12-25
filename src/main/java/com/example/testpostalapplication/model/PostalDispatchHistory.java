package com.example.testpostalapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dispatch_history")
public class PostalDispatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "postal_dispatch_id", referencedColumnName = "id", nullable = false)
    private PostalDispatch postalDispatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_destination_post_id", referencedColumnName = "id", nullable = false)
    private PostOffice currentDestination;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostalDispatchStatus status;
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
}
