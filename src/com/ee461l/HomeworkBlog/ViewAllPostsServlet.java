package com.ee461l.HomeworkBlog;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewAllPostsServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		String blogName = req.getParameter("blogName");
		
		resp.sendRedirect("/showAllPosts.jsp?blogName=" + blogName);
	}
}
