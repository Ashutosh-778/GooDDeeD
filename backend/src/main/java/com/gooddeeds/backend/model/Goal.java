package com.gooddeeds.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User ownerUser;

    @ManyToOne
    @JoinColumn(name = "cause_id")
    private Cause cause;

    @Builder.Default
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubGoal> subGoals = new ArrayList<>();

    @Builder.Default
    @Column(nullable = false)
    private int progress = 0; // 0-100

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @PrePersist
    @PreUpdate
    private void validateOwnership() {
        boolean hasUser = this.ownerUser != null;
        boolean hasCause = this.cause != null;
        if (hasUser == hasCause) { // either both true or both false
            throw new IllegalArgumentException("Goal must belong to exactly one of user or cause");
        }
    }
}
