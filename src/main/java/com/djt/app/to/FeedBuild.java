package com.djt.app.to;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A feed build is a particular instance of a feed. Feeds are generally built according
 * to a schedule (hourly etc) and this class represents the results of calling the feed
 * at a specific time. The build content is stored, and the feed marked with the lastest
 * build time and a change to the schedules. Different feed types (RSS / Twitter) will
 * provide the build / schedule in different ways (i.e. nodes)
 * <P>
 * @author Dan
 *
 */
public abstract class FeedBuild {
	private Calendar lastBuildDate = null;
	private List<DataItem> entries = new ArrayList<DataItem>(15);

	/**
	 * @param lastBuildDate the lastBuildDate to set
	 */
	public void setLastBuildDate(Calendar lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	/**
	 * @return the lastBuildDate
	 */
	public Calendar getLastBuildDate() {
		return lastBuildDate;
	}

	public void add(DataItem di) {
		entries.add(di);
	}

	public List<DataItem> getEntries() {
		return new ArrayList<DataItem>(entries); // defensive copying
	}
}
