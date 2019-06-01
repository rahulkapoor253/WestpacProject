package com.example.rahulkapoor.westpacproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class MailUtils extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Session session;
    //Information to send email
    private String email;
    private String subject;
    private String message;
    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;
    private String senderEmail = "rahul253801@gmail.com";
    private String senderPassword = "Rahul@1996";
    private String filename;
    private ArrayList<String> cacheFileData = new ArrayList<>();
    private ArrayList<BodyPart> messageBodyList = new ArrayList<>();

    public MailUtils(Context context, ArrayList<String> cacheData) {
        //Initializing variables
        this.context = context;
        this.cacheFileData = cacheData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(final Void... voids) {

        Properties props = new Properties();

        //Configuring properties for gmail;
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });
        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);
            //Setting sender address
            mm.setFrom(new InternetAddress(senderEmail));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress("rahul253801@gmail.com"));
            //Adding subject
            mm.setSubject("WorkFlow Images");
            //Adding message
            // Now set the actual message
            messageBodyList.clear();

            for (int i = 0; i < cacheFileData.size(); i++) {

                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(cacheFileData.get(i));
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(cacheFileData.get(i) + ".jpg");

                //add the message body part in list then clear it;
                messageBodyList.add(messageBodyPart);
            }

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            for (int i = 0; i < messageBodyList.size(); i++) {
                multipart.addBodyPart(messageBodyList.get(i));
            }

            // Send the complete multipart with list of body parts;
            mm.setContent(multipart);
            //Sending email
            Transport.send(mm);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }


}

