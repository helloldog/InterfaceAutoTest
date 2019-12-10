package com.parking_1x;

import com.google.gson.JsonObject;
import com.parking_1x.utils.*;

import groovy.json.JsonParser;
import io.restassured.RestAssured;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.config.ConnectionConfig.connectionConfig;
import static java.lang.Thread.sleep;

/**
 * Created by weijl on 2019-5-28.
 */
public class Runjar {
    public static Logger logger = Logger.getLogger(Runjar.class);
    static String sessionid = "";
    static String ishavedbpostgres=BaseLib.getPropertyString("is.have.db");
    static String ishavedbsimulator=BaseLib.getPropertyString("is.have.dbsimulator");
    public static void Runjar(String caseNo, String testPoit, String preResult, String path, String isUploadPic, String DevORFront, String bodyString) throws Exception {
        //数据初始化
        bodyString=DataInitialize.bodyString(bodyString);
        preResult=DataInitialize.preResult(preResult);
        path=DataInitialize.path(path);
        if (DevORFront.contains("DEV")) {
            RestAssured.baseURI = BaseLib.getPropertyString("test.server.url");
            RestAssured.port = Integer.parseInt(BaseLib.getPropertyString("test.server.port"));
            BasePost.Postdev(caseNo, testPoit, path, isUploadPic, bodyString, preResult);
        } else if (DevORFront.contains("FRO")) {
            RestAssured.baseURI = BaseLib.getPropertyString("test.web.url");
            RestAssured.port = Integer.parseInt(BaseLib.getPropertyString("test.web.port"));
            BasePost.Postfront(caseNo, testPoit, path, bodyString, sessionid, preResult);
        } else if (DevORFront.contains("PGSQL")&&ishavedbsimulator.equals("true")) {
            BasePost.selfromDB(caseNo, testPoit, path, bodyString, preResult);
        }
//        RestAssured.config = RestAssured.config()
//                .connectionConfig(connectionConfig().closeIdleConnectionsAfterEachResponse());
        if (DevORFront.contains("WAIT")) {
            logger.info("等待" + path + "秒");
            int waitTime=Integer.parseInt(path);
            for(int i=5;i<=waitTime;i=i+5){
                sleep( 5 * 1000);
                logger.info(i+"/"+waitTime);
            }
            logger.info("等待完成");
        }
        if (DevORFront.contains("CLEAR")&&ishavedbsimulator.equals("true")) {
            String sql_delete_simulator = BaseLib.readFile("./sql/delete_simulator.sql");
            BaseLib.sqloperationsimulator(sql_delete_simulator);
        }
//写入bodyString到缓存
        if (bodyString.equals("")||bodyString.isEmpty()) {
            BaseLib.AddList("{\"bodyList\":\"这是个空的\"}");

        } else {
            BaseLib.AddList(bodyString);
        }


    }

    public static void main(String[] args) throws Exception {

        //日志配置文件初始化
        PropertyConfigurator.configure("./log4j.properties");
        //用例初始化
        ArrayList bodyList ;
        bodyList=BaseLib.getFileList("./testCases");
        String sql_delete_mydata = BaseLib.readFile("./sql/delete_mydata.sql");
        String sql_delete_simulator = BaseLib.readFile("./sql/delete_simulator.sql");
        String sql_delete_base = BaseLib.readFile("./sql/delete_base_sql.sql");
        if(ishavedbpostgres.equals("true")){
            BaseLib.sqloperation(sql_delete_base);
            BaseLib.sqloperation(sql_delete_mydata);
        }
        if(ishavedbsimulator.equals("true")){
            BaseLib.sqloperationsimulator(sql_delete_simulator);
        }
        //登录，获取sessionid
        sessionid = BaseLib.getsess(BaseLib.getPropertyString("test.usrname"), BaseLib.getPropertyString("test.password"));
        //导入必要的停车区和设备
        BaseLib.inputDevice("./excelData/myTestdevice.xlsx", sessionid);
        sleep(2000);
        for(int j=0;j<bodyList.size();j++){
            String TestCaseFileName=bodyList.get(j).toString();
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss-");
            String nowDate = sf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(nowDate));
            String orderDate = sf.format(cal.getTime());
            String filePath = "./testCases/"+TestCaseFileName;
            //测试案例相对路径
            HTMLReport HR =new HTMLReport();
            HR.HTMLLogWriteStart(orderDate+TestCaseFileName.split("\\.xls")[0]+"-自动用例报告");
            logger.info("测试地址是：" + BaseLib.getPropertyString("test.server.url") + ":" + BaseLib.getPropertyString("test.server.port"));
            logger.info("~~~~~~~~~~~开始"+TestCaseFileName+"测试~~~~~~~~~~~~");
            Object[][] cases = ReadExcelCases.readCases(filePath);
            for (int i = 0; i < cases.length; i++) {
                logger.info("~~~~~~~~~~~第" + (i + 1) + "/" + cases.length +
                        "个Case：~~~~~~~~~~~~");
                Runjar((String) cases[i][0], (String) cases[i][1], (String) cases[i][2], (String) cases[i][3], (String) cases[i][4], (String) cases[i][5], (String) cases[i][6]);
            }
            HR.HTMLLogWriteclose();
            logger.info("~~~~~~~~~~~~"+TestCaseFileName+"测试完成~~~~~~~~~~~~");
            BaseLib.getListClear();
        }

        if(ishavedbpostgres.equals("true")){
            BaseLib.sqloperation(sql_delete_base);
            BaseLib.sqloperation(sql_delete_mydata);
        }
        if(ishavedbsimulator.equals("true")){
            BaseLib.sqloperationsimulator(sql_delete_simulator);
        }

        logger.info("~~~~~~~~~~~~所有测试均已完成~~~~~~~~~~~~");





    }


}
