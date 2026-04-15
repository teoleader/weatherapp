package com.matteo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

    public static void main(String[] args) throws Exception {
        /*
         * Scanner scanner = new Scanner(System.in);
         * ObjectMapper mapper = new ObjectMapper();
         * System.out.print("Inserisci città: ");
         * String city = scanner.nextLine();
         * 
         * HttpClient client = HttpClient.newHttpClient();
         * 
         * // 1. GEOCODING
         * String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" +
         * city;
         * 
         * HttpRequest geoRequest = HttpRequest.newBuilder()
         * .uri(URI.create(geoUrl))
         * .build();
         * 
         * HttpResponse<String> geoResponse =
         * client.send(geoRequest, HttpResponse.BodyHandlers.ofString());
         * 
         * String geoJson = geoResponse.body();
         * 
         * // parsing semplice (senza librerie)
         * // double lat = extractValue(geoJson, "\"latitude\":", ",");
         * // double lon = extractValue(geoJson, "\"longitude\":", ",");
         * 
         * JsonNode geoRoot = mapper.readTree(geoJson);
         * 
         * JsonNode result = geoRoot.path("results").get(0);
         * 
         * if (result == null) {
         * throw new RuntimeException("Città non trovata");
         * }
         * 
         * double lat = result.path("latitude").asDouble();
         * double lon = result.path("longitude").asDouble();
         * 
         * // 2. WEATHER API
         * String weatherUrl = "https://api.open-meteo.com/v1/forecast"
         * + "?latitude=" + lat
         * + "&longitude=" + lon
         * + "&current_weather=true";
         * 
         * HttpRequest weatherRequest = HttpRequest.newBuilder()
         * .uri(URI.create(weatherUrl))
         * .build();
         * 
         * HttpResponse<String> weatherResponse =
         * client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
         * 
         * 
         * // double temp = extractValue(weatherJson, "\"temperature\":", ",");
         * //double wind = extractValue(weatherJson, "\"windspeed\":", ",");
         * String weatherJson = weatherResponse.body();
         * 
         * JsonNode weatherRoot = mapper.readTree(weatherJson);
         * 
         * JsonNode current = weatherRoot.path("current_weather");
         * 
         * double temp = current.path("temperature").asDouble();
         * double wind = current.path("windspeed").asDouble();
         * 
         * System.out.println("\n🌤️ Meteo per: " + city);
         * System.out.println("🌡️ Temperatura: " + temp + "°C");
         * System.out.println("💨 Vento: " + wind + " km/h");
         * try {
         * 
         * ProcessBuilder pb = new ProcessBuilder(
         * "node",
         * "C:\\Users\\matteog\\Desktop\\Personale\\CorsoGeneration\\weather-app2\\weather-app\\scripts\\weather.js"
         * ,
         * city
         * );
         * 
         * pb.redirectErrorStream(true);
         * Process process = pb.start();
         * 
         * BufferedReader reader = new BufferedReader(
         * new InputStreamReader(process.getInputStream())
         * );
         * 
         * String line;
         * while ((line = reader.readLine()) != null) {
         * System.out.println(line);
         * }
         * 
         * process.waitFor();
         * 
         * } catch (Exception e) {
         * e.printStackTrace();
         * }
         * System.out.println("Prova");
         * // System.out.println(getWeatherByCity("Milano"));
         * 
         */

        Scanner scanner = new Scanner(System.in);

        String city;

        while (true) {
            System.out.print("Inserisci città: ");
            city = scanner.nextLine();

            if (city != null && city.matches("[a-zA-Z\\s]+") && !city.trim().isEmpty()) {
                break; // ✅ input valido → esci dal ciclo
            }

            System.out.println("❌ Input non valido. Inserisci solo lettere.");
        }

        WeatherService service = new WeatherService(
                "https://geocoding-api.open-meteo.com",
                "https://api.open-meteo.com");
        /*
         * Weather weather = service.getWeatherByCity(city);
         * 
         * System.out.println("\n🌤️ Meteo per: " + city);
         * System.out.println("🌡️ Temperatura: " + weather.getTemperature() + "°C");
         * System.out.println("💨 Vento: " + weather.getWindSpeed() + " km/h");
         */
        service.printFiveDayForecast(city);
        scanner.close();

    }

    /*
     * // helper super semplice (non perfetto ma ok per iniziare)
     * private static double extractValue(String json, String key, String endChar) {
     * int start = json.indexOf(key) + key.length();
     * int end = json.indexOf(endChar, start);
     * return Double.parseDouble(json.substring(start, end).replaceAll("[^0-9.\\-]",
     * ""));
     * }
     */
}