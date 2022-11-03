package ru.itis.suhd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.suhd.service.DataGenerateService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/generate")
@RequiredArgsConstructor
public class DataGenerateController {

    private final DataGenerateService dataGenerateService;

    @PostMapping("/postgres")
    public void generatePostgresData(@RequestParam(required = false, defaultValue = "1000") int count) {
        dataGenerateService.generatePostgresData(count);
    }

    @PostMapping("/mongo")
    public void generateMongoData() {
        dataGenerateService.generateMongoData();
    }

    @PostMapping("/kafka")
    public void generateKafkaLogs() throws IOException {
        dataGenerateService.generateKafkaLogs();
    }
}
