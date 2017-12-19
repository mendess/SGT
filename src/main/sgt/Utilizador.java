package main.sgt;

public abstract class Utilizador {

    /**
     * O identificador do utilizador
     */
	private String userNum;
    /**
     * A password do utilizador
     */
	private String password;
    /**
     * O email do utilizador
     */
	private String email;
    /**
     * O nome do utilizador
     */
	private String name;

	/**
	 * Construtor de utilizador
	 * @param userNum O identificador do utilizador
	 * @param password A password do utilizador
	 * @param email O email do utilizador
	 * @param name O nome do utilizador
	 */
	Utilizador(String userNum, String password, String email, String name) {
		this.userNum = userNum;
		this.password = password;
		this.email = email;
		this.name = name;
	}

    /**
     * Retorna o identificador do utilizador
     * @return O identificador do utilizador
     */
	public String getUserNum() {
		return this.userNum;
	}

	/**
	 * Altera o identificador do utilizador
	 * @param userNum Novo identificador do utilizador
	 */
	void setUserNum(String userNum) {
		this.userNum = userNum;
	}

    /**
     * Retorna a password do utilizador
     * @return A password do utilizador
     */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Altera a password do utilizador
	 * @param password Nova password do utilizador
	 */
	void setPassword(String password) {
		this.password = password;
	}

    /**
     * Retorna o email do utilizador
     * @return O email do utilizador
     */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Altera o email do utilizador
	 * @param email Novo email do utilizador
	 */
	void setEmail(String email) {
		this.email = email;
	}

    /**
     * Retorna o nome do utilizador
     * @return O nome do utilizador
     */
	public String getName() {
		return this.name;
	}

	/**
	 * Altera o nome do utilizador
	 * @param name Novo nome do utilizador
	 */
	void setName(String name) {
		this.name = name;
	}

    @Override
    public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null || o.getClass() != this.getClass()){
            return false;
        }
        Utilizador u = (Utilizador) o;
        return super.equals(o)
                && this.email.equals(u.getEmail())
                && this.name.equals(u.getName())
                && this.password.equals(u.getPassword())
                && this.userNum.equals(u.getUserNum());
    }
}