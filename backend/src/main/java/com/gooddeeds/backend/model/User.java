//package com.gooddeeds.backend.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Table(name = "users")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String passwordHash;
//
//    @Builder.Default
//    @Column(nullable = false, updatable = false)
//    private Instant createdAt = Instant.now();
//
//    @Builder.Default
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CauseMembership> memberships = new ArrayList<>();
//
//    @Builder.Default
//    @OneToMany(mappedBy = "ownerUser", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Goal> goals = new ArrayList<>();
//}
//package com.gooddeeds.backend.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Table(
//        name = "users",
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "email")
//        }
//)
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    @Column(nullable = false, length = 100)
//    private String name;
//
//    @Column(nullable = false, unique = true, length = 150)
//    private String email;
//
//    @Column(nullable = false)
//    private String passwordHash;
//
//    @Column(nullable = false, updatable = false)
//    private Instant createdAt;
//
//    /* ---------------- RELATIONSHIPS ---------------- */
//
//    @JsonIgnore
//    @OneToMany(
//            mappedBy = "user",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<CauseMembership> memberships = new ArrayList<>();
//
//    @JsonIgnore
//    @OneToMany(
//            mappedBy = "ownerUser",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Goal> goals = new ArrayList<>();
//
//    /* ---------------- LIFECYCLE ---------------- */
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = Instant.now();
//    }
//}

package com.gooddeeds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /* 🔥 IMPORTANT: Prevent infinite JSON recursion */
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CauseMembership> memberships = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}



