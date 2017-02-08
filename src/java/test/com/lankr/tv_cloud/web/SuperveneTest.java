package com.lankr.tv_cloud.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

@Ignore
public class SuperveneTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SuperveneTest test=new SuperveneTest();
		List<ThreadTest> testList=test.creatThreadTests(10);
		for (ThreadTest threadTest : testList) {
			threadTest.start();
		}

	}
	
	public List<ThreadTest> creatThreadTests(int num){
		List<ThreadTest> testList=new ArrayList<ThreadTest>();
		for(int i=0;i<num;i++)
		testList.add(new ThreadTest());
		return testList;
	}

}
