/**
 * File Name: QuestionInitTask.java
 * Date: 2019-10-25 14:16:32
 */
package me.belucky.exam.core.task;

import me.belucky.easytool.task.AbstractTask;
import me.belucky.exam.core.QuestionUtils;

/**
 * Description: 
 * @author shenzulun
 * @date 2019-10-25
 * @version 1.0
 */
public class QuestionInitTask extends AbstractTask{

	public QuestionInitTask(String taskName) {
		super(taskName);
	}

	public void execute() {
		QuestionUtils.refreshCache();
	}

}
