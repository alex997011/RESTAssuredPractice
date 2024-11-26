package com.globant.api.utils;

public final class Constants {
    public static final String VALUE_CONTENT_TYPE = "application/json";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CLIENTS_PATH = "/Clients";  // Cambiado a la ruta correcta
    public static final String BASE_URL = "https://6743f860b4e2e04abea02a29.mockapi.io";
    // Quitamos URL ya que no necesitamos el /api/v1/
    public static final String URL = "%s";  // o simplemente usa BASE_URL + CLIENTS_PATH

    private Constants() {
    }
}