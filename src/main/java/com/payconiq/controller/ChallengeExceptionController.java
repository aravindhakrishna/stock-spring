package com.payconiq.controller;

import com.payconiq.util.NotSupportedCurrencyException;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeParseException;

@ControllerAdvice
@RestController
public class ChallengeExceptionController {

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,reason = "Payload not parsable")
    @ExceptionHandler({DateTimeParseException.class})
    public void handleParseError(DateTimeParseException exe){

    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,reason = "Payload not parsable")
    @ExceptionHandler({NumberFormatException.class})
    public void handleError(){

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public String handleDataNotAvailableError(Exception exe){
        return exe.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "Payload not parsable")
    @ExceptionHandler({NullPointerException.class})
    public void handleNullError(){

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "Currency not supported and ! Only EURO")
    @ExceptionHandler({NotSupportedCurrencyException.class})
    public void handleCurrency(){

    }

    @Bean
    public RestTemplate restTemplate(HttpMessageConverters bootConverters) {
        RestTemplate restTemplate = new RestTemplate(bootConverters.getConverters());
        return restTemplate;
    }
}
