package main.sgt;

public class DiretorDeCurso extends Utilizador {

	/**
	 * Construtor do diretor de curso
	 * @param userNum O identificador do utilizador
	 * @param password A password do utilizador
	 * @param email O email do utilizador
	 * @param name O nome do utilizador
	 */
    public DiretorDeCurso(String userNum, String password, String email, String name) {
		super(userNum,password,email,name);
	}

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "Diretor de Curso:"
                + super.toString();
    }
}