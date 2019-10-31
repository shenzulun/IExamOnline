/**
 * File Name: SqliteMetaBuilder.java
 * Date: 2019-10-28 08:45:03
 */
package me.belucky.exam.model.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import me.belucky.easytool.util.StringUtils;

/**
 * Description: 
 * @author shenzulun
 * @date 2019-10-28
 * @version 1.0
 */
public class SqliteMetaBuilder extends MetaBuilder{

	public SqliteMetaBuilder(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void buildColumnMetas(TableMeta tableMeta) throws SQLException {
		String sql = dialect.forTableBuilderDoBuild(tableMeta.name);
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		
		Map<String, ColumnMeta> columnMetaMap = new HashMap<>();
		if (generateRemarks) {
			ResultSet colMetaRs = null;
			try {
				colMetaRs = dbMeta.getColumns(conn.getCatalog(), null, tableMeta.name, null);
				while (colMetaRs.next()) {
					ColumnMeta columnMeta = new ColumnMeta();
					columnMeta.name = colMetaRs.getString("COLUMN_NAME");
					columnMeta.remarks = colMetaRs.getString("REMARKS");
					columnMetaMap.put(columnMeta.name, columnMeta);
				}
			} catch (Exception e) {
				System.out.println("无法生成 REMARKS");
			} finally {
				if (colMetaRs != null) {
					colMetaRs.close();
				}
			}
		}
		
		
		for (int i=1; i<=columnCount; i++) {
			ColumnMeta cm = new ColumnMeta();
			cm.name = rsmd.getColumnName(i);
			
			String typeStr = null;
			if (dialect.isKeepByteAndShort()) {
				int type = rsmd.getColumnType(i);
				if (type == Types.TINYINT) {
					typeStr = "java.lang.Byte";
				} else if (type == Types.SMALLINT) {
					typeStr = "java.lang.Short";
				}
			}
			
			if (typeStr == null) {
				String colClassName = rsmd.getColumnClassName(i);
				typeStr = typeMapping.getType(colClassName);
			}
			
			if (typeStr == null) {
				int type = rsmd.getColumnType(i);
				if (type == Types.BINARY || type == Types.VARBINARY || type == Types.LONGVARBINARY || type == Types.BLOB) {
					typeStr = "byte[]";
				} else if (type == Types.CLOB || type == Types.NCLOB) {
					typeStr = "java.lang.String";
				}
				// 支持 oracle 的 TIMESTAMP、DATE 字段类型，其中 Types.DATE 值并不会出现
				// 保留对 Types.DATE 的判断，一是为了逻辑上的正确性、完备性，二是其它类型的数据库可能用得着
				else if (type == Types.TIMESTAMP || type == Types.DATE) {
					typeStr = "java.util.Date";
				}
				// 支持 PostgreSql 的 jsonb json
				else if (type == Types.OTHER) {
					typeStr = "java.lang.Object";
				} else {
					typeStr = "java.lang.String";
					String columnName = cm.name.toUpperCase();
					String[] intArr = {"QUESTION_TYPE","SEQ"};
					if(columnName.equals("ID") || columnName.endsWith("_ID") || StringUtils.isExsit(columnName, intArr , true)) {
						typeStr = "java.lang.Integer";
					}
				}
			}
			
			typeStr = handleJavaType(typeStr, rsmd, i);
			
			System.out.println(tableMeta.name + ":" + cm.name + "-" + typeStr);
			cm.javaType = typeStr;
			
			// 构造字段对应的属性名 attrName
			cm.attrName = buildAttrName(cm.name);
			
			// 备注字段赋值
			if (generateRemarks && columnMetaMap.containsKey(cm.name)) {
				cm.remarks = columnMetaMap.get(cm.name).remarks;
			}
			
			tableMeta.columnMetas.add(cm);
		}
		
		rs.close();
		stm.close();
	}
}
