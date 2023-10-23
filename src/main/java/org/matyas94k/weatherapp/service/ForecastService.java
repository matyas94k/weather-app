package org.matyas94k.weatherapp.service;

import org.matyas94k.weatherapp.model.DailyForecast;
import org.matyas94k.weatherapp.model.ForecastResult;
import org.matyas94k.weatherapp.model.WeatherApiResponse;
import org.matyas94k.weatherapp.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Function;

@Service
public class ForecastService {
    @Value("${weather-api.url}")
    private String WEATHER_API_URL;
    @Value("${selected-cities}")
    private List<String> selectedCities;

    private final RestTemplate restTemplate;

    public ForecastService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getForecastAverage(List<String> cityList) {
        final Function<String, ForecastResult> getForecast = city -> {
            try {
                final WeatherApiResponse apiResponse = restTemplate.getForObject("https://" + WEATHER_API_URL + "/" + city, WeatherApiResponse.class);
                if (null == apiResponse) {
                    throw new IllegalStateException("The weather api returned null");
                }
                final List<DailyForecast> forecastList = apiResponse.dailyResults();
                final double avgTemp = forecastList.stream().mapToInt(DailyForecast::temperature).summaryStatistics().getAverage();
                final double avgWind = forecastList.stream().mapToInt(DailyForecast::wind).summaryStatistics().getAverage();
                return new ForecastResult(city, Integer.toString((int) avgTemp), Integer.toString((int) avgWind));
            } catch (HttpClientErrorException.NotFound e) {
                return new ForecastResult(city, "", "");
            }
        };
        return new WeatherResponse(cityList.stream().distinct().filter(selectedCities::contains).sorted().map(getForecast).toList());
    }
}
