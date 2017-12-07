package main.sgt;

public class Utilizador {
    private String userNum;
    private String password;
    private String email;

    public String getUserNum() {
        return this.userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
