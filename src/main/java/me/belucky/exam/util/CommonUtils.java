/**
 * File Name: CommonUtils.java
 * Date: 2019-5-6 下午03:19:36
 */
package me.belucky.exam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.PropKit;

import me.belucky.easytool.task.TaskInitCenter;
import me.belucky.easytool.util.FileTools;

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
			e.printStackTrace();
			log.error("系统初始化失败...",e);
		}
	}
	
	/**
	 * 批量初始化prop目录
	 * @param propFolderName
	 */
	public static void initProp(String... propFolderNames){
		//类似遍历resources/logAnalyse目录
		for(String propFolderName : propFolderNames){
			String path = Thread.currentThread().getContextClassLoader().getResource(propFolderName).getFile();
			String[] arr = FileTools.getFileNameArray(path, ".properties");
			for(String s : arr){
				PropKit.use(propFolderName + "/" + s);
			}
		}
	}
	
	/**
	 * 批量初始化prop目录
	 * @param propFolderName prop目录
	 * @param ignorePropName 忽略的prop文件名
	 */
	public static void initPropIgnore(String propFolderName, String ignorePropName){
		String prePath = "";
		if(!"".contentEquals(propFolderName)) {
			prePath = propFolderName + "/";
		}
		String path = Thread.currentThread().getContextClassLoader().getResource(propFolderName).getFile();
		String[] arr = FileTools.getFileNameArray(path, ".properties");
		for(String s : arr){
			if(!s.equals(ignorePropName)) {
				PropKit.use(prePath + s);
				log.info("配置文件[{}]加载成功...", s);
			}
		}
	}
	
	/**
	 * 查询数组中的指定数据的位置
	 * @param arr
	 * @param target
	 * @return
	 */
	public static int indexOfArray(String[] arr, String target){
		for(int i=0,len=arr.length;i<len;i++){
			if(target.indexOf(arr[i]) != -1){
				return i;
			}
		}
		return -1;
	}
	
}
