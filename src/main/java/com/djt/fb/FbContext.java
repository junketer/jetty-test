package com.djt.fb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Document;

import com.facebook.api.FacebookException;
import com.facebook.api.FacebookXmlRestClient;
import com.facebook.api.ProfileField;

/**
 * Acts as a wrapper around the Facebook API
 * <P>
 * @author Dan
 *
 */
public class FbContext {
	private static final String APP_ID= "154418354667612";
	private static final String APP_SECRET="786e5838bea0f55ad3255a787d13ef0e";
	
	private static final String LOGIN_REDIRECT_URL ="{0}";
	
	public static final String OAUTH_URL = "https://graph.facebook.com/oauth/access_token?client_id="+APP_ID+
			"&redirect_uri={0}&client_secret="+APP_SECRET+"&code={1}";
	
	public static final String USER_URL = " https://graph.facebook.com/me?access_token={0}";
	
	private final String code;
	private final String state;
	private String authToken;
	
	private FacebookXmlRestClient fbClient = null;
	
	public static final String LOGIN_DIALOG_URL = "https://www.facebook.com/dialog/oauth?client_id="+APP_ID+
			"&redirect_uri="+LOGIN_REDIRECT_URL+"&scope=publish_actions&state=";
	

	public FbContext(String code, String state) {
		super();
		this.code = code;
		this.state = state;
	}


	private FacebookXmlRestClient getFBClient() {
		assert code !=null;
		if (fbClient == null) {
			fbClient = new FacebookXmlRestClient(APP_ID, APP_SECRET, code);
		}
		return fbClient;
	}
	
	public String getAuthToken(String redirectUrl) throws FacebookException, IOException {
		assert (code !=null && code.length()>1);
		String urlString = MessageFormat.format(OAUTH_URL, redirectUrl, code);
		StringBuffer data = callFb(urlString);
		int i = data.toString().indexOf("&");
		authToken = data.toString().substring(0, i);
		return authToken;
	}


	private StringBuffer callFb(String urlString) throws MalformedURLException,
			IOException {
		URL url = new URL(urlString);
		URLConnection con = url.openConnection();
		con.connect();
		InputStream is = null;
		is = con.getInputStream();
		int read = 0;
		StringBuffer data = new StringBuffer();
		byte[] b = new byte[128];
		while ((read = is.read(b))>0) {
			data.append(new String(b, 0, read));
		}
		is.close();
		return data;
	}
	
	public String getCode() {
		return code;
	}


	public String getState() {
		return state;
	}
	
	public String getUserName() throws FacebookException, IOException {
		assert authToken !=null;
		String url = MessageFormat.format(USER_URL, authToken);
		StringBuffer data = callFb(url);
		System.out.println("user data: " + data);
		return data.toString();
		
	}
}
