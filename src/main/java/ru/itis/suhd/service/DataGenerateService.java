package ru.itis.suhd.service;

import java.io.IOException;

public interface DataGenerateService {

    void generatePostgresData(int count);

    void generateMongoData();

    void generateKafkaLogs() throws IOException;
}
