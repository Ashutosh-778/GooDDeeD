package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.CauseMembership;
import com.gooddeeds.backend.model.Role;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.CauseMembershipRepository;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.repository.UserRepository;
import com.gooddeeds.backend.service.MembershipService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipServiceImpl implements MembershipService {

    private final UserRepository userRepository;
    private final CauseRepository causeRepository;
    private final CauseMembershipRepository membershipRepository;

    //join cause

    @Override
    public CauseMembership joinCause(UUID userId, UUID causeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cause cause = causeRepository.findById(causeId)
                .orElseThrow(() -> new RuntimeException("Cause not found"));

        if (membershipRepository.existsByUserAndCause(user, cause)) {
            throw new RuntimeException("User already joined this cause");
        }

        boolean approved = !cause.isRestricted();

        // First member becomes ADMIN
        Role role = membershipRepository.findByCauseId(causeId).isEmpty()
                ? Role.ADMIN
                : Role.SUPPORTER;

        CauseMembership membership = CauseMembership.builder()
                .user(user)
                .cause(cause)
                .role(role)
                .approved(approved || role == Role.ADMIN)
                .build();

        return membershipRepository.save(membership);
    }

    //get membership by ID

    @Override
    public CauseMembership getMembershipById(UUID membershipId) {
        return membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    //get members of cause

    @Override
    public List<CauseMembership> getMembersOfCause(UUID causeId) {
        return membershipRepository.findByCauseId(causeId);
    }

    //get memberships by user ID

    @Override
    public List<CauseMembership> getMembershipsByUserId(UUID userId) {
        return membershipRepository.findByUserId(userId);
    }

    //approve member by the admin

    @Override
    public CauseMembership approveMembership(UUID adminUserId, UUID membershipId) {

        CauseMembership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        UUID causeId = membership.getCause().getId();

        CauseMembership adminMembership =
                membershipRepository.findByUserIdAndCauseId(adminUserId, causeId)
                        .orElseThrow(() -> new RuntimeException("Admin not part of this cause"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can approve members");
        }

        membership.approve();
        return membershipRepository.save(membership);
    }

    //reject member by the admin

    @Override
    public void rejectMembership(UUID adminUserId, UUID membershipId) {

        CauseMembership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        UUID causeId = membership.getCause().getId();

        CauseMembership adminMembership =
                membershipRepository.findByUserIdAndCauseId(adminUserId, causeId)
                        .orElseThrow(() -> new RuntimeException("Admin not part of this cause"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can reject members");
        }

        membershipRepository.delete(membership);
    }

    //leave cause

    @Override
    public void leaveCause(UUID userId, UUID causeId) {
        CauseMembership membership = membershipRepository.findByUserIdAndCauseId(userId, causeId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        membershipRepository.delete(membership);
    }
}


