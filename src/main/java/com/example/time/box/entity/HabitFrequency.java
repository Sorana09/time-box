package com.example.time.box.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum HabitFrequency {
    DAILY, WEEKLY, MONTHLY;

    @JsonCreator
    public static HabitFrequency fromString(String value) {
        return HabitFrequency.valueOf(value.toUpperCase());
    }
}
