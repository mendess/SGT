package main.sgt;

public class Utilizador {

	private String userNum;
	private String password;
	private String email;
	private String name;

	/**
	 * 
	 * @param userNum
	 * @param password
	 * @param email
	 * @param name
	 */
	Utilizador(String userNum, String password, String email, String name) {
		// TODO - implement Utilizador.Utilizador
		throw new UnsupportedOperationException();
	}

	String getUserNum() {
		return this.userNum;
	}

	/**
	 * 
	 * @param userNum
	 */
	void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @param password
	 */
	void setPassword(String password) {
		this.password = password;
	}

	String getEmail() {
		return this.email;
	}

	/**
	 * 
	 * @param email
	 */
	void setEmail(String email) {
		this.email = email;
	}

	String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	void setName(String name) {
		this.name = name;
	}

}