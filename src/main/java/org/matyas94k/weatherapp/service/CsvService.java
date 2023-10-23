package org.matyas94k.weatherapp.service;

import org.matyas94k.weatherapp.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class CsvService {
    @Value("${result.filename}")
    private String filename;

    public void saveToFile(WeatherResponse weatherResponse) throws IOException {
        final File csvOutputFile = new File(filename);
        csvOutputFile.createNewFile();
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println("Name,temperature,wind");
            weatherResponse.results().stream().map(fr -> fr.city() + "," + fr.temperature() + "," + fr.wind()).forEach(pw::println);
        }
    }
}
