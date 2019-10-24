/**
 * File Name: TaskDefinitionDTO.java
 * Date: 2019-1-9 下午08:44:07
 */
package me.belucky.exam.dto;

import com.jfinal.plugin.activerecord.IBean;

/**
 * 功能说明: 定时任务定义DTO
 * @author shenzl
 * @date 2019-1-9
 * @version 1.0
 */
public class TaskDefinitionDTO implements IBean{
	
	/**
	 * 任务类型：1-定时任务  2-一次性
	 */
	public int taskType;
	
	/**
	 * 任务ID
	 */
	public String taskId;
	/**
	 * 任务名称
	 */
	public String taskName;
	/**
	 * 任务实例
	 */
	public String taskClassPath;
	/**
	 * 配置文件路径
	 */
	public String propPath;
	/**
	 * 首次运行日期
	 */
	public String firstExpectStart;
	/**
	 * 运行间隔
	 */
	public String intervalExpr;
	
	/**
	 * 最近运行日期
	 */
	public String lastRunDateStr;
	
	public TaskDefinitionDTO(){}
	
	public TaskDefinitionDTO(String taskId, String taskName,
			String taskClassPath, String propPath) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskClassPath = taskClassPath;
		this.propPath = propPath;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskClassPath() {
		return taskClassPath;
	}

	public void setTaskClassPath(String taskClassPath) {
		this.taskClassPath = taskClassPath;
	}

	public String getPropPath() {
		return propPath;
	}

	public void setPropPath(String propPath) {
		this.propPath = propPath;
	}

	public String getFirstExpectStart() {
		return firstExpectStart;
	}

	public void setFirstExpectStart(String firstExpectStart) {
		this.firstExpectStart = firstExpectStart;
	}

	public String getIntervalExpr() {
		return intervalExpr;
	}

	public void setIntervalExpr(String intervalExpr) {
		this.intervalExpr = intervalExpr;
	}

	public String getLastRunDateStr() {
		return lastRunDateStr == null ? "" : lastRunDateStr;
	}

	public void setLastRunDateStr(String lastRunDateStr) {
		this.lastRunDateStr = lastRunDateStr;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	
}
