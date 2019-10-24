/**
 * File Name: TestQuestionRecord.java
 * Date: 2019-5-13 下午04:06:31
 */
package me.belucky.exam.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 功能说明: 试题答题记录
 * @author shenzl
 * @date 2019-5-13
 * @version 1.0
 */
public class TestQuestionRecord extends Model<TestQuestionRecord>{
	private static final long serialVersionUID = -355375420222126231L;

	public static final TestQuestionRecord dao = new TestQuestionRecord();
}
