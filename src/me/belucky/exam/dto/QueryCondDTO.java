/**
 * File Name: QueryCondDTO.java
 * Date: 2019-5-15 下午05:36:02
 */
package me.belucky.exam.dto;

import com.jfinal.plugin.activerecord.IBean;

/**
 * 功能说明: 查询条件DTO
 * @author shenzl
 * @date 2019-5-15
 * @version 1.0
 */
public class QueryCondDTO implements IBean{

	/**
	 * 试题类型
	 */
	private String questionType;
	/**
	 * 查询条件
	 */
	private String queryValue;
	
	public QueryCondDTO(){}
	
	public QueryCondDTO(String questionType, String queryValue) {
		super();
		this.questionType = questionType;
		this.queryValue = queryValue;
	}
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getQueryValue() {
		return queryValue;
	}
	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}
	
	
}
