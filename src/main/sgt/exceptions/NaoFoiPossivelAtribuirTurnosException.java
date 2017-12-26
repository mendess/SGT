package main.sgt.exceptions;

public class NaoFoiPossivelAtribuirTurnosException extends Exception {

    private final String message;

    public NaoFoiPossivelAtribuirTurnosException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
