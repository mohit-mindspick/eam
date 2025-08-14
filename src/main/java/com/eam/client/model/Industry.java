package com.eam.client.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "industries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String industryCode;

    @Column(nullable = false, length = 100)
    private String name;
}