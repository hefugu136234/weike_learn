package com.lankr.tv_cloud.web;

import org.junit.Ignore;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

@Ignore
public class ThreadTest extends Thread{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		JUnitCore core=new JUnitCore();
		core.run(Request.method(JunTest.class, "test"));
	}

}
