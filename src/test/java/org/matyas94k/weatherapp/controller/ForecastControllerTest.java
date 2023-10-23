package org.matyas94k.weatherapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matyas94k.weatherapp.model.WeatherResponse;
import org.matyas94k.weatherapp.service.ForecastService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ForecastControllerTest {
    private ForecastController forecastController;

    @Mock
    private ForecastService forecastService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        forecastController = new ForecastController(forecastService);
    }

    @Test
    void getWeather_missingRequestParam_badRequest() {
        final ResponseEntity<?> response = forecastController.getWeather(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getWeather_success_ok() {
        when(forecastService.getForecastAverage(anyList())).thenReturn(mock(WeatherResponse.class));

        final ResponseEntity<?> response = forecastController.getWeather(Collections.singletonList("Atlantis"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getWeather_serviceThrows_internalServerError() {
        when(forecastService.getForecastAverage(anyList())).thenThrow(new RuntimeException("service error"));

        final ResponseEntity<?> response = forecastController.getWeather(Collections.singletonList("Atlantis"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
