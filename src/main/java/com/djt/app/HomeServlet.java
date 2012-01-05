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
			pw.print("<H2>Dan's Fabulous Heroku App</H2>");
			pw.print("<form action=\"/twitter\" method=\"post\">");
			pw.print("Twitter search: <input name=\"query\" type=\"text\" onBlur=\"javascript:setPage()\" /><BR/>");
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
						String url = "https://www.facebook.com/dialog/oauth?client_id=154418354667612"+
							"&redirect_uri=https://apps.facebook.com/djt-test/";
					RequestDispatcher rd = req.getRequestDispatcher(url);
					rd.forward(req,resp);
				}
				doGet(req,resp);
			}
}