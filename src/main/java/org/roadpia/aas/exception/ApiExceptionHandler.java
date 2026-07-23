package org.roadpia.aas.exception;

import org.roadpia.aas.domain.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        HttpStatus resolvedStatus = status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;

        return ResponseEntity.status(resolvedStatus)
                .body(new ErrorResponse(
                        OffsetDateTime.now(),
                        resolvedStatus.value(),
                        ex.getReason() == null ? resolvedStatus.getReasonPhrase() : ex.getReason(),
                        null
                ));
    }

    @ExceptionHandler(BasyxClientException.class)
    public ResponseEntity<ErrorResponse> handleBasyxClientException(BasyxClientException ex) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;

        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        OffsetDateTime.now(),
                        status.value(),
                        ex.getMessage(),
                        ex.getResponseBody()
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        OffsetDateTime.now(),
                        status.value(),
                        "서버 내부 오류가 발생했습니다.",
                        ex.getMessage()
                ));
    }
}
