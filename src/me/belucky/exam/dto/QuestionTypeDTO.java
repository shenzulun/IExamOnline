/**
 * File Name: QuestionTypeDTO.java
 * Date: 2019-5-8 下午09:14:02
 */
package me.belucky.exam.dto;

import com.jfinal.plugin.activerecord.IBean;

/**
 * 功能说明: 试题分类DTO
 * @author shenzl
 * @date 2019-5-8
 * @version 1.0
 */
public class QuestionTypeDTO implements IBean{
	
	/**
	 * 试题分类ID
	 */
	private String typeId;
	/**
	 * 试题分类名称
	 */
	private String typeName;
	
	public QuestionTypeDTO(){}
	
	public QuestionTypeDTO(String typeId, String typeName) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	

}
