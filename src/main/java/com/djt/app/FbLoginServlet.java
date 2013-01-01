package com.djt.app;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.djt.fb.FbContext;
import com.facebook.api.FacebookException;
import com.facebook.api.FacebookRestClient;

/**
 * see <code>https://developers.facebook.com/docs/howtos/login/server-side-login/</code>
 * for documentation on this implementation
 * <P>
 * @author Dan
 *
 */
public class FbLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3698638708352454055L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		process(req, resp);		
	}

	private void process(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String code = req.getParameter("code");
		String sessionState = getStateValue(req);
		String reqState = req.getParameter("state");
		
			if (code == null || "".equals(code)) {
				String url = MessageFormat.format(FbContext.LOGIN_DIALOG_URL, req.getRequestURL());
				resp.sendRedirect(url+createStateString(req));
			} else {
				if (reqState != null && reqState.equals(sessionState)) {
					FbContext fbContext = new FbContext(code, reqState);
					req.getSession().setAttribute("fbContext", fbContext);
					try {
						System.out.println(" auth token: " + fbContext.getAuthToken(req.getRequestURL().toString()));
						fbContext.getUserName();
					} catch (FacebookException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// Being hacked... ? Set forbidden header
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}
			
	
			}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	private String getStateValue(HttpServletRequest req) {
		if (req.getSession()!=null) {
			return (String) req.getSession().getAttribute("state");
		}
		return null;
	}
	private String createStateString(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		String state = MyBase64.encode(Long.toString(Math.round(Math.random())).getBytes());
		session.setAttribute("state", state);
		return state;
	}

}
