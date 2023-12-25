package com.example.testpostalapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "postal_dispatch")
public class PostalDispatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostalDispatchType type;
    @ManyToOne
    @JoinColumn(name = "destination_post_id", referencedColumnName = "id", nullable = false)
    private PostOffice destination;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostalDispatchStatus status;
}
