/**
 * File Name: CodeValue.java
 * Date: 2019-5-13 下午04:34:34
 */
package me.belucky.exam.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 功能说明: 字典
 * @author shenzl
 * @date 2019-5-13
 * @version 1.0
 */
public class CodeValue extends Model<CodeValue>{
	private static final long serialVersionUID = 3640637457860275347L;
	
	public static final CodeValue dao = new CodeValue();
}
