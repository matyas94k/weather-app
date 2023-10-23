package org.matyas94k.weatherapp.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matyas94k.weatherapp.model.WeatherResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CsvServiceTest {
    private CsvService csvService;

    private static final String FILENAME = "test.csv";

    @BeforeEach
    void setup() {
        csvService = new CsvService();
        ReflectionTestUtils.setField(csvService, "filename", FILENAME);
    }

    @Test
    void success() throws IOException {
        final File file = new File(FILENAME);

        csvService.saveToFile(mock(WeatherResponse.class));

        assertTrue(file.exists());
    }

    @AfterAll
    static void cleanup() {
        final File file = new File(FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
