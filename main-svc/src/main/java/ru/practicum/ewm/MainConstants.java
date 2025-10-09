package ru.practicum.ewm;

import java.time.format.DateTimeFormatter;

public final class MainConstants {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static final String EWM_APP_NAME = "ewm-main-service";

    private MainConstants() {
    }
}
