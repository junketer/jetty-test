package com.djt.app.to;

import java.util.Calendar;

public class TwitterFeedBuild extends FeedBuild {

	private String nextPage = null;

	/**
	 * @param nextPage the nextPage to set
	 */
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * @return the nextPage
	 */
	public String getNextPage() {
		return nextPage;
	}
}
