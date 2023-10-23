package org.matyas94k.weatherapp.controller;

import org.matyas94k.weatherapp.model.WeatherResponse;
import org.matyas94k.weatherapp.service.ForecastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class ForecastController {
    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam(value = "city", required = false) List<String> cityList) {
        if (null == cityList) {
            return ResponseEntity.badRequest().body("Request parameter \"city\" needed to specify the queried cities, in a comma separated list");
        }
        try {
            final WeatherResponse results = forecastService.getForecastAverage(cityList);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.internalServerError().body("Something went wrong on our side");
        }
    }
}
