package ru.yandex.practicum.notification.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.yandex.practicum.notification.interceptor.KafkaEventInterceptor;
import ru.yandex.practicum.notification.model.SendNotificationRq;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Bean
    public ConsumerFactory<String, SendNotificationRq> consumerFactory(@Value("${kafka.consumer.bootstraps}") String bootstraps,
                                                                       @Value("${kafka.consumer.groupId}") String groupId) {
        Map<String, Object> consumerParams = new HashMap<>();
        consumerParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstraps);
        consumerParams.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(consumerParams, new StringDeserializer(), new JsonDeserializer<>(SendNotificationRq.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SendNotificationRq> kafkaListenerContainerFactory(ConsumerFactory<String, SendNotificationRq> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, SendNotificationRq> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordInterceptor(new KafkaEventInterceptor());
        return factory;
    }
}
