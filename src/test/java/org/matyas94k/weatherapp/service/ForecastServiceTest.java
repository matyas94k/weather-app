package org.matyas94k.weatherapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matyas94k.weatherapp.model.DailyForecast;
import org.matyas94k.weatherapp.model.WeatherApiResponse;
import org.matyas94k.weatherapp.model.WeatherResponse;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ForecastServiceTest {
    private ForecastService forecastService;

    @Mock
    private RestTemplate restTemplate;
    private final List<String> selectedCities = Collections.singletonList("Atlantis");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        forecastService = new ForecastService(restTemplate);
        ReflectionTestUtils.setField(forecastService, "selectedCities", selectedCities);
    }

    @Test
    void excludedCities_emptyResult() {
        final WeatherResponse result = forecastService.getForecastAverage(Collections.singletonList("Athens"));

        assertTrue(result.results().isEmpty());
    }

    @Test
    void weatherApiDown_exception() {
        assertThrows(IllegalStateException.class, () -> forecastService.getForecastAverage(Collections.singletonList("Atlantis")));
    }

    @Test
    void cityNotFound_blankResult() {
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenThrow(mock(HttpClientErrorException.NotFound.class));

        final WeatherResponse result = forecastService.getForecastAverage(Collections.singletonList("Atlantis"));

        assertTrue(result.results().get(0).temperature().isBlank());
        assertTrue(result.results().get(0).wind().isBlank());
    }

    @Test
    void success() {
        final WeatherApiResponse responseMock = mock(WeatherApiResponse.class);
        final DailyForecast forecastMock = mock(DailyForecast.class);
        when(forecastMock.temperature()).thenReturn(24);
        when(forecastMock.wind()).thenReturn(42);
        when(responseMock.dailyResults()).thenReturn(Collections.singletonList(forecastMock));
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(responseMock);

        final WeatherResponse result = forecastService.getForecastAverage(Collections.singletonList("Atlantis"));

        assertFalse(result.results().get(0).temperature().isBlank());
        assertFalse(result.results().get(0).wind().isBlank());
    }
}
