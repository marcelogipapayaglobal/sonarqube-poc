package com.papaya.cycleactivitylog.service;


import com.papaya.cycleactivitylog.service.utils.SystemUtils;

public enum EnvironmentVariables {
    // Name of the SQS queue used by the client
    CYCLE_ACTIVITY_LOG_SQS_QUEUE_URL(null, true),

    // Name of the DynamoDb table for logged items
    CYCLE_ACTIVITY_LOG_DYNAMODB_LOGGED_ITEMS_TABLE_NAME(null, true),

    CYCLE_ACTIVITY_LOG_SQS_BATCH_SIZE("10", true),

    CYCLE_ACTIVITY_LOG_SQS_WAIT_TIME_SECONDS("10", true),

    CYCLE_ACTIVITY_OPENAPI_ENABLED("true", false),

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

    public int getIntValue() {
        return Integer.parseInt(this.getValue());
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(getValue());
    }
}
