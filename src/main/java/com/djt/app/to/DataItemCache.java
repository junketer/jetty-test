package com.djt.app.to;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DataItemCache {
	private static DataItemCache INSTANCE = new DataItemCache();
	private static int CACHE_SIZE = 100;

	public static DataItemCache getInstance() {
		return INSTANCE;
	}

	private HashMap<Long,DataItem> cache = new HashMap<Long,DataItem>(CACHE_SIZE);

	/**
	 * Add an item to the cache
	 * @param item
	 */
	public void add(DataItem item) {
		synchronized(cache) {
			if (cache.size()==CACHE_SIZE) {
				// maintain the cache size
				List<Long> keys = new ArrayList<Long>(cache.keySet());
				Collections.sort(keys);
				cache.remove(keys.get(0));// remove oldest item
			}
			cache.put(item.getId(),item);
		}
	}

	/**
	 * Return all cached items.
	 * @return
	 */
	public List<DataItem> getAllItems() {
		// use defensive copying to maintain the integrity of the cache
		return new ArrayList<DataItem>(cache.values());
	}

	/**
	 * Return the items in the cache with id > from. clients can passs the highest
	 * id they hold to this method to obtain new data items
	 * @param from
	 * @return
	 */
	public List<DataItem> getItems(long from) {
		ArrayList<DataItem> items = new ArrayList<DataItem>((int) (cache.size()-from));
		for(DataItem i: cache.values()) {
			if (i.getId()>from) {
				items.add(i);
			}
		}
		return items;
	}

	public int getCacheSize() {
		return cache.size();
	}
}
