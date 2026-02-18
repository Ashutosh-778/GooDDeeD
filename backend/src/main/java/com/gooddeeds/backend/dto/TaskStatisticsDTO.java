package com.gooddeeds.backend.dto;

import java.util.UUID;

public record TaskStatisticsDTO(
        long totalTasks,
        long completedTasks,
        long ongoingTasks,
        long comingUpTasks,
        UUID causeId,
        String causeName,
        UUID goalId,
        String goalTitle
) {
    // Constructor for overall statistics (no cause/goal filter)
    public TaskStatisticsDTO(long totalTasks, long completedTasks, long ongoingTasks, long comingUpTasks) {
        this(totalTasks, completedTasks, ongoingTasks, comingUpTasks, null, null, null, null);
    }

    // Constructor for cause-filtered statistics
    public TaskStatisticsDTO(long totalTasks, long completedTasks, long ongoingTasks, long comingUpTasks,
                             UUID causeId, String causeName) {
        this(totalTasks, completedTasks, ongoingTasks, comingUpTasks, causeId, causeName, null, null);
    }
}
