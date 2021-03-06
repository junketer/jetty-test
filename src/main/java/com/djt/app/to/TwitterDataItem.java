package com.djt.app.to;

import java.io.Serializable;

public class TwitterDataItem extends AbstractDataItem {

	private final String authorName;
	private final String authorProfileUrl;
	private final String authorImageUrl;
	private final String tweetUrl;
	private final String representation;

	public TwitterDataItem(long id, String description, String data, String author, String profileUrl, String imgUrl, String tweetUrl) {
		super(id,description,data);
		this.authorName=author;
		this.authorProfileUrl = profileUrl;
		this.authorImageUrl = imgUrl;
		this.tweetUrl = tweetUrl;

		StringBuffer buff = new StringBuffer("id:");
		buff.append(id);
		buff.append("; desc: ");
		buff.append(description);
		buff.append("; data: ");
		buff.append(data);
		buff.append("; author: ");
		buff.append(author);
		buff.append("; profileUrl: ");
		buff.append(profileUrl);
		buff.append("; imgUrl: ");
		buff.append(imgUrl);
		buff.append("; tweetUrl: ");
		buff.append(tweetUrl);
		representation = buff.toString();
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getAuthorProfileUrl() {
		return authorProfileUrl;
	}

	public String getAuthorImageUrl() {
		return authorImageUrl;
	}

	public String getTweetUrl() {
		return tweetUrl;
	}

	public String toString() {
		return representation;
	}

	@Override
	public String asHtml() {
		StringBuffer b = new StringBuffer();
		b.append("<TABLE><TR><TD><img src=\"");
		b.append(authorImageUrl);
		b.append("\" /></TD><TD class=\"data\">");
		b.append(data);
		b.append("</TD></TR><TR><TD>");
		b.append("</TD><TD><a href=\"");
		b.append(authorProfileUrl);
		b.append("\" target=\"_blank\">");
		b.append(authorName);
		b.append("</a></TD></TR></TABLE>");
		return b.toString();
	}
}
