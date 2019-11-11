/**
 * File Name: XczxExamInitTask.java
 * Date: 2019-10-25 14:59:07
 */
package me.belucky.exam.core.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import jxl.read.biff.BiffException;
import me.belucky.easytool.task.AbstractTask;
import me.belucky.easytool.util.StringUtils;
import me.belucky.exam.model.table.TQuestion;
import me.belucky.exam.model.table.TQuestionSelectOption;
import me.belucky.exam.util.XLSData;


/**
 * Description: 乡村振兴考试的EXCEL初始化类
 * @author shenzulun
 * @date 2019-10-25
 * @version 1.0
 */
public class XczxExamInitTask extends AbstractTask{
	/**
	 * 乡村振兴考试
	 */
	public static final int EXAM_ID = 2;

	public XczxExamInitTask(String taskName) {
		super(taskName);
	}

	public void execute() {
		String filePath = PropKit.get("exam_xczx_path");
		FileInputStream fis = null;
		try {
			List<TQuestion> questionList = new ArrayList<TQuestion>();
			List<TQuestionSelectOption> selectList = new ArrayList<TQuestionSelectOption>();
			String[] selectArr = {"A", "B", "C", "D"};
			File file = new File(filePath);
			fis = new FileInputStream(file);
			XLSData xlsd = new XLSData(fis);  
			int sheetSize = xlsd.getBook().getNumberOfSheets();
			for(int i=0;i<sheetSize;i++) {
				List<String[]> dataList = xlsd.getDataList(i);
				String sheetName = xlsd.getSheetName();
				int sheetType = -1;
				switch(sheetName) {
					case "单选题": sheetType=1;break;
					case "多选题": sheetType=3;break;
					case "判断题": sheetType=2;break;
				}
				for(int j=1,len=dataList.size();j<len;j++){
					String[] row = dataList.get(j);
					if(StringUtils.isNull(row[0])) {
						continue;
					}
					if(sheetType == 1) {
						/**
						 * 单选题
						 * A	B			C	D	E	F	G
						 * 序号	问题描述		A	B	C	D	答案
						 */
						TQuestion t = new TQuestion();
						t.setExamId(EXAM_ID);			
						t.setQuestionType(1);
						t.setTitle(row[1]);
						t.setSeq(Integer.parseInt(row[0]));
						t.setSelectAnswer(row[6]);
						questionList.add(t);
						//选项
						for(int k=0;k<=3;k++) {
							TQuestionSelectOption s1 = new TQuestionSelectOption();
							s1.setSelectKey(selectArr[k]);
							s1.setSelectDesc(row[k + 2]);
							s1.setId(1);
							s1.setQuestionId(Integer.parseInt(row[0]));
//							s1.put("flag_key", "1_" + row[0]);
							selectList.add(s1);
						}
					}else if(sheetType == 2) {
						TQuestion t = new TQuestion();
						t.setExamId(EXAM_ID);
						t.setQuestionType(3);
						t.setTitle(row[1]);
						t.setSeq(Integer.parseInt(row[0]));
						t.setSelectAnswer("T".equals(row[2]) ? "1" : "0");
						questionList.add(t);
					}else if(sheetType == 3) {
						TQuestion t = new TQuestion();
						t.setExamId(EXAM_ID);
						t.setQuestionType(2);
						t.setTitle(row[1]);
						t.setSeq(Integer.parseInt(row[0]));
						t.setSelectAnswer(row[6].replaceAll(",", "").replaceAll(" ", ""));
						questionList.add(t);
						//选项
						for(int k=0;k<=3;k++) {
							TQuestionSelectOption s1 = new TQuestionSelectOption();
							s1.setSelectKey(selectArr[k]);
							s1.setSelectDesc(row[k + 2]);
							s1.setId(2);
							s1.setQuestionId(Integer.parseInt(row[0]));
//							s1.put("flag_key", "2_" + row[0]);
							selectList.add(s1);
						}
					}
				}
			}
			//持久化试题
			//先清空
			Db.update("delete from t_question_select_option where question_id in (select id from t_question where exam_id=?)", EXAM_ID);
			Db.update("delete from t_question where exam_id=?", EXAM_ID);
			//再保存
			Db.batchSave(questionList, 200);
			//获取主键
			List<TQuestion> list = TQuestion.dao.findAll();
			Map<String, TQuestion> map = new HashMap<String, TQuestion>();
			for(TQuestion testQ : list){
				map.put(testQ.getQuestionType() + "_" + testQ.getInt("seq"), testQ);
			}
			for(TQuestionSelectOption select : selectList) {
				TQuestion t = map.get(select.getId() + "_" + select.getQuestionId());
				select.setQuestionId(t.getId());
				select.remove("id");
//				select.put("flag_key", null);
			}
			Db.batchSave(selectList, 200);
		} catch (FileNotFoundException e) {
			log.error("",e);
		} catch (BiffException e) {
			log.error("",e);
		} catch (IOException e) {
			log.error("",e);
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("",e);
				}
			}
		}
	}

}
