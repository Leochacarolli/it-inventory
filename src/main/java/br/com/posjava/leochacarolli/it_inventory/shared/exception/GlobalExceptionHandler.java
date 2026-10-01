package br.com.posjava.leochacarolli.it_inventory.shared.exception;

import br.com.posjava.leochacarolli.it_inventory.asset.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.asset.exception.DuplicateAssetException;
import br.com.posjava.leochacarolli.it_inventory.asset.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.catalog.exception.AssetModelNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.catalog.exception.CategoryNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.location.exception.DuplicateLocationException;
import br.com.posjava.leochacarolli.it_inventory.location.exception.InvalidLocationDataException;
import br.com.posjava.leochacarolli.it_inventory.location.exception.LocationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Existem campos inválidos na requisição");
        response.put("errors", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler({
            AssetNotFoundException.class,
            AssetModelNotFoundException.class,
            CategoryNotFoundException.class,
            LocationNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(
            RuntimeException exception) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler({
            InvalidAssetDataException.class,
            InvalidLocationDataException.class
    })
    public ResponseEntity<Map<String, Object>> handleInvalidData(
            RuntimeException exception) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler({
            DuplicateAssetException.class,
            DuplicateLocationException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(
            RuntimeException exception) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}