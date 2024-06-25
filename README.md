# GoodTripAndroid

## Description

Android application GoodTrip about planning trips with social interaction.

## Links

You can see back-end of application at:

* [GoodTripServer](https://github.com/GoodTripProject/GoodTripServer)

## Dependencies

- Android SDK 28+
- Google Maps SDK
- Google OAuth 2.0
- Firebase 33 for saving photos
- JUnit 4
- [**Lombok**](https://projectlombok.org/) for getters and setters

## Build and Run

To build and run the project from the command line:

1) Set properties in file:

   `local.properties`
         
         sdk.dir =             # path for Android SDK directory
         GOOGLE_OAUTH_TOKEN =  # token for Google OAuth
         MAPS_API_KEY =        # token for Google Maps API
         URL_API =             # URL to access server REST API

2) Place `google-services.json` file from Firebase API in `app/` directory

3) Run application at emulator or at your device

## Links for APIs

1) [**Google Maps SDK**](https://developers.google.com/maps/documentation/android-sdk?hl=ru) - to show
   maps
2) [**Firebase Storage**](https://firebase.google.com/docs/storage) - to save photos
3) [**Google OAuth 2.0**](https://developers.google.com/identity/protocols/oauth2?hl=ru) - to auth via Google

