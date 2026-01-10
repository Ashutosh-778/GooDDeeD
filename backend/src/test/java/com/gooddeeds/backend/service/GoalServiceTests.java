package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.Goal;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
public class GoalServiceTests {
    @Autowired
    private GoalService goalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CauseRepository causeRepository;

    @Test
    void goalMustBelongToUserOrCause() {
        Goal g = Goal.builder().title("G").build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            goalService.create(g);
        });

        User u = userRepository.save(User.builder().name("U").email("u@example.com").passwordHash("x").build());
        Goal gu = Goal.builder().title("Gu").ownerUser(u).build();
        Goal created = goalService.create(gu);
        Assertions.assertNotNull(created.getId());

        Cause c = causeRepository.save(Cause.builder().name("C").build());
        Goal gc = Goal.builder().title("Gc").cause(c).build();
        Goal created2 = goalService.create(gc);
        Assertions.assertNotNull(created2.getId());
    }

    @Test
    void goalCannotBelongToBothUserAndCause() {
        User u = userRepository.save(User.builder().name("U2").email("u2@example.com").passwordHash("x").build());
        Cause c = causeRepository.save(Cause.builder().name("C2").build());
        Goal g = Goal.builder().title("GBoth").ownerUser(u).cause(c).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            goalService.create(g);
        });
    }
}

