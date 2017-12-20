package main.sgt.exceptions;

public class AlunoNaoEstaInscritoNaUcException extends Throwable {
    private final String message;

    public AlunoNaoEstaInscritoNaUcException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + message;
    }
}
