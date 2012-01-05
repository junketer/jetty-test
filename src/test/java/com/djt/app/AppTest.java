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
}
