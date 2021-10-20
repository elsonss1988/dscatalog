package com.example.dscatalog.resources.exceptions;

import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandle {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError();
        HttpStatus status = HttpStatus.NOT_FOUND;
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Resource Not found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBaseException(DataBaseException e, HttpServletRequest request){
        StandardError err= new StandardError();
        HttpStatus status=HttpStatus.BAD_REQUEST;
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setMessage(e.getMessage());
        err.setError("Database Exception");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}
