package com.lankr.tv_cloud.facade.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.lankr.orm.mybatis.mapper.ActivationCodeMapper;
import com.lankr.orm.mybatis.mapper.ActivityApplicationMapper;
import com.lankr.orm.mybatis.mapper.ActivityMapper;
import com.lankr.orm.mybatis.mapper.BannerMapper;
import com.lankr.orm.mybatis.mapper.BroadcastMapper;
import com.lankr.orm.mybatis.mapper.CategoryExpandMapper;
import com.lankr.orm.mybatis.mapper.CertificationMapper;
import com.lankr.orm.mybatis.mapper.IntegralConsumeMapper;
import com.lankr.orm.mybatis.mapper.IntegralMapper;
import com.lankr.orm.mybatis.mapper.MyCollectionMapper;
import com.lankr.orm.mybatis.mapper.NormalCollectMapper;
import com.lankr.orm.mybatis.mapper.NormalCollectQuestionnaireMapper;
import com.lankr.orm.mybatis.mapper.NormalCollectScheduleMapper;
import com.lankr.orm.mybatis.mapper.NotificaitonMapper;
import com.lankr.orm.mybatis.mapper.OfflineActivityMapper;
import com.lankr.orm.mybatis.mapper.PageRemainMapper;
import com.lankr.orm.mybatis.mapper.PraiseMapper;
import com.lankr.orm.mybatis.mapper.ProjectCodeMapper;
import com.lankr.orm.mybatis.mapper.QrCodeMapper;
import com.lankr.orm.mybatis.mapper.QrInteractChannelMapper;
import com.lankr.orm.mybatis.mapper.QuestionnaireMapper;
import com.lankr.orm.mybatis.mapper.ReceiptAddressMapper;
import com.lankr.orm.mybatis.mapper.RegisterTmpMapper;
import com.lankr.orm.mybatis.mapper.ResourceAccessIgnoreMapper;
import com.lankr.orm.mybatis.mapper.ResourceGroupMapper;
import com.lankr.orm.mybatis.mapper.ResourceMapper;
import com.lankr.orm.mybatis.mapper.ResourceVoteMapper;
import com.lankr.orm.mybatis.mapper.ShareGiftMpper;
import com.lankr.orm.mybatis.mapper.SignUpUserMapper;
import com.lankr.orm.mybatis.mapper.SpeakerMapper;
import com.lankr.orm.mybatis.mapper.TagMapper;
import com.lankr.orm.mybatis.mapper.TempleMessageMapper;
import com.lankr.orm.mybatis.mapper.ThreeScreenMapper;
import com.lankr.orm.mybatis.mapper.UserExpandMapper;
import com.lankr.orm.mybatis.mapper.WxSubjectMapper;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.annotations.DataAlias;
import com.lankr.tv_cloud.cache.CacheBucket;
import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.common.dao.CommonPersistence;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.Advertisement;
import com.lankr.tv_cloud.model.AdvertisementPosition;
import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.AssetPrice;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.InvitcodeRecord;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.ProductGroup;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.Resourceable;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.Subscribe;
import com.lankr.tv_cloud.model.ThreeScreen;
import com.lankr.tv_cloud.model.TvAuthentication;
import com.lankr.tv_cloud.model.TvBox;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.model.Widget;
import com.lankr.tv_cloud.support.rocketMQ.SendMessageToMQ;
import com.lankr.tv_cloud.utils.JsonFormat;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.snapshot.ResourceSimpleItem;
import com.lankr.tv_cloud.web.AdminWebInterceptor;

@SuppressWarnings("all")
public abstract class FacadeBaseImpl {

	protected static Log logger = LogFactory.getLog(FacadeBaseImpl.class);
	public final static List EMPTY_ARRAY = new ArrayList(0);
	private final static ExecutorService MSG_POOL = Executors
			.newCachedThreadPool();
	protected JdbcTemplate jdbcTemplate;

	protected CommonPersistence<User, Serializable> userDao;
	protected CommonPersistence<Project, Serializable> projectDao;
	protected CommonPersistence<Role, Serializable> roleDao;
	protected CommonPersistence<UserReference, Serializable> userReferenceDao;
	protected CommonPersistence<Category, Serializable> categoryDao;
	protected CommonPersistence<Widget, Serializable> widgetDao;
	protected CommonPersistence<Video, Serializable> videoDao;
	protected CommonPersistence<Advertisement, Serializable> adverDao;
	protected CommonPersistence<AdvertisementPosition, Serializable> adPostionDao;
	protected CommonPersistence<AssetPrice, Serializable> priceDao;
	protected CommonPersistence<TvBox, Serializable> boxDao;
	protected CommonPersistence<TvAuthentication, Serializable> tvAuthDao;
	protected CommonPersistence<NewsInfo, Serializable> newsInfoDao;
	protected CommonPersistence<PdfInfo, Serializable> pdfInfoDao;
	protected CommonPersistence<WebchatUser, Serializable> webChatDao;
	protected CommonPersistence<ApplicableRecords, Serializable> applicableDao;
	protected CommonPersistence<InvitcodeRecord, Serializable> inviteDao;
	protected CommonPersistence<Subscribe, Serializable> subscribeDao;
	protected CommonPersistence<ProductGroup, Serializable> groupDao;
	protected CommonPersistence<Manufacturer, Serializable> manufacturerDao;
	protected CommonPersistence<ActivationCode, Serializable> activationDao;
	protected CommonPersistence<ThreeScreen, Serializable> threeScreenDao;

	protected ResourceMapper resourceMapper;
	private EhCacheCacheManager cacheManager;
	@Autowired
	protected DataSourceTransactionManager transactionManager;
	@Autowired
	protected ShareGiftMpper shareGiftMpper;
	@Autowired
	protected ActivationCodeMapper activationCodeMapper;
	@Autowired
	protected CategoryExpandMapper categoryExpandMapper;
	@Autowired
	protected BannerMapper bannerMapper;
	@Autowired
	protected TagMapper tagMapper;
	@Autowired
	protected QrCodeMapper qrCodeMapper;
	@Autowired
	protected ActivityMapper activityMapper;
	@Autowired
	protected BroadcastMapper broadMapper;
	@Autowired
	protected JdbcTemplate accessLogJdbc;
	@Autowired
	protected ActivityApplicationMapper activityApplicationMapper;
	@Autowired
	protected CertificationMapper certificationMapper;
	@Autowired
	protected SpeakerMapper speakerMapper;
	@Autowired
	protected UserExpandMapper userExpandMapper;
	@Autowired
	protected IntegralMapper integralMapper;
	@Autowired
	protected IntegralConsumeMapper integralConsumeMapper;
	@Autowired
	protected MyCollectionMapper collectionMapper;
	@Autowired
	protected PraiseMapper praiseMapper;
	@Autowired
	protected ResourceVoteMapper voteMapper;
	@Autowired
	protected ThreeScreenMapper threeScreenMapper;
	@Autowired
	protected RegisterTmpMapper registerTmpDao;
	@Autowired
	protected TempleMessageMapper templeMessageMapper;
	@Autowired
	protected ReceiptAddressMapper receiptAddressMapper;
	@Autowired
	protected WxSubjectMapper wxSubjectMapper;
	@Autowired
	protected CodeProvider codeProvider;
	@Autowired
	protected QuestionnaireMapper questionnaireMapper;
	@Autowired
	protected NormalCollectMapper normalCollectMapper;
	@Autowired
	protected PageRemainMapper pageRemainMapper;
	@Autowired
	protected NotificaitonMapper notificaitonMapper;
	@Autowired
	protected QrInteractChannelMapper qrInteractChannelMapper;
	@Autowired
	protected NormalCollectScheduleMapper normalCollectScheduleMapper;
	@Autowired
	protected ResourceGroupMapper resourceGroupMapper;
	
	@Autowired
	protected NormalCollectQuestionnaireMapper normalCollectQuestionnaireMapper;
	
	@Autowired
	protected OfflineActivityMapper offlineActivityMapper;
	
	@Autowired
	protected ProjectCodeMapper projectCodeMapper;
	
	@Autowired
	protected ResourceAccessIgnoreMapper resourceAccessIgnoreMapper;
	
	
	@Autowired
	protected SignUpUserMapper signUpUserMapper;

	public JdbcTemplate getAccessLogJdbc() {
		return accessLogJdbc;
	}

	public void setAccessLogJdbc(JdbcTemplate accessLogJdbc) {
		this.accessLogJdbc = accessLogJdbc;

	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setUserDao(CommonPersistence<User, Serializable> userDao) {
		this.userDao = userDao;
	}

	public void setProjectDao(
			CommonPersistence<Project, Serializable> projectDao) {
		this.projectDao = projectDao;
	}

	private static boolean admin_active_registed = false;

	@Autowired
	public final void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
		if (admin_active_registed)
			return;
		// 注册监听时间
		synchronized (cacheManager) {
			AdminWebInterceptor
					.registerAdminActiveListener(new AdminWebInterceptor.AdminActiveListener() {
						@Override
						public void onActive(boolean busy) {
							if (busy) {
								onAdminBusy();
							} else {
								onAdminIdle();
							}
						}
					});
			admin_active_registed = true;
		}

	}

	protected Cache getCache(String bucket) {
		if (cacheManager != null && cacheManager.getCacheManager() != null) {
			return cacheManager.getCacheManager().getCache(bucket);
		}
		return null;
	}

	protected void putToCache(String bucket, Object k, Object v) {
		try {
			Cache cache = getCache(bucket);
			if (cache == null)
				return;
			cache.put(new Element(k, v));
		} catch (Throwable th) {
			th.printStackTrace();
			logger.error("putToCache error", th);
		}
	}

	protected boolean evictCache(String bucket, Object k) {
		try {
			Cache cache = getCache(bucket);
			if (cache == null)
				return false;
			return cache.remove(k);
		} catch (Throwable th) {
			th.printStackTrace();
			logger.error("evictCache error", th);
		}
		return false;
	}

	protected void evictAll(String bucket) {
		try {
			Cache cache = getCache(bucket);
			if (cache == null)
				return;
			cache.removeAll();
		} catch (Throwable th) {
			th.printStackTrace();
			logger.error("evictAll error", th);
		}
	}

	public void setThreeScreenDao(
			CommonPersistence<ThreeScreen, Serializable> threeScreenDao) {
		this.threeScreenDao = threeScreenDao;
	}

	public void setActivationDao(
			CommonPersistence<ActivationCode, Serializable> activationDao) {
		this.activationDao = activationDao;
	}

	public void setGroupDao(
			CommonPersistence<ProductGroup, Serializable> groupDao) {
		this.groupDao = groupDao;
	}

	public void setManufacturerDao(
			CommonPersistence<Manufacturer, Serializable> manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}

	public void setSubscribeDao(
			CommonPersistence<Subscribe, Serializable> subscribeDao) {
		this.subscribeDao = subscribeDao;
	}

	public void setInviteDao(
			CommonPersistence<InvitcodeRecord, Serializable> inviteDao) {
		this.inviteDao = inviteDao;
	}

	public void setApplicableDao(
			CommonPersistence<ApplicableRecords, Serializable> applicableDao) {
		this.applicableDao = applicableDao;
	}

	public void setWebChatDao(
			CommonPersistence<WebchatUser, Serializable> webChatDao) {
		this.webChatDao = webChatDao;
	}

	@Autowired
	public void setResourceMapper(ResourceMapper resourceMapper) {
		this.resourceMapper = resourceMapper;
	}

	public void setPdfInfoDao(
			CommonPersistence<PdfInfo, Serializable> pdfInfoDao) {
		this.pdfInfoDao = pdfInfoDao;
	}

	public void setNewsInfoDao(
			CommonPersistence<NewsInfo, Serializable> newsInfoDao) {
		this.newsInfoDao = newsInfoDao;
	}

	public void setTvAuthDao(
			CommonPersistence<TvAuthentication, Serializable> tvAuthDao) {
		this.tvAuthDao = tvAuthDao;
	}

	public void setBoxDao(CommonPersistence<TvBox, Serializable> boxDao) {
		this.boxDao = boxDao;
	}

	public void setPriceDao(CommonPersistence<AssetPrice, Serializable> priceDao) {
		this.priceDao = priceDao;
	}

	public void setVideoDao(CommonPersistence<Video, Serializable> videoDao) {
		this.videoDao = videoDao;
	}

	public void setWidgetDao(CommonPersistence<Widget, Serializable> widgetDao) {
		this.widgetDao = widgetDao;
	}

	public void setCategoryDao(
			CommonPersistence<Category, Serializable> categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void setUserReferenceDao(
			CommonPersistence<UserReference, Serializable> userReferenceDao) {
		this.userReferenceDao = userReferenceDao;
	}

	public void setRoleDao(CommonPersistence<Role, Serializable> roleDao) {
		this.roleDao = roleDao;
	}

	public void setAdverDao(
			CommonPersistence<Advertisement, Serializable> adverDao) {
		this.adverDao = adverDao;
	}

	public void setAdPostionDao(
			CommonPersistence<AdvertisementPosition, Serializable> adPostionDao) {
		this.adPostionDao = adPostionDao;
	}

	private static String[] special_chars = { "+", "/", "?", "%", "#", "&",
			">", "<", "\"", "'", ",", ")", "(", "*" };

	protected abstract String namespace();

	protected String getSqlAlias(String sql_id) {
		return getSqlAlias(namespace(), sql_id);
	}

	protected String getSqlAlias(String ns, String sql_id) {
		return ns + "." + sql_id;
	}

	public static String filterSQLSpecialChars(String query) {
		if (query == null)
			return "";
		for (int i = 0; i < special_chars.length; i++) {
			query = query.replace(special_chars[i], ""); // 用空字符串替换query字符串中所有在special_chars中的字符
		}
		return query;
	}

	protected <T> List<T> searchSql(String sql, Object[] params,
			final Class<?> t) {
		long begin = System.currentTimeMillis();
		List<T> list = jdbcTemplate.query(sql, params, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					@SuppressWarnings("unchecked")
					T ret = (T) t.newInstance();
					Field[] fs = t.getDeclaredFields();
					if (fs != null && fs.length > 0) {
						for (int i = 0; i < fs.length; i++) {
							Field f = fs[i];
							DataAlias da = f.getAnnotation(DataAlias.class);
							if (da != null) {
								String column = da.column();
								if (Tools.isBlank(column)) {
									column = f.getName();
								}
								Object obj = rs.getObject(column);
								if (da.from() == Date.class
										&& da.target() == Long.class) {
									obj = ((Date) obj).getTime();
								}
								Method m = t.getMethod(
										boxSetMethod(f.getName()), f.getType());
								m.invoke(ret, obj);
							}
						}
					}
					return ret;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
				}
				return null;
			}
		});
		logger.info("execute :" + sql + "\tuse "
				+ (System.currentTimeMillis() - begin) + "ms");
		return list;
	}

	protected <T> Pagination<T> initPage(String total_sql, int p, int rows) {
		Pagination<T> page = new Pagination<T>();
		int total = jdbcTemplate.queryForInt(total_sql);
		page.setTotal(total);
		int max_page = Math.max(0,
				(int) Math.ceil(Float.valueOf(total) / rows) - 1);
		page.setMax_page(max_page);
		p = Math.min(p, total);
		page.setBegin(p);
		page.setPage_rows(rows);
		return page;
	}

	public static String boxSetMethod(String field) {
		return "set" + WordUtils.capitalize(field);
	}

	public String nullFilterString(String value) {
		return value == null ? "" : value;
	}

	public Resource recodeResource(Resourceable resourceable) {
		try {
			Resource resource = new Resource();
			resource.setModifyDate(resourceable.getModifyDate());
			resource.setCoverTaskId(resourceable.getCover());
			resource.setCreateDate(resourceable.getCreateDate());
			resource.setIsActive(resourceable.getIsActive());
			resource.setStatus(resourceable.getStatus());
			resource.setCategory(resourceable.getCategory());
			resource.setName(resourceable.getName());
			resource.setPinyin(Tools.getPinYin(resourceable.getName()));
			resource.setMark(resourceable.getDescription());
			BaseModel data = resourceable.resource();
			if (data instanceof Video) {
				resource.setVideo((Video) data);
			} else if (data instanceof PdfInfo) {
				resource.setPdf((PdfInfo) data);
			} else if (data instanceof NewsInfo) {
				resource.setNews((NewsInfo) data);
			} else if (data instanceof ThreeScreen) {
				resource.setThreeScreen((ThreeScreen) data);
			} else {
				throw new Exception("#resource is empty");
			}
			if (!exist(resourceable)) {
				resource.setUuid(Tools.getUUID());
				resource.setCreateDate(Tools.getCurrentDate());
				resource.setModifyDate(Tools.getCurrentDate());
				resourceMapper.addResource(resource);
				return resource;
			} else {
				Resource before = getResource_(resourceable);
				if (null == before)
					return null;
				Resource res = updateResource(resourceable);
				Resource after = getResource_(resourceable);
				sendMessage(before, after);
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// 更新缓存
			Resource res = getResource(resourceable);
			if (res != null) {
				if (evictCache(CacheBucket.RESOURCEITEM, res.getUuid())) {
					logger.info("evict RESOURCEITEM cache @" + res.getUuid());
				}
			}
		}
	}

	/**
	 * @Description: 发送消息到RocketMQ
	 *
	 * @author mayuan
	 * @createDate 2016年6月30日
	 * @modifyDate 2016年6月30日
	 *
	 * @param before
	 *            更新之前的资源
	 * @param after
	 *            更新之后的资源
	 */
	private void sendMessage(Resource before, Resource after) {
		try {
			ResourceSimpleItem item = new ResourceSimpleItem(after);
			String messageData = item.itemApiJson(true);
			SendMessageToMQ sendMessage = new SendMessageToMQ();
			if (isAdd(before, after)) {
				if (Config.sendMessage) {
					sendMessage.setName("resource");
					sendMessage.setType("add");
					sendMessage.setData(messageData);
					MSG_POOL.execute(sendMessage);
				}
			} else if (isDelete(before, after)) {
				if (Config.sendMessage) {
					sendMessage.setName("resource");
					sendMessage.setType("delete");
					sendMessage.setData(messageData);
					MSG_POOL.execute(sendMessage);
				}
			} else if (isUpdate(before, after)) {
				if (Config.sendMessage) {
					sendMessage.setName("resource");
					sendMessage.setType("update");
					sendMessage.setData(messageData);
					MSG_POOL.execute(sendMessage);
				}
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("消息发送失败!");
				logger.info(e);
			}
		}
	}

	private boolean isUpdate(Resource before, Resource after) {
		/*
		 * return BaseModel.ACTIVE == before.getIsActive() && BaseModel.ACTIVE
		 * == after.getIsActive() && ((BaseModel.UNDERLINE == before.getStatus()
		 * && BaseModel.UNDERLINE == after .getStatus()) || (BaseModel.APPROVED
		 * == before .getStatus() && BaseModel.APPROVED == after.getStatus()));
		 */

		return BaseModel.ACTIVE == before.getIsActive()
				&& BaseModel.ACTIVE == after.getIsActive()
				&& ((BaseModel.APPROVED == before.getStatus() && BaseModel.APPROVED == after
						.getStatus()));
	}

	private boolean isAdd(Resource before, Resource after) {
		return BaseModel.ACTIVE == before.getIsActive()
				&& BaseModel.ACTIVE == after.getIsActive()
				&& (BaseModel.UNAPPROVED == before.getStatus() || BaseModel.UNDERLINE == before
						.getStatus())
				&& BaseModel.APPROVED == after.getStatus();
	}

	private boolean isDelete(Resource before, Resource after) {
		return BaseModel.ACTIVE == before.getIsActive()
				&& BaseModel.ACTIVE == after.getIsActive()
				&& BaseModel.APPROVED == before.getStatus()
				&& BaseModel.UNDERLINE == after.getStatus();
	}

	private Resource updateResource(Resourceable resourceable) {
		BaseModel data = resourceable.resource();
		String sql = "update resource set modifyDate=?,name=?,pinyin=?,coverTaskId=?,qrTaskId=?,categoryId=?,status=?,isActive=?,mark=?,createDate=? where 1 = 1";
		String condition = null;
		if (data instanceof Video) {
			Video video = videoDao.getById(data.getId(),
					getSqlAlias("asset", "getVideoById"));
			resourceable = video;
			condition = " AND assetId=" + video.getId();
		} else if (data instanceof PdfInfo) {
			PdfInfo pdf = pdfInfoDao.getById(data.getId(),
					getSqlAlias("pdf_info", "getPdfInfoById"));
			resourceable = pdf;
			condition = " AND pdfId=" + pdf.getId();
		} else if (data instanceof NewsInfo) {
			NewsInfo news = newsInfoDao.getById(
					data.getId(),
					getSqlAlias("com.lankr.orm.mybatis.mapper.NewsMaper",
							"selectInfoById"));
			resourceable = news;
			condition = " AND newsId=" + news.getId();
		} else if (data instanceof ThreeScreen) {
			ThreeScreen threeScreen = threeScreenDao.getById(data.getId(),
					getSqlAlias("getThreeScreenById"));
			resourceable = threeScreen;
			condition = " AND threeScreenId=" + threeScreen.getId();
		}
		if (StringUtils.isEmpty(condition)) {
			logger.error("update asset fail");
			return null;
		}
		sql += condition;
		final Resource resource = new Resource();
		resource.setModifyDate(resourceable.getModifyDate());
		resource.setCoverTaskId(resourceable.getCover());
		resource.setCreateDate(resourceable.getCreateDate());
		resource.setIsActive(resourceable.getIsActive());
		resource.setStatus(resourceable.getStatus());
		resource.setCategory(resourceable.getCategory());
		resource.setName(resourceable.getName());
		resource.setPinyin(Tools.getPinYin(resourceable.getName()));
		resource.setMark(resourceable.getDescription());
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, resource.getModifyDate());
				ps.setString(2, resource.getName());
				ps.setString(3, Tools.getPinYin(resource.getName()));
				ps.setString(4, resource.getCoverTaskId());
				ps.setString(5, resource.getQrTaskId());
				Category category = resource.getCategory();
				ps.setInt(6, category != null ? resource.getCategory().getId()
						: null);
				ps.setInt(7, resource.getStatus());
				ps.setInt(8, resource.getIsActive());
				ps.setString(9, resource.getMark());
				ps.setObject(10, resource.getCreateDate());
			}
		});
		return resource;
	}

	private boolean exist(Resourceable resourceable) {
		String sql = "select count(id) from resource where 1 = 1";
		BaseModel data = resourceable.resource();
		if (data instanceof Video) {
			sql += " and assetId=" + data.getId();
		} else if (data instanceof PdfInfo) {
			sql += " and pdfId=" + data.getId();
		} else if (data instanceof NewsInfo) {
			sql += " and newsId=" + data.getId();
		} else if (data instanceof ThreeScreen) {
			sql += " and threeScreenId=" + data.getId();
		}
		return jdbcTemplate.queryForInt(sql) > 0;
	}

	public Resource getResource(Resourceable resourceable) {
		if (resourceable == null)
			return null;
		Resource res = resourceable.getResource();
		if (res != null)
			return res;
		if (resourceable.getType() == Type.VIDEO) {
			res = resourceMapper.findResourceByVideoId(resourceable
					.getPrototypeId());
		} else if (resourceable.getType() == Type.PDF) {
			res = resourceMapper.findResourceByPdfId(resourceable
					.getPrototypeId());
		} else if (resourceable.getType() == Type.NEWS) {
			res = resourceMapper.findResourceByNewsId(resourceable
					.getPrototypeId());
		} else if (resourceable.getType() == Type.THREESCREEN) {
			res = resourceMapper.findResourceByThreeScreenId(resourceable
					.getPrototypeId());
		}
		return res;
	}

	private Resource getResource_(Resourceable resourceable) {
		if (resourceable == null)
			return null;
		if (resourceable.getType() == Type.VIDEO) {
			return resourceMapper.findResourceByVideoId(resourceable
					.getPrototypeId());
		} else if (resourceable.getType() == Type.PDF) {
			return resourceMapper.findResourceByPdfId(resourceable
					.getPrototypeId());
		} else if (resourceable.getType() == Type.NEWS) {
			return resourceMapper.findResourceByNewsId(resourceable
					.getPrototypeId());
		} else if (resourceable.getType() == Type.THREESCREEN) {
			return resourceMapper.findResourceByThreeScreenId(resourceable
					.getPrototypeId());
		}
		return null;
	}

	public void recordResouceCode(Resource res, String code) {
		if (res == null)
			return;
		try {
			String sql = "update resource set code=? where id=?";
			jdbcTemplate.update(sql, new Object[] { code, res.getId() });
		} catch (Exception e) {
			logger.error("recordResouceCode error ", e);
		}
	}

	public void removeResouceCode(Resource res) {
		recordResouceCode(res, null);
	}

	// 后台处于操作状态
	protected void onAdminBusy() {
		if (cacheManager != null)
			CacheBucket.resetConfig(cacheManager.getCacheManager(), true);
	}

	// 后台处于空闲状态
	protected void onAdminIdle() {
		if (cacheManager != null)
			CacheBucket.resetConfig(cacheManager.getCacheManager(), false);
	}

	public void recordResourceSpeaker(Resource res, Speaker speaker)
			throws Exception {
		if (res != null) {
			// 如果speaker一致，不用更新
			if (speaker != null) {
				Speaker tempSpaker = res.getSpeaker();
				if (tempSpaker != null
						&& tempSpaker.getUuid().equals(speaker.getUuid())) {

				}
				resourceMapper.updateResourceSpeaker(res.getId(),
						speaker.getId());
			} else {
				resourceMapper.updateResourceSpeaker(res.getId(), null);
			}
			// 更新缓存
			evictCache(CacheBucket.RESOURCEITEM, res.getUuid());
		}

	}
}
