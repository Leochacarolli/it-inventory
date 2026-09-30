package br.com.posjava.leochacarolli.it_inventory.location.exception;

public class DuplicateLocationException extends RuntimeException {

    public DuplicateLocationException(String message) {
        super(message);
    }
}
