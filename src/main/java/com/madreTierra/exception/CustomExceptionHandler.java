package com.madreTierra.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(IdNotFound.class)
    public ResponseEntity<Object> handleIdNotFound(IdNotFound ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "ID no encontrado", ex.getMessage());
        return  handleExceptionInternal(ex, errorDetails, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ParamNotFound.class)
    public ResponseEntity<Object> paramNotFound(ParamNotFound ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Bad Request", ex.getMessage());
        return  handleExceptionInternal(ex, errorDetails, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }




}
