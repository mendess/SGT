package main.sgt.exceptions;

public class WrongCredentialsException extends Throwable {
    private final String message;
    public WrongCredentialsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
