/**
 * File Name: JsonLogController.java
 * Date: 2016-7-7 上午09:15:55
 */
package me.belucky.exam.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import me.belucky.easytool.tail.Tail;
import me.belucky.exam.dto.MsgDTO;

/**
 * 功能说明: 返回json格式的log信息
 * @author shenzl
 * @date 2016-7-7
 * @version 1.0
 */
public abstract class JsonLogController extends Controller{
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	public abstract Class<?> setObj();
		
	public abstract void go(Object dto, String methodName);
	
	private String remoteAddr;
	
	private MsgDTO retDto = null;
	
	public void autorun(){
		String methodName = this.getPara("methodName");
		log.debug(methodName);
		this.remoteAddr = super.getRequest().getRemoteAddr();
		log.debug("当前访问IP: {}", remoteAddr == null ? "" : remoteAddr);
		Object dto = getBean(setObj());
		//日志监视
		StringBuffer buff = new StringBuffer();
		Tail tail = new Tail(PropKit.getProp("log4j.properties").get("log4j.appender.file.File"),buff);
		go(dto, methodName);
		if(retDto == null){
			retDto = new MsgDTO();
			retDto.setRetMsg(tail.getBuffer().toString());
		}
		renderJson(retDto);
	}
	
	public void invoke(Object target, String methodName, Object dto) {
		try {
			Method m = target.getClass().getDeclaredMethod(methodName, dto.getClass());
			m.invoke(target, dto);
		} catch (SecurityException e) {
			log.error("",e);
		} catch (NoSuchMethodException e) {
			log.error("",e);
		} catch (IllegalAccessException e) {
			log.error("",e);
		} catch (IllegalArgumentException e) {
			log.error("",e);
		} catch (InvocationTargetException e) {
			log.error("",e);
		}
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public MsgDTO getRetDto() {
		return retDto;
	}

	public void setRetDto(MsgDTO retDto) {
		this.retDto = retDto;
	}

}
