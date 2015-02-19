<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
	<head>
		<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  		<title>The Software Blog</title>
	</head>
	<body>
		<h2>Create Post</h2>
		<div>
		
			<%
				String blogName = request.getParameter("blogName");
    			if (blogName == null) {
       				blogName = "default";
    			}
    			pageContext.setAttribute("blogName", blogName);
  				UserService userService = UserServiceFactory.getUserService();
    			User user = userService.getCurrentUser(); 
    			if (user != null) {
      				pageContext.setAttribute("user", user);
  			%>
  	
  			<p>Hey, ${fn:escapeXml(user.nickname)}! Go ahead and write something!</p>
			<%
 	   			} else {
			%>
			<p>Hey, <strong>anonymous</strong>!
			<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
			to include your name with your post! </p>
			<%
    			}
			%>
		<form action="/createPost" method="post">
			<div>
				<div style="margin-bottom: 15px;">
				<input type="text" size="30" style="font-size: 25px;" name="postTitle">
				</div>
				<textarea name="content" rows="5" cols="60"></textarea>
			</div>
			<div>
				<form action="index.jsp">
					<input type="submit" value="Cancel">
				</form>
				<input type="submit" value="Submit">
				<input type="hidden" name="blogName" value="${fn:escapeXml(blogName)}"/>
			</div>
		</div>
	</body>
</html>