/**
 * File Name: MsgDTO.java
 * Date: 2019-10-15 14:35:59
 */
package me.belucky.exam.dto;

import com.jfinal.plugin.activerecord.IBean;

/**
 * Description: 前后台交互DTO
 * @author shenzulun
 * @date 2019-10-15
 * @version 1.0
 */
public class MsgDTO implements IBean {
	
	/**
	 * 请求IP
	 */
	private String reqIP;
	/**
	 * 查询方式
	 */
	private String queryType;
	/**
	 * 查询值
	 */
	private String queryValue;
	/**
	 * 耗时
	 */
	private Long cost;
	/**
	 * 返回代码
	 * 000000：成功
	 * 其它失败
	 */
	private String retCode;
	/**
	 * 返回信息
	 */
	private String retMsg;
	
	
	public MsgDTO() {}
	
	
	public String getReqIP() {
		return reqIP;
	}

	public void setReqIP(String reqIP) {
		this.reqIP = reqIP;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

}
