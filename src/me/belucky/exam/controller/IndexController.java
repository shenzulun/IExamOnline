/**
 * File Name: IndexController.java
 * Date: 2016-12-23 04:37:49
 */
package me.belucky.exam.controller;

import java.util.List;
import me.belucky.exam.core.QuestionUtils;
import me.belucky.exam.dto.QueryCondDTO;
import me.belucky.exam.dto.QuestionAnswerDTO;
import me.belucky.exam.dto.QuestionTypeDTO;
import me.belucky.exam.dto.TestQuestionDTO;

/**
 * 功能说明: 首页控制器
 * @author shenzl
 * @date 2016-12-23
 * @version 1.0
 */
public class IndexController extends JsonLogController{

	public void go(Object dto, String methodName) {
		invoke(this, methodName, dto);
	}

	public Class<?> setObj() {
		return null;
	}

	public void index() {
		List<QuestionTypeDTO> result = QuestionUtils.getQuestionTypes();
		//setAttr("firstTabId",result.get(1).getTypeId());
		setAttr("tab",result);
		String ip = super.getRequest().getRemoteAddr();
		QueryCondDTO queryCond = QuestionUtils.getRecentQueryValue(ip);
		setAttr("firstTabId",queryCond.getQuestionType());
		setAttr("recentQueryValue",queryCond.getQueryValue());
		render("index.html");
	}
	
	/**
	 * 查询试题
	 */
	public void listQuestions(){
		String queryValue = getPara("queryValue");
		String questionType = getPara("questionType");
		log.info("开始查询试题库,输入条件: {}", queryValue);
		String ip = super.getRequest().getRemoteAddr();
		List<TestQuestionDTO> result = QuestionUtils.queryTestQuestionsBySeq(ip, queryValue, questionType);
		QuestionUtils.saveQueryCond(ip, new QueryCondDTO(questionType,queryValue));
		renderJson(result);
		log.info("条件[{}]查询完成", queryValue);
	}
	
	/**
	 * 提交试题答案,返回解答结果
	 */
	public void submitAnswer(){
		String checkAnswers = getPara("checkAnswers");
		log.info("开始检查试题,输入条件: {}", checkAnswers);
		String ip = super.getRequest().getRemoteAddr();
		List<QuestionAnswerDTO> questionAnswersList = QuestionUtils.submitAnswers(ip, checkAnswers);
		renderJson(questionAnswersList);
		log.info("条件[{}]执行完成", checkAnswers);
	}
}
