package com.javanauta.ts.taskscheduler.infrastructure.persistence.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.ZoneId;

@WritingConverter
public class ZoneIdWriteConverter implements Converter<ZoneId, String> {

    @Override
    public String convert(ZoneId source) {
        return source.getId();
    }
}