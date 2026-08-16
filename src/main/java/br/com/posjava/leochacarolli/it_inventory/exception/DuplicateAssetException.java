package br.com.posjava.leochacarolli.it_inventory.exception;

public class DuplicateAssetException extends RuntimeException{

    public DuplicateAssetException(String message) {
        super(message);
    }
}
