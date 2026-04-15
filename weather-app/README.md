# 🌤️ Weather App

Applicazione Java che consente agli utenti di inserire il nome di una
città e ottenere informazioni meteo in tempo reale utilizzando le API di
Open-Meteo.

## 📌 Panoramica del progetto

Questa applicazione da riga di comando permette di: 1. Inserire il nome
di una città 2. Recuperare le coordinate geografiche tramite API di
geocoding 3. Ottenere dati meteo aggiornati (temperatura, vento,
umidità) 4. Gestire errori (città non valide, problemi API) 5.
Registrare le risposte in un file di log

## ⚙️ Installazione

### Prerequisiti

-   Java 17+
-   Maven 3+

### Clonare il repository

git clone `<repo-url>`{=html} cd weather-app

### Compilare il progetto

mvn clean install

## ▶️ Guida all'uso

mvn exec:java -Dexec.mainClass="com.matteo.App"

Inserisci il nome di una città quando richiesto.

## 💻 Output di esempio

Inserisci città: Milano

🌤️ Meteo per: Milano 🌡️ Temperatura: 22.5°C 💨 Vento: 8.3 km/h 💧
Umidità: 60%

## 🚀 Funzionalità

-   Ricerca città (anche parziale)
-   Geocoding automatico
-   Temperatura attuale
-   Velocità del vento
-   Umidità
-   Gestione errori
-   Logging
-   Test automatici

## 🔮 Miglioramenti futuri

-   Supporto multi-lingua
-   Autocomplete città
-   Previsioni meteo
-   API REST
-   Docker
-   Deploy cloud

## 👨‍💻 Autore

Matteo
