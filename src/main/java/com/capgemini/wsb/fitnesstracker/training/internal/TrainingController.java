package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDto;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    public final TrainingServiceImpl trainingService;
    public final TrainingMapper trainingMapper;

    @GetMapping
    public List<TrainingDto> getAllTraining() {
        return trainingService.findAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("/{userId}")
    public List<TrainingDto> getTrainingsForUser(@PathVariable Long userId) {
        return trainingService.findTrainingsByUserId(userId)
                .stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/finished/{afterTime}")
    public List<TrainingDto> getFinishedTrainingsAfter(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterTime) {
        return trainingService.findFinishedTrainingsAfter(afterTime)
                .stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/activityType")
    public List<TrainingDto> getTrainingsByActivityType(@RequestParam ActivityType activityType) {
        return trainingService.findTrainingsByActivityType(activityType)
                .stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto createTraining(@RequestBody TrainingRequestDto trainingRequestDto) {
        Training training = trainingService.createTraining(trainingRequestDto);
        return trainingMapper.toDto(training);
    }

    @PutMapping("/{trainingId}")
    public TrainingDto updateTraining(
            @PathVariable Long trainingId,
            @RequestBody TrainingRequestDto trainingRequestDto) {
        Training updatedTraining = trainingService.updateTraining(trainingId, trainingRequestDto);
        return trainingMapper.toDto(updatedTraining);
    }
}
