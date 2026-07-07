package org.payment.exception;

public class BasyxClientException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public BasyxClientException(String message, int statusCode, String responseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
