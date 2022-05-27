package es.uji.ei1027.skillSharing;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
    final static String from = "pruebadecorreosandru@gmail.com";
    final static String password = "Sandru1234";

    //Este método solo se utilizaría en el run de la aplicación, no cada vez que queramos enviar un mensaje por correo
    public static Session connect(){
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        session.setDebug(true);
        Transport transport;
        try {
            transport = session.getTransport();
            transport.connect();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return session;
    }


    // Este método sirve para enviar correos una vez conectados
    public static void send(Session session, String subject, String msg, String to){
        try {
            InternetAddress addressFrom;
            addressFrom = new InternetAddress(from);
            MimeMessage message = new MimeMessage(session);
            message.setSender(addressFrom);

            message.setSubject(subject);
            message.setText(msg);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


    public static void close(Session session){

        try{
            Transport t = session.getTransport();
            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
