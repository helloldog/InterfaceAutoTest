package com.parking_1x.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.sleep;

/**
 * Created by weijl on 2019-5-27.
 */
public class BasePost {
    public  static Logger logger = Logger.getLogger(BasePost.class);
    static int SleepTime =Integer.parseInt(BaseLib.getPropertyString("test.wait.time.out"))*1000;
    static int TryCount=Integer.parseInt(BaseLib.getPropertyString("test.try.count"));
    static  String ParkingCollectName=BaseLib.getPropertyString("test.war.collect.name");


    public static void Postdev(String caseNo, String testPoit, String path, String isUploadPic, String bodyString,String preResult) throws InterruptedException, ParseException {
           Response response = null;
           String res="成功";
           boolean isok=false;
           int count=0;
           while (isok==false&&count<5) {
              response = given()
                       .contentType("application/json;charset=UTF-8")
                       .request()
                       .body(bodyString)
                       .post(path);

               if(preResult.contains("否")){
                   preResult=preResult.split("/")[1];
                   isok= !response.asString().contains(preResult);
               }
               else{
                   isok= response.asString().contains(preResult);
               }
               res="成功";

               count++;
               if(!isok){
                   sleep(SleepTime);
               }
               }

        logger.info(
                        "路径："+ path+
                        "\nBody：\n" + bodyString
                        +"\n断言："+ preResult+
                        "\n返回："+ response.asString());
        HTMLReport.HTMLLogWrite(caseNo,testPoit,res,isok);
        if(!isok){
            logger.info("用例"+testPoit+"失败");
        }
        JsonPath thisPostJson = new JsonPath(bodyString);
        if (isUploadPic.contains("Y")&&path.contains("carPass.htm")) {
            String formbody = "{\n" +
                    "\t\"sn\":\"" + thisPostJson.get("sn") + "\",\n" +
                    "\t\"mac\":\"" + thisPostJson.get("mac") + "\",\n" +
                    "\t\"guid\":\"" + thisPostJson.get("guid") + "\",\n" +
                    "\t\"deviceTime\":\"" + thisPostJson.get("inoutTime") + "\",\n" +
                    "\"deviceType\":\"0\"," +
                    "\"fullImgPlatePositon\":\"0,0,0,0,2048,3072\"," +
                    "\"fullImgCarPositon\":\"100,200,400,500,1920,1080\"," +
                    "\"featureImgPlatePositon\":\"100,200,400,500,1920,1080\"," +
                    "\"featureImgCarPositon\":\"100,200,400,500,1920,1080\"" +
                    "}\n";
            Response response2;
            boolean isok2=false;
            count = 0;
            while (isok2==false&&count<5) {
                 response2 = given()
                        .formParam("text", formbody)
                        .multiPart("fullImg", new File("./testPic/1.jpg"), "image/jpeg")
                        .multiPart("featureImg", new File("./testPic/2.jpg"), "image/jpeg")
                        .multiPart("plateImg", new File("./testPic/3.bmp"), "image/bmp")
                        .post("/"+ParkingCollectName+"/deviceUpload/carPassPic.htm");
                preResult="执行成功";
                 isok2 = response2.asString().contains(preResult);
                 res="成功";
                logger.info(
                        "路径："+ "/deviceUpload/carPassPic.htm"+
                                "\nBody：\n" + formbody
                                +"\n断言："+ preResult+
                                "\n返回："+ response2.asString());
                count++;
                if(!isok){
                    sleep(SleepTime);
                }
            }
            HTMLReport.HTMLLogWrite(caseNo, caseNo + "的图片上传", res, isok2);
            if(!isok2){
                logger.info("用例"+testPoit+"的图片上传失败");
            }
           }


           else if(isUploadPic.contains("Y")&&path.contains("berthParkingSituation.htm")){


            String formbody = "{\n" +
                    "\t\"sn\":\"" + thisPostJson.get("sn") + "\",\n" +
                    "\t\"mac\":\"" + thisPostJson.get("mac") + "\",\n" +
                    "\t\"guid\":\"" + thisPostJson.get("guid") + "\",\n" +
                    "\t\"img1DeviceTime\":\"" + thisPostJson.get("snapTime") + "\",\n" +
                    "\t\"img2DeviceTime\":\"" + thisPostJson.get("snapTime") + "\",\n" +
                    "\t\"img3DeviceTime\":\"" + thisPostJson.get("snapTime") + "\",\n" +
                    "\t\"img4DeviceTime\":\"" + thisPostJson.get("snapTime") + "\"\n" +
                    "}\n";
            Response response2=null;
            boolean isok2=false;
            count = 0;
            while (isok2==false&&count<5) {
                response2 = given()
                        .formParam("text", formbody)
                        .multiPart("img1", new File("./testPic/1.jpg"), "image/jpeg")
                        .multiPart("img2", new File("./testPic/2.jpg"), "image/jpeg")
                        .multiPart("img3", new File("./testPic/2.jpg"), "image/jpeg")
                        .multiPart("plateImg", new File("./testPic/3.bmp"), "image/bmp")
                        .multiPart("berthImg", new File("./testPic/2.jpg"), "image/jpeg")
                        .post("/"+ParkingCollectName+"/deviceUpload/berthParkingSituationPic.htm");
                preResult="执行成功";
                isok2 = response2.asString().contains(preResult);
                res="成功";
                logger.info(
                        "路径："+ "/deviceUpload/berthParkingSituationPic.htm"+
                                "\nBody：\n" + formbody
                                +"\n断言："+ preResult+
                                "\n返回："+ response2.asString());

                if(!isok){
                    sleep(SleepTime);
                }
            }
            HTMLReport.HTMLLogWrite(caseNo, caseNo + "的图片上传", res, isok2);
            if(!isok2){
                logger.info("用例"+testPoit+"的图片上传失败");
            }

            count++;
        }
        else if(isUploadPic.contains("Y")&&path.contains("plateNumberAndBerth.htm")){


            String formbody = "{\n" +
                    "\t\"sn\":\"" + thisPostJson.get("sn") + "\",\n" +
                    "\t\"mac\":\"" + thisPostJson.get("mac") + "\",\n" +
                    "\t\"guid\":\"" + thisPostJson.get("guid") + "\",\n" +
                    "\t\"img1DeviceTime\":\"" + thisPostJson.get("snapTime") + "\",\n" +
                    "\t\"img2DeviceTime\":\"" + thisPostJson.get("snapTime") + "\",\n" +
                    "\t\"img3DeviceTime\":\"" + thisPostJson.get("snapTime") + "\",\n" +
                    "\t\"img4DeviceTime\":\"" + thisPostJson.get("snapTime") + "\"\n" +
                    "}\n";
            Response response2;
            boolean isok2=false;
            count = 0;
            while (isok2==false&&count<5) {
                response2 = given()
                        .formParam("text", formbody)
                        .multiPart("img1", new File("./testPic/1.jpg"), "image/jpeg")
                        .multiPart("img2", new File("./testPic/2.jpg"), "image/jpeg")
                        .multiPart("img3", new File("./testPic/2.jpg"), "image/jpeg")
                        .multiPart("plateImg", new File("./testPic/3.bmp"), "image/bmp")
                        .multiPart("berthImg", new File("./testPic/2.jpg"), "image/jpeg")
                        .post("/"+ParkingCollectName+"/deviceUpload/plateNumberAndBerthPic.htm");
                preResult="执行成功";
                isok2 = response2.asString().contains(preResult);
                res="成功";
                logger.info(
                        "路径："+ "/deviceUpload/plateNumberAndBerthPic.htm"+
                                "\nBody：\n" + formbody
                                +"\n断言："+ preResult+
                                "\n返回："+ response2.asString());

                if(!isok){
                    sleep(SleepTime);
                }

            }
            HTMLReport.HTMLLogWrite(caseNo, caseNo + "的图片上传", res, isok2);
            if(!isok2){
                logger.info("用例"+testPoit+"的图片上传失败");
            }

            count++;
        }



    }

    public static void Postfront(String caseNo,String testPoit,String path, String bodyString, String sessionid, String preResult) throws InterruptedException {

        Response response = null;
        String res=null;
        boolean isok=false;
        int count=0;
        while (isok==false&&count<5) {
            response = given()
                    .contentType("application/json;charset=UTF-8")
                    .header("sessionid", sessionid)
                    .request()
                    .body(bodyString)
                    .post(path);

            if(preResult.contains("否")){
                preResult=preResult.split("/")[1];
                isok= !response.asString().contains(preResult);
            }
            else{
                isok= response.asString().contains(preResult);
            }
            res="成功";;
            count++;
            if(!isok){
                sleep(SleepTime);
            }
        }
        logger.info(
                "路径："+ path+
                        "\nBody：\n" + bodyString
                        +"\n断言："+ preResult+
                        "\n返回："+ response.asString());
        HTMLReport.HTMLLogWrite(caseNo,testPoit,res,isok);
        if(!isok){
            logger.info("用例"+testPoit+"失败");
        }

   //     Assert.assertTrue(isok);
    }

    public static void selfromDB(String caseNo, String testPoit,String path, String bodyString, String preResult) throws Exception {

        boolean isok = false;
        int count=0;
        List dblist;
        while (isok ==false) {
            if (path.contains("模拟器")) {
                dblist = BaseLib.sqlselcetsimulator(bodyString);
            } else {
                dblist = BaseLib.sqlselcet(bodyString);
            }
            for (int i = 0; i < dblist.size(); i++) {

                if(preResult.contains("否")){
                    preResult=preResult.split("/")[1];
                    isok = !dblist.get(i).toString().contains(preResult);
                }
                else{
                    isok = dblist.get(i).toString().contains(preResult);
                }

                if (isok) {
                    HTMLReport.HTMLLogWrite(caseNo, testPoit, "成功", isok);

                    logger.info(
                             path +
                                    "\nBody：" + bodyString
                                    + "\n断言：" + preResult +
                                    "\n返回：" + dblist.get(i).toString());
                    break;
                }
            }


            if(count>TryCount){
                HTMLReport.HTMLLogWrite(caseNo, testPoit, "没有找到："+preResult, isok);
                if(!isok){
                    logger.info("用例"+testPoit+"失败");
                }
                logger.info(
                                 path +
                                "\nBody：" + bodyString
                                + "\n断言：" + preResult +
                                "\n返回：" + "没有找到："+preResult);
                break;
            }
            count++;
            if(!isok){
                sleep(SleepTime);
            }
        }
    }
}
