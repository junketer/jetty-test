package com.djt.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.djt.app.feed.handlers.TwitterFeedHandler;
import com.djt.app.to.DataItem;

public class TwitterFeedServlet extends GenericServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


				PrintWriter pw = resp.getWriter();
				printHTMLStartTags(pw);
				try {

					resp.setHeader("Cache-Control", "no-cache");
					SAXParserFactory factory = SAXParserFactory.newInstance();
					SAXParser parser = factory.newSAXParser();
					String urlS = req.getParameter("url");
					String page = "http://search.twitter.com/search.atom?q=airport+delay";
					String query = req.getParameter("query");
					if (urlS !=null && urlS.length() > 0) {
						page = URLDecoder.decode(urlS).replace(" ", "+");
					}
					if (query != null && query.length() > 0) {
						query= query.replace(" ","+");
						page = "http://search.twitter.com/search.atom?q="+query;
						pw.print("<H3>Twitter search results for : ");
						pw.print(query);
						pw.print("</H3>");
					}
					System.out.println("page="+page);

					URL url = new URL(page);
					InputStream is = url.openConnection().getInputStream();

					TwitterFeedHandler h = new TwitterFeedHandler();
					parser.parse(is, h);
					pw.print("<TABLE><TR><TD>");
					for(DataItem d: h.getBuild().getEntries()){
						pw.print(d.asHtml());
					}
					if (h.getBuild().getNextPage() != null) {
						pw.print("<a href=\"twitter?url=");
						pw.print(URLEncoder.encode(h.getBuild().getNextPage()));
						pw.print("\">Next page</a>");
					}
					pw.print("</TD></TR></TABLE>");
				} catch (SAXParseException e) {
					printError(e, pw);
				} catch (IOException ioe) {
					printError(ioe, pw);
				} catch (SAXException e) {
					printError(e, pw);
				} catch (ParserConfigurationException e) {
					printError(e, pw);
				} finally {
					printHTMLEndTags(pw);
				}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
				doGet(req,resp);
	}

	@Override
	public String getPageTitle() {
		return "Twitter Feed Example";
	}


}
