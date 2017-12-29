package main.sgt;

import main.sgt.exceptions.BadlyFormatedFileException;
import main.sgt.exceptions.InvalidUserTypeException;
import main.sgt.exceptions.UtilizadorJaExisteException;

import javax.json.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class inscreverAlunosEmUCs {

    public static void main(String[] args) throws FileNotFoundException, BadlyFormatedFileException{
        SGT sgt = new SGT();
        System.out.println("UCS");
        JsonReader jreader = Json.createReader(new FileReader(new File("jsons/inscreveAlunosUCs.json")));
        JsonArray jarray = jreader.readArray();
        for(JsonValue j : jarray){
            JsonObject jobj = (JsonObject) j;
            String aluno = jobj.getString("Num");
            JsonArray jsonArray = jobj.getJsonArray("UCs");
            for(JsonValue uc : jsonArray){
                String ucID = uc.toString();
                System.out.println("aluno: " + aluno + " -> uc: " + uc);
                try{
                    sgt.addAlunoToUC(aluno, ucID);
                }catch(UtilizadorJaExisteException | InvalidUserTypeException ignored){
                }
            }
        }
    }
}
