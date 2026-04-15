
async function getWeatherByCity(city) {
       
  try {
    // 1. GEOCODING (città → coordinate)
    const geoUrl = `https://geocoding-api.open-meteo.com/v1/search?name=${encodeURIComponent(city)}`;
    
    const geoResponse = await fetch(geoUrl);

    // errore API
    if (!geoResponse.ok) {
        
      throw new Error("Errore nella richiesta di geocoding");
    }

    const geoData = await geoResponse.json();

    // città non trovata
    if (!geoData.results || geoData.results.length === 0) {
      throw new Error("Città non trovata");
    }

    const location = geoData.results[0];
    const { latitude, longitude, name } = location;

    // 2. METEO (coordinate → meteo)
    const weatherUrl = `https://api.open-meteo.com/v1/forecast?latitude=${latitude}&longitude=${longitude}&current_weather=true`;

    const weatherResponse = await fetch(weatherUrl);

    if (!weatherResponse.ok) {
      throw new Error("Errore nella richiesta meteo");
    }

    const weatherData = await weatherResponse.json();

    if (!weatherData.current_weather) {
      throw new Error("Dati meteo non disponibili");
    }

    const { temperature, weathercode } = weatherData.current_weather;

    // 3. Traduzione weathercode → descrizione
    const description = getWeatherDescription(weathercode);

    // 4. Risultato finale
    return {
      city: name,
      temperature: temperature,
      description: description
    };

  } catch (error) {
    // gestione errori globale
    return {
      error: true,
      message: error.message
    };
  }
}

function getWeatherDescription(code) {
  const map = {
    0: "Sereno ☀️",
    1: "Prevalentemente sereno 🌤️",
    2: "Parzialmente nuvoloso ⛅",
    3: "Coperto ☁️",
    45: "Nebbia 🌫️",
    48: "Nebbia con brina 🌫️",
    51: "Pioggia leggera 🌦️",
    61: "Pioggia 🌧️",
    71: "Neve ❄️",
    80: "Rovesci 🌦️",
    95: "Temporale ⛈️"
  };

  return map[code] || "Condizioni sconosciute";
}

getWeatherByCity("Milano")
  .then(result => console.log(result));

const city = process.argv[2];

if (!city) {
  console.log("Nessuna città fornita");
  process.exit(1);
}

getWeatherByCity(city).then(result => console.log(result));


