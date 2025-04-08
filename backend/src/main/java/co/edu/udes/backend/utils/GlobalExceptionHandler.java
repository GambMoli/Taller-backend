package co.edu.udes.backend.utils;

import co.edu.udes.backend.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice //Luis, recuerda que esto es para las excepciones de forma global
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class) // Y esta notacion le dice basicamente a spring, maneja el error de esta manera, y esto lo devuelve para aca por que la clase que hice de CustomException extiende de Runtime
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("code", ex.getErrorCode().name());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
