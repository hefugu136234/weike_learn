package com.lankr.tv_cloud.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.IntegralFacade;
import com.lankr.tv_cloud.facade.WxModelMessageFacade;
import com.lankr.tv_cloud.model.IntegralWeekReport;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.api.webchat.util.TempleKeyWord;

public class WxIntegralTask {// extends BaseAPIController{

	@Autowired
	protected IntegralFacade integralFacade;

	@Autowired
	private WxModelMessageFacade wxModelMessageFacade;
	
	@Autowired
	private WxModelMessageFacade wxUndueModelMessageFacade;
	
	public WxModelMessageFacade getModelMessageFacade(){
//		if(Config.env.equals(Config.Environment.PRODUCT.getValue())){
//			return wxUndueModelMessageFacade;
//		}
		return wxModelMessageFacade;
	}

	public void integralTask() {
		System.out.println("任务执行时间：" + System.currentTimeMillis());
		Date date = new Date();
		// 一周之前的时间
		Date start = getDateBefore(date, 7);
		List<IntegralWeekReport> list = integralFacade
				.userIntegralWeekReportWx(start, date);
		if (list == null || list.isEmpty()) {
			return;
		}
		for (IntegralWeekReport integralWeekReport : list) {

			// 每一条记录的最大时间，比上一周发送时间大，说明这周积分有变动，否则积分无变化，则剔除掉
			if (integralWeekReport.getAddition()>0) {
				// 此条记录可以发送
				User user = new User();
				user.setId(integralWeekReport.getUserId());
				TempleKeyWord templeKeyWord=buildTempleKeyWord(integralWeekReport);
				getModelMessageFacade().integralDaily(templeKeyWord, user);
			}
		}
	}
	
	public TempleKeyWord buildTempleKeyWord(IntegralWeekReport integralWeekReport){
		TempleKeyWord templeKeyWord=new TempleKeyWord();
		int resultChange=integralWeekReport.getAddition();
		String val="";
		if(resultChange>0){
			val="增长了"+resultChange+"分！";
		}else if(resultChange<0){
			resultChange=-resultChange;
			val="减少了"+resultChange+"分！";
		}else{
			val="变化了"+resultChange+"分！";
		}
		templeKeyWord.setFirst("好厉害，在过去的一周,您的积分"+val);
		templeKeyWord.setKeyword1(integralWeekReport.getCurrent()+"");
		templeKeyWord.setKeyword2(integralWeekReport.getHistory()+"");
		return templeKeyWord;
	}

	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	public static void main(String[] args) {
		Date date = new Date();
		date = getDateBefore(date, 7);
		System.out.println(Tools.df1.format(date));
	}

}
