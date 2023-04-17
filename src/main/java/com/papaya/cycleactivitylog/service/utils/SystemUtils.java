package com.papaya.cycleactivitylog.service.utils;

public class SystemUtils {
    public static String getEnvOrDefault(String name, String defaultVal) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return defaultVal;
        }
        return value;
    }
}
