package com.javanauta.ts.taskscheduler.infrastructure.persistence.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.ZoneId;

@ReadingConverter
public class ZoneIdReadConverter implements Converter<String, ZoneId> {

    @Override
    public ZoneId convert(String source) {
        return ZoneId.of(source);
    }
}