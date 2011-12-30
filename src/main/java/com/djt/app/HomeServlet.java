package com.djt.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
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
			pw.print("<P><form action=\"/twitter\" method=\"post\">");
			pw.print("Twitter search: <input name=\"query\" type=\"text\" onBlur=\"javascript:setPage()\" /><BR/>");
			pw.print("<input type=\"hidden\" name=\"page\" />");
			pw.print("<input type=\"submit\" value=\"Search\" />");
			pw.print("</form>");
		} finally {
			printHTMLEndTags(pw);
		}
	}
}