package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingRequestDto {

    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ActivityType activityType;
    private double distance;
    private double averageSpeed;

}