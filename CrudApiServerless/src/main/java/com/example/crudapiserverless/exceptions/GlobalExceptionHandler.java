package com.example.crudapiserverless.exceptions;

import com.example.crudapiserverless.constants.LoggingConstants;
import com.example.crudapiserverless.models.ApiResponse;
import com.example.crudapiserverless.models.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex, WebRequest request) {
        // Map exception to ErrorResponse
        ErrorResponse errorResponse = modelMapper.map(ex, ErrorResponse.class);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getDescription(false));

        // Create ApiResponse with error details
        ApiResponse<Void> response = new ApiResponse<>(null, errorResponse);

        // Log the general error
        logger.error("General Exception: {}", ex.getMessage(), ex);
        logger.error(LoggingConstants.ERROR_RESPONSE_LOG, response);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
