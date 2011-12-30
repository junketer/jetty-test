package com.djt.app.feed.handlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.djt.app.to.FeedBuild;
import com.djt.app.to.SimpleDataItem;
import com.djt.app.to.TwitterDataItem;
import com.djt.app.to.TwitterFeedBuild;

public class TwitterFeedHandler extends DefaultHandler {

	private static final String UPDATED_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final DateFormat updatedDateFormat = new SimpleDateFormat(UPDATED_PATTERN);

	// node names
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

	private static final Map<String,NodeHandler> handlers = new HashMap<String,NodeHandler>(10);

	private TwitterFeedBuild feedBuild= new TwitterFeedBuild();

	private String currentNodeName = null;
	private String authorName = null;
	private String authorUrl = null;
	private String title = null;
	private String content = null;
	private String linkUrl = null;
	private String tweetUrl = null;
	private Calendar tweetUpdated = null;
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

	private class TitleCharHandler extends NodeHandler {

		@Override
		void handleEndNode() {
			// TODO Auto-generated method stub
			title = charBuffer.toString();
			resetCharBuffer();
		}

	}

	private class ContentCharHandler extends NodeHandler {

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
			String relValue = attributes.getValue(LINK_REL_ATT_NAME);
			String href = attributes.getValue(LINK_HREF_ATT_NAME);
			if (LINK_ALT_ATT_VAL.equals(relValue)) {
				tweetUrl = href;
			} else if (LINK_IMG_ATT_VAL.equals(relValue)) {
				imgUrl = href;
			} else if (LINK_NEXT_ATT_VAL.equals(relValue)) {
				feedBuild.setNextPage(href);
			}
		}

		@Override
		void handleEndNode() {
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

	private class EntryNodeHandler extends NodeHandler {

		@Override
		void handleEndNode() {
			// add the data item
			feedBuild.add(new TwitterDataItem(0,title,content,authorName, authorUrl,imgUrl,tweetUrl));
			isEntryNode=false;
		}

		@Override
		void handleStartNode(Attributes attributes){
			isEntryNode=true;
		};
	}

	public TwitterFeedHandler() {
		super();
		handlers.put(UPDATED_NODE_NAME, new UpdatedNodeHandler());
		handlers.put(NAME_NODE_NAME, new AuthorNameCharHandler());
		handlers.put(ENTRY_NODE_NAME, new EntryNodeHandler());
		handlers.put(TITLE_NODE_NAME, new TitleCharHandler());
		handlers.put(LINK_NODE_NAME, new LinkNodeHandler());
		handlers.put(URI_NODE_NAME, new AuthorUriCharHandler());
		handlers.put(CONTENT_NODE_NAME, new ContentCharHandler());

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

	private String resolve(String localName, String qName) {
		return (localName==null || localName.length()==0) ? qName : localName;
	}

	public TwitterFeedBuild getBuild() {
		return feedBuild;
	}
}
