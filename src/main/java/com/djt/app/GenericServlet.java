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
		pw.println("<script language=\"JavaScript\">");
		pw.println("function setPage() {var q = document.getElementsByName('query')[0].value; ");
		pw.println(" document.getElementsByName('page')[0].value='http;//search.twitter.com/search.atom?q='+q;} ");
		pw.println("function callAjax(url) {");
		//pw.println(" alert(url);");
		pw.println("var xmlhttp; if (window.XMLHttpRequest) ");
		pw.println("  {// code for IE7+, Firefox, Chrome, Opera, Safari ");
		pw.println(" xmlhttp=new XMLHttpRequest(); } else {// code for IE6, IE5 ");
		pw.println(" xmlhttp=new ActiveXObject(\"Microsoft.XMLHTTP\"); }");
		pw.println("xmlhttp.open(\"GET\",url,false);");
		pw.println("xmlhttp.send();");
		//pw.println("alert(xmlhttp.responseText);");
		pw.println("document.getElementById(\"searchResults\").innerHTML=xmlhttp.responseText;} ");
		pw.println("function twitterQuery() {");
		//pw.println("alert('twitter query'); ");
		pw.println(" var q= document.getElementsByName('query')[0].value; callAjax('./twitter?query='+q);} ");
		pw.println("function nextPage(url) {");
		pw.println(" var q= document.getElementsByName('query')[0].value; ");
		pw.println(" callAjax('./twitter?url='+url+'&query='+q);");
		pw.println("}");
		pw.println("</script>");

	}

	protected void printCSS(PrintWriter pw) {
		pw.println("<style type=\"text/css\">");
		pw.println("hr {color:sienna;}");
		pw.println("p {margin-left:20px;}");
		pw.println("td.data {background-color: #CCFFCC;}");
//		pw.println("td.data {background-color: #CCFFCC;}");
		pw.println("div.results {height: 80%;}");
		pw.println("div.innerResults {height: 500px; overflow: auto;}");
//		pw.println("body {background-image:url(\"images/back40.gif\");}");
		pw.println("</style>");
	}

	protected void printError(Throwable t, PrintWriter pw) {
		pw.print("<hr/>An error has occured.<BR/>");
		pw.print("<PRE>");
		t.printStackTrace(pw);
		pw.print("<PRE>");
	}

	protected String decode64(String encodedBytes) throws IOException {
		return new String(MyBase64.decode(encodedBytes));
	}
}