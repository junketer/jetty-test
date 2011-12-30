package com.djt.app.feed.handlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.djt.app.to.FeedBuild;
import com.djt.app.to.RSSDataItem;
import com.djt.app.to.RSSFeedBuild;
import com.djt.app.to.TwitterDataItem;

public class RSSFeedHandler extends DefaultHandler {


	private static final String UPDATED_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String LAST_BUILD_DATE_PATTERN ="E, d M yyyy HH:mm:ss Z";//Wed, 28 Sep 2011 13:58:43 +0000
	private static final DateFormat updatedDateFormat = new SimpleDateFormat(UPDATED_PATTERN);

	// node names
	private static final String LAST_BUILD_DATE_NODE_NAME = "lastBuildDate";
	private static final String DESC_NODE_NAME = "description";
	private static final String UPDATED_NODE_NAME = "updated";
	private static final String ENTRY_NODE_NAME = "entry";
	private static final String PUBLISHED_NODE_NAME = "published";
	private static final String AUTHOR_NODE_NAME = "author";
	private static final String NAME_NODE_NAME = "name";
	private static final String URI_NODE_NAME = "uri";
	private static final String LINK_NODE_NAME = "link";
	private static final String LINK_REL_ATT_NAME = "rel";
	private static final String LINK_NEXT_ATT_VAL = "next";
	private static final String LINK_ALT_ATT_VAL = "alternate";
	private static final String LINK_HREF_ATT_NAME = "href";
	private static final String LINK_IMG_ATT_VAL = "image";
	private static final String TITLE_NODE_NAME = "title";
	private static final String CONTENT_NODE_NAME = "content";
	private static final String ITEM_NODE_NAME = "item";

	private static final Map<String,NodeHandler> handlers = new HashMap<String,NodeHandler>(10);

	private RSSFeedBuild feedBuild= new RSSFeedBuild();

	private String currentNodeName = null;
	private String authorName = null;
	private String authorUrl = null;
	private String title = null;
	private String content = null;
	private String linkUrl = null;
	private String tweetUrl = null;
	private Calendar tweetUpdated = null;
	private Calendar lastBuildDate = null;
	private String imgUrl = null;

	private boolean isAuthorNode = false;
	private boolean isEntryNode = false;

	private StringBuffer charBuffer = new StringBuffer(128);

	private abstract class NodeHandler {
		void handleStartNode(Attributes attributes){};
		abstract void handleEndNode();
		void handle(char[] ch, int start, int length) {
			charBuffer.append(new String(ch,start,length));
		};
	}

	private class LastBuildDateHandler extends NodeHandler {
		void handleEndNode() {
			try {
				lastBuildDate = parseTime(charBuffer.toString(), LAST_BUILD_DATE_PATTERN);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resetCharBuffer();
		}
	}

	private class ItemNodeHandler extends NodeHandler {
		void handleEndNode() {
			// add the data item
			feedBuild.add(new RSSDataItem(0,title,content,linkUrl));
			isEntryNode=false;
		}
	}

	private class TitleCharHandler extends NodeHandler {

		@Override
		void handleEndNode() {
			// TODO Auto-generated method stub
			title = charBuffer.toString();
			resetCharBuffer();
		}

	}

	private class DescriptionCharHandler extends NodeHandler {

		@Override
		void handleEndNode() {
			// TODO Auto-generated method stub
			content = charBuffer.toString();
			resetCharBuffer();
		}

	}

	private class AuthorNameCharHandler extends NodeHandler {

		@Override
		void handleEndNode() {
			if (isAuthorNode) {
				authorName = charBuffer.toString();
				resetCharBuffer();
			}
		}

	}
	private class AuthorUriCharHandler extends NodeHandler {

		@Override
		void handleEndNode() {
			if (isAuthorNode) {
				authorUrl = charBuffer.toString();
			}
			resetCharBuffer();
		}

	}
	private class LinkNodeHandler extends NodeHandler {

		@Override
		void handleStartNode(Attributes attributes) {

		}

		@Override
		void handleEndNode() {
			linkUrl = charBuffer.toString();
			resetCharBuffer();
		}

	}

	private class UpdatedNodeHandler extends NodeHandler {

		@Override
		void handleEndNode() {
			try {
				Calendar cal = parseTime(charBuffer.toString());
				if (!isEntryNode) {
					feedBuild.setLastBuildDate(cal);
				} else {
					tweetUpdated = cal;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				resetCharBuffer();
			}

		}

	}

	public RSSFeedHandler() {
		super();
		handlers.put(UPDATED_NODE_NAME, new UpdatedNodeHandler());
		handlers.put(NAME_NODE_NAME, new AuthorNameCharHandler());
		handlers.put(TITLE_NODE_NAME, new TitleCharHandler());
		handlers.put(LINK_NODE_NAME, new LinkNodeHandler());
		handlers.put(URI_NODE_NAME, new AuthorUriCharHandler());
		handlers.put(DESC_NODE_NAME, new DescriptionCharHandler());
		handlers.put(LAST_BUILD_DATE_NODE_NAME, new LastBuildDateHandler());
		handlers.put(ITEM_NODE_NAME, new ItemNodeHandler());
	}

	private void resetCharBuffer() {
		charBuffer = new StringBuffer(128);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		if (handlers.containsKey(currentNodeName)) {
			handlers.get(currentNodeName).handle(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, name);
		currentNodeName = resolve(localName, name);
		System.out.println("end: " + localName == null ? name : localName);
		if (AUTHOR_NODE_NAME.equals(currentNodeName)) {
			isAuthorNode = false;
		}
		if (handlers.containsKey(currentNodeName)) {
			handlers.get(currentNodeName).handleEndNode();
		}
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, name, attributes);
		currentNodeName = resolve(localName, name);
		if (AUTHOR_NODE_NAME.equals(currentNodeName)) {
			isAuthorNode = true;
		}
		if (handlers.containsKey(currentNodeName)) {
			handlers.get(currentNodeName).handleStartNode(attributes);
		}
	}

	private Calendar parseTime(String timeString) throws ParseException {
		Calendar cal = Calendar.getInstance();
		timeString = timeString.replace("T", " ").replace("Z", "");
		cal.setTime(updatedDateFormat.parse(timeString));
		return cal;
	}


	private Calendar parseTime(String timeString, String pattern) throws ParseException {
		Calendar cal = Calendar.getInstance();
		timeString = timeString.replace("T", " ").replace("Z", "");
		cal.setTime(new SimpleDateFormat(pattern).parse(timeString));
		return cal;
	}

	private String resolve(String localName, String qName) {
		return (localName==null || localName.length()==0) ? qName : localName;
	}

	public FeedBuild getBuild() {
		return feedBuild;
	}

}
