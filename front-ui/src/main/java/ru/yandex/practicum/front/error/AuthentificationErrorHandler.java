package ru.yandex.practicum.front.error;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import ru.yandex.practicum.front.metrics.AuthenticationCounter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthentificationErrorHandler implements AuthenticationFailureHandler {

    private final AuthenticationCounter authenticationCounter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("Ошибка аутентификации пользователя {}", exception.getAuthenticationRequest().getName());
        authenticationCounter.incrementFail(exception.getAuthenticationRequest().getName());
    }
}
