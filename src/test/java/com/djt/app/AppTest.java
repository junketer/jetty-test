package com.djt.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.djt.app.feed.handlers.RSSFeedHandler;
import com.djt.app.feed.handlers.TwitterFeedHandler;
import com.djt.app.to.DataItem;

import org.json.*;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }


	public void testTwitterFeed() {
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
			for(DataItem d: h.getBuild().getEntries()){
				System.out.println(d.asHtml());
			}
			System.out.println(h.getBuild().getNextPage());

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

	public void testOAGFeedTestCase() {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			URL url = new URL("http://www.ubmaviation.com/Press-Office/feed/rss/");
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
			RSSFeedHandler h = new RSSFeedHandler();
			parser.parse(is, h);
			for(DataItem d: h.getBuild().getEntries()){
				System.out.println(d);
			}

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

	public void testEncodeDecode() {
		String dan = "Dan";
		String dan64 = MyBase64.encode(dan.getBytes());
		System.out.println(dan + " encoded: " + dan64);
		String decoded = new String(MyBase64.decode(dan64));
		System.out.println(dan64 + " decoded: " + decoded);
		assertEquals(dan, decoded);
	}

	public void testFBRequestDecode() throws JSONException {
		String signed_request="4QQj8zlP-QZwuXwAexwA-a2XU4A"+
			"foWbNsMu6bEhWrzY.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEzMjU4MDQ0MDA"+
			"sImlzc3VlZF9hdCI6MTMyNTgwMDA0Niwib2F1dGhfdG9rZW4iOiJBQUFDTWNWTXRRRndCQUVSSWRtajJ"+
			"pOGdobTJXWkFUemlQb2ZPYktWNXBzMHFZWkM5bWY4WkNVWkFEQzFiNXZaQjV5cmtwWkFjUHBWa1ZYbGt"+
			"Cc1Fqb1hyUzQzbzBsNkJ6YVFUV2FuWkJmcTE2QVpEWkQiLCJ1c2VyIjp7ImNvdW50cnkiOiJnYiIsImx"+
			"vY2FsZSI6ImVuX0dCIiwiYWdlIjp7Im1pbiI6MjF9fSwidXNlcl9pZCI6IjYxODM0Mjk5NCJ9";
		String hashBit = signed_request.substring(0,signed_request.indexOf("."));
		String dataBit= signed_request.substring(signed_request.indexOf(".")+1);

		System.out.println(signed_request+ " encoded: " + signed_request);

		System.out.println(" hashBit: " + hashBit);
		System.out.println(" dataBit: " + dataBit);

		String decoded = new String(MyBase64.decode(dataBit));
		System.out.println(" decoded: " + decoded);
		JSONObject jObj = new JSONObject(decoded);
		System.out.println("user_id: " + jObj.get("user_id"));
	}

}
