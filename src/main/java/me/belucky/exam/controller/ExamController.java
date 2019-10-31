/**
 * File Name: ExamController.java
 * Date: 2019-10-30 18:19:43
 */
package me.belucky.exam.controller;

import java.util.List;

import me.belucky.easytool.dto.MsgDTO;
import me.belucky.easytool.jfinal.JsonLogController;
import me.belucky.easytool.util.StringUtils;
import me.belucky.exam.core.QuestionUtils;
import me.belucky.exam.dto.QueryCondDTO;
import me.belucky.exam.dto.QuestionAnswerDTO;
import me.belucky.exam.dto.QuestionTypeDTO;
import me.belucky.exam.dto.TestQuestionDTO;
import me.belucky.exam.model.table.TExam;

/**
 * Description: 考试控制器
 * @author shenzulun
 * @date 2019-10-30
 * @version 1.0
 */
public class ExamController extends JsonLogController{

	public Class<?> getBeanClass() {
		return MsgDTO.class;
	}

	public void go(Object dto, String methodName) {
		invoke(this, methodName, dto);
	}
	
	public void index() {
		String url = getPara(0);
		TExam exam = QuestionUtils.getExamByUrl(url);
		if(StringUtils.isBlank(url)) {
			redirect301("/");
			return;
		}
		List<QuestionTypeDTO> result = QuestionUtils.getQuestionTypes();
		//setAttr("firstTabId",result.get(1).getTypeId());
		setAttr("tab",result);
		String ip = super.getRequest().getRemoteAddr();
		QueryCondDTO queryCond = QuestionUtils.getRecentQueryValue(ip);
		setAttr("firstTabId",queryCond.getQuestionType());
		setAttr("exam", exam);
		setAttr("recentQueryValue",queryCond.getQueryValue());
		render("_exam_index.html");
	}
	
	/**
	 * 查询试题
	 */
	public void listQuestions(){
		String queryValue = getPara("queryValue");
		String questionType = getPara("questionType");
		int examId = getParaToInt("examId");
		log.info("开始查询试题库,输入条件: {}", queryValue);
		String ip = super.getRequest().getRemoteAddr();
		QueryCondDTO queryCond = new QueryCondDTO(questionType, queryValue, examId, ip);
		List<TestQuestionDTO> result = QuestionUtils.queryTestQuestionsBySeq(queryCond);
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
		int examId = getParaToInt("examId");
		String ip = super.getRequest().getRemoteAddr();
		QueryCondDTO queryCond = new QueryCondDTO(null, checkAnswers, examId, ip);
		List<QuestionAnswerDTO> questionAnswersList = QuestionUtils.submitAnswers(queryCond);
		renderJson(questionAnswersList);
		log.info("条件[{}]执行完成", checkAnswers);
	}

}
