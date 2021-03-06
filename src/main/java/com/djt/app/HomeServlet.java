package com.djt.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;

public class HomeServlet extends GenericServlet {

	@Override
	public String getPageTitle() {
		return "Home";
	}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

		PrintWriter pw = resp.getWriter();
		try {
			printHTMLStartTags(pw);
			pw.print("<H2>Dan's Fabulous Heroku App</H2><g:plusone></g:plusone>");
			pw.println("<a href=\"https://twitter.com/share\" class=\"twitter-share-button\" data-url=\"http://djt-test.herokuapp.com\"");
			pw.println("data-text=\"See my fab Twitter search app\" data-via=\"junketer\" data-hashtags=\"heroku\">Tweet</a>");
			pw.println("<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=\"//platform.twitter.com/widgets.js\";fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>");
			pw.print("<P/><form action=\"/twitter\" method=\"post\">");
			pw.print("Twitter search: <input name=\"query\" type=\"text\" onBlur=\"javascript:setPage()\" title=\"Enter a search string and click 'Search'\"/>&nbsp;");
			pw.print("<input type=\"hidden\" name=\"page\" />");
			pw.print("<input type=\"button\" value=\"Search\" onclick=\"javascript:twitterQuery();\" />");
			pw.print("</form>");
			pw.print("<div id=\"searchResults\" class=\"results\" />");
		} finally {
			printHTMLEndTags(pw);
		}
	}


	    @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

				Enumeration names = req.getParameterNames();
				while (names.hasMoreElements()) {
					String s = names.nextElement().toString();
					System.out.println(s + "="+req.getParameter(s));
				}
				String signedReq = req.getParameter("signed_request");
				if (signedReq != null  && signedReq.length() >0) {
					try {
						String hashBit = signedReq.substring(0,signedReq.indexOf("."));
						System.out.println("hashBit="+hashBit);
						String dataBit= signedReq.substring(signedReq.indexOf(".")+1);
						System.out.println("dataBit="+(dataBit==null ? "null":dataBit));
						System.out.println("signed_request="+signedReq);
						String unsignedReq = decode64(dataBit);
						System.out.println("unsigned_req="+unsignedReq);
						if (!unsignedReq.contains("user_id"))  {
							String url = "http://www.facebook.com/dialog/oauth?client_id=154418354667612"+
								"&redirect_uri=https://apps.facebook.com/djt-test/";
								resp.sendRedirect(url);
						}
					} catch (Throwable e) {
						PrintWriter pw = resp.getWriter();
						printHTMLStartTags(pw);
						pw.print("<PRE>");
						printError(e, pw);
						printHTMLEndTags(pw);
					}
				}

				doGet(req,resp);
			}
}