package com.lankr.tv_cloud.cache;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RemoteRef;

import sun.rmi.registry.RegistryImpl;
import sun.rmi.server.UnicastRef;
import sun.rmi.server.UnicastRef2;
import sun.rmi.server.Util;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

public class Client {
	
	public static void main(String[] args) throws Exception {
//		Registry register = getRegistry("127.0.0.1", 51000, ConfigurableRMIClientSocketFactory.getConfiguredRMISocketFactory());
//		String[] ss = register.list();		
	}
	
	public static Registry getRegistry(String host, int port, RMIClientSocketFactory csf) throws RemoteException
	{
	    Registry registry = null;

	    if (port <= 0)
	        port = Registry.REGISTRY_PORT;

	    if (host == null || host.length() == 0) {
	        try {
	            host = java.net.InetAddress.getLocalHost().getHostAddress();
	        } catch (Exception e) {
	            // If that failed, at least try "" (localhost) anyway...
	            host = "";
	        }
	    }

	    LiveRef liveRef = new LiveRef(new ObjID(ObjID.REGISTRY_ID), new TCPEndpoint(host, port, csf, null), false);
	    RemoteRef ref = (csf == null) ? new UnicastRef(liveRef) : new UnicastRef2(liveRef);

	    return (Registry) Util.createProxy(RegistryImpl.class, ref, false);
	}

}
