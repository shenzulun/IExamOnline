/**
 * File Name: TestQuestion.java
 * Date: 2019-5-10 上午11:42:45
 */
package me.belucky.exam.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 功能说明: 试题
 * @author shenzl
 * @date 2019-5-10
 * @version 1.0
 */
public class TestQuestion extends Model<TestQuestion>{
	private static final long serialVersionUID = -5564512521252905102L;
	
	public static final TestQuestion dao = new TestQuestion();
}
