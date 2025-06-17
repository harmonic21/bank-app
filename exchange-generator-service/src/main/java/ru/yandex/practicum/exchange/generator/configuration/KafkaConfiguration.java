package ru.yandex.practicum.exchange.generator.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.yandex.practicum.exchange.model.CurrencyInfo;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, CurrencyInfo> producerFactory(@Value("${kafka.producer.bootstraps}") String bootstraps) {
        Map<String, Object> producerParams = new HashMap<>();
        producerParams.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstraps);
        producerParams.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerParams.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(producerParams);
    }

    @Bean
    public KafkaTemplate<String, CurrencyInfo> kafkaTemplate(ProducerFactory<String, CurrencyInfo> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
