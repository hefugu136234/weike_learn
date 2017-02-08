package com.lankr.tv_cloud.web;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({APIControllerTest.class,AdvertisementControllerTest.class,
	ProjectControllerTest.class,AssetControllerTest.class,UserControllerTest.class})
public class ControllerUniltTest {
}
