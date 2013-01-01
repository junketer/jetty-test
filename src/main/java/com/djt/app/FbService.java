package com.djt.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.djt.fb.FbContext;

public class FbService extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getRequestURI();
		HttpSession session = req.getSession();
		JSONObject jo = null;
		System.out.println("Action: " + action);
		try {
			if (session != null && session.getAttribute("fbContext") != null) {
				FbContext ctx = (FbContext) session.getAttribute("fbContext");

				if (action.endsWith("user")) {
					System.out.println(" getting user info...");
					jo = ctx.getUserJson();
				}

				if (jo == null) {
					jo = new JSONObject();
					jo.put("name", "User not logged in");
				}

			} else {
				// data not available.
				System.out.println(" session or fbContext not set...");
				jo = new JSONObject();
				jo.put("name", "User not logged in");
			}

			writeJSONData(resp, jo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void writeJSONData(HttpServletResponse resp, JSONObject jo)
			throws JSONException, IOException {
		jo.write(resp.getWriter());
	}

}
