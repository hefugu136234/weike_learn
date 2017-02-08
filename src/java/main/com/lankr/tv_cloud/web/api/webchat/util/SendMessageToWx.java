package com.lankr.tv_cloud.web.api.webchat.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.Tools;

public class SendMessageToWx {
	
	public static void main(String[] args) {
		sendMessageToWx();
	}
	
	public static String sendMessageToWx(){
		//message:{"errcode":0,"errmsg":"ok","msgid":402400426} message:{"errcode":40003,"errmsg":"invalid openid hint: [1cW750953age1]"}
		//message:{"errcode":40037,"errmsg":"invalid template_id hint: [zAcYqa0981ge10]"}
		String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+WechatTokenDeal.getAccessToken();
		Gson gson=new Gson();
		String json=gson.toJson(testbuildMessage());
		String message=HttpUtils.sendPostRequest(url, json,true);
		System.out.println("message:"+message);
		return message;
	}
	
	public static ModelMessage testbuildMessage(){
		ModelMessage mesageMessage=new ModelMessage();
		mesageMessage.setTouser("o9UKvw8f0tM0R47ajWSnL8yo_ZZM");
		mesageMessage.setTemplate_id("RwwNJSb40TVtwpDQCEchqzxyM3tYUU20hB92nzZlJ1E");
		mesageMessage.setUrl("www.baidu.com");
		//Map<String,ModelKeyNote> data=new HashMap<String, ModelKeyNote>();
		Map<String,ModelKeyNote> data=new HashMap<String, ModelKeyNote>();
		
		ModelKeyNote first=new ModelKeyNote();
		first.setColor(ModelKeyNote.BULE_COLOR);
		first.setValue("积分变动");
		
		ModelKeyNote FieldName=new ModelKeyNote();
		//FieldName.setColor(ModelKeyNote.BULE_COLOR);
		FieldName.setValue("时间");
		
		ModelKeyNote Account=new ModelKeyNote();
		Account.setColor(ModelKeyNote.BULE_COLOR);
		Account.setValue(Tools.df1.format(new Date()));
		
		ModelKeyNote change=new ModelKeyNote();
		//change.setColor(ModelKeyNote.BULE_COLOR);
		change.setValue("历史");
		
		
		ModelKeyNote CreditChange=new ModelKeyNote();
		CreditChange.setColor(ModelKeyNote.BULE_COLOR);
		CreditChange.setValue("1000");
		
		ModelKeyNote CreditTotal=new ModelKeyNote();
		CreditTotal.setColor(ModelKeyNote.BULE_COLOR);
		CreditTotal.setValue("800");
		
		ModelKeyNote Remark=new ModelKeyNote();
		Remark.setColor(ModelKeyNote.BULE_COLOR);
		Remark.setValue("感谢您的加入");
		
		data.put("FieldName", FieldName);
		data.put("Account", Account);
		data.put("first", first);
		data.put("change", change);
		data.put("CreditChange", CreditChange);
		data.put("CreditTotal", CreditTotal);
		data.put("Remark", Remark);
		mesageMessage.setData(data);
		return mesageMessage;
	}

}
