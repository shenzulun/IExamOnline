/**
 * File Name: WgxwExamInitTask.java
 * Date: 2020-04-13 16:24:10
 */
package me.belucky.exam.core.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jfinal.plugin.activerecord.Db;
import me.belucky.easytool.task.AbstractTask;
import me.belucky.easytool.util.FileTools;
import me.belucky.exam.model.table.TQuestion;
import me.belucky.exam.model.table.TQuestionSelectOption;
import me.belucky.exam.util.CommonUtils;

/**
 * Description: 
 * @author shenzulun
 * @date 2020-04-13
 * @version 1.0
 */
public class WgxwExamInitTask extends AbstractTask{
	/**
	 * 农信违规行为处理办法考试
	 */
	public static final int EXAM_ID = 3;

	public WgxwExamInitTask(String taskName) {
		super(taskName);
	}

	public void execute() {
		String filePath = "E:/学习平台/需求/法律合规部-农信违规试题库/tk.txt";
		List<TQuestion> questionList = new ArrayList<TQuestion>();
		List<TQuestionSelectOption> selectList = new ArrayList<TQuestionSelectOption>();
		String[] selectArr = {"A", "B", "C", "D"};
		String[] typeFlag = {"二、选择题","二、多选题","一、判断题"};
		List<String> list = FileTools.getContentList(filePath, true, "UTF-8");
		
		Pattern pattern = Pattern.compile("^[0-9]+、");
		int type = 0;
		TQuestion testDTO = null;
		int seq = 0;  //题目序号
		boolean isTitleLineBreaked = false;   //标题换行
		String tempTile = "";
		for(String line : list){
			if(line == null || "".equals(line)){
				continue;
			}
			if(isTitleLineBreaked){
				line = tempTile + line;
			}
			int ind = CommonUtils.indexOfArray(typeFlag,line);
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
					questionList.add(testDTO);
//					log.debug("type" + testDTO.getType() + ":" + testDTO.getTitle());
				}
				testDTO = new TQuestion();
				testDTO.setQuestionType(type);
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
				testDTO.setSeq(seq);
				testDTO.setExamId(EXAM_ID);
				//判断试题类型
				if(type == 1){
					//单选
					testDTO.setSelectAnswer(answer);
				}else if(type == 2){
					//多选
					testDTO.setSelectAnswer(answer);
				}else if(type == 3){
					//判断
					testDTO.setSelectAnswer("√".equals(answer) ? "1" : "0");
				}
			}else{
				isTitleLineBreaked = false;
				//非标题
				if(seq != 0){
					//解析选项
					if(type == 1 || type == 2){
						List<TQuestionSelectOption> options = new ArrayList<TQuestionSelectOption>();
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
									String desc = buff.toString().trim();
									String[] arr = desc.split("、");
									TQuestionSelectOption tso = new TQuestionSelectOption();
									tso.setSelectKey(arr[0]);
									tso.setSelectDesc(desc);
									tso.setId(type);
									tso.setQuestionId(seq);
									options.add(tso);
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
									String desc = buff.toString().trim();
									String[] arr = desc.split("、");
									TQuestionSelectOption tso = new TQuestionSelectOption();
									tso.setSelectKey(arr[0]);
									tso.setSelectDesc(desc);
									tso.setId(type);
									tso.setQuestionId(seq);
									options.add(tso);
									buff = new StringBuffer();
								}
								buff.append(arr1[i]).append("、");
								flag = 1;
								continue;
							}{
								buff.append(arr1[i]);
							}
						}
						String desc = buff.toString().trim();
						String[] arr = desc.split("、");
						TQuestionSelectOption tso = new TQuestionSelectOption();
						tso.setSelectKey(arr[0]);
						tso.setSelectDesc(desc);
						tso.setId(type);
						tso.setQuestionId(seq);
						options.add(tso);
						selectList.addAll(options);
					}
				}
			}
		}
		
		//最后一题
		if(testDTO != null && testDTO.getTitle() != null) {
			questionList.add(testDTO);
		}
		
		//持久化试题
		//先清空
		Db.update("delete from t_question_select_option where question_id in (select id from t_question where exam_id=?)", EXAM_ID);
		Db.update("delete from t_question where exam_id=?", EXAM_ID);
		//再保存
		Db.batchSave(questionList, 200);
		//获取主键
		List<TQuestion> list1 = TQuestion.dao.findAll();
		Map<String, TQuestion> map = new HashMap<String, TQuestion>();
		for(TQuestion testQ : list1){
			map.put(testQ.getQuestionType() + "_" + testQ.getInt("seq"), testQ);
		}
		for(TQuestionSelectOption select : selectList) {
			TQuestion t = map.get(select.getId() + "_" + select.getQuestionId());
			select.setQuestionId(t.getId());
			select.remove("id");
//			select.put("flag_key", null);
		}
		Db.batchSave(selectList, 200);
		
	}

}
