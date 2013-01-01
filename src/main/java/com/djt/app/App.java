package com.djt.app;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

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
        context.addServlet(new ServletHolder(new FbCanvas()),"/");
        context.addServlet(new ServletHolder(new HomeServlet()),"/search/*");
        context.addServlet(new ServletHolder(new TwitterFeedServlet()),"/twitter/*");
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(false);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
 
        resource_handler.setResourceBase("./src/main/webapp/");
 
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, context, new DefaultHandler() });
        server.setHandler(handlers);       

        //context.addBean(new Date());
        server.start();
        server.join();
    }
}
