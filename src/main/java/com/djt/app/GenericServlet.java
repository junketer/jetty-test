package com.djt.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.http.*;

public abstract class GenericServlet extends HttpServlet {

	protected abstract String getPageTitle();

	protected void printHTMLStartTags(PrintWriter pw) {

		pw.print("<HTML><HEAD><TITLE>");
		pw.print(getPageTitle());
		pw.print("</TITLE>");
		printCSS(pw);
		printJavaScript(pw);
		pw.print("</HEAD><BODY>");
	}


	protected void printHTMLEndTags(PrintWriter pw) {
		pw.print("</BODY></HTML>");
	}


	protected void printJavaScript(PrintWriter pw) {
		pw.print("<script language=\"JavaScript\">function setPage() {var q = document.getElementByName('query')[0].value; document.getElementByName('page').value='http;//search.twitter.com/search.atom?q='+q;}</script>");

	}

	protected void printCSS(PrintWriter pw) {
		pw.println("<style type=\"text/css\">");
		pw.println("hr {color:sienna;}");
		pw.println("p {margin-left:20px;}");
		pw.println("td.data {background-color: #CCFFCC;}");
//		pw.println("table {border: 5px;}");
//		pw.println("body {background-image:url(\"images/back40.gif\");}");
		pw.println("</style>");
	}

	protected void printError(Throwable t, PrintWriter pw) {
		pw.print("<hr/>An error has occured.<BR/>");
		pw.print("<PRE>");
		t.printStackTrace(pw);
		pw.print("<PRE>");
	}

}