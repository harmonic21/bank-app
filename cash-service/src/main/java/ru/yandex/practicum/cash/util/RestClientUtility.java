package ru.yandex.practicum.cash.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestClientUtility {

    public static boolean isWebClientResponseException(Throwable throwable) {
        return throwable instanceof WebClientResponseException;
    }

    public static <T> T getResponseBodyFromError(Throwable t, Class<T> bodyClass) {
        if (t instanceof WebClientResponseException ex) {
            return ex.getResponseBodyAs(bodyClass);
        }
        return null;
    }
}
