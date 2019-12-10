package com.parking_1x.utils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by weijl on 2019-5-23.
 */
public class ReadExcelCases {
    public static Object[][] readCases(String filePath) throws IOException, BiffException {

        InputStream inputStream = new FileInputStream(filePath);
        Workbook rwb = Workbook.getWorkbook(inputStream);

        Sheet sheet = rwb.getSheet(0);
        int rsRows = sheet.getRows(); // 获取总行数
        int rsColums = sheet.getColumns();//获取总列数

        int countY = 0;
        for (int i = 1; i < rsRows; i++) {
            if (sheet.getCell(0, i).getContents().contains("case"))  //统计需要执行的案例数
                countY++;
        }

        Object[][] cases = new Object[countY][rsColums];

        int x = 0;
        for (int i = 1; i < rsRows; i++) {
            if (sheet.getCell(0, i).getContents().contains("case")) {  //执行标识为“case”才记录数组
                for (int j = 0; j < rsColums; j++) {
                    cases[x][j] = sheet.getCell(j, i).getContents();
                }
                x++;
            }
        }
        inputStream.close();
        return cases;

    }
}
