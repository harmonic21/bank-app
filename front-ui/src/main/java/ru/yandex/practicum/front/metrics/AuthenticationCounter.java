package ru.yandex.practicum.front.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationCounter {

    private static final String NAME = "authentication_result";
    private static final String FAIL = "FAIL";
    private static final String SUCCESS = "SUCCESS";

    private final MeterRegistry meterRegistry;

    public void incrementSuccess(String username) {
        Counter.builder(NAME)
                .tag("result", SUCCESS)
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public void incrementFail(String username) {
        Counter.builder(NAME)
                .tag("result", FAIL)
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }
}
