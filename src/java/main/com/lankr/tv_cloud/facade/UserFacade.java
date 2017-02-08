package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
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
import com.lankr.tv_cloud.vo.UserInfo;

public interface UserFacade {

	public Status addUser(User user);

	public User getUserById(Integer id);

	public User getUserByUuid(String uuid);

	public User getUserByUsername(String username);

	public List<UserInfo> fetchUserBaseInfo(String query, int page);

	public List<Role> loadRoles();

	public Status addUserReference(UserReference userReference);

	public User login(String username, String password);

	public List<UserReference> getUserReferences(User user);

	public User reloadUser(User user);

	public Pagination<User> searchUsers(String q, int from, int to);

	public Pagination<User> searchUsersByProject(String q, int from, int to,
			int projectId);

	public UserReference searchUserReference(User user, Project project);

	public Status changeUserProjectRef(User user, Project pro);

	public List<Role> getRolesByUser();

	public Status updateUser(User user);

	public Status updateUserAvatar(User user);

	public void addAppAuth(AppAuthentication auth);

	public AppAuthentication getAppAuthByToken(String token);

	public Status sendRegisterCode(String mobile, String device, String ip);

	public Status sendSubscribeCode(String mobile, String ip);

	public Status sendForgetPassCode(String mobile, String ip);

	public RegisterTmp validRegisterCode(String code, String mobile);

	public RegisterTmp validSubscribeCode(String code, String mobile);

	public RegisterTmp validForgetPassCode(String code, String mobile);

	public RegisterTmp getRegisterTmpByUuid(String uuid);

	public Status disableRegisterTmp(RegisterTmp tmp);

	public void addCustomUserWithAppAuth(User user, Project project)
			throws Exception;

	public Pagination<User> searchClinicUser(int clinicId, String searchValue,
			int from, int pageItemTotal);

	public Status upgradeUserToBoxUserByInvitation(User user,
			InvitcodeRecord invitation);

	// 充值流量卡
	public ActionMessage increaseUserValidDateForBox(User user,
			ActivationCode activation);

	public Status addUserExpand(UserExpand userExpand);

	public Status addUserByWX(User user, UserReference ur);

	public Status updateUserInfo(User user, UserExpand expand, boolean flag);

	public boolean hasUser(String mobile);
	
	public Status updateReceiptExpand(UserExpand userExpand);

	public List<User> searchUserListByQ(String queryKey);

	public UserExpand getUserExpendByUserId(int userId);

	public Speaker speakerAssociationUserOrNot(int userId);

	public ActionMessage updateUserInfoBySuperAdmin(UserReference ref,
			Certification certification, UserExpand userExpand, User user);

	public Pagination<Resource> getUserWorksRecord(User userByUuid,
			String q, int from, int size);
	
	public Status updateSexExpand(UserExpand userExpand);
	
	public Status addUserByPcWeb(WebchatUser webchatUser,UserReference ur);

	public Pagination<MyCollection> getUserCollectionRecord(User userByUuid,
			String q, int from, int size);

	public Pagination<Praise> getUserPraiseRecord(User userByUuid, String q,
			int from, int size);
	
}
