package com.djt.app.to;

public class RSSDataItem extends AbstractDataItem {

	private final String url;
	public RSSDataItem(long id, String description, String data, String url) {
		super(id, description, data);
		this.url = url;
	}

}
