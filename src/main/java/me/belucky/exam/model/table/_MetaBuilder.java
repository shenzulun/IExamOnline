/**
 * File Name: _MetaBuilder.java
 * Date: 2019-10-16 14:57:31
 */
package me.belucky.exam.model.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;

/**
 * Description: 
 * @author shenzulun
 * @date 2019-10-16
 * @version 1.0
 */
public class _MetaBuilder extends MetaBuilder{

	public _MetaBuilder(DataSource dataSource) {
		super(dataSource);
	}
	
	protected ResultSet getTablesResultSet() throws SQLException {
		return dbMeta.getTables(conn.getCatalog(), "SHENZL", null, new String[]{"TABLE"});	// 不支持 view 生成
	}

}
