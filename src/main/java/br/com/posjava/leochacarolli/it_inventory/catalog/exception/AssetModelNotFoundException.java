package br.com.posjava.leochacarolli.it_inventory.catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssetModelNotFoundException extends RuntimeException {

    public AssetModelNotFoundException(String message) {
        super(message);
    }
}
