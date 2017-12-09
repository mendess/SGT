package main.sgt;

class AlunoNaoEstaInscritoNaUcException extends Throwable {
    private final String message;

    AlunoNaoEstaInscritoNaUcException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + message;
    }
}
