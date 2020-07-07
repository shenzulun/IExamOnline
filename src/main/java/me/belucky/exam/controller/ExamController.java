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
		if(StringUtils.isBlank(url)) {
			redirect301("/");
			return;
		}
		TExam exam = QuestionUtils.getExamByUrl(url);
		String testFlag = getPara("test");
		if(testFlag == null) {
			List<QuestionTypeDTO> result = QuestionUtils.getQuestionTypes();
			//setAttr("firstTabId",result.get(1).getTypeId());
			setAttr("tab",result);
			String ip = super.getRequest().getRemoteAddr();
			QueryCondDTO queryCond = QuestionUtils.getRecentQueryValue(ip);
			setAttr("firstTabId",queryCond.getQuestionType());
			setAttr("exam", exam);
			setAttr("recentQueryValue",queryCond.getQueryValue());
			render("_exam_index.html");
		}else {
			setAttr("exam", exam);
			//生成题库
			//1. 单选
			QueryCondDTO queryCond1 = new QueryCondDTO("1", "RANDOM_50", exam.getId(), null);
			List<TestQuestionDTO> result1 = QuestionUtils.queryTestQuestionsBySeq(queryCond1);
			//2. 多选
			QueryCondDTO queryCond2 = new QueryCondDTO("2", "RANDOM_20", exam.getId(), null);
			List<TestQuestionDTO> result2 = QuestionUtils.queryTestQuestionsBySeq(queryCond2);
			//3. 判断
			QueryCondDTO queryCond3 = new QueryCondDTO("3", "RANDOM_30", exam.getId(), null);
			List<TestQuestionDTO> result3 = QuestionUtils.queryTestQuestionsBySeq(queryCond3);
			setAttr("examList1", result1);
			setAttr("examList2", result2);
			setAttr("examList3", result3);
			render("_exam_test.html");
		}
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
