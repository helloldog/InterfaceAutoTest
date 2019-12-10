package com.parking_1x.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HTMLReportbakup {
	public static int count=1;
    public static int mylenth=0;
    public static Object[][] reportlist= new Object[100][];

    static StringBuilder sb = new StringBuilder();
    static PrintStream printStream = null ;
	public static void  HTMLLogWriteStart(){
         try {
            // printStream= new PrintStream(new FileOutputStream(BaseLib.getPropertyString("Loglocalsaveposition")+"myreport.html"));//路径默认在项目根目录下
        	 printStream= new PrintStream(new FileOutputStream(".\\report\\"+"my_report.html"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            sb.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">");
            sb.append("<head>");
            sb.append("<title>测试报告</title>");
            sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            //样式内容
            sb.append("<style type=\"text/css\">");
            sb.append(".tablename table th {background:#8FBC8F}");
            sb.append(".tablename table tr{ background:#FAEBD7;text-align:center}");       
            sb.append("</style></head>");
            //样式结尾
            //主体部分一个div
            sb.append("<div class=\"tablename\">");
            //一个table
            sb.append("<table align=\"center\" width=\"1500\"  height=\"100\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;\">");
            sb.append("<h1 align=\"center\">自动化测试报告</h1>");
            sb.append("<th >序号</th><th >功能模块</th><th>用例描述</th><th>执行时间</th><th>备注</th><th>测试状态</th>");

	}
	
	
	public static void  HTMLLogWrite(String testcase, String testname,String msg, boolean result){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date()).toString();
        if(result){
            sb.append("<tr><td>"+count+"</td><td align=\"left\">"+testcase+"</td><td align=\"left\">"+testname+"</td><td>"+time+"</td><td>"+msg+"</td><td  bgcolor=\"#66CD00\">PASS</td>");
        }
        else if(!result){
            sb.append("<tr><td>"+count+"</td><td align=\"left\">"+testcase+"</td><td align=\"left\">"+testname+"</td><td>"+time+"</td><td>"+msg+"</td><td  bgcolor=\"#FF0000\">FAILD</td>");
	}
        count++;
	}
	
	
	
public static void  HTMLLogWriteclose(){

    sb.append("</tr></table>");
    sb.append("</div></body></html>");
   
   printStream.println(sb.toString());
	}
	


public static void  HTMLLogWritecount(){

   sb.append("<tr><th>用例名称</th><th>总计</th><th>通过</th><th>失败</th><th>通过率</th></tr><tr>");
   sb.append("<td>test集合</td><td>10</td><td>8</td><td>2</td><td>80%</td> </tr>");

}
}
 
