<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
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
  	<div class="titleWrap">
  		<div class="login">
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
  	
  			<p class="loginText">Hello, ${fn:escapeXml(user.nickname)}! (You can
			<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
			<%
 	   			} else {
			%>
			<p class="loginText">Hello!
			<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
			to include your name with your blog posts.</p>
			<%
    			}
			%>
		</div>
		<div class="title">
			<h2 style="margin: 10px;">The Software Blog</h2>
		</div>	
	</div>
	<div class="contentWrap">
	
	<%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key blogKey = KeyFactory.createKey("Blog", blogName);
    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    Query query = new Query("Post", blogKey).addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> posts = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    if (posts.isEmpty()) {
        %>
        <p>Blog '${fn:escapeXml(guestbookName)}' has no messages.</p>
        <%
    } else {
        %>
        <p>Messages in Blog '${fn:escapeXml(guestbookName)}'.</p>
        <%
        for (Entity post : posts) {
        	pageContext.setAttribute("post_title",
                                     post.getProperty("title"));
            pageContext.setAttribute("post_content",
                                     post.getProperty("content"));
            pageContext.setAttribute("post_date",
                                     post.getProperty("date"));
            if (post.getProperty("user") == null) {
                %>
                <p>An anonymous person wrote:</p>
                <%
            } else {
                pageContext.setAttribute("post_user",
                                         post.getProperty("user"));
                %>
                <p><b>${fn:escapeXml(post_user.nickname)}</b> wrote:</p>
                <%
            }
            %>
            <div class="postWrap">
            	<h3>${fn:escapeXml(post_title)}</h3>
            	<p>${fn:escapeXml(post_content)}</p> 
            	<p class="timeStamp">${fn:escapeXml(post_date)}</p>
            </div>
            <%
        }
    }
	%>
	
	<% if (user != null){ %>
		<form action="createPostForm.jsp">
			<input type="submit" value="Create Blog Post">
		</form>
		<% } else{ %>
		<p>Log in to post in this blog!</p>
		<% } %>	
		</div>
  	</body>
</html>