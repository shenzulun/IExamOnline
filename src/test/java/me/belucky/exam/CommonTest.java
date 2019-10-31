/**
 * File Name: CommonTest.java
 * Date: 2019-10-31 09:35:22
 */
package me.belucky.exam;

import org.junit.Test;

/**
 * Description: 
 * @author shenzulun
 * @date 2019-10-31
 * @version 1.0
 */
public class CommonTest {
	
	@Test
	public void test1() {
		String str = ",,,Q_2_1_4_D";
		String[] arr = str.split(",");
		for(String s : arr) {
			if(!"".equals(s)) {
				System.out.println(s);
			}
		}
	}

}
