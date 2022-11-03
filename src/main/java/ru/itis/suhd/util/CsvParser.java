package ru.itis.suhd.util;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvParser {
    public static <T> List<T> parse(String path, Class<T> tClass) {
        try {
            return new CsvToBeanBuilder<T>(new FileReader(path))
                    .withSeparator(',')
                    .withType(tClass)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
