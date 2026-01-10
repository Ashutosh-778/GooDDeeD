package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.CauseMembership;
import com.gooddeeds.backend.model.Role;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.CauseMembershipRepository;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.repository.UserRepository;
import com.gooddeeds.backend.service.MembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {
    private final CauseMembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final CauseRepository causeRepository;

    public MembershipServiceImpl(CauseMembershipRepository membershipRepository, UserRepository userRepository, CauseRepository causeRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.causeRepository = causeRepository;
    }

    @Override
    public CauseMembership joinCause(UUID userId, UUID causeId, String roleStr) {
        if (membershipRepository.findByUserIdAndCauseId(userId, causeId).isPresent()) {
            throw new IllegalArgumentException("User is already a member of the cause");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cause cause = causeRepository.findById(causeId).orElseThrow(() -> new IllegalArgumentException("Cause not found"));

        Role role = Role.SUPPORTER;
        if (roleStr != null && roleStr.equalsIgnoreCase("ADMIN")) role = Role.ADMIN;

        CauseMembership membership = CauseMembership.builder()
                .user(user)
                .cause(cause)
                .role(role)
                .approved(!cause.isRestricted()) // auto-approved if cause is not restricted
                .build();

        return membershipRepository.save(membership);
    }

    @Override
    public CauseMembership approveMembership(UUID approverUserId, UUID membershipId) {
        CauseMembership toApprove = membershipRepository.findById(membershipId).orElseThrow(() -> new IllegalArgumentException("Membership not found"));

        // check approver is admin of the same cause
        boolean isAdmin = membershipRepository.findByUserIdAndCauseId(approverUserId, toApprove.getCause().getId())
                .map(m -> m.getRole() == Role.ADMIN && m.isApproved())
                .orElse(false);

        if (!isAdmin) throw new SecurityException("Only an approved admin can approve memberships");

        toApprove.setApproved(true);
        return membershipRepository.save(toApprove);
    }
}
