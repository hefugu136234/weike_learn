package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.OptionalUtils;

@SuppressWarnings("all")
public class BooleanResultVo {
	private String bool;
	private String speakerName;
	private String sex;
	private String phoneNum;
	private String province;
	private String cityName;
	private String hospitalName;
	private String deparmentName;
	
	public static BooleanResultVo build(Speaker speaker){
		BooleanResultVo boolVo = new BooleanResultVo();
		if(null == speaker){
			boolVo.bool = "false";
		}else{
			boolVo.bool = "true";
			boolVo.speakerName = OptionalUtils.traceValue(speaker, "name");
			int _sex = speaker.getSex();
			if(_sex == 1){
				boolVo.sex = "男";
			} else if (_sex == 0){
				boolVo.sex = "女";
			}
			boolVo.phoneNum = OptionalUtils.traceValue(speaker, "mobile");
			boolVo.hospitalName = OptionalUtils.traceValue(speaker, "hospital.name");
			boolVo.deparmentName = OptionalUtils.traceValue(speaker, "department.name");
			boolVo.cityName = OptionalUtils.traceValue(speaker, "user.userExpand.city.name");
			boolVo.province = OptionalUtils.traceValue(speaker, "user.userExpand.city.province.name");
		}
		return boolVo;
	}
}
