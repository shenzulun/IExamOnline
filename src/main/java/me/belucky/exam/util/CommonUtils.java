/**
 * File Name: CommonUtils.java
 * Date: 2019-5-6 下午03:19:36
 */
package me.belucky.exam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.belucky.easytool.task.TaskInitCenter;

/**
 * 功能说明: 通用工具类
 * @author shenzl
 * @date 2019-5-6
 * @version 1.0
 */
public class CommonUtils extends me.belucky.easytool.util.CommonUtils{
	protected static Logger log = LoggerFactory.getLogger(CommonUtils.class);
	private static volatile boolean taskInitFlag = false;         //任务初始化标志
	
	/**
	 * 初始化缓存
	 */
	public static void initCache(){
		try {
			if(!taskInitFlag){
				taskInitFlag = true;
				TaskInitCenter.go();
				log.info("系统初始化成功...");
			}
		} catch (Exception e) {
			log.error("系统初始化失败...",e);
		}
	}
	
}
