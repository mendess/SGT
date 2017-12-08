package main.sgt;

public class WrongCredentialsException extends Throwable {
    private String message;
    WrongCredentialsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + message;
    }
}
