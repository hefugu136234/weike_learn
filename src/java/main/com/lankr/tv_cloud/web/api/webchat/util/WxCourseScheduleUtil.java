package com.lankr.tv_cloud.web.api.webchat.util;

import java.text.DecimalFormat;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class WxCourseScheduleUtil {

	// 计算课程的学习进度
	public static int calculationProgress(int totalNum, int passNum) {
		if (totalNum == 0) {
			return 0;
		}
		DecimalFormat format = new DecimalFormat("0");
		float f = (float) passNum;
		float result = f / totalNum * 100;
		return Integer.parseInt(format.format(result));
	}

	// 计算一个资源是否学习完毕
	public static boolean calculationResourceFinish(Resource resource,
			int viewTime) {
		boolean flag = false;
		if(resource==null){
			return flag;
		}
		if (resource.getType() == Type.PDF) {
			if (viewTime > 0) {
				flag = true;
			}
		} else if (resource.getType() == Type.THREESCREEN) {
			int videoTime = OptionalUtils.traceInt(resource,
					"threeScreen.videoTime");
			if (viewTime >= videoTime) {
				flag = true;
			}
		} else if (resource.getType() == Type.VIDEO) {
			int tmp = OptionalUtils.traceInt(resource, "video.duration");
			if (viewTime >= tmp) {
				flag = true;
			}
		}
		return flag;
	}

}
