package ru.itis.suhd.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic eventStatusTopic() {
        return TopicBuilder.name("watchLogsTopic")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
