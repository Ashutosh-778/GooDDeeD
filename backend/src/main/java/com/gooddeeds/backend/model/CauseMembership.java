package com.gooddeeds.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cause_memberships", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "cause_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauseMembership {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cause_id")
    private Cause cause;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.SUPPORTER;

    @Builder.Default
    @Column(nullable = false)
    private boolean approved = false;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant joinedAt = Instant.now();
}
