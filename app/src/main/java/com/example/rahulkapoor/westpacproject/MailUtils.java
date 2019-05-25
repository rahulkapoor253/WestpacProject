package com.example.rahulkapoor.westpacproject;

import java.util.List;


public class MailUtils {

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail;
    String fromPassword;
    List toEmailList;
    String emailSubject;
    String emailBody;


}
