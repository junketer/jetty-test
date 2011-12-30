package com.djt.app;

import java.util.Enumeration;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

/**
 * App class to set up the Jetty servlets / contexts
 *
 */
public class App
{
    public static void main(String[] args) throws Exception{
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new HomeServlet()),"/*");
        context.addServlet(new ServletHolder(new FeedServlet()),"/twitter/*");
        server.start();
        server.join();
    }
}
