package com.parking_1x.utils;


import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by weijl on 2019-6-19.
 */
public class DataInitialize {

    public static String preResult(String preResult){

        if (preResult.contains("${loginname}")) {
            preResult = preResult.replace("${loginname}", BaseLib.getPropertyString("test.usrname"));
        }
//获取上一条用例的数据，规则是：${get.x.value},拿第X个用例的value值
        if (preResult.contains("${get.")) {
            String regex = "\\$\\{.+?\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(preResult);
            ArrayList ParimList = new ArrayList();
            while (matcher.find()) {
                ParimList.add(matcher.group());
            }
            for (int i = 0; i < ParimList.size(); i++) {
                int ThisbodyStringIndex = Integer.parseInt(ParimList.get(i).toString().split("\\.")[1]) - 1;
                String ThisParm = ParimList.get(i).toString().split("\\.")[2].replaceAll("\\$","").replaceAll("\\{","").replaceAll("\\}","");
                String ThisBodyString = BaseLib.getList(ThisbodyStringIndex).toString();
                JsonObject MyJsonObject = (JsonObject) new com.google.gson.JsonParser().parse(ThisBodyString);
                String oldString = ParimList.get(i).toString();
                String newString = MyJsonObject.get(ThisParm).getAsString();
                preResult= preResult.replace(oldString, newString);
            }
        }

       return preResult;
    }

    public static String bodyString (String bodyString) throws ParseException {
        if (bodyString.contains("${guid}")) {
            String id = UUID.randomUUID().toString();
            bodyString = bodyString.replace("${guid}", id);
        }
        if (bodyString.contains("${nowdate}")) {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(nowDate));
            String orderDate = sf.format(cal.getTime());
            bodyString = bodyString.replace("${nowdate}", orderDate);
        }
        if (bodyString.contains("${yesterday}")) {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(nowDate));
            cal.add(Calendar.DAY_OF_YEAR, -1);
            String orderDate = sf.format(cal.getTime());
            bodyString = bodyString.replace("${yesterday}", orderDate);
        }
        if (bodyString.contains("${nowtime}")) {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat(" HH:mm:ss");
            String nowDate = sf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(nowDate));
            String orderDate = sf.format(cal.getTime());
            cal.add(Calendar.SECOND, -20);
            bodyString = bodyString.replace("${nowtime}", orderDate);
        }
        if (bodyString.contains("${nowtime-1}")) {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat(" HH:mm:ss");
            String nowDate = sf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(nowDate));
            cal.add(Calendar.MINUTE, -1);
            String orderDate = sf.format(cal.getTime());
            bodyString = bodyString.replace("${nowtime-1}", orderDate);
        }
        if (bodyString.contains("${plateNumber}")) {
            Random rand = new Random();
            String Random ="ABCDEFGHIJKLMNOPQWXYZ0987654321";
            String RandomPlateNumber="测";
            for(int i=0;i<6;i++)
                RandomPlateNumber=RandomPlateNumber+Random.charAt(rand.nextInt(Random.length()));
            bodyString = bodyString.replace("${plateNumber}", RandomPlateNumber);
        }
        //获取上一条用例的数据，规则是：${get.x.value},拿第X个用例的value值
        if (bodyString.contains("${get.")) {
            String regex = "\\$\\{.+?\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(bodyString);
            ArrayList ParimList = new ArrayList();
            while (matcher.find()) {
                ParimList.add(matcher.group());
            }
            for (int i = 0; i < ParimList.size(); i++) {
                int ThisbodyStringIndex = Integer.parseInt(ParimList.get(i).toString().split("\\.")[1]) - 1;
                String ThisParm = ParimList.get(i).toString().split("\\.")[2].replaceAll("\\$","").replaceAll("\\{","").replaceAll("\\}","");
                String ThisBodyString = BaseLib.getList(ThisbodyStringIndex).toString();
                JsonObject MyJsonObject = (JsonObject) new com.google.gson.JsonParser().parse(ThisBodyString);
                String oldString = ParimList.get(i).toString();
                String newString = MyJsonObject.get(ThisParm).getAsString();
                bodyString= bodyString.replace(oldString, newString);
            }
        }
        //智能加减时间，规则是：时间#SEC#-5,代表这个时间减5秒

        if (BaseLib.isJson(bodyString)) {
        JsonObject MyJsonObject = (JsonObject) new com.google.gson.JsonParser().parse(bodyString);
        Set<String> keySet = MyJsonObject.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String NextValue=MyJsonObject.get(it.next()).toString().replace("\"","");
             boolean SecondJudge =NextValue.contains("SEC")||NextValue.contains("MIN")||NextValue.contains("HOU")||NextValue.contains("DAY");
            if(NextValue.contains("#")&&SecondJudge){
                String oldValue=NextValue;
                String oldTime=oldValue.split("\\#")[0];
                String TimeType=oldValue.split("\\#")[1];
                int Timevalue=Integer.parseInt(oldValue.split("\\#")[2]);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sf.parse(oldTime);
                String nowDate = sf.format(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(sf.parse(nowDate));
                switch (TimeType){
                    case "SEC":
                        cal.add(Calendar.SECOND,Timevalue);
                        break;
                    case "MIN":
                        cal.add(Calendar.MINUTE,Timevalue);
                        break;
                    case "HOU":
                        cal.add(Calendar.HOUR_OF_DAY,Timevalue);
                        break;
                    case "DAY":
                        cal.add(Calendar.DAY_OF_MONTH,Timevalue);
                        break;
                }
                String orderDate = sf.format(cal.getTime());
                bodyString= bodyString.replace(oldValue,orderDate);
            }
        }

        }


        return bodyString;
    }

    static  String ParkingCollectName=BaseLib.getPropertyString("test.war.collect.name");
    static  String ParkingBusinessName=BaseLib.getPropertyString("test.war.business.name");

    public static String path(String path){
             if(path.contains("parkingBusinessEntry")){
                 path=path.replace("parkingBusinessEntry",ParkingBusinessName);
             }
             if(path.contains("parking_business_entry")){
                 path= path.replace("parking_business_entry",ParkingBusinessName);
             }
             if(path.contains("parking_collect_entry")){
                 path= path.replace("parking_collect_entry",ParkingCollectName);
             }
             if(path.contains("parkingCollectEntry")){
                 path= path.replace("parkingCollectEntry",ParkingCollectName);
             }
        return  path;
    }

}
