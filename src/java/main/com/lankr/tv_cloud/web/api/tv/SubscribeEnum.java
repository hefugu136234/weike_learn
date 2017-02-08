package com.lankr.tv_cloud.web.api.tv;

public enum SubscribeEnum {
	CZ// 初诊
	{
		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "初诊";
		}
	},
	XY// 洗牙
	{
		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "上门洗牙";
		}
	},
	FZ {
		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "复诊";
		}
	};// 复诊
	public String getDescription() {
		return "";
	}
}
