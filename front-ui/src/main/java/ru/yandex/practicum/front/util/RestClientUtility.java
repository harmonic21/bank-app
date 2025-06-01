package ru.yandex.practicum.front.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestClientUtility {
    public static final String ACCOUNT_SERVICE_NOT_AVAILABLE_ERROR = "Сервис аккаунтов временно не доступен. Попробуйте повторить операцию позже";

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
