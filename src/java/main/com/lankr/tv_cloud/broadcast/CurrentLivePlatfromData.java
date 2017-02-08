package com.lankr.tv_cloud.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class CurrentLivePlatfromData {
	
	public static final int YIDUO_PLAT=1;//翼多平台
	
	public static final int ZHILIAO_PLAT=2;//知了平台
	
	public static final int BAIDU_PLAT=3;//百度直播平台
	
	public static Map<Integer, LivePlatform> map=new HashMap<Integer, LivePlatform>();
	
	
	public static Map<Integer, LivePlatform> getCurrentLiveFromData(){
		if(map.size()>0)
			return map;
		LivePlatform yiduo=new LivePlatform(YIDUO_PLAT, "翼多直播平台","http://live.vhdong.com/Home/LoginAPI",LivePlatform.GET_METHOD);
		map.put(YIDUO_PLAT, yiduo);
//		LivePlatform zhiliao=new LivePlatform(ZHILIAO_PLAT, "知了直播平台","http://lankr.gensee.com",LivePlatform.GET_METHOD);
//		map.put(ZHILIAO_PLAT, zhiliao);
		LivePlatform baidu=new LivePlatform(BAIDU_PLAT, "百度直播平台","",LivePlatform.GET_METHOD);
		map.put(BAIDU_PLAT, baidu);
		return map;
	}
	
	public static void main(String[] args) {
		LivePlatform from=new LivePlatform(1, "直播测试一",null,null);
		map.put(1, from);
		from=new LivePlatform(2, "直播测试二",null,null);
		map.put(2, from);
		System.out.println(map.size());
		for (int key : map.keySet()) {
			System.out.println(map.get(key).getPlatfromName());
		}
		Gson gson=new Gson();
		System.out.println(gson.toJson(map));
		List<LivePlatform> mapList=new ArrayList<LivePlatform>(map.values());
		System.out.println(mapList.size());
		for (LivePlatform livePlatform : mapList) {
			System.out.println(livePlatform.getPlatfromName());
		}
	}
	

}
