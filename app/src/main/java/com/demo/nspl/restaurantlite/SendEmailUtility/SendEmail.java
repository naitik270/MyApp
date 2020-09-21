package com.demo.nspl.restaurantlite.SendEmailUtility;

import android.util.Log;

import com.demo.nspl.restaurantlite.classes.ClsEmailConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmail {

//    String emailPort = "587";// gmail's smtp port
//    String smtpAuth = "true";
//    String starttls = "true";
//    String emailHost = "mail.nathanisoftware.com";
//    String senderName = "Nathani Software";

    String emailPort = "";// gmail's smtp port
    String smtpAuth = "true";
    String starttls = "";
    String emailHost = "";
    String senderName = "";


    String fromEmail;
    String fromPassword;
    List<String> toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public SendEmail() {
    }

    public SendEmail(String fromEmail, String fromPassword,
                     List<String> toEmailList, String emailSubject, String emailBody, ClsEmailConfiguration clsEmailConfiguration) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        this.starttls = clsEmailConfiguration.getSSl().toLowerCase();
        this.emailPort = clsEmailConfiguration.getPort();
        this.emailHost = clsEmailConfiguration.getSMTP();
        this.senderName = clsEmailConfiguration.getDisplay_Name();


//        props.put("mail.smtp.host", "smtp-mail.outlook.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.starttls.enable","true");
//        props.put("mail.smtp.auth", "true");

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        emailProperties.put("mail.smtp.debug", "true");
        // emailProperties.put("mail.smtp.host", emailHost);
        emailProperties.put("mail.smtp.socketFactory.port", "587");
        emailProperties.put("mail.smtp.socketFactory.fallback", "false");

        // emailProperties.put("mail.smtp.socketFactory.fallback", "true");

        emailProperties.put("mail.smtps.timeout", 1000);
        emailProperties.put("mail.smtps.connectiontimeout", 1000);
        Log.i("GMail", "Mail server properties set.");
    }


    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, senderName));
        for (String toEmail : toEmailList) {

            Log.i("GMail", "toEmail: " + toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));

        }

        emailMessage.addRecipient(Message.RecipientType.BCC,
                new InternetAddress("analysis@nathanisoftware.com"));


        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail", "allrecipients: " + Arrays.toString(emailMessage.getAllRecipients()));
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }
}
