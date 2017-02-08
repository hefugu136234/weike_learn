/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月20日
 * 	@modifyDate 2016年6月1日
 *  
 */
package com.lankr.tv_cloud.model;

import java.util.Date;

/**
 * @author Kalean.Xiang
 *
 */
public class NormalCollect extends BaseModel {

	private static final long serialVersionUID = 1L;

	private final static int SHIFT = 1;

	/***
	 * 20bit 0b10000000000000000000 不可修改
	 */
	private final static int LOGIC = 0x80000;

	// 是否私有
	private final static int SIGN_LOGIC_PRIVATE = LOGIC;

	// 是否需要实名参与
	private final static int SIGN_LOGIC_JOIN_CERTIFICATED = SIGN_LOGIC_PRIVATE >> SHIFT;

	// 需要上一个等级通过
	private final static int SIGN_LOGIC_SEGMENT_HOOK = SIGN_LOGIC_JOIN_CERTIFICATED >> SHIFT;

	/***
	 * 9bit 0b100000000 不可修改
	 */
	private static final int TYPE = 0x100;

	// 课程
	public static final int SIGN_TYPE_COURSE = TYPE;
	// 等级
	public static final int SIGN_TYPE_COURSE_SEGMENT = SIGN_TYPE_COURSE << SHIFT;

	private static final int SIGN_TYPE_GENERAL_COLLECT = SIGN_TYPE_COURSE_SEGMENT << SHIFT;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	public int getPassScore() {
		return passScore;
	}

	public void setPassScore(int passScore) {
		this.passScore = passScore;
	}
	
	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public Date getRecommendDate() {
		return recommendDate;
	}

	public void setRecommendDate(Date recommendDate) {
		this.recommendDate = recommendDate;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getGrade() {
		return grade;
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public int getNumbers() {
		return numbers;
	}

	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public NormalCollect getParent() {
		return parent;
	}

	public void setParent(NormalCollect parent) {
		this.parent = parent;
	}

	private String name;

	private String pinyin;

	private String description;

	private int level;

	private int sign;

	private float version;

	private int status;

	private Speaker speaker;

	private int passScore; // 及格分数

	private float grade;// 评分统计
	
	private float price;
	
	private int praise;// 总赞数统计
	
	private Date recommendDate;
	
	private int numbers;// 学习人数统计

	private NormalCollect parent;
	

	// 是否是课程
	public boolean isCourse() {
		return (sign & SIGN_TYPE_COURSE) != 0;
	}

	// 是否是课程的等级
	public boolean isCourseSegment() {
		return (sign & SIGN_TYPE_COURSE_SEGMENT) != 0;
	}

	// 普通合集
	public boolean isGeneralCollect() {
		return (sign & SIGN_TYPE_GENERAL_COLLECT) != 0;
	}

	public boolean isPrivate() {
		return (sign & SIGN_LOGIC_PRIVATE) != 0;
	}

	public boolean needPreviousPassed() {
		return (sign & SIGN_LOGIC_SEGMENT_HOOK) != 0;
	}

	// 是否需要实名认证
	public boolean needCertificated() {
		return (sign & SIGN_LOGIC_JOIN_CERTIFICATED) != 0;
	}

	// 创建课程的sign
	public static int getCourseSign(boolean isPrivated, boolean certificated) {
		return makeSign(isPrivated, certificated, false, SIGN_TYPE_COURSE);
	}

	// 课程章节 Sign
	public static int getCourseSegmentSign(boolean isPrivated,
			boolean certificated, boolean needPreviousPassed) {
		return makeSign(isPrivated, certificated, needPreviousPassed,
				SIGN_TYPE_COURSE_SEGMENT);
	}

	// 普通合集 Sign
	public static int getGeneralCollect(boolean isPrivated, boolean certificated) {
		return makeSign(isPrivated, certificated, false,
				SIGN_TYPE_GENERAL_COLLECT);
	}

	public Type getType() {
		if (isCourse()) {
			return Type.COURSE;
		} else if (isCourseSegment()) {
			return Type.SEGMENT;
		} else {
			return Type.GENERAL;
		}
	}

	private static int makeSign(boolean isPrivated, boolean certificated,
			boolean needPreviousPassed, int sign_logic) {
		int s = 0;
		if (isPrivated) {
			s |= SIGN_LOGIC_PRIVATE;
		}
		if (certificated) {
			s |= SIGN_LOGIC_JOIN_CERTIFICATED;
		}
		if (needPreviousPassed) {
			s |= SIGN_LOGIC_SEGMENT_HOOK;
		}
		return s | sign_logic;
	}

	public static enum Type {
		COURSE {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.lankr.tv_cloud.model.NormalCollect.Type#getSign()
			 */
			@Override
			int getSign() {
				return SIGN_TYPE_COURSE;
			}
		},
		GENERAL {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.lankr.tv_cloud.model.NormalCollect.Type#getSign()
			 */
			@Override
			int getSign() {
				return SIGN_TYPE_GENERAL_COLLECT;
			}

		},
		SEGMENT {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.lankr.tv_cloud.model.NormalCollect.Type#getSign()
			 */
			@Override
			int getSign() {
				return SIGN_TYPE_COURSE_SEGMENT;
			}
		};

		int getSign() {
			throw new AbstractMethodError();
		}

		public static int getSign(Type... types) {
			int s = 0x0;
			if (types != null) {
				for (Type type : types) {
					s |= type.getSign();
				}
			}
			return s;
		}
	}
}
