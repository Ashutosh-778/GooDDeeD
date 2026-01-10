package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.CauseMembership;
import com.gooddeeds.backend.model.Role;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.CauseMembershipRepository;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.repository.UserRepository;
import com.gooddeeds.backend.service.impl.MembershipServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MembershipServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CauseRepository causeRepository;

    @Autowired
    private CauseMembershipRepository membershipRepository;

    @Autowired
    private MembershipService membershipService;

    @Test
    void joinTwiceShouldFail() {
        User u = userRepository.save(User.builder().name("A").email("a@example.com").passwordHash("x").build());
        Cause c = causeRepository.save(Cause.builder().name("C").build());

        CauseMembership m1 = membershipService.joinCause(u.getId(), c.getId(), "SUPPORTER");
        Assertions.assertNotNull(m1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            membershipService.joinCause(u.getId(), c.getId(), "SUPPORTER");
        });
    }

    @Test
    void onlyAdminCanApproveRestricted() {
        User admin = userRepository.save(User.builder().name("Admin").email("admin@example.com").passwordHash("x").build());
        User bob = userRepository.save(User.builder().name("Bob").email("bob@example.com").passwordHash("x").build());

        Cause restricted = causeRepository.save(Cause.builder().name("R").restricted(true).build());

        // make admin an approved admin
        CauseMembership adm = membershipRepository.save(CauseMembership.builder().user(admin).cause(restricted).role(Role.ADMIN).approved(true).build());

        // Bob requests to join
        CauseMembership bobReq = membershipService.joinCause(bob.getId(), restricted.getId(), "SUPPORTER");
        Assertions.assertFalse(bobReq.isApproved());

        // Non-admin should not be able to approve
        Assertions.assertThrows(SecurityException.class, () -> {
            membershipService.approveMembership(bob.getId(), bobReq.getId());
        });

        // Admin approves
        CauseMembership approved = membershipService.approveMembership(admin.getId(), bobReq.getId());
        Assertions.assertTrue(approved.isApproved());
    }
}
