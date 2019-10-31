/**
 * File Name: LaunchEntry.java
 * Date: 2019-5-5 下午03:23:31
 */
package me.belucky.exam;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.belucky.exam.controller.*;
import me.belucky.exam.model.table._MappingKit;
import me.belucky.exam.util.CommonUtils;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

/**
 * 功能说明: 启动入口
 * @author shenzl
 * @date 2019-5-5
 * @version 1.0
 */
public class LaunchEntry extends JFinalConfig{
	protected static Logger log = LoggerFactory.getLogger(LaunchEntry.class);
	private static Prop prop;
	
	public void configConstant(Constants me) {
		loadConfig();
		PropKit.use("sys-config.properties");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setMaxPostSize(1024 * 1024 * 1024);    //1G
		//me.setControllerFactory(new FastControllerFactory());
		me.setViewType(ViewType.FREE_MARKER);
		/**
		 * 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
		 * 注入动作支持任意深度并自动处理循环注入
		 */
		me.setInjectDependency(true);
		// 配置对超类中的属性进行注入
		me.setInjectSuperClass(true);
	}
	
	/**
	 * 载入配置文件
	 */
	static void loadConfig() {
		if (prop == null) {
			prop = PropKit.useFirstFound("sys-config.properties");
			CommonUtils.initPropIgnore("", "sys-config.properties");
		}
	}

	public void configHandler(Handlers me) {
		
	}

	public void configInterceptor(Interceptors me) {
		me.add(new TxByMethods("initSaveIntoDB"));
	}

	public void configPlugin(Plugins me) {
		DruidPlugin druid = createDruidPlugin(); 
		me.add(druid);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druid);
		arp.setShowSql(prop.getBoolean("showSql", false));
		arp.setDialect(new Sqlite3Dialect());
		_MappingKit.mapping(arp);
		me.add(arp);
	}
	
	public static DruidPlugin createDruidPlugin() {	
		loadConfig();
		String url = prop.get("jdbc-url");
		String driverClass = prop.get("jdbc-driverClass");
		String username = prop.get("jdbc-user");
		String password = prop.get("jdbc-password");
		DruidPlugin druid = new DruidPlugin(url, username, password,driverClass); 
		return druid;
	}

	public void configRoute(Routes me) {
		me.setMappingSuperClass(true);
//		me.setBaseViewPath("/src/main/webapp/");
		me.add("/", IndexController.class);	
		me.add("/exam", ExamController.class, "/exams");	
	}

	@Override
	public void onStart(){
		CommonUtils.initCache();
		try {
			String indexUrl = PropKit.get("index_url");
			if(indexUrl != null && !"".equals(indexUrl)){
				Desktop.getDesktop().browse(new URI(indexUrl));
			}
		} catch (IOException e) {
			log.error("打开浏览器失败...",e);
		} catch (URISyntaxException e) {
			log.error("打开浏览器失败...",e);
		}
	}

	@Override
	public void onStop(){
		
	}
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 9999, "/", 5);
	}

	public void configEngine(Engine me) {
	}
}
