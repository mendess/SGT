package main.sgt;

class AlunoNaoEstaInscritoNaUc extends Throwable {
    private final String message;

    AlunoNaoEstaInscritoNaUc(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + message;
    }
}
