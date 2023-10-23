package org.matyas94k.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherApiResponse(int temperature, int wind, String description, @JsonProperty("forecast") List<DailyForecast> dailyResults) {
}
