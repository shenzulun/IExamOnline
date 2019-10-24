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
import me.belucky.exam.model.*;
import me.belucky.exam.util.CommonUtils;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.FastControllerFactory;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

/**
 * 功能说明: 启动入口
 * @author shenzl
 * @date 2019-5-5
 * @version 1.0
 */
public class LaunchEntry extends JFinalConfig{
	protected static Logger log = LoggerFactory.getLogger(LaunchEntry.class);
	
	public void configConstant(Constants me) {
		PropKit.use("sys-config.properties");
		me.setDevMode(PropKit.getBoolean("devMode", false));
//		me.setViewType(ViewType.JSP);   //默认freemarker
		me.setMaxPostSize(1024 * 1024 * 1024);    //1G
		//me.setControllerFactory(new FastControllerFactory());
	}

	public void configHandler(Handlers me) {
		
	}

	public void configInterceptor(Interceptors me) {
		me.add(new TxByMethods("initSaveIntoDB"));
	}

	public void configPlugin(Plugins me) {
		String url = PropKit.get("jdbc-url");
		log.info(url);
		String driverClass = PropKit.get("jdbc-driverClass");
		String username = PropKit.get("jdbc-user");
		String password = PropKit.get("jdbc-password");
		DruidPlugin druid = new DruidPlugin(url, username, password,driverClass); 
		me.add(druid);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druid);
		arp.setShowSql(PropKit.getBoolean("showSql", false));
		me.add(arp);
		arp.addMapping("T_QUESTION", TestQuestion.class);
		arp.addMapping("T_QUESTION_RECORD", TestQuestionRecord.class);
		arp.addMapping("T_QUESTION_SELECT_OPTION", TestQuestionSelectOption.class);
		arp.addMapping("T_CODE_VALUE", CodeValue.class);
		arp.addMapping("T_QUERY_COND", TestQueryCond.class);
	}

	public void configRoute(Routes me) {
		me.add("/", IndexController.class);	
	}
	
	public void afterJFinalStart(){
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
	
	public void beforeJFinalStop(){
		
	}
	
	public static void main(String[] args) {
		JFinal.start("WebContent", 9999, "/", 5);
	}

	public void configEngine(Engine me) {
	}
}
