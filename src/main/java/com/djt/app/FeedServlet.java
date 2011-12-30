package com.djt.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.net.URL;

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

public class FeedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


				try {
					SAXParserFactory factory = SAXParserFactory.newInstance();
					SAXParser parser = factory.newSAXParser();
					URL url = new URL("http://search.twitter.com/search.atom?q=airport+delay");
					InputStream is = url.openConnection().getInputStream();
		//			InputStream is = new FileInputStream(new File("C:\\Users\\Dan\\Documents\\twitter04OCT11-1740.xml"));
					int available = is.available();

		/*			StringBuffer buff = new StringBuffer(available);
					byte[] b = new byte[128];
					int read = 0;
					while ((read = is.read(b))>0) {
						buff.append(new String(b));
					}
					System.out.println(buff);
		*/
					TwitterFeedHandler h = new TwitterFeedHandler();
					parser.parse(is, h);
					PrintWriter pw = resp.getWriter();
					pw.print("<HTML><HEAD><TITLE>Twitter feed example</TITLE><HEAD><BODY>");
					for(DataItem d: h.getBuild().getEntries()){
						pw.print(d);
						pw.print("<BR/>");
					}
					pw.print("<a href=\"");
					pw.print(h.getBuild().getNextPage());
					pw.print("\">Next page</a>");
					pw.print("</BODY></HTML>");
				} catch (SAXParseException e) {
					e.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
    }

}