package com.lankr.tv_cloud.facade.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.lankr.orm.mybatis.mapper.MessageMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.TagFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceVoteAnswer;
import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.model.Resourceable;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TagsResource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;

@SuppressWarnings("all")
public class ResourceFacadeImp extends FacadeBaseImpl implements ResourceFacade {

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private TagFacade tagFacade;

	@Override
	public Resource findFromResourceable(Resourceable res) {
		// if (res instanceof Video) {
		// return resourceMapper.findResourceByVideoId(((Video) res).getId());
		// } else if (res instanceof PdfInfo) {
		// return resourceMapper.findResourceByPdfId(((PdfInfo) res).getId());
		// } else if (res instanceof NewsInfo) {
		// return resourceMapper
		// .findResourceByNewsId(((NewsInfo) res).getId());
		// }
		// return null;
		return getResource(res);
	}

	@Override
	protected String namespace() {
		return null;
	}

	@Override
	public List<ResourceVoteSubject> getVotesByResourceId(Resource resource) {
		if (resource == null)
			return null;
		return voteMapper.getVotesByResourceId(resource.getId());
	}

	@Override
	public Status addOrUpdateResourceVote(ResourceVoteSubject subject) {
		try {
			Resource res = subject.getResource();
			if (res == null)
				return Status.PARAM_ERROR;
			List<ResourceVoteOption> options = subject.getOptions();
			if (options == null || options.isEmpty())
				return Status.PARAM_ERROR;
			// 新增
			if (StringUtils.isEmpty(subject.getUuid())) {
				subject.setUuid(Tools.getUUID());
				voteMapper.addVoteSubject(subject);
				for (int i = 0; i < options.size(); i++) {
					ResourceVoteOption option = options.get(i);
					option.setSubject(subject);
					option.setUuid(Tools.getUUID());
					voteMapper.addVoteOption(option);
				}
			}// 修改
			else {
				ResourceVoteSubject pres = voteMapper
						.getVoteSubjectByUuid(subject.getUuid());
				// 找不到题目或者题目被删除，放回
				if (pres == null || !pres.isActive())
					return Status.NOT_FOUND;
				voteMapper.updateVoteSubject(subject);
				for (int i = 0; i < options.size(); i++) {
					ResourceVoteOption option = options.get(i);
					option.setSubject(pres);
					int option_eff = voteMapper.updateVoteOption(option);
					// 如果更新失败则插入
					if (option_eff == 0) {
						option.setUuid(Tools.getUUID());
						voteMapper.addVoteOption(option);
					}
				}
			}
			return Status.SUCCESS;
		} catch (Exception e) {
			logger.error("add vote error ", e);
			return Status.FAILURE;
		}
	}

	@Override
	public Status delVote(String uuid) {
		int e = voteMapper.disableVote(uuid);
		if (e >= 1) {
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}

	@Override
	public Status delVoteOption(String uuid) {
		int e = voteMapper.disableVoteOption(uuid);
		if (e == 1) {
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}

	@Override
	public Status votedByUser(User user, String subject_uuid,
			List<String> options) {
		return vote(user, null, subject_uuid, new HashSet<String>(options));
	}

	@Override
	public Status votedByOpenId(String openId, String subject_uuid,
			List<String> options) {
		return vote(null, openId, subject_uuid, new HashSet<String>(options));
	}

	private Status vote(User user, String openId, String subject_uuid,
			Set<String> option_uuids) {
		if (option_uuids == null || option_uuids.isEmpty()) {
			return Status.PARAM_ERROR;
		}
		if (user == null && StringUtils.isEmpty(openId)) {
			return Status.NO_PERMISSION;
		}
		ResourceVoteSubject subject = voteMapper
				.getVoteSubjectByUuid(subject_uuid);
		if (subject == null || !subject.isActive()) {
			return Status.NOT_FOUND;
		}
		if (subject.getType() != 0 && subject.getType() < option_uuids.size()) {
			return Status.PARAM_ERROR;
		}
		// 判断用户有没有投过票
		String sql = "";
		if (user != null) {
			sql = "select count(id) from resource_vote_answer where voteSubjectId=? and userId=?";
		} else {
			sql = "select count(id) from resource_vote_answer where voteSubjectId=? and openId=?";
		}
		boolean voted = jdbcTemplate.queryForInt(
				sql,
				new Object[] { subject.getId(),
						user == null ? openId : user.getId() }) > 0;
		if (voted) {
			return Status.SUBMIT_REPEAT;
		}
		// 判断用户
		List<ResourceVoteOption> options = subject.getOptions();
		Iterator<String> it = option_uuids.iterator();
		int effect_count = 0;
		try {
			while (it.hasNext()) {
				ResourceVoteOption option = getOptionByUuid(it.next(), options);
				if (option != null) {
					ResourceVoteAnswer answer = new ResourceVoteAnswer();
					answer.setUuid(Tools.getUUID());
					answer.setSubject(subject);
					answer.setOption(option);
					answer.setUser(user);
					if (!StringUtils.isEmpty(openId)) {
						answer.setOpenId(openId);
					}
					// 插入到数据库
					int e = voteMapper.addResourceVoteAnswer(answer);
					if (e == 1) {
						voteMapper.increaseOptionCount(option.getId());
					}
					effect_count += e;
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			return Status.FAILURE;
		}
		if (effect_count == 0) {
			return Status.PARAM_ERROR;
		}
		return Status.SUCCESS;
	}

	private ResourceVoteOption getOptionByUuid(String uuid,
			List<ResourceVoteOption> options) {
		for (ResourceVoteOption resourceVoteOption : options) {
			if (resourceVoteOption.getUuid().equals(uuid)) {
				return resourceVoteOption;
			}
		}
		return null;
	}

	// 查询用户已经投选的选项
	@Override
	public List<Integer> seachVotedSubjectOptions(Integer userId,
			Integer subjectId) {
		String sql = "select voteOptionId from resource_vote_answer where isActive=1 and userId=? and voteSubjectId=?";
		List<Integer> results = jdbcTemplate.query(sql, new Object[] { userId,
				subjectId }, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("voteOptionId");
			}
		});
		return results;
	}

	@Override
	public List<Integer> seachVotedSubjectOptions(String openId,
			Integer subjectId) {
		String sql = "select voteOptionId from resource_vote_answer where openId=? and voteSubjectId=?";
		List<Integer> results = jdbcTemplate.query(sql, new Object[] { openId,
				subjectId }, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("voteOptionId");
			}
		});
		return results;
	}

	@Override
	public ResourceVoteSubject getVoteSubjectByUuid(String uuid) {
		// TODO Auto-generated method stub
		return voteMapper.getVoteSubjectByUuid(uuid);
	}

	@Override
	public List<Resource> getResourcesByCateId(int id) {
		// TODO Auto-generated method stub
		return resourceMapper.getResourcesByCateId(id);
	}

	@Override
	public Status recordResourceSpeaker(Resourceable resourceable,
			Speaker speaker) {
		try {
			Resource res = getResource(resourceable);
			recordResourceSpeaker(res, speaker);
		} catch (Exception e) {
			e.printStackTrace();
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public List<Resource> getResourceRelated(Resource resource) {
		if (resource == null)
			return null;
		Category category = resource.getCategory();
		List<Resource> resources = resourceMapper.findResouceRelated(
				resource.getId(), category.getId());
		return resources;
	}

	// @Cacheable(value = CacheBucket.RESOURCEITEM, key = "#uuid")
	@Override
	public Resource getResourceByUuid(String uuid) {
		Resource res = resourceMapper.getResourceByUuid(uuid);
		return res;
	}

	@Override
	public List<Resource> searchAPIResources(Date updated_at,
			Category category, int batch_size) {
		if (category == null)
			return null;
		List<Resource> resources = resourceMapper.searchAPIResources(
				updated_at, category.getId(), Math.min(batch_size, 50));
		return resources;
	}

	// 点击分类显示资源 ==> XiaoMa
	@Override
	public Pagination<Resource> getResourcesList(String searchValue,
			Category category, int startPage, int pageSize, String type,
			String state) {
		SubParams subParams = new SubParams();
		subParams.setQuery(searchValue);
		subParams.setStart(startPage);
		subParams.setSize(pageSize);
		subParams.setResourceType(type);
		Pagination<Resource> pagination = null;
		StringBuffer sqlBuffer = new StringBuffer(
				" select count(id) from resource where isActive=1 ");

		// 查询符合条件的总记录，设置分页参数
		searchValue = filterSQLSpecialChars(searchValue);
		if (StringUtils.isNotEmpty(type)) {
			if ("VIDEO".equals(type)) {
				sqlBuffer.append(" and assetId > 0 ");
			} else if ("PDF".equals(type)) {
				sqlBuffer.append(" and pdfId > 0");
			} else if ("NEWS".equals(type)) {
				sqlBuffer.append(" and newsId > 0");
			} else if ("THREESCREEN".equals(type)) {
				sqlBuffer.append(" and threeScreenId > 0");
			} else {
			}
		}
		if (StringUtils.isNotEmpty(state)) {
			sqlBuffer.append(" and status = '" + state + "'");
			subParams.setState(state);
		}
		if (null != category) {
			sqlBuffer.append(" and categoryId = '" + category.getId() + "'");
			subParams.setId(category.getId());
		}
		sqlBuffer.append(" and (name like '%" + searchValue
				+ "%' or pinyin like '%" + searchValue + "%')");
		pagination = initPage(sqlBuffer.toString(), startPage, pageSize);

		List<Resource> resources = resourceMapper.getResources(subParams);
		pagination.setResults(resources);
		return pagination;
	}

	@Override
	public List<Resource> searchResourceListByQ(String queryKey) {
		queryKey = filterSQLSpecialChars(queryKey);
		return resourceMapper.searchResourceListByQ(queryKey);
	}

	@Override
	public List<TagChild> getTagsByResourceUuid(String resourceUuid) {
		return resourceMapper.getTagsByResourceUuid(resourceUuid);
	}

	@Override
	public ActionMessage resAddLabels(int[] requesttagsIds, int[] existsTagIds,
			int resId) {
		int effectSum = 0;
		List existsList = new ArrayList<Integer>();
		List queryList = new ArrayList<Integer>();
		// 去除用户已添加的标签，如果用户选择的标签全部已存在，直接返回添加成功信息
		for (int i = 0; i < existsTagIds.length; i++) {
			existsList.add(existsTagIds[i]);
		}
		for (int i = 0; i < requesttagsIds.length; i++) {
			if (existsList.contains(requesttagsIds[i])) {
				continue;
			} else {
				queryList.add(requesttagsIds[i]);
			}
		}
		if (queryList.size() == 0) {
			return ActionMessage.successStatus();
		}
		// 插入数据
		try {
			for (int i = 0; i < queryList.size(); i++) {
				TagsResource tagsResource = new TagsResource();
				tagsResource.setUuid(Tools.getUUID());
				tagsResource.setTagsId((int) queryList.get(i));
				tagsResource.setResourceId(resId);
				tagsResource.setStatus(BaseModel.ACTIVE);
				tagsResource.setIsActive(1);
				int effect = resourceMapper.resAddLabels(tagsResource);
				if (effect == 1) {
					effectSum += effect;
				}
			}
			if (effectSum == queryList.size()) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("resAddLabels failure");
	}

	@Override
	public int[] getTagsIdsByResourceId(int id) {
		List<TagsResource> tagsResources = resourceMapper
				.getTagsResourceByResourceId(id);
		if (null != tagsResources) {
			int[] tagsIds = new int[tagsResources.size()];
			for (int i = 0; i < tagsResources.size(); i++) {
				tagsIds[i] = tagsResources.get(i).getTagsId();
			}
			return tagsIds;
		}
		return null;
	}

	@Override
	public synchronized ActionMessage delResTagByResourceIdAndTagId(
			Integer resId, Integer tagId) {
		int effect = 0;
		try {
			effect = resourceMapper.delResTagByResourceIdAndTagId(resId, tagId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return ActionMessage.failStatus("删除失败");
	}

	@Override
	public List<Resource> searchAPIResourcesAllLatest(Date updated_at,
			int batch_size) {
		List<Resource> resources = resourceMapper.searchAPIResourcesAllLatest(
				updated_at, batch_size);
		return resources;
	}

	@Override
	public List<Resource> searchAPIResourcesLatest(Date updated_at,
			Category category, int batch_size) {
		// TODO Auto-generated method stub
		return resourceMapper.searchAPIResourcesLatest(updated_at,
				category.getId(), batch_size);
	}

	@Override
	public int countResourceView(final Resource res) {
		if (res == null)
			return 0;
		String sql = "update resource set viewCount = Coalesce(viewCount, 0) + 1 where id = ?";
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, res.getId());
			}
		});
	}

	@Override
	public List<Resource> getWebCastInitList() {
		// TODO Auto-generated method stub
		return resourceMapper.getWebCastInitList();
	}

	@Override
	public List<Resource> getWebCastSearchList(String search) {
		// TODO Auto-generated method stub
		return resourceMapper.getWebCastSearchList(search);
	}

	@Override
	public List<Resource> searchResourceByQrSence(String search) {
		// TODO Auto-generated method stub
		return resourceMapper.searchResourceByQrSence(search);
	}

	@Override
	public Resource getResourceById(int id) {
		// TODO Auto-generated method stub
		return resourceMapper.getResourceById(id);
	}

	@Override
	public int resourceCountByCategory(List<Integer> list) {
		// TODO Auto-generated method stub
		Integer count = resourceMapper.resourceCountByCategory(list);
		if (count == null) {
			return 0;
		}
		return count;
	}

	@Override
	public List<Resource> resourceLatestByCategory(List<Integer> list, int size) {
		List<Resource> resources = resourceMapper.resourceLatestByCategory(
				list, size);
		return resources;
	}

	@Override
	public int resourceCountByCateId(int categoryId) {
		// TODO Auto-generated method stub
		Integer count = resourceMapper.resourceCountByCateId(categoryId);
		if (count == null) {
			return 0;
		}
		return count;
	}

	@Override
	public Pagination<Resource> resourceFrontPage(int categoryId, int from,
			int size) {
		String sql = "select count(id) from resource where isActive=1 and status=1 and categoryId="
				+ categoryId;
		Pagination<Resource> pagination = initPage(sql, from, size);
		List<Resource> list = resourceMapper.resourceFrontPage(categoryId,
				from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public Pagination<Resource> resourceActivityFrontPage(int activityId,
			int from, int size) {
		// TODO Auto-generated method stub
		String sql = "SELECT count(r.id) from resource r RIGHT JOIN activity_resource ar ON r.id=ar.resourceId"
				+ " WHERE ar.activityId="
				+ activityId
				+ " AND r.isActive=1 AND ar.isActive=1 AND r.status=1 AND ar.status=1 AND ar.recommendDate is null";
		Pagination<Resource> pagination = initPage(sql, from, size);
		List<Resource> list = resourceMapper.resourceActivityFrontPage(
				activityId, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public List<Resource> resourceBySpeakerWxPage(int speakerId,
			String startTime, int size) {
		// TODO Auto-generated method stub
		return resourceMapper.resourceBySpeakerWxPage(speakerId, startTime,
				size);
	}

	@Override
	public List<Resource> resourceWxPage(int categoryId, String startTime,
			int size) {
		List<Resource> resources = resourceMapper.resourceWxPage(categoryId,
				startTime, size);
		return resources;
	}

	@Override
	public Pagination<ResourceVoteAnswer> getResourceVoteAnswerListByOptionUuid(
			String uuid, String query, int startPage, int pageSize) {
		Pagination<ResourceVoteAnswer> pagination;
		List<ResourceVoteAnswer> results;
		try {
			ResourceVoteOption option = voteMapper.getVoteOptionByUuid(uuid);
			query = filterSQLSpecialChars(query);
			pagination = initPage(
					"select count(*) from resource_vote_answer r left join user u on r.userId = u.id"
							+ " where r.voteOptionId = "
							+ option.getId()
							+ " and r.voteSubjectId = "
							+ option.getSubject().getId()
							+ " and ("
							+ " u.nickname like '%"
							+ query
							+ "%' or "
							+ " u.pinyin like '%" + query + "%' )", startPage,
					pageSize);
			results = voteMapper.getUserListByOptionIdAndSubjectId(
					option.getId(), option.getSubject().getId(), query,
					startPage, pageSize);
			pagination.setResults(results);
			return pagination;
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getResourceVoteAnswerListByOptionUuid] -> 查询投票用户列表出错",
						e);
		}
		return null;
	}

	@Override
	public ResourceVoteOption getResourceVoteOptionByUuid(String uuid) {
		try {
			ResourceVoteOption option = voteMapper.getVoteOptionByUuid(uuid);
			return option;
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getResourceVoteOptionByUuid] -> 根据uuid获取option出错",
						e);
		}
		return null;
	}

	@Override
	public Pagination<MyCollection> getCollectionUserRecord(
			Resource resourceByUuid, String query, int startPage, int pageSize) {
		Pagination<MyCollection> pagination = new Pagination<MyCollection>();
		List<MyCollection> results = new ArrayList<MyCollection>();
		try {
			query = filterSQLSpecialChars(query);
			pagination = initPage(
					"select count(c.id) from my_collection c left join user u on u.id=c.userId "
							+ " where c.resourceId="
							+ resourceByUuid.getId()
							+ " and c.status=1 and c.isActive=1 and u.isActive=1 and ("
							+ " u.nickname like '%" + query + "%' or "
							+ " u.pinyin like '%" + query + "%' )", startPage,
					pageSize);
			results = collectionMapper.getCollectionUserRecord(
					resourceByUuid.getId(), query, startPage, pageSize);
		} catch (NullPointerException e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getCommentUserRecord] -> 查询收藏列表出错,资源获取ID失败",
						e);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getCommentUserRecord] -> 查询收藏列表出错",
						e);
		}
		pagination.setResults(results);
		return pagination;
	}

	@Override
	public Pagination<Praise> getPraiseUserRecord(Resource resourceByUuid,
			String query, int startPage, int pageSize) {
		Pagination<Praise> pagination = new Pagination<Praise>();
		List<Praise> results = new ArrayList<Praise>();
		try {
			query = filterSQLSpecialChars(query);
			pagination = initPage(
					"select count(p.id) from praise p left join user u on u.id=p.userId "
							+ " where p.resourceId="
							+ resourceByUuid.getId()
							+ " and p.status=1 and p.isActive=1 and u.isActive=1 and ("
							+ " u.nickname like '%" + query + "%' or "
							+ " u.pinyin like '%" + query + "%' )", startPage,
					pageSize);
			results = praiseMapper.getPraiseUserRecord(resourceByUuid.getId(),
					query, startPage, pageSize);
		} catch (NullPointerException e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getCommentUserRecord] -> 查询点赞列表出错,资源获取ID失败",
						e);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getCommentUserRecord] -> 查询点赞列表出错",
						e);
		}
		pagination.setResults(results);
		return pagination;
	}

	@Override
	public Pagination<Message> getCommentUserRecord(Resource resourceByUuid,
			String query, int startPage, int pageSize) {
		List<Message> results = new ArrayList<Message>();
		Pagination<Message> pagination = new Pagination<Message>();
		try {
			query = filterSQLSpecialChars(query);
			pagination = initPage(
					"select count(m.id) from message m left join user u on u.id=m.userId "
							+ " where m.referId=" + resourceByUuid.getId()
							+ " and (m.referType="
							+ Message.REFER_TYPE_RESOURCE + " or m.referType="
							+ Message.REFER_TYPE_MESSAGE + ") and (m.type="
							+ Message.COMMENT_TYPE + " or m.type="
							+ Message.REPLY_COMMENT_TYPE
							+ ") and m.status=1 and m.isActive=1 and ("
							+ " u.nickname like '%" + query + "%' or "
							+ " m.body like '%" + query + "%' or "
							+ " u.pinyin like '%" + query + "%' )", startPage,
					pageSize);
			results = messageMapper.getCommentUserRecord(
					resourceByUuid.getId(), query, startPage, pageSize,
					Message.REFER_TYPE_RESOURCE, Message.REFER_TYPE_MESSAGE,
					Message.COMMENT_TYPE, Message.REPLY_COMMENT_TYPE);
		} catch (NullPointerException e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getCommentUserRecord] -> 查询评论列表出错,资源获取ID失败",
						e);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceFacadeImp.getCommentUserRecord] -> 查询评论列表出错",
						e);
		}
		pagination.setResults(results);
		return pagination;
	}

}
