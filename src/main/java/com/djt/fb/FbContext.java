package com.djt.fb;

import java.io.IOException;
import java.util.ArrayList;

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
	
	public String getAuthToken() throws FacebookException, IOException {
		authToken = getFBClient().auth_createToken();
		return authToken;
	}
	
	public String getCode() {
		return code;
	}


	public String getState() {
		return state;
	}
	
	public String getUserName() throws FacebookException, IOException {
		assert authToken !=null;
		ArrayList<Long> userids = new ArrayList<Long>(1);
		ArrayList<ProfileField> fields = new ArrayList<ProfileField>(1);
		fields.add(ProfileField.FIRST_NAME);
		userids.add(fbClient.users_getLoggedInUser());
		Document d = fbClient.users_getStandardInfo(userids, fields);
		System.out.println(d);
		return "Dan";
		
	}
}
