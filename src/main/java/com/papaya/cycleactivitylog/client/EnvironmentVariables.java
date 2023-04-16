package com.papaya.cycleactivitylog.client;

import com.papaya.cycleactivitylog.client.utils.SystemUtils;

public enum EnvironmentVariables {
    // Name of the SQS queue used by the client
    CYCLE_ACTIVITY_LOG_SQS_QUEUE_URL(null, true),

    // Name of the SQS queue used by the client
    CYCLE_ACTIVITY_LOG_PUBLISH_EMULATION_ENABLED("false", false),

    AWS_ENDPOINT_URL(null, false),
    ;

    private final String defaultValue;

    private final boolean required;

    EnvironmentVariables(String defaultValue, boolean required) {
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public String getValue() {
        var value = SystemUtils.getEnvOrDefault(this.name(), this.defaultValue);
        if (value == null && this.required) {
            throw new IllegalArgumentException("Undefined required property " + this.name());
        }
        return value;
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(getValue());
    }
}
