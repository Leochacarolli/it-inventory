package br.com.posjava.leochacarolli.it_inventory.exception;

public class InvalidAssetDataException extends RuntimeException {
    public InvalidAssetDataException(String message) {
        super(message);
    }
}
