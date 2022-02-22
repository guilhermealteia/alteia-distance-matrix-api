package br.com.alteia.microservicechangeit.adapters.handlers;

import br.com.alteia.common.exceptions.CircuitException;
import br.com.alteia.common.exceptions.ErrorResponse;
import br.com.alteia.microservicechangeit.common.exceptions.GenericException;
import br.com.alteia.microservicechangeit.common.exceptions.InvalidEntityException;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.IntegrationException;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.InvalidRequestException;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.MismatchException;
import br.com.alteia.microservicechangeit.use_cases.common.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class SharedExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = Logger.getLogger(SharedExceptionHandler.class.getName());

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(NotFoundException ex, WebRequest request) {
        LOG.log(Level.SEVERE, ex.getCode(), ex);
        ErrorResponse error = new ErrorResponse(NOT_FOUND, ex.getCode(), List.of(ex.getMessage()));
        return new ResponseEntity<>(error, NOT_FOUND);
    }

    @ExceptionHandler({CircuitException.class})
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureExceptions(RuntimeException ex, WebRequest request) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);
        String exMessage = Optional.ofNullable(ex.getMessage()).orElse("");
        ErrorResponse error = new ErrorResponse(UNPROCESSABLE_ENTITY, null, List.of(exMessage));
        return new ResponseEntity<>(error, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({InvalidRequestException.class, InvalidEntityException.class, MismatchException.class})
    @ResponseStatus(BAD_REQUEST)
    public final ResponseEntity<ErrorResponse> handleGenericsExceptions(
            GenericException ex, WebRequest request) {
        LOG.log(Level.SEVERE, ex.getCode(), ex);
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, ex.getCode(), List.of(ex.getMessage()));
        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    @ExceptionHandler({IntegrationException.class})
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public final ResponseEntity<ErrorResponse> handleIntegrationExceptions(
            GenericException ex, WebRequest request) {
        LOG.log(Level.SEVERE, ex.getCode(), ex);
        ErrorResponse error = new ErrorResponse(UNPROCESSABLE_ENTITY, ex.getCode(), List.of(ex.getMessage()));
        return new ResponseEntity<>(error, UNPROCESSABLE_ENTITY);
    }
}
