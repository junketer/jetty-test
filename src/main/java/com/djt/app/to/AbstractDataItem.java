package com.djt.app.to;

import java.io.Serializable;

public class AbstractDataItem implements DataItem {

	protected final long id;
	protected final String description;
	protected final String data;

	public AbstractDataItem(long id, String description, String data) {
		this.id = id;
		this.description = description;
		this.data = data;
	}

	public Serializable getData() {
		return data;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
