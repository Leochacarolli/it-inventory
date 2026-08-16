package br.com.posjava.leochacarolli.it_inventory.exception;

public class AssetNotFoundException extends RuntimeException{

    public AssetNotFoundException(String message) {
        super(message);
    }
}
