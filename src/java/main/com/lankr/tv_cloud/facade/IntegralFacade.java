package com.lankr.tv_cloud.facade;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.IntegralWeekReport;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;

/**
 * @version 1.2
 * @author Kalean.Xiang
 * */
public interface IntegralFacade {

	/**
	 * 用户注册成功
	 * 
	 * @param user
	 *            成功注册用户
	 * */
	public int actionRegister(User user);

	/**
	 * 实名认证成功
	 * 
	 * @param user
	 *            实名用户
	 * */
	public int actionCertification(User user);

	/**
	 * 通过分享有礼，邀请好友并注册成功
	 * 
	 * @param user
	 *            分享者
	 * @param registerUser
	 *            注册者
	 * */

	public int actionShareRegister(User user, User registerUser);

	/**
	 * 贡献资源
	 * 
	 * @param resource
	 *            贡献的资源
	 * */
	public int actionContributeResource(Resource resource);

	/**
	 * 资源被播放，资源拥有者加分
	 * 
	 * @param resource
	 *            播放的资源
	 * @param fromUser查看的用户
	 * */
	public int actionResourcePlayed(Resource resource, User fromUser);

	/**
	 * 资源被点赞，资源拥有者加分
	 * 
	 * @param 点赞的用户
	 * */
	public int actionResourcePraised(Resource resource, User fromUser);

	/**
	 * 资源被分享，资源拥有者加分
	 * 
	 * @param fromUser分享资源的用户
	 * */
	public int actionResourceShared(Resource resource, User fromUser);

	/**
	 * 用户播放资源
	 * 
	 * @param user
	 *            用户
	 * @param resource
	 *            播放的资源
	 * */
	public int actionUserPlayResource(User user, Resource resource);

	/**
	 * 用户参与投票
	 * 
	 * @param user
	 *            用户
	 * @param resource
	 *            投票的资源
	 * */
	public int actionUserVoteResource(User user, Resource resource);

	/**
	 * 用户点赞
	 * 
	 * @param user
	 *            用户
	 * @param resource
	 *            点赞的资源
	 * 
	 * */
	public int actionUserPraiseResource(User user, Resource resource);

	/**
	 * 用户分享资源被点击
	 * 
	 * @param sharingUser
	 *            分享资源的用户
	 * @param resource
	 *            分享的资源
	 * @param viewedUser
	 *            查看分享的用户
	 * */
	public int actionUserSharingViewed(User sharingUser, Resource resource,
			User viewedUser);

	/**
	 * 获取用户的总积分
	 * 
	 * @param user
	 *            target用户
	 * */
	public int fetchUserIntegralTotal(User user);

	/**
	 * 用户消耗积分
	 * 
	 * @he 修改 2016-4-1 用户积分兑换，的实体商品，需要添加兑换的地址
	 *
	 * */
	public ActionMessage userConsumeIntgral(User user,
			IntegralConsume consumeable, ReceiptAddress receiptAddress);

	/**
	 * 获取积分历史记录，时间排序
	 * */

	public Pagination<IntegralRecord> userIntegralRecords(User user, String q,
			int page, int batch_size);

	/**
	 * 获取积分兑换历史记录，时间排序
	 * */

	public Pagination<IntegralRecord> userIntegralConsumeRecords(User user,
			int page, int batch_size);

	/**
	 * 获取积分新增记录，时间排序
	 * */

	public Pagination<IntegralRecord> userIntegralFetchRecords(User user,
			int page, int batch_size);

	/**
	 * 获取所有用户积分兑换历史记录，时间排序
	 */
	public Pagination<IntegralRecord> allIntegralConsumeRecords(String query,
			int startPage, int pageSize);

	/**
	 * 新增商品
	 * */
	public ActionMessage addIntegralConsume(IntegralConsume consume);

	/**
	 * 新增积分商品列表
	 */
	public Pagination<IntegralConsume> searchIntegralConsumeList(
			String searchvalue, int from, int size);

	/**
	 * 积分商品信息修改
	 */
	public Status updateIntegralConsume(IntegralConsume integralConsume);

	/**
	 * 积分商品状态更改
	 */
	public Status updateIntegralConsumeStatus(IntegralConsume integralConsume);

	public IntegralConsume getIntegralConsumeByUuid(String uuid);

	/**
	 * 根据兑换记录uuid查询该记录详情
	 */
	public IntegralRecord getIntegralRecordByUuid(String uuid);

	/**
	 * 更新兑换记录状态
	 */
	public IntegralRecord updateIntegralRecordStatus(IntegralRecord recordByUuid);

	public List<IntegralConsume> getLatestGoodsForWx();

	/**
	 * 超管手动修改积分
	 * 
	 * @param user
	 *            目标修改用户
	 * @param integral
	 *            本次修改积分数量
	 */
	// public ActionMessage superAdminChangeIntegral(User user, int integral);

	/**
	 * 系统后台给用户增减积分
	 * 
	 * @param user
	 *            target user
	 * @param integral
	 *            变动的积分
	 * @return 变更的积分值
	 * */
	public int actionSystemGrantUserIntegral(User user, int integral,
			String mark);

	/**
	 * 积分兑换记录微信展示
	 */
	public List<IntegralRecord> userExchangeIntegralWx(User user,
			String startTime, int batch_size);

	/**
	 * 我的积分详情微信展示
	 */
	public List<IntegralRecord> userIntegralDetailWx(User user,
			String startTime, int batch_size);

	/**
	 * 微信发送每周积分消息
	 * 
	 * @return
	 */
	public List<IntegralWeekReport> userIntegralWeekReportWx(Date start,
			Date end);

	/**
	 * 后台创建直播时候，指定进入直播需要消耗的积分
	 * 
	 * @author Kalean.Xiang
	 * @createDate: 2016年3月23日
	 * @modifyDate: 2016年3月23日
	 * @param cast
	 *            直播对象
	 * @param intergral
	 *            消耗
	 */
	public ActionMessage saveBroadcastIntegeralConsume(Broadcast cast,
			int integral);

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年3月23日
	 * @modifyDate: 2016年3月23日 根据直播获取积分消耗规则
	 */
	public IntegralConsume searchBroadcastIntegralConsume(Broadcast cast);

	/**
	 * 获取指定用户的历史最高积分和当前积分
	 * 
	 * @param user
	 * @return
	 */
	public IntegralWeekReport getUserMaxAndHisRecordByUserId(User user);
	
	/**
	 * 上一周积分的具体变化
	 * @param userId
	 * @param lastWeek
	 * @return
	 */
	public int userIntegralWeekChange(int userId,Date lastWeek);
	
	/**
	 * 用户评论资源,增加积分
	 */
	public int userResourceComment(User user,Resource resource); 
	
	/**
	 * 线下活动消耗的积分
	 * @param cast
	 * @param integral
	 * @return
	 */
	public ActionMessage<?> offlineActivityIntegeralConsume(OfflineActivity offlineActivity,
			int integral);
	
	/**
	 * 用户评论增加积分
	 */
	public int userCommentAddIntegeral(User user); 
	

}
