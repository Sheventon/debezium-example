package ru.itis.suhd.kafka.service;

import ru.itis.suhd.kafka.model.WatchLog;

public interface KafkaMessageListener {

    void onMessage(WatchLog watchLog);
}
