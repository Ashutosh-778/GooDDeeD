package com.gooddeeds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "cause_memberships",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "cause_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CauseMembership {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cause_id", nullable = false)
    private Cause cause;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean approved;

    @Column(nullable = false, updatable = false)
    private Instant joinedAt;

    /* ---------- Lifecycle ---------- */

    @PrePersist
    protected void onCreate() {
        this.joinedAt = Instant.now();
    }

    /* ---------- Domain behavior ---------- */

    public void approve() {
        this.approved = true;
    }
}


