package ru.itis.suhd.kafka.service;

public interface KafkaMessageService {
    void sendMessage(String topic, Object object);
}
