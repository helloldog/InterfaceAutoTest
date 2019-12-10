package com.parking_1x;
import com.parking_1x.data.CasesDataProvider;
import com.parking_1x.utils.BaseLib;
import com.parking_1x.utils.BasePost;
import com.parking_1x.utils.HTMLReport;
import io.restassured.RestAssured;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import java.io.IOException;

public class RunTest {
    String sql_delete_mydata =BaseLib.readFile(".\\sql\\delete_mydata.sql");
    String sql_delete_simulator =BaseLib.readFile(".\\sql\\delete_simulator.sql");
    String sql_delete_base =BaseLib.readFile(".\\sql\\delete_base_sql.sql");
    static String  sessionid ="";

    public RunTest() throws IOException {
    }

    @BeforeClass
    public void setUp() throws Exception {
        //日志配置文件初始化
        PropertyConfigurator.configure("./log4j.properties");
        //设置接口的地址和端口
        RestAssured.baseURI = BaseLib.getPropertyString("test.server.url");
        RestAssured.port = Integer.parseInt(BaseLib.getPropertyString("test.server.port"));
        //html报告初始化
        HTMLReport.HTMLLogWriteStart("Testa");
        //登录，获取sessionid
        sessionid =BaseLib.getsess(BaseLib.getPropertyString("test.usrname"),BaseLib.getPropertyString("test.password"));
        //导入必要的停车区和设备
        BaseLib.inputDevice(".\\excelData\\myTestdevice.xlsx",sessionid);
        //清空测试数据和模拟器
        BaseLib.sqloperation(sql_delete_mydata);
        BaseLib.sqloperationsimulator(sql_delete_simulator);
    }
    @AfterClass
    public void tearDown() throws Exception {
        HTMLReport.HTMLLogWriteclose();
        BaseLib.sqloperation(sql_delete_base);
        BaseLib.sqloperation(sql_delete_mydata);
        BaseLib.sqloperationsimulator(sql_delete_simulator);
    }
    @Test(dataProvider = "casesProvider", dataProviderClass = CasesDataProvider.class)
    public void runCases(String caseNo, String testPoit, String preResult, String path, String isUploadPic,String DevORFront,String bodyString) throws Exception {
        if(DevORFront.contains("clear")){
            BaseLib.sqloperationsimulator(sql_delete_simulator);
        }
        if(DevORFront.contains("D")){
               BasePost.Postdev(caseNo,testPoit,path,isUploadPic, bodyString,preResult);
    }
        else if(DevORFront.contains("F")){
               BasePost.Postfront(caseNo,testPoit,path,bodyString,sessionid,preResult);
            }
        else if(DevORFront.contains("PGSQL")){
              BasePost.selfromDB(caseNo,testPoit,path,bodyString,preResult);
        }
        }
}
