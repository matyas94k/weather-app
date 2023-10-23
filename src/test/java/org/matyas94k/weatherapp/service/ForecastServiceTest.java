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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ForecastServiceTest {
    private ForecastService forecastService;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private CsvService csvService;
    private final List<String> SELECTED_CITIES = Collections.singletonList("Atlantis");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        forecastService = new ForecastService(restTemplate, csvService);
        ReflectionTestUtils.setField(forecastService, "selectedCities", SELECTED_CITIES);
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
    void csvServiceThrows_continue() throws IOException {
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(mock(WeatherApiResponse.class));
        doThrow(mock(IOException.class)).when(csvService).saveToFile(any(WeatherResponse.class));

        final WeatherResponse result = forecastService.getForecastAverage(Collections.singletonList("Atlantis"));

        verify(csvService).saveToFile(any(WeatherResponse.class));
    }

    @Test
    void success() throws IOException {
        final WeatherApiResponse responseMock = mock(WeatherApiResponse.class);
        final DailyForecast forecastMock = mock(DailyForecast.class);
        when(forecastMock.temperature()).thenReturn(24);
        when(forecastMock.wind()).thenReturn(42);
        when(responseMock.dailyResults()).thenReturn(Collections.singletonList(forecastMock));
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(responseMock);

        final WeatherResponse result = forecastService.getForecastAverage(Collections.singletonList("Atlantis"));

        assertFalse(result.results().get(0).temperature().isBlank());
        assertFalse(result.results().get(0).wind().isBlank());
        verify(csvService).saveToFile(any(WeatherResponse.class));
    }
}
