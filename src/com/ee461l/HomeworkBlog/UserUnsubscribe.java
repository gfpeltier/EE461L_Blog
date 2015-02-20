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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserUnsubscribe extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		 
	    String blogName = req.getParameter("blogName");
	    Key blogKey = KeyFactory.createKey("Blog", blogName);
	    Query subQuery = new Query("Subscription", blogKey);
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    List<Entity> subs = datastore.prepare(subQuery).asList(FetchOptions.Builder.withDefaults());
	    for(Entity sub : subs){
	    	String mail = (String) sub.getProperty("user");
	    	if(user.getEmail().equals(mail)){
	    		datastore.delete(sub.getKey());
	    	}
	    }
		
		resp.sendRedirect("/index.jsp?blogName=" + blogName);
	}
}
