package com.matteo;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

public class WeatherServiceTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeEach
    void resetMocks() {
        wireMockServer.resetAll();
    }

    // ✅ TEST 1: città valida
    @Test
    void testGetWeatherSuccess() throws Exception {

        // Mock GEOCODING
        stubFor(get(urlPathEqualTo("/v1/search"))
                .willReturn(okJson("""
                {
                  "results": [
                    {
                      "latitude": 45.4642,
                      "longitude": 9.1900
                    }
                  ]
                }
                """)));

        // Mock WEATHER
        stubFor(get(urlPathEqualTo("/v1/forecast"))
                .willReturn(okJson("""
                {
                  "current_weather": {
                    "temperature": 20.5,
                    "windspeed": 10.2
                  }
                }
                """)));

        WeatherService service = new WeatherService(
                "http://localhost:8089",
                "http://localhost:8089"
        );

        Weather result = service.getWeatherByCity("Milano");

        assertNotNull(result);
        assertEquals(20.5, result.getTemperature());
        assertEquals(10.2, result.getWindSpeed());
    }

    // ❌ TEST 2: città non trovata
    @Test
    void testCityNotFound() {

        stubFor(get(urlPathEqualTo("/v1/search"))
                .willReturn(okJson("""
                { "results": [] }
                """)));

        WeatherService service = new WeatherService(
                "http://localhost:8089",
                "http://localhost:8089"
        );

        assertThrows(RuntimeException.class, () ->
                service.getWeatherByCity("asdasdasd"));
    }

    // ❌ TEST 3: weather senza current_weather
    @Test
    void testWeatherMissingField() throws Exception {

        stubFor(get(urlPathEqualTo("/v1/search"))
                .willReturn(okJson("""
                {
                  "results": [
                    { "latitude": 10, "longitude": 20 }
                  ]
                }
                """)));

        stubFor(get(urlPathEqualTo("/v1/forecast"))
                .willReturn(okJson("""
                {}
                """)));

        WeatherService service = new WeatherService(
                "http://localhost:8089",
                "http://localhost:8089"
        );

        Weather result = service.getWeatherByCity("Roma");

        assertNotNull(result);
        assertEquals(0.0, result.getTemperature());
        assertEquals(0.0, result.getWindSpeed());
    }

    // ❌ TEST 4: errore API (500)
    @Test
    void testApiError() {

        stubFor(get(urlPathEqualTo("/v1/search"))
                .willReturn(serverError()));

        WeatherService service = new WeatherService(
                "http://localhost:8089",
                "http://localhost:8089"
        );

        assertThrows(Exception.class, () ->
                service.getWeatherByCity("Milano"));
    }

    // ⚠️ TEST 5: input vuoto
    @Test
    void testEmptyInput() {

        WeatherService service = new WeatherService(
                "http://localhost:8089",
                "http://localhost:8089"
        );

        assertThrows(IllegalArgumentException.class, () ->
                service.getWeatherByCity(""));
    }
}