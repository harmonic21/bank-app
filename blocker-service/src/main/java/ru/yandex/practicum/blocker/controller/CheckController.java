package ru.yandex.practicum.blocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.blocker.api.OperationApi;
import ru.yandex.practicum.blocker.model.CheckOperationRq;
import ru.yandex.practicum.blocker.model.CheckResultRs;

import java.util.List;
import java.util.Random;

@Slf4j
@RestController
public class CheckController implements OperationApi {

    private static final Random RANDOM = new Random();
    private static final List<String> BLOCKER_REASONS = List.of(
            "Пользователь в розыске",
            "Банк утратил доверие",
            "Большое количество операций за малый промежуток времени. Подозрительные действия",
            "Сумма перевода превышает лимиты дня, установленные ЦБ РФ"
    );

    @Override
    public ResponseEntity<CheckResultRs> checkClientOperation(CheckOperationRq checkOperationRq) {
        int checkResult = RANDOM.nextInt(100);
        CheckResultRs checkResultRs;
        if (checkResult < 4) {
            log.info("Операция {} запрещена: {}", checkOperationRq.getRequestAction(), BLOCKER_REASONS.get(checkResult));
            checkResultRs = new CheckResultRs().approved(false).reason(BLOCKER_REASONS.get(checkResult));
        } else {
            log.info("Операция {} одобрена.", checkOperationRq.getRequestAction());
            checkResultRs = new CheckResultRs().approved(true);
        }
        return ResponseEntity.ok(checkResultRs);
    }
}
