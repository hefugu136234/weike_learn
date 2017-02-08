package com.lankr.tv_cloud.web.front;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.web.api.webchat.util.BroadcastPaltUrl;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontAcvitiyPageList;
import com.lankr.tv_cloud.web.front.vo.FrontBroadcastItem;

@Controller
@RequestMapping(value = BaseFrontController.PC_PRIOR)
public class WebBroadcastController extends BaseFrontController{
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/live/detail/{uuid}")
	public ModelAndView liveDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			return redirectErrorPage(request, "本直播不存在或已下线");
		}

		FrontBroadcastItem item = new FrontBroadcastItem();
		IntegralConsume consume = integralFacade
				.searchBroadcastIntegralConsume(broadcast);
		int existNUm = broadcastFacade.broadcastBookCount(broadcast);
		item.buildDetail(broadcast, existNUm, consume);
		request.setAttribute("broadcast_data", item);
		
		bulidRequest("web查看直播详情", "broadcast", broadcast.getId(),
		Status.SUCCESS.message(), null, "成功", request);
		
		return redirectPageView("web/live");
	}
	
	/**
	 * 点击观看直播后的操作
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/live/click/view", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String liveClickView(HttpServletRequest request,
			@RequestParam String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			return failureModel("本直播不存在或已下线").toJSON();
		}
		Date nowDate = new Date();
		if(nowDate.before(broadcast.getEndDate())){
			//返回直播链接
			User user=getCurrentUser(request);
//			//测试代码
//			if(user==null){
//				user=new User();
//				user.setUsername("13856567891");
//			}
			String url=BroadcastPaltUrl.platCastEntrance(user, broadcast,BroadcastPaltUrl.WEB_PLAT);
			if(url!=null){
				//此处进入三方直播
				bulidRequest("web直播跳转第三方平台", "broadcast", broadcast.getId(),
						Status.SUCCESS.message(), null, "成功", request);
				return successModel(url).toJSON();
			}
			return failureModel("直播链接已损坏").toJSON();
		}
		//返回录播链接
		Resource resource=broadcast.getResource();
		if(resource!=null){
			String url=BaseFrontController.PC_PRIOR+"/resource/detail/"+resource.getUuid();
			return successModel(url).toJSON();
		}
		return failureModel("直播已结束，暂无录播，请稍后！").toJSON();
	}
	
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/broadcast/baidu/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView baiduPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			return redirectErrorPage(request, "本直播不存在或已下线");
		}
		FrontBroadcastItem item = new FrontBroadcastItem();
		item.buildBaiduLive(broadcast);
		request.setAttribute("vo_data", item);
		bulidRequest("web观看百度直播直播", "broadcast", broadcast.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("web/live_baidu");
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/tv/broadcast/baidu/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView tvbaiduPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null || !broadcast.apiUseable()) {
			return redirectErrorPage(request, "本直播不存在或已下线");
		}
		FrontBroadcastItem item = new FrontBroadcastItem();
		item.buildBaiduLive(broadcast);
		request.setAttribute("vo_data", item);
		bulidRequest("web观看百度直播直播", "broadcast", broadcast.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("web/live_baidu");
	}
	
	/**
	 * 精彩直播回顾分页
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/wonder/live/record", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String wonderLive(HttpServletRequest request,
			@RequestParam int size, @RequestParam int currentPage) {
		int from = Math.max(currentPage - 1, 0);
		if (size > 10) {
			size = 10;
		}
		from = from * size;
		Pagination<Broadcast> lived = broadcastFacade.broadcastFrontList(from,
				size);
		FrontAcvitiyPageList pageList = new FrontAcvitiyPageList();
		pageList.buildLived(lived, broadcastFacade);
		return pageList.toJSON();
	}

}
