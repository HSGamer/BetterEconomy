package me.hsgamer.bettereconomy.config.converter;

import me.hsgamer.hscore.config.annotation.converter.Converter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class StringObjectMapConverter implements Converter {
    @Override
    public Object convert(Object raw) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (raw instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) raw).entrySet()) {
                map.put(Objects.toString(entry.getKey()), entry.getValue());
            }
        }
        return map;
    }

    @Override
    public Object convertToRaw(Object value) {
        return value;
    }
}
