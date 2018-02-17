package com.gia.utils;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.scheduling.annotation.Async;
import com.gia.config.SystemParams;


public class MailHelper {

	Locale locale = new Locale("en", "US");
	NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

	static MailHelper s_singleton = new MailHelper();

	public static MailHelper getInstance() {
		return s_singleton;
	}

	public static Properties getProperties() {

		Properties prop = new Properties();
		prop.put("mail.transport.protocol", SystemParams.EMAIL_PROTOCOL);
		prop.put("mail.smtp.auth", SystemParams.EMAIL_AUTH_REQUIRED);
		prop.put("mail.smtp.starttls.enable", SystemParams.EMAIL_TTLS_ENABLED);
		// prop.put("mail.smtp.host", "smtp.mail.yahoo.com");
		prop.put("mail.smtp.host", SystemParams.EMAIL_SMTP_HOST);
		prop.put("mail.smtp.ssl.enable", "true");
		// prop.put("mail.smtp.host", "smtp.mail.com");
		prop.put("mail.smtp.port", SystemParams.EMAIL_SMTP_PORT);

		return prop;

	}

	public Session getSessionToSendMail() {

		Session session = Session.getInstance(MailHelper.getProperties(), new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SystemParams.FROMADDRESS, SystemParams.PASSWORD);
			}
		});

		return session;
	}

	public void sendMessageTemplate(Message message, String email, String subject, StringWriter writer)
			throws UnsupportedEncodingException, MessagingException {

		message.setFrom(new InternetAddress(SystemParams.FROMADDRESS, "Gia"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject(subject);
		message.setContent(writer.toString(), "text/html;charset=utf-8");
		Transport.send(message);
		System.out.println("Mail sent Sucessfully");

	}

	public VelocityEngine getVelocityEngine() throws Exception {

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		return ve;
	}


	@Async
	public void sendingSummaryTemplate(String email, List<String> myList, String imageUrl) throws ResourceNotFoundException, ParseErrorException, Exception {
		String imageMessage = null;
		for(String s:myList){  
		     if(s.charAt(s.length() - 2) == ':'){
		    	 imageMessage = s;
		    	 System.out.println("///////////"+imageMessage);
		     }
		   }  
		
		String subject = "Conversation Summary";
		String respondURL = SystemParams.BASEURL;
		VelocityEngine ve = getVelocityEngine();
		VelocityContext context = new VelocityContext();
		context.put("respondURL", respondURL);
		context.put("myList", myList);
		context.put("imageUrl", imageUrl);
		context.put("imageMessage", imageMessage);
		Template t = ve.getTemplate(SystemParams.TEMPLATES_PATH + "summaryTemplate.vm");
		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		Session session = getSessionToSendMail();
		try {
			Message message = new MimeMessage(session);
			sendMessageTemplate(message, email, subject, writer);

		} catch (MessagingException me) {
			me.printStackTrace();
		}

	}

}

