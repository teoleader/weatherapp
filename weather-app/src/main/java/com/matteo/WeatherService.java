package com.matteo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private final String geoBaseUrl;
    private final String weatherBaseUrl;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherService(String geoBaseUrl, String weatherBaseUrl) {
        this.geoBaseUrl = geoBaseUrl;
        this.weatherBaseUrl = weatherBaseUrl;
    }

    public Weather getWeatherByCity(String city) throws Exception {

        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("Città non valida");
        }

        city = city.trim();

        // 🔹 GEOCODING
        String geoUrl = geoBaseUrl + "/v1/search?name=" + city;

        HttpRequest geoRequest = HttpRequest.newBuilder()
                .uri(URI.create(geoUrl))
                .build();

        HttpResponse<String> geoResponse =
                client.send(geoRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode geoRoot = mapper.readTree(geoResponse.body());
        JsonNode results = geoRoot.path("results");

        if (!results.isArray() || results.size() == 0) {
            throw new RuntimeException("Città non trovata");
        }

        JsonNode result = results.get(0);

        double lat = result.path("latitude").asDouble();
        double lon = result.path("longitude").asDouble();

        // 🔹 WEATHER
        String weatherUrl = weatherBaseUrl + "/v1/forecast"
                + "?latitude=" + lat
                + "&longitude=" + lon
                + "&current_weather=true";

        HttpRequest weatherRequest = HttpRequest.newBuilder()
                .uri(URI.create(weatherUrl))
                .build();

        HttpResponse<String> weatherResponse =
                client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode weatherRoot = mapper.readTree(weatherResponse.body());
        JsonNode current = weatherRoot.path("current_weather");

        double temp = current.path("temperature").asDouble();
        double wind = current.path("windspeed").asDouble();

        return new Weather(temp, wind);
    }

    public void printFiveDayForecast(String city) throws Exception {

    if (city == null || city.trim().isEmpty()) {
        throw new IllegalArgumentException("Città non valida");
    }

    city = city.trim();
    String encodedCity = java.net.URLEncoder.encode(city, java.nio.charset.StandardCharsets.UTF_8);

    // 🔹 1. GEOCODING
    String geoUrl = geoBaseUrl + "/v1/search?name=" + encodedCity;

    HttpRequest geoRequest = HttpRequest.newBuilder()
            .uri(URI.create(geoUrl))
            .GET()
            .build();

    HttpResponse<String> geoResponse =
            client.send(geoRequest, HttpResponse.BodyHandlers.ofString());

    JsonNode geoRoot = mapper.readTree(geoResponse.body());
    JsonNode results = geoRoot.path("results");

    if (!results.isArray() || results.isEmpty()) {
        throw new RuntimeException("Città non trovata");
    }

    JsonNode result = results.get(0);

    double lat = result.path("latitude").asDouble();
    double lon = result.path("longitude").asDouble();

    // 🔹 2. FORECAST 5 GIORNI
    String forecastUrl = weatherBaseUrl + "/v1/forecast"
            + "?latitude=" + lat
            + "&longitude=" + lon
            + "&daily=temperature_2m_max,temperature_2m_min,windspeed_10m_max"
            + "&timezone=auto";

    HttpRequest forecastRequest = HttpRequest.newBuilder()
            .uri(URI.create(forecastUrl))
            .GET()
            .build();

    HttpResponse<String> forecastResponse =
            client.send(forecastRequest, HttpResponse.BodyHandlers.ofString());

    JsonNode root = mapper.readTree(forecastResponse.body());
    JsonNode daily = root.path("daily");

    JsonNode dates = daily.path("time");
    JsonNode tempMax = daily.path("temperature_2m_max");
    JsonNode tempMin = daily.path("temperature_2m_min");
    JsonNode windMax = daily.path("windspeed_10m_max");

    System.out.println(" Previsioni meteo 5 giorni per: " + city.toUpperCase());
    System.out.println("--------------------------------------------------");

    for (int i = 0; i < 5 && i < dates.size(); i++) {
        System.out.println( dates.get(i).asText());
        System.out.println("   Termperatura Max: " + tempMax.get(i).asDouble() + "°C");
        System.out.println("   Temperatura Min: " + tempMin.get(i).asDouble() + "°C");
        System.out.println("    Vento max: " + windMax.get(i).asDouble() + " km/h");
        System.out.println();
    }
}
}