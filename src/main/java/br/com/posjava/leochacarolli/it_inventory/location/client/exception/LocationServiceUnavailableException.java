package br.com.posjava.leochacarolli.it_inventory.location.client.exception;

public class LocationServiceUnavailableException extends RuntimeException {
    public LocationServiceUnavailableException(String message) {
        super(message);
    }
}
