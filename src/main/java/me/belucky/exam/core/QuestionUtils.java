/**
 * File Name: QuestionUtils.java
 * Date: 2019-5-5 下午04:37:14
 */
package me.belucky.exam.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.belucky.easytool.util.CacheUtils;
import me.belucky.easytool.util.FileTools;
import me.belucky.easytool.util.StringUtils;
import me.belucky.exam.dto.QueryCondDTO;
import me.belucky.exam.dto.QuestionAnswerDTO;
import me.belucky.exam.dto.QuestionTypeDTO;
import me.belucky.exam.dto.SelectOptionDTO;
import me.belucky.exam.dto.TestQuestionDTO;
import me.belucky.exam.model.CodeValue;
import me.belucky.exam.model.TestQueryCond;
import me.belucky.exam.model.TestQuestion;
import me.belucky.exam.model.TestQuestionRecord;
import me.belucky.exam.model.TestQuestionSelectOption;
import me.belucky.exam.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 功能说明: 题目工具类
 * @author shenzl
 * @date 2019-5-5
 * @version 1.0
 */
public class QuestionUtils {
	protected static Logger log = LoggerFactory.getLogger(QuestionUtils.class);
	public static final String TEST_QUESTIONS_MAP = "test_questions_map";
	
	/**
	 * 刷新缓存
	 */
	public static void refreshCache(){
		List<TestQuestionDTO> testQuestionList = null;
		int readType = PropKit.getInt("readType");
		if(readType == 1){
			//从试题txt中读取
			testQuestionList = parseFromTxt();
		}
		if(testQuestionList == null || testQuestionList.size() == 0 || testQuestionList.get(0) == null){
			//为空的话,则从数据库初始化试题信息
			testQuestionList = initFromDB();
		}else{
			//不为空且文件发生变化,则写入数据库
			int updateTestQuestionIntoDB = PropKit.getInt("UpdateTestQuestionIntoDB");
			if(updateTestQuestionIntoDB == 1){
				initSaveIntoDB(testQuestionList);
			}
		}
		List<TestQuestionDTO> testQuestionList1 = new ArrayList<TestQuestionDTO>();
		List<TestQuestionDTO> testQuestionList2 = new ArrayList<TestQuestionDTO>();
		List<TestQuestionDTO> testQuestionList3 = new ArrayList<TestQuestionDTO>();
		Map<String,TestQuestionDTO> cacheMap = new ConcurrentHashMap<String,TestQuestionDTO>();
		Map<String, TestQuestion> idMap = null;
		if(testQuestionList != null && testQuestionList.size() > 0){
			if(testQuestionList.get(0).getId() == 0){
				//判断主键ID是否为空
				List<TestQuestion> list = TestQuestion.dao.find("select * from t_question");
				idMap = new HashMap<String, TestQuestion>();
				for(TestQuestion testQ : list){
					idMap.put(testQ.getInt("question_type") + "_" + testQ.getInt("seq"), testQ);
				}
			}
		}
		for(TestQuestionDTO testDto : testQuestionList){
			int type = testDto.getType();
			String key = type + "_" + testDto.getSeq();
			if(idMap != null){
				testDto.setId(idMap.get(key).getInt("id"));
			}
			cacheMap.put(key, testDto);
			if(type == 1){
				testQuestionList1.add(testDto);
			}else if(type == 2){
				testQuestionList2.add(testDto);
			}else if(type == 3){
				testQuestionList3.add(testDto);
			}
		}
		CacheUtils.putCache("test_questions_1", testQuestionList1);
		CacheUtils.putCache("test_questions_2", testQuestionList2);
		CacheUtils.putCache("test_questions_3", testQuestionList3);
		CacheUtils.putCache(TEST_QUESTIONS_MAP, cacheMap);
		log.info("试题库加载成功...");
		//判断是否写入数据库
		
	}
	
	/**
	 * 获取试题类型
	 * @return
	 */
	public static List<QuestionTypeDTO> getQuestionTypes(){
		List<QuestionTypeDTO> result = new ArrayList<QuestionTypeDTO>();
		result.add(new QuestionTypeDTO("1","单选题"));
		result.add(new QuestionTypeDTO("2","多选题"));
		result.add(new QuestionTypeDTO("3","判断题"));
		result.add(new QuestionTypeDTO("4","错题簿"));
//		result.add(new QuestionTypeDTO("5","历史"));
		return result;
	}
	
	/**
	 * 根据试题顺序号查询试题
	 * @param seqno
	 * @return
	 */
	public static List<TestQuestionDTO> queryTestQuestionsBySeq(String userNo, String seqno, String questionType){
		Map<String,TestQuestionDTO> cacheMap = CacheUtils.getCache(TEST_QUESTIONS_MAP);
		List<TestQuestionDTO> list = null;
		if("4".equals(questionType)){
			//查询错题簿
			list = queryIncorrectQuestions(userNo);
			if(seqno == null || "".equals(seqno)){
				return list;
			}
		}else if("5".equals(questionType)){
			list = queryIncorrectQuestions(userNo);
			if(seqno == null || "".equals(seqno)){
				return list;
			}
		}else{
			list = CacheUtils.getCache("test_questions_" + questionType);
			if(seqno == null || "".equals(seqno)){
				seqno = new Random().nextInt(list.size()) + "";
			}
		}
		List<TestQuestionDTO> result = new ArrayList<TestQuestionDTO>();
		String[] arr1 = seqno.split(",");
		for(String s : arr1){
			int start = 0,end = 0;
			//判断是否随机
			if(s.startsWith("RANDOM_")){
				String[] arr2 = s.split("_");
				int[] randArr = CommonUtils.randomInt(list.size(), Integer.valueOf(arr2[1]));
				if("4".equals(questionType) || "5".equals(questionType)){
					//错题簿
					for(int r : randArr){
						result.add(list.get(r));
					}
				}else{
					for(int r : randArr){
						result.add(cacheMap.get(questionType + "_" + r));
					}
				}
				continue;
			}
			String[] arr2 = s.split("-");
			if(arr2.length == 2){
				start = Integer.valueOf(arr2[0]);
				end = Integer.valueOf(arr2[1]);
			}else if(arr2.length == 1){
				start = end = Integer.valueOf(s);
			}
			for(TestQuestionDTO testDto : list){
				if(testDto.getSeq() == null){
					log.info(testDto.getTitle());
					continue;
				}
				int seq = Integer.valueOf(testDto.getSeq());
				if(seq >= start && seq <= end){
					result.add(testDto);
				}
			}
		}
//		result = CommonUtils.query(list, "^" + seqno + "$", "seq");
		return result;
	}
	
	/**
	 * 查询数组中的指定数据的位置
	 * @param arr
	 * @param target
	 * @return
	 */
	public static int indexOfArray(String[] arr, String target){
		for(int i=0,len=arr.length;i<len;i++){
			if(arr[i].indexOf(target) != -1){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 根据文本解析试题
	 * @return
	 */
	private static List<TestQuestionDTO> parseFromTxt(){
		List<TestQuestionDTO> testQuestionList = new ArrayList<TestQuestionDTO>();
		String[] typeFlag = {"一、单选题","二、多选题","三、判断题"};
		String tkPath = PropKit.get("tk_path");
		boolean isAbsolutePath = (tkPath.indexOf(":") != -1);
		List<String> lines = FileTools.getContentList(tkPath,isAbsolutePath,"UTF-8");
		Pattern pattern = Pattern.compile("^[0-9]+、");
		int type = 0;
		TestQuestionDTO testDTO = null;
		int seq = 0;  //题目序号
		boolean isTitleLineBreaked = false;   //标题换行
		String tempTile = "";
		for(String line : lines){
			if(line == null || "".equals(line)){
				continue;
			}
			if(isTitleLineBreaked){
				line = tempTile + line;
			}
			int ind = indexOfArray(typeFlag,line);
			if(ind != -1){
				type = ind + 1;
				seq = 0;   //题目序号重置
				continue;
			}
			Matcher matcher = pattern.matcher(line);
			boolean isTitle = isTitleLineBreaked;
			if(!isTitleLineBreaked){
				while(matcher.find()){
					isTitle = true;
					break;
				}
			}
			if(isTitle){
				if(testDTO != null && testDTO.getTitle() != null){
					testQuestionList.add(testDTO);
					log.debug("type" + testDTO.getType() + ":" + testDTO.getTitle());
				}
				testDTO = new TestQuestionDTO();
				testDTO.setType(type);
				char[] arr1 = line.toCharArray();
				StringBuffer buff = new StringBuffer();
				int flag = 0;
				for(int i=arr1.length - 1;i>=0;i--){
					if(arr1[i] == ')' || arr1[i] == '）'){
						flag = 1;
						continue;
					}else if(arr1[i] == '(' || arr1[i] == '（'){
						flag = i;
						break;
					}
					if(flag == 1){
						buff.append(arr1[i]);
					}
				}
				if(flag == 0){
					//题目换行了
					tempTile = line;
					isTitleLineBreaked = true;
					continue;
				}else{
					tempTile = "";
					isTitleLineBreaked = false;
				}
				String seqStr = "";
				for(int i=0,len=arr1.length;i<len;i++){
					if(arr1[i] == '、'){
						break;
					}
					seqStr += arr1[i];
				}
				String title = line.substring(0, flag);
				String answer = buff.reverse().toString();
				log.debug(title);
				//题目
				testDTO.setTitle(title);
				//序号
				seq = Integer.valueOf(seqStr);
				testDTO.setSeq(seqStr);
				//判断试题类型
				if(type == 1){
					//单选
					testDTO.setSelectAnswer(answer);
				}else if(type == 2){
					//多选
					testDTO.setSelectAnswer(answer);
				}else if(type == 3){
					//判断
					testDTO.setJudgeAnswer("√".equals(answer));
				}
			}else{
				isTitleLineBreaked = false;
				//非标题
				if(seq != 0){
					//解析选项
					if(type == 1 || type == 2){
						List<SelectOptionDTO> options = new ArrayList<SelectOptionDTO>();
						int flag = 0;
						StringBuffer buff = new StringBuffer();
						char[] arr1 = line.toCharArray();
						for(int i=0,len=arr1.length;i<len;i++){
							if(arr1[i] >= 'A' && arr1[i] <= 'Z' && arr1[i+1] == '、'){
								//适配以下类型
								//A、三年  	B、永久  	C、十五年  	D、五年
								//选项start
								if(flag == 0){
									
								}else{
									//遇到新的选项									
									options.add(parse(buff.toString().trim()));
									buff = new StringBuffer();
								}
								buff.append(arr1[i]).append("、");
								i++;
								flag = 1;
								continue;
							}else if(arr1[i] >= 'A' && arr1[i] <= 'Z' && arr1[i+1] != '、' && (i ==0 || arr1[i-1] == ' ' || arr1[i-1] == '	')){
								//适配以下类型
								/**
								 * 	A购买的普通宣传品取得的增值税专用发票  	
								 *  B购买购物卡取得增值税专用发票  	
								 *C购买的农产品取得的增值税专用发票、农产品收购发票  	
								 *D购买加油卡无法取得增值税专用发票  
								 */
								if(flag == 0){
									
								}else{
									//遇到新的选项									
									options.add(parse(buff.toString().trim()));
									buff = new StringBuffer();
								}
								buff.append(arr1[i]).append("、");
								flag = 1;
								continue;
							}{
								buff.append(arr1[i]);
							}
						}
						options.add(parse(buff.toString().trim()));
						List<SelectOptionDTO> selects = testDTO.getSelectOptions();
						if(selects != null){
							selects.addAll(options);
							testDTO.setSelectOptions(selects);
						}else{
							testDTO.setSelectOptions(options);
						}
					}
				}
			}
		}
		testQuestionList.add(testDTO);
		return testQuestionList;
	}
	
	/**
	 * 解析选择题的选项
	 * @param desc
	 * @return
	 */
	static SelectOptionDTO parse(String desc){
		String[] arr = desc.split("、");
		return new SelectOptionDTO(arr[0],desc);
	}
	
	/**
	 * 提交试题,检查答案
	 * @param checkAnswers
	 * @return
	 */
	public static List<QuestionAnswerDTO> submitAnswers(String userNo, String checkAnswers){
		List<QuestionAnswerDTO> questionAnswersList = new ArrayList<QuestionAnswerDTO>();
		Map<String,TestQuestionDTO> cacheMap = CacheUtils.getCache(TEST_QUESTIONS_MAP);
		//由于存在多选题,需先合并试题选项
		Map<String, String> optionMap = new HashMap<String, String>();
		String[] answers = checkAnswers.split(",");
		for(String answer : answers){
			//格式如：Q_1_1_A
			//第1位：固定Q,第二位：试题类型,第三位:试题序号,选项
			String[] arr = answer.split("_");
			String key = arr[1] + "_" + arr[2];
			if(optionMap.containsKey(key)){
				String v = optionMap.get(key);
				optionMap.put(key, v + arr[3]);
			}else{
				optionMap.put(key, arr[3]);
			}
		}
		//检查答案是否正确
		Set<String> keys = optionMap.keySet();
		for(String key : keys){
			QuestionAnswerDTO ansDto = new QuestionAnswerDTO();
			//试题ID
			ansDto.setQuestionId(key);
			ansDto.setTestQuestionDTO(cacheMap.get(key));
			ansDto.setUserNo(userNo);
			//试题选择的选项
			String selectOption = optionMap.get(key);
			ansDto.setSelectOption(selectOption);
			TestQuestionDTO testDto = cacheMap.get(key);
			if(testDto.getType() == 1 || testDto.getType() == 2){
				//选择题
				//正确答案
				String correctAnswer = cacheMap.get(key).getSelectAnswer();
				ansDto.setCorrectAnswer(correctAnswer);
				ansDto.setCorrect(StringUtils.compareTwoString(correctAnswer, selectOption));
			}else if(testDto.getType() == 3){
				//判断题
				boolean correctAnswer = cacheMap.get(key).isJudgeAnswer();
				String correctAnswerStr = correctAnswer ? "1" : "0";
				ansDto.setCorrectAnswer(correctAnswerStr);
				ansDto.setCorrect(correctAnswerStr.equals(selectOption));
			}
			questionAnswersList.add(ansDto);
		}
		//记录到数据库
		saveTestQuestionRecord(questionAnswersList);
		return questionAnswersList;
	}
	
	/**
	 * 从数据库读取试题信息
	 * @return
	 */
	private static List<TestQuestionDTO> initFromDB(){
		List<TestQuestionDTO> testQuestionList = new ArrayList<TestQuestionDTO>();
		List<TestQuestion> list = TestQuestion.dao.find("select t1.*,t2.select_key,t2.select_desc from t_question t1 left join t_question_select_option t2 on t1.id=t2.question_id order by t1.question_type,t1.seq asc");
		String lastKey = "";
		TestQuestionDTO tmp = null;
		for(TestQuestion testQ : list){
			String key = testQ.getInt("question_type") + "_" + testQ.getInt("seq");
			if(!lastKey.equals(key)){
				//和上一个key不同,说明出现新的试题
				lastKey = key;
				if(tmp != null){
					testQuestionList.add(tmp);
				}
				//初始化新的试题
				TestQuestionDTO t = new TestQuestionDTO();
				t.setId(testQ.getInt("id"));
				t.setType(testQ.getInt("question_type"));
				t.setSeq(testQ.getInt("seq") + "");
				t.setTitle(testQ.getStr("title"));
				t.setSelectAnswer(testQ.getStr("select_answer"));
				if(testQ.getInt("question_type") == 1 || testQ.getInt("question_type") == 2){
					//选择题
					List<SelectOptionDTO> selectOptions = new ArrayList<SelectOptionDTO>();
					selectOptions.add(new SelectOptionDTO(testQ.getStr("select_key"),testQ.getStr("select_desc")));
					t.setSelectOptions(selectOptions);
				}else if(testQ.getInt("question_type") == 3){
					//判断题
					t.setJudgeAnswer("1".equals(testQ.getStr("select_answer")));
				}
				tmp = t;
			}else{
				//否则,则是同一个试题,新的选择题选项
				List<SelectOptionDTO> selectOptions = tmp.getSelectOptions();
				selectOptions.add(new SelectOptionDTO(testQ.getStr("select_key"),testQ.getStr("select_desc")));
			}
		}
		if(tmp != null){
			testQuestionList.add(tmp);
		}
		return testQuestionList;
	}
	
	/**
	 * 持久化试题到数据库
	 * @param testQuestionList
	 */
	@Before(Tx.class)
	private static void initSaveIntoDB(List<TestQuestionDTO> testQuestionList){
		boolean isChange = true;
		CodeValue dto = CodeValue.dao.findFirst("select * from T_CODE_VALUE where code=?", "last_modify_str");
		String lastModifyStr = dto.get("value_desc");
		File file = new File("C:/Users/tzbank/Desktop/工作记录/20190419/tk_2017.txt");
		long lastModified = 0L;
		if(file.exists()){
			lastModified = file.lastModified();
			long diff = Long.valueOf(lastModifyStr) - lastModified;
			if(Math.abs(diff) < 1000){
				//文件发生变化
				isChange = true;
			}
		}
		if(isChange && testQuestionList != null && testQuestionList.size() > 0){
			//持久化
			//先清空
			Db.update("delete from t_question where 1=1");
			Db.update("delete from t_question_select_option where 1=1");
			
			List<TestQuestion> TestQuestionList = new ArrayList<TestQuestion>();
			
			for(TestQuestionDTO testDto : testQuestionList){
				TestQuestion t = new TestQuestion().set("question_type", testDto.getType())
								  .set("seq", Integer.valueOf(testDto.getSeq()))
								  .set("title", testDto.getTitle())
								  .set("select_answer", testDto.getSelectAnswer());
				if(testDto.getType() == 3){
					//判断题
					t.set("select_answer", testDto.isJudgeAnswer() ? "1" : "0");
				}
				TestQuestionList.add(t);
			}
			//批量插入
			Db.batchSave(TestQuestionList, 200);
			List<TestQuestion> list = TestQuestion.dao.find("select * from t_question");
			Map<String, TestQuestion> map = new HashMap<String, TestQuestion>();
			for(TestQuestion testQ : list){
				map.put(testQ.getInt("question_type") + "_" + testQ.getInt("seq"), testQ);
			}
			
			List<TestQuestionSelectOption> TestQuestionSelectOptionList = new ArrayList<TestQuestionSelectOption>();
			for(TestQuestionDTO testDto : testQuestionList){
//				TestQuestion testQuestion = TestQuestion.dao.findFirst("select * from t_question where question_type=? and seq=?", 
//													testDto.getType(),Integer.valueOf(testDto.getSeq()));
				String key = testDto.getType() + "_" + testDto.getSeq();
				if(!map.containsKey(key)){
					log.error(key);
					continue;
				}
				if(testDto.getType() == 1 || testDto.getType() == 2){
					//选择题
					List<SelectOptionDTO> selectOptions = testDto.getSelectOptions();
					if(selectOptions == null){
						log.error("选择题选项为空,请检查,key:{}",key);
						continue;
					}
					for(SelectOptionDTO selectOption : selectOptions){
						TestQuestionSelectOption t = new TestQuestionSelectOption().set("question_id", map.get(key).getInt("id"))
													  .set("select_key", selectOption.getKey())
													  .set("select_desc", selectOption.getDesc());
						TestQuestionSelectOptionList.add(t);
					}
				}
			}
			Db.batchSave(TestQuestionSelectOptionList, 200);
			
			//更新时间戳
			Db.update("update t_code_value set value_desc = ? where CODE = 'last_modify_str'", lastModified + "");
			log.info("试题信息持久化成功...");
		}
	}	
	
	/**
	 * 保存做题记录到数据库
	 * @param questionAnswersList
	 */
	private static void saveTestQuestionRecord(List<QuestionAnswerDTO> questionAnswersList){
		List<TestQuestionRecord> TestQuestionRecordList = new ArrayList<TestQuestionRecord>();
		for(QuestionAnswerDTO questionAnswer : questionAnswersList){
			TestQuestionDTO testQuestionDto = questionAnswer.getTestQuestionDTO();
			TestQuestionRecord t = new TestQuestionRecord().set("question_id", testQuestionDto.getId())
											               .set("question_type", testQuestionDto.getType())
											               .set("seq", Integer.valueOf(testQuestionDto.getSeq()))
											               .set("select_option", questionAnswer.getSelectOption())
											               .set("correct_option", questionAnswer.getCorrectAnswer())
											               .set("correct_flag", questionAnswer.isCorrect() ? 1 : 0)
											               .set("user_no", questionAnswer.getUserNo());
			TestQuestionRecordList.add(t);
		}
		Db.batchSave(TestQuestionRecordList, 200);
		log.info("做题记录保存成功...");
	}
	
	/**
	 * 从数据库读取错题登记簿
	 * @return
	 */
	private static List<TestQuestionDTO> queryIncorrectQuestions(String userNo){
		List<TestQuestionDTO> testQuestionList = new ArrayList<TestQuestionDTO>();
		StringBuffer buff = new StringBuffer("select t2.*,t3.select_key,t3.select_desc,t1.select_option,datetime(t1.create_dt) as 'create_dt' ")
									 .append(" from t_question_record t1 left join t_question t2 on t1.question_id = t2.id left join t_question_select_option t3 on t1.question_id = t3.question_id ")
									 .append(" where t1.correct_flag=0 and user_no=? order by t1.create_dt desc");
//		List<TestQuestion> list = TestQuestion.dao.find("select t2.*,t3.select_key,t3.select_desc,t1.select_option,datetime(t1.create_dt) as 'create_dt' from t_question_record t1 left join t_question t2 on t1.question_id = t2.id left join t_question_select_option t3 on t1.question_id = t3.question_id where t1.correct_flag=0 order by t1.create_dt desc");
		List<TestQuestion> list = TestQuestion.dao.find(buff.toString(),userNo);
		String lastKey = "";
		TestQuestionDTO tmp = null;
		for(TestQuestion testQ : list){
			String key = testQ.getInt("question_type") + "_" + testQ.getInt("seq");
			if(!lastKey.equals(key)){
				//和上一个key不同,说明出现新的试题
				lastKey = key;
				if(tmp != null){
					testQuestionList.add(tmp);
				}
				//初始化新的试题
				TestQuestionDTO t = new TestQuestionDTO();
				t.setId(testQ.getInt("id"));
				t.setType(testQ.getInt("question_type"));
				t.setSeq(testQ.getInt("seq") + "");
				t.setTitle(testQ.getStr("title"));
				t.setSelectAnswer(testQ.getStr("select_answer"));
				t.setSelectChoose(testQ.getStr("select_option"));
				t.setCreateDtStr(testQ.getStr("create_dt"));
				if(testQ.getInt("question_type") == 1 || testQ.getInt("question_type") == 2){
					//选择题
					List<SelectOptionDTO> selectOptions = new ArrayList<SelectOptionDTO>();
					selectOptions.add(new SelectOptionDTO(testQ.getStr("select_key"),testQ.getStr("select_desc")));
					t.setSelectOptions(selectOptions);
				}else if(testQ.getInt("question_type") == 3){
					//判断题
					t.setJudgeAnswer("1".equals(testQ.getStr("select_answer")));
				}
				tmp = t;
			}else{
				//否则,则是同一个试题,新的选择题选项
				List<SelectOptionDTO> selectOptions = tmp.getSelectOptions();
				selectOptions.add(new SelectOptionDTO(testQ.getStr("select_key"),testQ.getStr("select_desc")));
			}
		}
		if(tmp != null){
			testQuestionList.add(tmp);
		}
		return testQuestionList;
	}
	
	/**
	 * 获取该IP最近一次的查询条件
	 * @param ip
	 * @return
	 */
	public static QueryCondDTO getRecentQueryValue(String ip){
		QueryCondDTO queryCond = new QueryCondDTO("1","1");
		if(ip != null && !"".equals(ip)){
			TestQueryCond dto = TestQueryCond.dao.findFirst("select * from T_QUERY_COND where ip=?", ip);
			if(dto != null && dto.get("ip") != null){
				queryCond.setQuestionType(dto.getStr("question_type"));
				queryCond.setQueryValue(dto.getStr("query_value"));
			}
		}
		return queryCond;
	}
	
	/**
	 * 记录查询条件
	 * @param seqno
	 * @param questionType
	 */
	public static void saveQueryCond(String ip, QueryCondDTO queryCond){
		if(ip != null && !"".equals(ip)){
			TestQueryCond dto = TestQueryCond.dao.findFirst("select * from T_QUERY_COND where ip=?", ip);
			if(dto != null && dto.get("ip") != null){
				//存在则更新
				dto.set("question_type", queryCond.getQuestionType()).set("query_value", queryCond.getQueryValue()).update();
			}else{
				//没有则新增
				new TestQueryCond().set("ip", ip)
								   .set("question_type", queryCond.getQuestionType())
								   .set("query_value", queryCond.getQueryValue())
								   .save();
			}
		}
	}
	
	public static void main(String[] args) {
		QuestionUtils.parseFromTxt(); 
	}

}
