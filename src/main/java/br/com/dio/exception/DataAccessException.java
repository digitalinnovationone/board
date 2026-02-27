package br.com.dio.exception;

public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message) {
        super(message);
    }
}
