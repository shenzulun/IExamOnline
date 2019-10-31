/**
 * File Name: XczxExamInitTaskTest.java
 * Date: 2019-10-29 08:44:47
 */
package me.belucky.exam.core.task;

import org.junit.Before;
import org.junit.Test;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.druid.DruidPlugin;
import me.belucky.easytool.task.TaskInitCenter;
import me.belucky.exam.LaunchEntry;
import me.belucky.exam.model.table._MappingKit;

/**
 * Description: 
 * @author shenzulun
 * @date 2019-10-29
 * @version 1.0
 */
public class XczxExamInitTaskTest {

	@Before
	public void before() throws Exception {
		DruidPlugin druidPlugin = LaunchEntry.createDruidPlugin();
		druidPlugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(true);
		arp.setDialect(new Sqlite3Dialect());
		_MappingKit.mapping(arp);
		arp.start();
	}
	
	@Test
	public void test0() throws Exception {
		TaskInitCenter.go();
	}
}
