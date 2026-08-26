package br.com.posjava.leochacarolli.it_inventory.exception;

public class InvalidLocationDataException extends RuntimeException {

    public InvalidLocationDataException(String message) {
        super(message);
    }
}
