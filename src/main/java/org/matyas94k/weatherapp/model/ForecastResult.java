package org.matyas94k.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ForecastResult(@JsonProperty("name") String city, String temperature, String wind) {
}
