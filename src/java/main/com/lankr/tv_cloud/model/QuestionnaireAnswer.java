package com.lankr.tv_cloud.model;

public class QuestionnaireAnswer extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7608087378616148903L;

	private User user;
	
	private Questionnaire questionnaire;
	
	private String answer;
	
	private String verdict;
	
	private float score;
	
	private int status;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getVerdict() {
		return verdict;
	}

	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
