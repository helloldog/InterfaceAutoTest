package com.parking_1x.utils;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;

/**
 * Created by weijl on 2019-5-24.
 */
public class BaseLib {
    public static String getPropertyString (String key){//读取配置文件
        String propertyFileName = System.getProperty("user.dir")+"/config.properties";

        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(propertyFileName));
        }catch (IOException e1){
            e1.printStackTrace();
        }
        if(key == null || key.equals("")|| key.equals("null")){
            return "";
        }
        String result = "";
        try {
            result = properties.getProperty(key);
        }catch (MissingResourceException e){
            e.printStackTrace();
        }
        return result;
    }

    public static void sqloperation(String sql) throws Exception{//执行sql语句
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(
                    getPropertyString("test.postgres.db.url"), getPropertyString("test.postgres.db.user"),
                    getPropertyString("test.postgres.db.password"));
            c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    public static void sqloperationsimulator(String sql) throws Exception{//执行sql语句
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(
                    getPropertyString("test.postgres.dbsimulator.url"), getPropertyString("test.postgres.dbsimulator.user"),
                    getPropertyString("test.postgres.dbsimulator.password"));
            c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    public static List sqlselcet(String sql) throws Exception{//执行查询语句
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(
                    getPropertyString("test.postgres.db.url"), getPropertyString("test.postgres.db.user"),
                    getPropertyString("test.postgres.db.password"));
            c.setAutoCommit(false);
            stmt = c.createStatement();
         ResultSet res=   stmt.executeQuery(sql);
            List list = new ArrayList();
            ResultSetMetaData md = res.getMetaData();//获取键名
            int columnCount = md.getColumnCount();//获取行的数量
            while (res.next()) {
                Map rowData = new HashMap();//声明Map
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), res.getObject(i));//获取键名及值
                }
                list.add(rowData);
            }
            stmt.close();
            c.commit();
            c.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }


        return null;
    }
    public static List sqlselcetsimulator(String sql) throws Exception{//执行查询语句
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(
                    getPropertyString("test.postgres.dbsimulator.url"), getPropertyString("test.postgres.dbsimulator.user"),
                    getPropertyString("test.postgres.dbsimulator.password"));
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet res=   stmt.executeQuery(sql);
            List list = new ArrayList();
            ResultSetMetaData md = res.getMetaData();//获取键名
            int columnCount = md.getColumnCount();//获取行的数量
            while (res.next()) {
                Map rowData = new HashMap();//声明Map
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), res.getObject(i));//获取键名及值
                }
                list.add(rowData);
            }
            stmt.close();
            c.commit();
            c.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }


        return null;
    }


    public static String  getsess(String usrName, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        RestAssured.baseURI = BaseLib.getPropertyString("test.web.url");
        RestAssured.port = Integer.parseInt(BaseLib.getPropertyString("test.web.port"));
        String sessionid="";
     String newpassword =   getPwd(password);
        Response response = given()
                .contentType("application/json;charset=UTF-8")
                .request()
                .body("{\"resourcesNo\":\"" +
                        "xlw" + "\"," +
                        "\"loginName\":\"" +
                        usrName +
                        "\"," + "\"loginPassword\":\"" +
                        newpassword +
                        "" + "\"}")
                .post("/"+BaseLib.getPropertyString("test.war.business.name")+"/systemright/users/login.htm");
        String json = response.asString();
        JsonPath jp = new JsonPath(json);
        sessionid =  jp.get("data.sessionid");

        return  sessionid;
    }



    public static String getPwd(String pwd){

//MD5加密的算法，有JDK实现，我们只需要使用
        try {
//获取加密的对象
            MessageDigest instance = MessageDigest.getInstance("MD5");
//使用加密对象的方法，完成加密
            byte[] bs = instance.digest(pwd.getBytes());
//朝着mysql加密结果的方向优化
/**
 * byte b 1111 1111
 * int b 0000 0000 0000 0000 0000 0000 1111 1111
 * int 255 0000 0000 0000 0000 0000 0000 1111 1111
 * &--------------------------------------------------
 * 0000 0000 0000 0000 0000 0000 1111 1111
 * */
            String str = "";
//第一，将所有的数据，转换成正数
            for (byte b : bs) {
                int temp = b & 255;
//第二，将所有的数据，转换成16进制格式
                if(temp >=0 && temp <=15){
                    str = str +"0"+ Integer.toHexString(temp);
                }else{
                    str = str + Integer.toHexString(temp);;
                }
            }
            return str;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }


    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        BaseLib.readToBuffer(sb, filePath);
        return sb.toString();
    }

    public static void  inputDevice(String  xlsxpath,String sessionid){
        RestAssured.baseURI = BaseLib.getPropertyString("test.web.url");
        RestAssured.port = Integer.parseInt(BaseLib.getPropertyString("test.web.port"));
                Response response2 = given()
                .contentType("multipart/form-data")
                .multiPart("excelData", new File(xlsxpath), "image/jpeg")
                        .header("sessionid",sessionid)
                .post("/"+BaseLib.getPropertyString("test.war.business.name")+"/parking/uploadParkingImfomation.htm");

    }
    public  static ArrayList bodyList = new ArrayList();
    public  static void AddList(String body){

        bodyList.add(body);

    }
    public  static Object getList(int  index){

         return bodyList.get(index);
    }
    public  static int getListLenth(){

        return bodyList.size();
    }

    public  static void getListClear(){

        bodyList.clear();
    }

    public static ArrayList getFileList(String Directorypath) {
        File file = new File(Directorypath);
        ArrayList FileListName = new ArrayList();
        File[] fileList = file.listFiles();

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                FileListName.add(fileName);
            }
        }
        return FileListName;
    }

    public static   boolean isJson(String content) {

        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            if(jsonStr.isEmpty()||jsonStr.equals("")){
                return false;
            }
                else{
                return true;
            }


        } catch (Exception e) {
            return false;
        }
    }






}
