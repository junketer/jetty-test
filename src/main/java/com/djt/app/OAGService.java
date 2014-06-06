package com.djt.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAGService extends HttpServlet {

	private HashMap<String,ArrayBlockingQueue<String>> serviceMap = new HashMap<String,ArrayBlockingQueue<String>>(5);
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8576738585940878244L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String[] parts = req.getPathInfo().substring(1).split("/");
		String service = null;
		if (parts.length>0) {
			service = parts[0];
		}
		System.out.println("GET with action: " + service);
		if (serviceMap.containsKey(service)) {		
			// need to obtain the data
			ArrayBlockingQueue<String> q = getQueue(service);
			resp.getWriter().write("q size: " + q.size());
			resp.getWriter().write("\n");
			if (q.peek()!=null) {
				try {
					resp.getWriter().write(q.take());
				} catch (InterruptedException e) {
					resp.getWriter().write("Interrupted requesting the data");		
				}				
			} else {
				resp.getWriter().write("No data");								
			}
		} else {
			resp.getWriter().write("No data");								
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//super.doPost(req, resp);
		
		String key = readKey(req);
		InputStream is = req.getInputStream();
		String data = read(is);
		System.out.println("POST for key: " + key +" payload len: " + data.length());
		put(key,data);
		
	}
	
	private String readKey(HttpServletRequest req) {
		
		return parsePathInfo(req.getPathInfo());
	}

	
	private String parseServiceKey(HttpServletRequest req) {
		String[] parts = req.getPathInfo().split("/");
		if ("".equals(parts[parts.length-1])) {
			return parts[parts.length-2];
		} else {
			return parts[parts.length-1];
		}
	}
	private String parsePathInfo(String pathInfo) {
		System.out.println(" parsing: " + pathInfo);
		String[] split = pathInfo.split("/");
		if ("".equals(split[split.length-1])) {
			return split[split.length-2];
		} else {
			return split[split.length-1];
		}
	}

	private String read(InputStream is) throws IOException {
		byte[] b = new byte[128];
		StringBuilder sb = new StringBuilder();
		int read=0;
		while ((read=(is.read(b))) > 0) {
			sb.append(new String(b,0,read));
		}
		return sb.toString();
	}

	private String get(String key) {
		ArrayBlockingQueue<String> q=getQueue(key);
		if (q.peek()!=null) {
			try {
				return q.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	private void put(String key, String data) {
		boolean success = getQueue(key).offer(data);
		if (!success) {
			System.out.println("Couldn't add data:");
			System.out.println(data);
		}
	}
	private ArrayBlockingQueue<String> getQueue(String key) {
		if (serviceMap.containsKey(key)) {
			return serviceMap.get(key);
		} else {
			ArrayBlockingQueue<String> q = new ArrayBlockingQueue<String>(500);
			serviceMap.put(key, q);
			return q;
		}
	}
}
