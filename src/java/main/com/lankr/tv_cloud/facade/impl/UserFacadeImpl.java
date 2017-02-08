package com.lankr.tv_cloud.facade.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.orm.mybatis.mapper.AppAuthenticationMapper;
import com.lankr.orm.mybatis.mapper.MyCollectionMapper;
import com.lankr.orm.mybatis.mapper.PraiseMapper;
import com.lankr.orm.mybatis.mapper.RegisterTmpMapper;
import com.lankr.orm.mybatis.mapper.SpeakerMapper;
import com.lankr.orm.mybatis.mapper.UserExpandMapper;
import com.lankr.orm.mybatis.mapper.UserOverViewMapper;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.UserFacade;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.AppAuthentication;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.InvitcodeRecord;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.RegisterTmp;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.UserWorksRecord;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.UserInfo;

public class UserFacadeImpl extends FacadeBaseImpl implements UserFacade {

	private static final int USER_FETCH_SIZE = 8;

	public static final String USER_DELETE_SIGN_PREFIX = "$DELETE_";

	private AppAuthenticationMapper appAuthDao;

	@Autowired
	private UserOverViewMapper userOverViewMapper;

	@Autowired
	private MyCollectionMapper myCollectionMapper;

	@Autowired
	private PraiseMapper praiseMapper;

	@Override
	public Status addUser(User user) {
		try {
			userDao.add(user, getSqlAlias("addUser"));
		} catch (Exception e) {
			logger.error("add user with an error ", e);
		}
		return Status.SUCCESS;
	}

	@Override
	public User getUserByUuid(String uuid) {
		return userDao.getById(uuid, getSqlAlias("searchUserByUuid"));
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userDao.getById(username, getSqlAlias("searchUserByUsername"));
	}

	@Override
	protected String namespace() {
		return "user";
	}

	@Override
	public List<UserInfo> fetchUserBaseInfo(String query, int page) {
		query = filterSQLSpecialChars(query).trim();
		String sql = "select * from user where isActive=1 and (username like "
				+ buildSqlSeg(query) + " or nickname like "
				+ buildSqlSeg(query) + " or pinyin like " + buildSqlSeg(query)
				+ ") limit " + USER_FETCH_SIZE * page + ", " + USER_FETCH_SIZE;
		List<UserInfo> userInfos = searchSql(sql, null, UserInfo.class);
		return userInfos;
	}

	@Override
	public List<Role> loadRoles() {
		return roleDao.searchAll(getSqlAlias("searchAllRoles"));
	}

	@Override
	public Status addUserReference(UserReference userReference) {
		try {
			userReferenceDao
					.add(userReference, getSqlAlias("addUserReference"));
		} catch (Exception e) {
			logger.error(e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	private static String buildSqlSeg(String str) {
		StringBuffer sb = new StringBuffer("'%");
		sb.append(str).append("%'");
		return sb.toString();
	}

	@Override
	public User login(String username, String password) {
		User fakeUser = new User();
		fakeUser.setUsername(username);
		fakeUser.setPassword(password);
		return userDao.search(fakeUser, getSqlAlias("login"));
	}

	@Override
	public List<UserReference> getUserReferences(User user) {
		if (user == null)
			return null;
		return userReferenceDao.searchByForeignKey(user.getId(),
				getSqlAlias("searchUserRefsByUserId"));
	}

	@Override
	public User reloadUser(User user) {
		if (user == null)
			return null;
		return userDao.getById(user.getId(), getSqlAlias("getUserById"));
	}

	@Override
	public Pagination<User> searchUsers(String q, int from, int rows) {
		Pagination<User> pu = initPage(
				"select count(id) from user where username like '%"
						+ filterSQLSpecialChars(q) + "%' or nickname like '%"
						+ filterSQLSpecialChars(q) + "%' or pinyin like '%"
						+ filterSQLSpecialChars(q) + "%'", from, rows);
		List<User> results = userDao.searchAllPagination(
				getSqlAlias("searchUsers"), filterSQLSpecialChars(q), from,
				rows);
		pu.setResults(results);
		return pu;
	}

	/**
	 * 查询项目下的用户列表中的用户
	 */

	@Override
	public Pagination<User> searchUsersByProject(String q, int from, int rows,
			int projectId) {
		Pagination<User> pu = initPage(
				"select count(user.id) from user left join user_reference on "
						+ "user.id=user_reference.userId  where user.isActive=1 and user_reference.projectId="
						+ projectId
						+ " and  user.roleId=2 and (user.username like '%"
						+ filterSQLSpecialChars(q)
						+ "%' or user.nickname like '%"
						+ filterSQLSpecialChars(q)
						+ "%' or user.pinyin like '%"
						+ filterSQLSpecialChars(q) + "%')", from, rows);
		SubParams params = new SubParams();
		params.id = projectId;
		params.query = filterSQLSpecialChars(q);
		List<User> results = userDao.searchAllPagination(
				getSqlAlias("searchUsersByProject"), params, from, rows);
		pu.setResults(results);
		return pu;
	}

	@Override
	public UserReference searchUserReference(User user, Project project) {
		UserReference reference = new UserReference();
		reference.setUser(user);
		reference.setProject(project);
		return userReferenceDao.search(reference,
				getSqlAlias("searchProjectUserference"));
	}

	@Override
	public Status changeUserProjectRef(User user, Project pro) {
		String sql = "update user_reference set isActive = isActive ^ 1 ,modifyDate = NOW() where userId= "
				+ user.getId() + " and projectId=" + pro.getId();
		int effect = jdbcTemplate.update(sql);
		if (effect == 1) {
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}

	@Override
	public List<Role> getRolesByUser() {
		return roleDao.searchAll(getSqlAlias("getRolesByUser"));
	}

	@Override
	public Status updateUser(User user) {
		int effet = 0;
		try {
			effet = userDao.update(user, getSqlAlias("updateUser"));
		} catch (Exception e) {
			logger.error("update user an error ", e);
		}
		if (effet == 1)
			return Status.SUCCESS;
		return Status.FAILURE;
	}

	@Override
	public void addAppAuth(AppAuthentication auth) {
		appAuthDao.disableAllAuth(auth.getUser().getId());
		appAuthDao.addAppAuthentication(auth);
	}

	@Override
	public AppAuthentication getAppAuthByToken(String token) {
		return appAuthDao.searchAppAuth(token);
	}

	private boolean hasRegistered(String username) {
		String sql = "select count(*) as count from user where username = ?";
		return jdbcTemplate.queryForInt(sql, new Object[] { username }) > 0;
	}

	@Override
	public Status sendRegisterCode(String mobile, String device, String ip) {
		if (hasRegistered(mobile)) {
			return Status.IN_USE;
		}
		// 先获取是否存在发送实例
		RegisterTmp tmp = registerTmpDao.getLatestRegisterByPhoneNumber(
				RegisterTmp.TYPE_REGISTER, mobile);
		if (tmp != null) {
			// 如果间隔时间少于60s
			if (System.currentTimeMillis() - tmp.getCreateDate().getTime() < 60 * 1000) {
				return Status.OPERATION_FAST;
			}
		}
		tmp = new RegisterTmp();
		tmp.setSmsCode(mkSmsCode());
		tmp.setMobile(mobile);
		tmp.setUuid(Tools.getUUID());
		tmp.setDevice(device);
		tmp.setIp(ip);
		tmp.setType(RegisterTmp.TYPE_REGISTER);
		try {
			registerTmpDao.addRegisterTmp(tmp);
			boolean flag = sendMessageToPhone(mobile, tmp.getSmsCode());
			if (flag) {
				return Status.SUCCESS;
			} else {
				return Status.FAILURE;
			}
		} catch (Exception e) {
			return Status.SAVE_ERROR;
		}
	}

	private boolean hasSubscribed(String username) {
		String sql = "select count(*) as count from subscribe where mobile = ?";
		return jdbcTemplate.queryForInt(sql, new Object[] { username }) > 0;
	}

	@Override
	public boolean hasUser(String mobile) {
		// TODO Auto-generated method stub
		String sql = "select count(*) as count from user where username = ?";
		return jdbcTemplate.queryForInt(sql, new Object[] { mobile }) > 0;
	}

	@Override
	public Status sendSubscribeCode(String mobile, String ip) {
		if (hasSubscribed(mobile)) {
			return Status.IN_USE;
		}
		// 先获取是否存在发送实例
		RegisterTmp tmp = registerTmpDao.getLatestRegisterByPhoneNumber(
				RegisterTmp.TYPE_SUBSCRIBE, mobile);
		if (tmp != null) {
			// 如果间隔时间少于60s
			if (System.currentTimeMillis() - tmp.getCreateDate().getTime() < 60 * 1000) {
				return Status.OPERATION_FAST;
			}
		}
		tmp = new RegisterTmp();
		tmp.setSmsCode(mkSmsCode());
		tmp.setMobile(mobile);
		tmp.setUuid(Tools.getUUID());
		tmp.setIp(ip);
		tmp.setType(RegisterTmp.TYPE_SUBSCRIBE);
		try {
			registerTmpDao.addRegisterTmp(tmp);
			boolean flag = sendMessageToPhone(mobile, tmp.getSmsCode());
			if (flag) {
				return Status.SUCCESS;
			} else {
				return Status.FAILURE;
			}
		} catch (Exception e) {
			return Status.SAVE_ERROR;
		}
	}

	@Override
	public Status sendForgetPassCode(String mobile, String ip) {
		// TODO Auto-generated method stub
		// 先获取是否存在发送实例
		RegisterTmp tmp = registerTmpDao.getLatestRegisterByPhoneNumber(
				RegisterTmp.TYPE_FORGETPASSWORD, mobile);
		if (tmp != null) {
			// 如果间隔时间少于60s
			if (System.currentTimeMillis() - tmp.getCreateDate().getTime() < 60 * 1000) {
				return Status.OPERATION_FAST;
			}
		}
		tmp = new RegisterTmp();
		tmp.setSmsCode(mkSmsCode());
		tmp.setMobile(mobile);
		tmp.setUuid(Tools.getUUID());
		tmp.setIp(ip);
		tmp.setType(RegisterTmp.TYPE_FORGETPASSWORD);
		try {
			registerTmpDao.addRegisterTmp(tmp);
			boolean flag = sendMessageToPhone(mobile, tmp.getSmsCode());
			if (flag) {
				return Status.SUCCESS;
			} else {
				return Status.FAILURE;
			}
		} catch (Exception e) {
			return Status.SAVE_ERROR;
		}
	}

	@Override
	public RegisterTmp validRegisterCode(String code, String mobile) {
		return registerTmpDao.searchRegisterTmpBycode(code, mobile,
				RegisterTmp.TYPE_REGISTER);
	}

	@Override
	public RegisterTmp validSubscribeCode(String code, String mobile) {
		// TODO Auto-generated method stub
		return registerTmpDao.searchRegisterTmpBycode(code, mobile,
				RegisterTmp.TYPE_SUBSCRIBE);
	}

	@Override
	public RegisterTmp validForgetPassCode(String code, String mobile) {
		// TODO Auto-generated method stub
		return registerTmpDao.searchRegisterTmpBycode(code, mobile,
				RegisterTmp.TYPE_FORGETPASSWORD);
	}

	private String mkSmsCode() {
		return "" + (new Random().nextInt(900000 - 1) + 100000);
		// fake code
		// return "123456";
	}

	private static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(20);

	public static boolean sendMessageToPhone(final String mobile,
			final String content) {
		// 短信运营平台接口
		boolean flag = false;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", mobile);
			params.put("code", content);
			params.put("template", Config.sms_template_code);
			String json = HttpUtils.sendPostRequest(
					"http://115.29.168.152/api/v1/send_sms_code", params);
			JSONObject dataObject = new JSONObject(json);
			Object object = dataObject.get("status");
			String status = String.valueOf(object);
			if ("success".equals(status)) {
				flag = true;
			}
			logger.info(mobile + " code :" + content + "\t" + json);
		} catch (Exception e) {
			logger.error(mobile + " send register code with an error ", e);
		}
		return flag;
	}

	@Override
	public void addCustomUserWithAppAuth(User user, Project project)
			throws Exception {
		user.setMainRole(getRole(Role.PRO_USER_LEVEL));
		if (user.getNickname() != null) {
			user.setPinyin(Tools.getPinYin(user.getNickname()));
		}
		// 添加用户角色
		addUser(user);
		// 添加用户权限
		UserReference ur = new UserReference();
		ur.setUser(user);
		ur.setRole(getRole(Role.PRO_CUSTOMER));
		ur.setUuid(Tools.getUUID());
		ur.setProject(project);
		addUserReference(ur);
	}

	@Override
	public RegisterTmp getRegisterTmpByUuid(String uuid) {
		// TODO Auto-generated method stub
		return registerTmpDao.getRegisterTmpByUuid(uuid);
	}

	public Role getRole(int role_level) {
		List<Role> sys_roles = loadRoles();
		if (sys_roles == null)
			return null;
		for (int i = 0; i < sys_roles.size(); i++) {
			Role role = sys_roles.get(i);
			if (role.getLevel() == role_level) {
				return role;
			}
		}
		return null;
	}

	@Override
	public Pagination<User> searchClinicUser(int clinicId, String searchValue,
			int from, int pageItemTotal) {
		Pagination<User> pu = initPage(
				"select count(user.id) from user left join user_reference on "
						+ "user.id=user_reference.userId  where user_reference.clinicId="
						+ clinicId + " and  user_reference.roleId="
						+ Role.PRO_CLIENT + " and (user.username like '%"
						+ filterSQLSpecialChars(searchValue)
						+ "%' or user.nickname like '%"
						+ filterSQLSpecialChars(searchValue)
						+ "%' or user.pinyin like '%"
						+ filterSQLSpecialChars(searchValue) + "%')", from,
				pageItemTotal);
		SubParams params = new SubParams();
		params.id = clinicId;
		params.query = filterSQLSpecialChars(searchValue);
		List<User> results = userDao.searchAllPagination(
				getSqlAlias("searchClinicUser"), params, from, pageItemTotal);
		pu.setResults(results);
		return pu;
	}

	@Override
	public Status upgradeUserToBoxUserByInvitation(User user,
			InvitcodeRecord invitation) {
		try {
			String sql = "update user_reference set roleId = ?,modifyDate = NOW() where userId=?";
			int effect = jdbcTemplate.update(sql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setInt(1, Role.PRO_CLIENT);
							ps.setInt(2, user.getId());
						}
					});
			if (effect > 0) {
				// 将邀请码设置为已经使用的状态
				String s = "update invitcode_record set userId=?,modifyDate=NOW(),status = 0 where id = ?";
				int e = jdbcTemplate.update(s, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						// TODO Auto-generated method stub
						ps.setInt(1, user.getId());
						ps.setInt(2, invitation.getId());
					}
				});
				if (e > 0) {
					return Status.SUCCESS;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	// @Override
	// public Status updateRegisterTmp(RegisterTmp tmp) {
	// // TODO Auto-generated method stub
	// try {
	// registerTmpDao.updateRegisterTmp(tmp);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	@Override
	public Status disableRegisterTmp(RegisterTmp tmp) {
		tmp.setIsActive(RegisterTmp.DISABLE);
		int effect = registerTmpDao.updateRegisterTmp(tmp);
		if (effect == 1) {
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateUserAvatar(User user) {
		// TODO Auto-generated method stub
		try {
			userDao.update(user, getSqlAlias("updateUserAvatar"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	private UserReference refreshUserRef(UserReference old) {
		if (old == null)
			return null;
		return userReferenceDao.getById(old.getId(),
				getSqlAlias("getUserRefById"));
	}

	private int increaseValidDate(UserReference ur) {
		return userReferenceDao.update(ur, getSqlAlias("increaseValidDate"));
	}

	// 整个过程在一个事务中完成
	@Override
	public ActionMessage increaseUserValidDateForBox(final User user,
			final ActivationCode activation) {
		if (activation == null || user == null) {
			return ActionMessage.failStatus("参数有错误");

		}
		UserReference ur = user.getUserReference();
		if (ur == null || !ur.isActive() || !ur.getRole().isBoxUser()) {
			return ActionMessage.failStatus("该卡无法充值到用户");
		}
		// 将卡锁住，避免同时绑定一张卡产生bug，此问题一般在并发的时候
		try {
			synchronized (activation.getDefaultLock()) {
				if (!activation.isAbled()) {
					return ActionMessage.failStatus("流量卡不可用");
				}
				if (!activation.isUsed()) {
					return ActionMessage.failStatus("流量卡已被使用");
				}
				synchronized (ur.getDefaultLock()) {
					TransactionTemplate transaction = new TransactionTemplate(
							transactionManager);
					transaction.execute(new TransactionCallback<Object>() {
						@Override
						public Object doInTransaction(TransactionStatus status) {
							// 将流量卡变为已经使用
							String sql = "update activation_code set modifyDate=NOW(), userId="
									+ user.getId()
									+ ",status="
									+ ActivationCode.STATUS_INUSED
									+ " where id="
									+ activation.getId()
									+ " and isActive=1 and status="
									+ ActivationCode.STATUS_USEABLE;
							System.out.println("sql:" + sql);
							int effect = jdbcTemplate.update(sql);
							if (effect > 0) {
								// 将流量卡的时间加到用户validDate中去
								UserReference nur = refreshUserRef(ur);
								long period = activation.getDeadline();
								// 获取当前用户的期限值
								Date date = nur.getValidDate();
								if (date == null || date.before(new Date())) {
									nur.setValidDate(new Date(System
											.currentTimeMillis()
											+ TimeUnit.SECONDS.toMillis(period)));
								} else {
									nur.setValidDate(new Date(nur
											.getValidDate().getTime()
											+ TimeUnit.SECONDS.toMillis(period)));
								}
								// 如果更新为0，则抛出异常，事务回滚
								int exception = 1 / increaseValidDate(nur);
							}
							return null;
						}
					});
				}
			}
		} catch (Throwable th) {
			th.printStackTrace();
			return ActionMessage.failStatus("流量卡充值失败");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public Status addUserExpand(UserExpand userExpand) {
		// TODO Auto-generated method stub
		try {
			userExpandMapper.addUserExpand(userExpand);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("添加医生专家扩展信息出错", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public Status addUserByWX(User user, UserReference ur) {
		// TODO Auto-generated method stub
		Status status = Status.FAILURE;
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean mark = true;
		try {
			transaction.execute(new TransactionCallback<Void>() {

				@Override
				public Void doInTransaction(TransactionStatus arg0) {
					// TODO Auto-generated method stub
					userDao.add(user, getSqlAlias("addUser"));
					ur.setUser(user);
					userReferenceDao.add(ur, getSqlAlias("addUserReference"));
					UserExpand userExpand = user.getUserExpand();
					userExpand.setUser(user);
					userExpandMapper.addUserExpand(userExpand);
					return null;
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mark = false;
		}
		if (mark) {
			status = Status.SUCCESS;
		}
		return status;
	}

	@Override
	public Status updateUserInfo(User user, UserExpand expand, boolean flag) {
		// TODO Auto-generated method stub
		try {
			userDao.update(user, "updateUserName");
			if (flag) {
				userExpandMapper.addUserExpand(expand);
			} else {
				userExpandMapper.updateUserExpand(expand);
			}
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("修改用户信息出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public User getUserById(Integer id) {
		return userDao.getById(id, getSqlAlias("getUserById"));
	}

	@Override
	public Status updateReceiptExpand(UserExpand userExpand) {
		try {
			userExpandMapper.updateReceiptExpand(userExpand);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("修改用户信息签收状态", e);
		}
		return Status.FAILURE;
	}

	@Override
	public List<User> searchUserListByQ(String queryKey) {
		String query = filterSQLSpecialChars(queryKey);
		return userDao.searchAll(getSqlAlias("searchUsers"), query);
	}

	@Override
	public UserExpand getUserExpendByUserId(int userId) {
		return userExpandMapper.selectUserExpandById(userId);
	}

	@Override
	public Speaker speakerAssociationUserOrNot(int userId) {
		return speakerMapper.getSpeakerByUserId(userId);
	}

	@Override
	public ActionMessage updateUserInfoBySuperAdmin(UserReference ref,
			Certification certification, UserExpand userExpand, User user) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean resultTag;
		try {
			resultTag = transaction.execute(new TransactionCallback<Boolean>() {
				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {
					if (null != ref) {
						userReferenceDao.update(ref,
								getSqlAlias("updateUserRefInfoBySuperAdmin"));
					}
					if (null != certification) {
						certificationMapper
								.updateCertificationStatus(certification);
					}
					if (null != userExpand) {
						userExpandMapper.updateUserExpand(userExpand);
					}
					if (null != user) {
						userDao.update(user,
								getSqlAlias("updateUserInfoBySuperAdmin"));
					}
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (!resultTag) {
			return ActionMessage.failStatus("更新用户失败");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public Pagination<Resource> getUserWorksRecord(User userByUuid, String q,
			int startPage, int pageSize) {
		Pagination<Resource> pagination = initPage(
				"select count(r.id) from user u left join speaker s on u.id=s.userId "
						+ "inner join resource r on s.id=r.speakerId where u.id="
						+ userByUuid.getId() + " and (" + "r.name like '%"
						+ filterSQLSpecialChars(q) + "%' or "
						+ "r.mark like '%" + filterSQLSpecialChars(q)
						+ "%' or " + "r.pinyin like '%"
						+ filterSQLSpecialChars(q) + "%' )", startPage,
				pageSize);

		List<Resource> results = userOverViewMapper
				.searchUserWorksRecordPagination(userByUuid.getId(),
						filterSQLSpecialChars(q), startPage, pageSize);
		pagination.setResults(results);
		return pagination;
	}

	@Override
	public Status updateSexExpand(UserExpand userExpand) {
		// TODO Auto-generated method stub
		try {
			int effect = userExpandMapper.updateSexExpand(userExpand);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Status addUserByPcWeb(WebchatUser webchatUser, UserReference ur) {
		// TODO Auto-generated method stub
		Status status = Status.FAILURE;
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean mark = true;
		try {
			transaction.execute(new TransactionCallback<Void>() {

				@Override
				public Void doInTransaction(TransactionStatus arg0) {
					// TODO Auto-generated method stub
					User user = webchatUser.getUser();
					userDao.add(user, getSqlAlias("addUser"));
					ur.setUser(user);
					userReferenceDao.add(ur, getSqlAlias("addUserReference"));
					UserExpand userExpand = user.getUserExpand();
					userExpand.setUser(user);
					userExpandMapper.addUserExpand(userExpand);
					webchatUser.setUser(user);
					webChatDao.update(webchatUser, "webChat.updateWebChatUser");
					return null;
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mark = false;
		}
		if (mark) {
			status = Status.SUCCESS;
		}
		return status;
	}

	@Override
	public Pagination<MyCollection> getUserCollectionRecord(User userByUuid,
			String query, int startPage, int pageSize) {
		query = filterSQLSpecialChars(query);
		Pagination<MyCollection> pagination = initPage(
				"select count(c.id) from my_collection c left join resource r on r.id=c.resourceId "
						+ " where c.userId="
						+ userByUuid.getId()
						+ " and c.status=1 and c.isActive=1 and r.status=1 and r.isActive=1 and ("
						+ " r.name like '%"
						+ query
						+ "%' or "
						+ " r.mark like '%"
						+ query
						+ "%' or "
						+ " r.pinyin like '%" + query + "%' )", startPage,
				pageSize);
		List<MyCollection> results = myCollectionMapper
				.getUserCollectionRecord(userByUuid.getId(), query, startPage,
						pageSize);
		pagination.setResults(results);
		return pagination;
	}

	@Override
	public Pagination<Praise> getUserPraiseRecord(User userByUuid,
			String query, int startPage, int pageSize) {
		query = filterSQLSpecialChars(query);
		Pagination<Praise> pagination = initPage(
				"select count(p.id) from praise p left join resource r on r.id=p.resourceId "
						+ " where p.userId="
						+ userByUuid.getId()
						+ " and p.status=1 and p.isActive=1 and r.status=1 and r.isActive=1 and ("
						+ " r.name like '%" + query + "%' or "
						+ " r.mark like '%" + query + "%' or "
						+ " r.pinyin like '%" + query + "%' )", startPage,
				pageSize);
		List<Praise> results = praiseMapper.getUserPraiseRecord(
				userByUuid.getId(), query, startPage, pageSize);
		pagination.setResults(results);
		return pagination;
	}
	
}
