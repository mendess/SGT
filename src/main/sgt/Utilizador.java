package main.sgt;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

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
     * Estado do login deste utilizador
     */
    private boolean loginAtivo;

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
        this.loginAtivo=false;
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

    /**
     * Ativa o Login deste aluno, enviando lhe um email com o seu numero e a sua password.
     */
    void ativarLogin() {
        if(this.loginAtivo) return;
        int tries = 0;
        boolean success = false;
        while (!success && tries != 5) {
            success = sendEmail(this.getEmail());
            tries++;
        }
        this.loginAtivo = success;
    }
    /**
     * Envia um email para o aluno para este poder fazer Login
     * @return <tt>true</tt> se o email foi enviado com sucesso, <tt>false</tt> caso contrario
     */
    private boolean sendEmail(String to){
        String host = "smtp.gmail.com";
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.user", "swap.dss.uminho@gmail.com");
        props.put("mail.smtp.password", "swapdssuminho");
        props.put("mail.smtp.prot",587);
        props.put("mail.smtp.auth","true");
        Session session = Session.getDefaultInstance(props,null);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress("swap.dss.uminho@gmail.com"));
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            mimeMessage.setSubject("Your swap account is ready");
            mimeMessage.setText("Your swap account is active.\n\n"
                    + "Here are your credentials:\n"
                    + "User Number: "+  this.getUserNum() +"\n"
                    + "User password: "+this.getPassword()+"\n");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, "swap.dss.uminho@gmail.com", "swapdssuminho");
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
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
        return this.email.equals(u.getEmail())
                && this.name.equals(u.getName())
                && this.password.equals(u.getPassword())
                && this.userNum.equals(u.getUserNum());
    }

    @Override
    public String toString() {
        return "Utilizador: \t"
                +"User Number: "+this.userNum+"\t"
                +"Nome: "+this.name+"\t"
                +"Email: "+this.email+"\t"
                +"Password: "+this.password+"\t";
    }

    public boolean isLoginAtivo() {
        return loginAtivo;
    }
}