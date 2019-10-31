/**
 * File Name: JFinalModelGenerator.java
 * Date: 2019-09-09 16:01:11
 */
package me.belucky.exam.model.table;

import javax.sql.DataSource;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;
import me.belucky.exam.LaunchEntry;


/**
 * Description: Model生成器
 * @author shenzulun
 * @date 2019-09-09
 * @version 1.0
 */
public class JFinalModelGenerator {
	
	public static DataSource getDataSource() {
		DruidPlugin druidPlugin = LaunchEntry.createDruidPlugin();
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}
	
	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "me.belucky.exam.model.table.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/me/belucky/exam/model/table/base";
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "me.belucky.exam.model.table";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		
		DataSource dataSource = getDataSource();
		// 创建生成器
		Generator generator = new Generator(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		
		// 配置是否生成备注
		generator.setGenerateRemarks(true);
		
		// 设置数据库方言
		generator.setDialect(new Sqlite3Dialect());
				
		// 设置是否生成链式 setter 方法
		generator.setGenerateChainSetter(false);
		
		// 添加不需要生成的表名
		generator.addExcludedTable("adv");
		
		// 设置是否在 Model 中生成 dao 对象
		generator.setGenerateDaoInModel(true);
		
		// 设置是否生成字典文件
		generator.setGenerateDataDictionary(false);
		
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		generator.setRemovedTableNamePrefixes("T_");
		
		generator.setMetaBuilder(new SqliteMetaBuilder(dataSource));
		// 生成
		generator.generate();
	}
}
