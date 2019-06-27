/**
 * File Name: TestQuestionSelectOption.java
 * Date: 2019-5-13 下午04:06:55
 */
package me.belucky.exam.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 功能说明: 选择题选项
 * @author shenzl
 * @date 2019-5-13
 * @version 1.0
 */
public class TestQuestionSelectOption extends Model<TestQuestionSelectOption>{
	private static final long serialVersionUID = 6171143569224112064L;
	
	public static final TestQuestionRecord dao = new TestQuestionRecord();
}
