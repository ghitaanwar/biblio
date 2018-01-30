package controler.util;


import bean.Site;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import service.SiteFacade;




public class MailUtil {
 
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;


 
	public  void SendEmail(Site config ,String sujet, String emailBody , String to) throws AddressException, MessagingException {
                String SmtpUser= config.getSmtpuser() ;
                String SmtpPass= config.getSmtppass() ;
                String SmtpHost= config.getSmtphost() ; 
                        
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
                 
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(to));
		generateMailMessage.setSubject(sujet);
		generateMailMessage.setContent(emailBody, "text/html");
		Transport transport = getMailSession.getTransport("smtp");
                
		transport.connect(SmtpHost, SmtpUser, SmtpPass);
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
               System.out.println("controler.util.MailUtil.SendEmail()");

	}

   
}