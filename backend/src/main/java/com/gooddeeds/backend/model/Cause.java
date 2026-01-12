package com.gooddeeds.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "causes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cause {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private boolean restricted;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "cause", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CauseMembership> memberships = new ArrayList<>();

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }
}



