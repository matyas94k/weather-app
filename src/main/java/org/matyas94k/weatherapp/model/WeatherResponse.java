package org.matyas94k.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherResponse(@JsonProperty("result") List<ForecastResult> results) {
}
