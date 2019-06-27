/**
 * File Name: SelectOptionDTO.java
 * Date: 2019-5-8 下午10:25:57
 */
package me.belucky.exam.dto;

import com.jfinal.plugin.activerecord.IBean;

/**
 * 功能说明: 选择题选项DTO
 * @author shenzl
 * @date 2019-5-8
 * @version 1.0
 */
public class SelectOptionDTO implements IBean{
	
	/**
	 * 选项值
	 */
	private String key;
	/**
	 * 选项描述
	 */
	private String desc;

	public SelectOptionDTO(){}
	
	public SelectOptionDTO(String key, String desc) {
		super();
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
