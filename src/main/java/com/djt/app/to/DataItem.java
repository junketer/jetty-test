package com.djt.app.to;

import java.io.Serializable;

public interface DataItem extends Serializable {

	public long getId();
	public String getDescription();
	public Serializable getData();
}
