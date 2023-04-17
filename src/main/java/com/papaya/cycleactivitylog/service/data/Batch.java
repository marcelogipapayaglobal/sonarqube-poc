package com.papaya.cycleactivitylog.service.data;

import java.util.List;

public interface Batch<T> {

    List<T> getItems();

    boolean isEmpty();

    void delete();
}
