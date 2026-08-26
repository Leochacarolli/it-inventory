package br.com.posjava.leochacarolli.it_inventory.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String message) {
        super(message);
    }
}
