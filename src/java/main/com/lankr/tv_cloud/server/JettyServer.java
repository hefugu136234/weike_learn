package com.lankr.tv_cloud.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;

public class JettyServer {
	
	private static Log logger = LogFactory.getLog(JettyServer.class);

	public static void main(String[] args) {
		Server server = new Server();
		QueuedThreadPool threads = new QueuedThreadPool(100);
		server.setThreadPool(threads);
		Connector connector = new SelectChannelConnector();
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });
		server.setSendDateHeader(false);
		server.setSendServerVersion(false);
		server.setStopAtShutdown(true);
		WebAppContext webapp = new WebAppContext("./WebRoot", "");
		server.addHandler(webapp);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
}
