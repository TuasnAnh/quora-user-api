/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ADMIN
 */
public class EmailUtils {

    public static void sendEmail(String recipient, String verifyUrl) throws AddressException, MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        //        props.put("mail.smtp.starttls.required", "true");

        String email = "modernquora1001@gmail.com";
        String password = "Quora1001";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("QUORA: VERIFY EMAIL");
        message.setText("Verification link: \n" + verifyUrl);

        Transport.send(message);
        System.out.println("Email Sended");
    }

    public static void sendForgotEmail(String recipient, int code) throws AddressException, MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        //        props.put("mail.smtp.starttls.required", "true");

        String email = "modernquora1001@gmail.com";
        String password = "Quora1001";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("QUORA: CHANGE PASSWORD VERIFICATION CODE");
        message.setText("Verification code: \n" + code);

        Transport.send(message);
        System.out.println("Forgot Email Sended");
    }

}
