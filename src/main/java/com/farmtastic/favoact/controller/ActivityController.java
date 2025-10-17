package com.farmtastic.favoact.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmtastic.favoact.entity.Activity;

import java.util.Arrays;
import java.util.List;

@RestController
public class ActivityController {

    @GetMapping("/api/activities")
    public List<Activity> getActivities(){
        return Arrays.asList(
            new Activity(1L, "農場採摘體驗", "2025-10-15", "🌾"),
            new Activity(2L, "手作果醬課程", "2025-10-18", "🍓"),
            new Activity(3L, "有機蔬菜教學", "2025-10-20", "🥕"),
            new Activity(4L, "自然生態導覽", "2025-10-22", "🍏"),
            new Activity(5L, "親子農耕活動", "2025-10-25", "🎃")
        );
    }
}
