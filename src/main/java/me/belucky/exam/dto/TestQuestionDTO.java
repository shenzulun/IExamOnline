/**
 * File Name: TestQuestionDTO.java
 * Date: 2019-5-6 上午08:35:30
 */
package me.belucky.exam.dto;

import java.util.List;

import com.jfinal.plugin.activerecord.IBean;

/**
 * 功能说明: 试题DTO
 * @author shenzl
 * @date 2019-5-6
 * @version 1.0
 */
public class TestQuestionDTO implements IBean{
	
	/**
	 * 主键
	 */
	private int id;
	/**
	 * 序号
	 */
	private String seq;
	/**
	 * 题目类型
	 * 1: 单选
	 * 2: 多选
	 * 3: 判断
	 * 4: 错题簿
	 */
	private int type;
	/**
	 * 题目
	 */
	private String title;
	/**
	 * 选择题选项
	 */
	private List<SelectOptionDTO> selectOptions;
	/**
	 * 判断题选项
	 */
	private String judgeOptions;
	/**
	 * 选择题答案
	 */
	private String selectAnswer;
	/**
	 * 判断题答案
	 */
	private boolean judgeAnswer;
	/**
	 * 主观题-答案
	 */
	private String subjectiveAnswer;
	/**
	 * 选择题/判断题选项-错题簿页面展示用
	 */
	private String selectChoose;
	/**
	 * 创建时间
	 */
	private String createDtStr;
	/**
	 * 试题类型
	 * 1-财会考试
	 * 2-乡村振兴
	 */
	private int examId;
		
	public TestQuestionDTO(){}
	
	public TestQuestionDTO(String seq, int type, String title,
			List<SelectOptionDTO> selectOptions, String judgeOptions, String selectAnswer,
			boolean judgeAnswer, String subjectiveAnswer) {
		super();
		this.seq = seq;
		this.type = type;
		this.title = title;
		this.selectOptions = selectOptions;
		this.judgeOptions = judgeOptions;
		this.selectAnswer = selectAnswer;
		this.judgeAnswer = judgeAnswer;
		this.subjectiveAnswer = subjectiveAnswer;
	}
	
	public TestQuestionDTO(int id, String seq, int type, String title,
			List<SelectOptionDTO> selectOptions, String judgeOptions,
			String selectAnswer, boolean judgeAnswer, String subjectiveAnswer) {
		super();
		this.id = id;
		this.seq = seq;
		this.type = type;
		this.title = title;
		this.selectOptions = selectOptions;
		this.judgeOptions = judgeOptions;
		this.selectAnswer = selectAnswer;
		this.judgeAnswer = judgeAnswer;
		this.subjectiveAnswer = subjectiveAnswer;
	}

	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<SelectOptionDTO> getSelectOptions() {
		return selectOptions;
	}
	public void setSelectOptions(List<SelectOptionDTO> selectOptions) {
		this.selectOptions = selectOptions;
	}
	public String getJudgeOptions() {
		return judgeOptions;
	}
	public void setJudgeOptions(String judgeOptions) {
		this.judgeOptions = judgeOptions;
	}
	public String getSelectAnswer() {
		return selectAnswer;
	}
	public void setSelectAnswer(String selectAnswer) {
		this.selectAnswer = selectAnswer;
	}
	public boolean isJudgeAnswer() {
		return judgeAnswer;
	}
	public void setJudgeAnswer(boolean judgeAnswer) {
		this.judgeAnswer = judgeAnswer;
	}
	public String getSubjectiveAnswer() {
		return subjectiveAnswer;
	}
	public void setSubjectiveAnswer(String subjectiveAnswer) {
		this.subjectiveAnswer = subjectiveAnswer;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSelectChoose() {
		return selectChoose;
	}

	public void setSelectChoose(String selectChoose) {
		this.selectChoose = selectChoose;
	}

	public String getCreateDtStr() {
		return createDtStr;
	}

	public void setCreateDtStr(String createDtStr) {
		this.createDtStr = createDtStr;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}
	
	
}
