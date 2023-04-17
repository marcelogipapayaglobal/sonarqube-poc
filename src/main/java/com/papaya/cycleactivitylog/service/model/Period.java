package com.papaya.cycleactivitylog.service.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Period {
    private final LocalDateTime from;

    private final LocalDateTime to;

    public boolean isClosed() {
        return this.from != null && this.to != null;
    }

    public boolean isOpen() {
        return !this.isClosed();
    }

    public boolean isEmpty() {
        return this.from == null && this.to == null;
    }

    public boolean contains(LocalDateTime when) {
        return !this.from.isAfter(when) && !this.to.isBefore(when);
    }
}
