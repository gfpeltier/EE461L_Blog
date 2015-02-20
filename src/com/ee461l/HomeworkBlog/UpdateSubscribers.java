package com.ee461l.HomeworkBlog;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UpdateSubscribers extends HttpServlet{
	
	private static final Logger _logger = Logger.getLogger(UpdateSubscribers.class.getName());
	
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		
		
		Key blogKey = KeyFactory.createKey("Blog", "default");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Post", blogKey).addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> posts = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		ArrayList<Entity> newPosts = new ArrayList<Entity>();
		Date lastDate = new Date();
		lastDate.setMinutes(lastDate.getMinutes() -10);
		for(Entity post : posts){
			Date old = (Date) post.getProperty("date");
			if(old.after(lastDate)){
				newPosts.add(post);
			}
		}
		String outMail = "";
		Iterator<Entity> i = newPosts.iterator();
		while(i.hasNext()){
			Entity tmp = i.next();
			String sPost = "At " + tmp.getProperty("date") + ", " + tmp.getProperty("user").toString()+ " said: \n\n" + tmp.getProperty("title")+"\n\n"+tmp.getProperty("content")+"\n\n\n";
			outMail += sPost;
		}
		_logger.info("New Post Update: " + outMail);
		System.out.println("New Post Update: " + outMail);
	    Query subQuery = new Query("Subscription", blogKey);
	    List<Entity> subs = datastore.prepare(subQuery).asList(FetchOptions.Builder.withDefaults());
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);
	    for(Entity sub : subs){
	    	String mail = (String) sub.getProperty("user");
	    	System.out.println("User Email: " + mail);
	    	_logger.info("User Email: " + mail);
	    	
	    	try {
	    	    Message msg = new MimeMessage(session);
	    	    msg.setFrom(new InternetAddress("admin@ee461l-blog.com", "EE461L Blog Admin"));
	    	    msg.addRecipient(Message.RecipientType.TO,
	    	    new InternetAddress(mail, "Mr. User"));
	    	    msg.setSubject("Your Daily EE461L Blog Update");
	    	    msg.setText(outMail);
	    	    Transport.send(msg);

	    	} catch (AddressException e) {
	    	    System.out.println("Address Exception thrown");
	    	    _logger.info("Address Exception thrown");
	    	} catch (MessagingException e) {
	    		System.out.println("Messaging Exception thrown");
	    		_logger.info("Messaging Exception thrown");
	    	}
	    }
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doGet(req, resp);
		}
}
