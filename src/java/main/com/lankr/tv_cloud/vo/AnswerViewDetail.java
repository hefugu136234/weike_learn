package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.QuestionnaireAnswer;
import com.lankr.tv_cloud.utils.JSONException;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class AnswerViewDetail {
	private String uuid;

	private String username;

	private String answerStatus;

	private int answerSeq;// 问卷网答题序号

	private String answerTime;

	private String answerStart;
	
	private float score;

	private List<AnswerDetailItemVo> list;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAnswerStatus() {
		return answerStatus;
	}

	public void setAnswerStatus(String answerStatus) {
		this.answerStatus = answerStatus;
	}

	public int getAnswerSeq() {
		return answerSeq;
	}

	public void setAnswerSeq(int answerSeq) {
		this.answerSeq = answerSeq;
	}

	public String getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}

	public String getAnswerStart() {
		return answerStart;
	}

	public void setAnswerStart(String answerStart) {
		this.answerStart = answerStart;
	}

	public List<AnswerDetailItemVo> getList() {
		return list;
	}

	public void setList(List<AnswerDetailItemVo> list) {
		this.list = list;
	}
	
	

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void buildAnswerView(QuestionnaireAnswer questionnaireAnswer) {
		this.setUuid(questionnaireAnswer.getUuid());
		this.setUsername(OptionalUtils.traceValue(questionnaireAnswer,
				"user.username"));
		String json = questionnaireAnswer.getAnswer();
		buildJsonData(json);
		this.setScore(questionnaireAnswer.getScore());
	}

	public void buildJsonData(String json) {
		if (json == null || json.isEmpty()) {
			return;
		}
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.has("status")) {
				this.setAnswerStatus(jsonObject.getString("status"));
			}
			if (jsonObject.has("seq")) {
				this.setAnswerSeq(jsonObject.getInt("seq"));
			}
			if (jsonObject.has("start")) {
				this.setAnswerStart(jsonObject.getString("start"));
			}
			if (jsonObject.has("time_used")) {
				this.setAnswerTime(jsonObject.getString("time_used"));
			}
			list = new ArrayList<AnswerViewDetail.AnswerDetailItemVo>();
			int key_i = 1;
			String key_q = "Q";
			JSONObject object = null;
			String key="";
			do {
				key = key_q + key_i;
				if(!jsonObject.has(key)){
					break;
				}
				object = jsonObject.getJSONObject(key);
				buildJsonTitle(object, key_i);
				key_i++;
			} while (object != null);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void buildJsonTitle(JSONObject object, int key_i) {
		if (object == null) {
			return;
		}
		String title = "问题-", answer = "答案：";
		title = title + key_i + "：";
		try {
			if (object.has("title")) {
				title = title + object.getString("title");
			}
			if (object.has("type_desc")) {
				title = title + " [" + object.getString("type_desc") + "]";
			}
			if (object.has("answer")) {
				answer = answer + object.getString("answer");
			}
			this.list.add(new AnswerDetailItemVo(title, answer));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class AnswerDetailItemVo {
		private String title;

		private String answer;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

		public AnswerDetailItemVo(String title, String answer) {
			this.title = title;
			this.answer = answer;
		}

	}



}
