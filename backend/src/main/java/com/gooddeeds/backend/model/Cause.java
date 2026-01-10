package com.gooddeeds.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Builder.Default
    @Column(nullable = false)
    private boolean restricted = false;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Builder.Default
    @OneToMany(mappedBy = "cause", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CauseMembership> memberships = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "cause", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals = new ArrayList<>();
}
