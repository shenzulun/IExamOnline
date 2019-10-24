/**
 * File Name: TestQueryCond.java
 * Date: 2019-5-15 下午05:33:49
 */
package me.belucky.exam.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 功能说明: 试题查询条件
 * @author shenzl
 * @date 2019-5-15
 * @version 1.0
 */
public class TestQueryCond extends Model<TestQueryCond>{
	private static final long serialVersionUID = -863542470587992953L;
	
	public static final TestQueryCond dao = new TestQueryCond();
}
