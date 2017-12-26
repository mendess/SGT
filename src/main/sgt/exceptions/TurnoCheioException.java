package main.sgt.exceptions;

public class TurnoCheioException extends Throwable {

    private final String message;

    public TurnoCheioException(String message){
        this.message = message;
    }

    public TurnoCheioException(){
        this.message = "";
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
