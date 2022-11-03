package ru.itis.suhd.service;

import io.debezium.data.Envelope;

import java.util.Map;


public interface DwhService {


    void replicateData(Map<String, Object> objectMap, Envelope.Operation operation);
}
