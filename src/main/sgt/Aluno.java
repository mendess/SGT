package main.sgt;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Aluno extends Utilizador {

	private boolean eEspecial;
	private Map<String, Integer> horario;

	/**
	 * O construtor do aluno
	 * @param userNum O identificador do aluno
	 * @param password A password do aluno
	 * @param email O email do aluno
	 * @param name O nome do aluno
	 * @param eEspecial <tt>true</tt> se o aluno tiver estatuto especial
	 * @param inscricoes As ucs e turnos a que o aluno esta inscrito
	 */
	public Aluno(String userNum, String password, String email, String name, boolean eEspecial, Map<String, Integer> inscricoes) {
		super(userNum,password,email,name);
		this.eEspecial = eEspecial;
		this.horario = inscricoes;
	}

    /**
     * Retorna se o aluno tem estatuto especial
     * @return <tt>true</tt> se o aluno tem estatuto especial, <tt>false</tt> caso contrario.
     */
	public boolean eEspecial() {
	    return this.eEspecial;
	}

	/**
	 * Muda o estatuto do aluno
	 * @param eEspecial Novo estatuto.
	 */
	void setEespecial(boolean eEspecial) {
	    this.eEspecial = eEspecial;
	}

    /**
     * Retorna o horario do aluno.
     * @return O horario do aluno
     */
	public Map<String, Integer> getHorario() {
        return new HashMap<>(this.horario);
	}

	/**
	 * Inscreve um aluno num turno. WARNING este metodo tambem retira o aluno do turno onde estava
	 * @param uc O identificador da UC do turno
	 * @param turno O numero do turno
	 */
	void inscrever(String uc, int turno) {
	    this.horario.put(uc,turno);
	}

	/**
	 * Desinscreve um aluno de um turno
	 * @param uc Identificador da UC do turno
	 * @param turno Numero do turno
	 */
	void desinscrever(String uc, int turno) {
	    this.horario.put(uc,null);
	}

    /**
     * Ativa o login deste aluno, enviando lhe um email com o seu numero e a sua password.
     */
	void ativarLogin() {
		int tries = 0;
		boolean success = false;
        while (!success && tries != 5) {
            success = sendEmail(this.getEmail());
            tries++;
        }
	}

    /**
     * Envia um email para o aluno para este poder fazer login
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
                                + "User Number: "+  super.getUserNum() +"\n"
                                + "User password: "+super.getPassword()+"\n");
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
        Aluno a = (Aluno) o;
        return super.equals(o)
                && this.eEspecial == a.eEspecial()
                && this.horario.equals(a.getHorario());
    }
}