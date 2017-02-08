package com.lankr.tv_cloud.web.front;

import java.util.List;

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
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.ActivityTotalApiData;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontActivityPage;
import com.lankr.tv_cloud.web.front.vo.FrontAcvitiyPageList;
import com.lankr.tv_cloud.web.front.vo.FrontResourceList;

@Controller
@RequestMapping(value = BaseFrontController.PC_PRIOR)
public class WebActivityController extends BaseFrontController {

	/**
	 * 活动的总页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/activity/list/page")
	public ModelAndView activityIndexPage(HttpServletRequest request) {
		// 顶部banner;
		getCommonBanner(request);

		FrontAcvitiyPageList pageList = new FrontAcvitiyPageList(true);
		// 推荐活动的数据
		List<Activity> list = activityFacade.recommendActivities(6);
		pageList.buildActivityList(list);

		// 最新直播数据
		List<Broadcast> living = broadcastFacade.searchCastCarousel(30);
		pageList.buildLiving(living, broadcastFacade);
		// 直播回顾
		Pagination<Broadcast> lived = broadcastFacade.broadcastFrontList(0, 10);
		pageList.buildLived(lived, broadcastFacade);

		request.setAttribute("data_total", pageList);
		
		bulidRequest("web查看活动列表", null, null,
				Status.SUCCESS.message(), null, "成功", request);

		return redirectPageView("web/activity_index");
	}


	/**
	 * 活动详情页面
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/activity/detail/{uuid}")
	public ModelAndView activityDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity=activityFacade.getByUuid(uuid);
		if(activity==null||!activity.apiUseable()){
			return redirectErrorPage(request, "此活动不存在或已下线");
		}
		ActivityTotalApiData data=activityCompletedJson(activity);
		
		FrontActivityPage page=new FrontActivityPage(true);
		page.buildActivityTotal(activity, data, effectResourceFacade());
		
		request.setAttribute("activity_data", page);
		
		bulidRequest("web查看活动详情", "activity", activity.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		
		return redirectPageView("web/activity");
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/activity/resource/page", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String subSubjectPage(HttpServletRequest request,
			@RequestParam int size, @RequestParam int currentPage,
			@RequestParam String uuid) {
		Activity activity=activityFacade.getByUuid(uuid);
		int searchId=0;
		if(activity!=null&&activity.apiUseable()){
			searchId=OptionalUtils.traceInt(activity, "id");
		}
		int from=Math.max(currentPage-1, 0);
		if(size>10){
			size=10;
		}
		from=from*size;
		Pagination<Resource> pagination=effectResourceFacade().resourceActivityFrontPage(searchId, from, size);
		FrontResourceList frontResourceList = new FrontResourceList();
		frontResourceList.buildPageItem(pagination);
		return frontResourceList.toJSON();
	}


}
