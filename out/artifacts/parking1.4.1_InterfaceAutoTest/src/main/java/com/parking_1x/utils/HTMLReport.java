package com.parking_1x.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HTMLReport {
	public static int count=1;
    static StringBuilder sb = new StringBuilder();
    static PrintStream printStream = null ;
	public static void  HTMLLogWriteStart(String reportname){
         try {
            // printStream= new PrintStream(new FileOutputStream(BaseLib.getPropertyString("Loglocalsaveposition")+"myreport.html"));//路径默认在项目根目录下
        	//printStream= new PrintStream(new FileOutputStream("./report/myreport.html"));
             printStream= new PrintStream(new FileOutputStream("./report/"+reportname+".html"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            sb.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html;charset="+BaseLib.getPropertyString("report.unicode")+"\">");
        sb.append("<h2>测试地址是："+BaseLib.getPropertyString("test.server.url")+"</h2>");
        sb.append("<h2>is.have.dbsimulator："+BaseLib.getPropertyString("is.have.dbsimulator")+"</h2>");
        sb.append("<h2>is.have.db："+BaseLib.getPropertyString("is.have.db")+"</h2>");
            sb.append("<head>");
            sb.append("<title>"+reportname+"</title>");
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
            sb.append("<h1 align=\"center\">"+reportname+"</h1>");
            sb.append("<th >序号</th><th >功能点</th><th>执行时间</th><th>备注</th><th>测试状态</th>");

	}
	
	
	public static void  HTMLLogWrite(String testcase, String testNo,String msg, boolean result){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date()).toString();
        if(result){
            String testcasesp=testcase.split("-")[0];
            testcasesp=testcasesp.trim();
            String num="";
            if(testcasesp != null && !"".equals(testcasesp)){
                for(int i=0;i<testcasesp.length();i++){
                    if(testcasesp.charAt(i)>=48 && testcasesp.charAt(i)<=57){
                        num+=testcasesp.charAt(i);
                    }
                }

            }
            int Testcasenum=Integer.parseInt(num);

            if(Testcasenum==count) {
                sb.append("<tr><td>" + count + "</td><td align=\"left\">" + testcase + "</td><td>" + time + "</td><td>" + msg + "</td><td  bgcolor=\"#66CD00\">PASS</td>");
                count++;
            }
        }
        else if(!result){
            sb.append("<tr><td>"+count+"</td><td align=\"left\">"+testcase+testNo+"</td><td>"+time+"</td><td>"+msg+"</td><td  bgcolor=\"#FF0000\">FAILD</td>");
	}
	else  if(msg.contains("跳过")){
            sb.append("<tr><td>" + count + "</td><td align=\"left\">" + testcase + "</td><td>" + time + "</td><td>" + msg + "</td><td  bgcolor=\"#66CD00\">PASS</td>");
        }

	}
	
	
	
public static void  HTMLLogWriteclose(){

    sb.append("</tr></table>");
    sb.append("</div></body></html>");
   
   printStream.println(sb.toString());

    sb = new StringBuilder();
    PrintStream printStream = null ;
    count=1;


}

}
 
