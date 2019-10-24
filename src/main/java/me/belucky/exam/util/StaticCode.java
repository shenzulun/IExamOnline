/**
 * File Name: StaticCode.java
 * Date: 2019-06-12 11:11:59
 */
 package me.belucky.exam.util;

/**
  * 功能说明: 常量集合
  * @author shenzl
  * @date 2016-12-28
  * @version 1.0
  */
 public final class StaticCode {
 	/**
 	 * 空白字符串
 	 */
 	public static final String BLANK_STRING = "";
 	
 	public static final String CACHE_CONCURRENT_CONTROL = "cache_concurrent_control";
 	/**
 	 * 换行符
 	 */
 	public static final String LINE_BREAK = System.getProperty("line.separator","\n");
 	/**
 	 * 行内分隔符  |
 	 */
 	public static final String LINE_SEPARATOR = "|";
 	/**
 	 * 任务类型-定时任务
 	 */
 	public static final int TASK_TYPE_TIME = 1;
 	/**
 	 * 任务类型-一次性任务
 	 */
 	public static final int TASK_TYPE_ONCE = 2;
 	/**
 	 * 默认的文件字符集
 	 */
 	public static final String DEFAULT_FILE_ENCODE = "UTF-8";
 }
