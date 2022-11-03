package ru.itis.suhd.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.itis.suhd.kafka.service.KafkaMessageService;

@Service
@RequiredArgsConstructor
public class KafkaMessageServiceImpl implements KafkaMessageService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(String topic, Object object) {
        kafkaTemplate.send(topic, object);
    }
}
