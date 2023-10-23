package org.matyas94k.weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WeatherApp {
    public static void main(String[] args) {
        SpringApplication.run(WeatherApp.class, args);
        System.out.println("Weather app listening");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
