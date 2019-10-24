/**
 * File Name: QuestionAnswerDTO.java
 * Date: 2019-5-9 上午11:26:59
 */
package me.belucky.exam.dto;

import com.jfinal.plugin.activerecord.IBean;

/**
 * 功能说明: 试题答案DTO
 * @author shenzl
 * @date 2019-5-9
 * @version 1.0
 */
public class QuestionAnswerDTO implements IBean{
	
	/**
	 * 问题ID
	 */
	private String questionId;
	/**
	 * 选择的选项
	 */
	private String selectOption;
	/**
	 * 正确的答案
	 */
	private String correctAnswer;
	/**
	 * 是否正确
	 */
	private boolean isCorrect;
	/**
	 * 试题
	 */
	private TestQuestionDTO testQuestionDTO;
	/**
	 * 用户号,当前用IP记录
	 */
	private String userNo;
	
	public QuestionAnswerDTO(){}
	
	public QuestionAnswerDTO(String questionId, String selectOption,
			String correctAnswer, boolean isCorrect) {
		super();
		this.questionId = questionId;
		this.selectOption = selectOption;
		this.correctAnswer = correctAnswer;
		this.isCorrect = isCorrect;
	}

	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getSelectOption() {
		return selectOption;
	}
	public void setSelectOption(String selectOption) {
		this.selectOption = selectOption;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public boolean isCorrect() {
		return isCorrect;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public TestQuestionDTO getTestQuestionDTO() {
		return testQuestionDTO;
	}

	public void setTestQuestionDTO(TestQuestionDTO testQuestionDTO) {
		this.testQuestionDTO = testQuestionDTO;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
}
