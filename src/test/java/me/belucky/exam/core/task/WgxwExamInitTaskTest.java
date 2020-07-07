/**
 * File Name: WgxwExamInitTaskTest.java
 * Date: 2020-04-13 16:48:57
 */
package me.belucky.exam.core.task;

import org.junit.Before;
import org.junit.Test;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.druid.DruidPlugin;
import me.belucky.exam.LaunchEntry;
import me.belucky.exam.model.table._MappingKit;

/**
 * Description: 
 * @author shenzulun
 * @date 2020-04-13
 * @version 1.0
 */
public class WgxwExamInitTaskTest {
	
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
		new WgxwExamInitTask("农信违规处理办法考试初始化任务").go();
	}
}
