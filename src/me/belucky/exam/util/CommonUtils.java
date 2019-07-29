/**
 * File Name: CommonUtils.java
 * Date: 2019-5-6 下午03:19:36
 */
package me.belucky.exam.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.magen.tool.common.EasyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.belucky.exam.core.task.TaskInitCenter;

/**
 * 功能说明: 通用工具类
 * @author shenzl
 * @date 2019-5-6
 * @version 1.0
 */
public class CommonUtils {
	protected static Logger log = LoggerFactory.getLogger(CommonUtils.class);	
	/**
	 * 初始化缓存
	 */
	public static void initCache(){
		//先清除所有缓存
		EasyUtil.chearCache();
		log.info("缓存清除成功");
		TaskInitCenter.go();
	}
	
	/**
	 * 正则查询
	 * 支持指定多个属性名
	 * @param <T>
	 * @param source
	 * @param regex
	 * @param fieldName
	 * @return
	 */
	public static <T> List<T> query(List<T> source, String regex, String... fieldName){
		List<T> result = new ArrayList<T>();
		if(source == null || source.size() == 0){
			return result;
		}
		Pattern pattern = Pattern.compile(regex.toUpperCase());
		for(T t : source){
			//反射查询值
			for(String fieldName0 : fieldName){
				String v = invoke(t, fieldName0);
				if(!isNotNull(v)){
					continue;
				}
				Matcher m = pattern.matcher(v.toUpperCase());
				if(m.find()){
					result.add(t);
					break;
				}
			}
			
		}
		return result;
	}
	
	/**
	 * Java bean  get 反射
	 * @param <T>
	 * @param target
	 * @param fieldName
	 * @return
	 */
	public static <T> T invoke(Object target, String fieldName) {
		return invoke(target, fieldName, "get", null);
	}
	
	/**
	 * Java bean 反射
	 * @param <T>
	 * @param target
	 * @param fieldName
	 * @param methodPrefix
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object target, String fieldName, String methodPrefix, T newValue) {
		String methodName = methodPrefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		T t = null;
		try {
			Class cls = target.getClass();
			if("set".equals(methodPrefix)){
				//set方法  先获取该字段的属性
				Field field = cls.getDeclaredField(fieldName);
				Method m = target.getClass().getDeclaredMethod(methodName, field.getType());
				Object v = m.invoke(target, newValue);
				t = (T)v;
			}else if("get".equals(methodPrefix)){
				Method m = target.getClass().getDeclaredMethod(methodName);
				Object v = m.invoke(target);
				t = (T)v;
			}
		} catch (SecurityException e) {
			log.error("",e);
		} catch (NoSuchMethodException e) {
			log.error("",e);
		} catch (IllegalArgumentException e) {
			log.error("",e);
		} catch (IllegalAccessException e) {
			log.error("",e);
		} catch (InvocationTargetException e) {
			log.error("",e);
		} catch (NoSuchFieldException e) {
			log.error("",e);
		}
		return t;
	}
	
	/**
	 * 是否非空
	 * @param input
	 * @return
	 */
	public static boolean isNotNull(String input){
		if(input == null || "".equals(input)){
			return false;
		}
		return true;
	}
	
	/**
	 * 是否非空白
	 * @param input
	 * @return
	 */
	public static boolean isNotBlank(String input){
		if(input == null || "".equals(input.trim())){
			return false;
		}
		return true;
	}
	
	/**
	 * 比较两个字符串是否相同
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean compareTwoString(String source, String target){
		if(source == null || target == null){
			return false;
		}
		//遍历比对
		char[] arr1 = source.toCharArray();
		Arrays.sort(arr1);
		char[] arr2 = target.toCharArray();
		Arrays.sort(arr2);
		String s1 = String.valueOf(arr1);
		String s2 = String.valueOf(arr2);
		return s1.equals(s2);
	}
	
	/**
	 * 随机生成指定个数的数组
	 * @param max	最大值
	 * @param count	个数
	 * @return
	 */
	public static int[] randomInt(int max, int count){
		if(count >= max){
			int[] arr = new int[max];
			for(int i=0;i<max;i++){
				arr[i] = i;
			}
			return arr;
		}
		Set<Integer> set = new HashSet<Integer>();
		int[] arr = new int[count];
		Random rand = new Random();
		for(int i=0;i<count;i++){
			int r = rand.nextInt(max);
			while(set.contains(r)){
				r = rand.nextInt(max);
			}
			set.add(r);
			arr[i] = r;
		}
		return arr;
	}
}
