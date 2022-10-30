package com.timeseries.seriestemporelles.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum UserPrivilage {
    READ_PRIVILAGE, WRITE_PRIVILAGE;
    
    private static Map<String, UserPrivilage> namesMap = new HashMap<>(2);
    
    static {
        namesMap.put("read", READ_PRIVILAGE);
        namesMap.put("write", WRITE_PRIVILAGE);
    }
    
    @JsonCreator
    public static UserPrivilage forValue(String value) {
        return namesMap.get(value);
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, UserPrivilage> entry: namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }
        return null;
    }
}
