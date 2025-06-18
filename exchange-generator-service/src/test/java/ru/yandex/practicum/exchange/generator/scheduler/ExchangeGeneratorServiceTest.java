package ru.yandex.practicum.exchange.generator.scheduler;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import ru.yandex.practicum.exchange.generator.configuration.KafkaConfiguration;
import ru.yandex.practicum.exchange.model.CurrencyInfo;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {KafkaConfiguration.class, ExchangeGeneratorService.class, ExchangeGeneratorServiceTest.TestKafkaConfiguration.class})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ExchangeGeneratorServiceTest {

    @Autowired
    private ConsumerFactory<String, CurrencyInfo> consumerFactory;

    @Autowired
    private ExchangeGeneratorService exchangeGeneratorService;

    @Test
    void shouldSendToTopic() {
        exchangeGeneratorService.generateActualExchangeInfo();
        Consumer<String, CurrencyInfo> testConsumer = consumerFactory.createConsumer();
        testConsumer.subscribe(List.of("CURRENCY.ACTUAL"));
        ConsumerRecords<String, CurrencyInfo> poll = testConsumer.poll(Duration.ofSeconds(5));
        assertNotNull(poll);
    }

    @EnableKafka
    @Configuration
    public static class TestKafkaConfiguration {

        @Bean
        public ConsumerFactory<String, CurrencyInfo> consumerFactory() {
            Map<String, Object> consumerParams = new HashMap<>();
            consumerParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            consumerParams.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
            return new DefaultKafkaConsumerFactory<>(consumerParams, new StringDeserializer(), new JsonDeserializer<>(CurrencyInfo.class));
        }
    }

}