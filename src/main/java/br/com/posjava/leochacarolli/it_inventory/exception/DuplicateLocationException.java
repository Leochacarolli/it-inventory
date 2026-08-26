package br.com.posjava.leochacarolli.it_inventory.exception;

public class DuplicateLocationException extends RuntimeException {

    public DuplicateLocationException(String message) {
        super(message);
    }
}
