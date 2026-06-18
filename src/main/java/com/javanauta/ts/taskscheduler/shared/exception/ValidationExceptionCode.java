package com.javanauta.ts.taskscheduler.shared.exception;

import com.javanauta.ts.taskscheduler.shared.exception.enums.ValidationExceptionSourceType;

public interface ValidationExceptionCode extends ExceptionCode {
    public ValidationExceptionSourceType getExceptionSourceType();
}
