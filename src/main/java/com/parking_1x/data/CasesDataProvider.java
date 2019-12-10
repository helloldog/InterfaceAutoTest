package com.parking_1x.data;

import com.parking_1x.utils.ReadExcelCases;
import jxl.read.biff.BiffException;
import org.testng.annotations.DataProvider;

import java.io.IOException;

/**
 * Created by weijl on 2019-5-23.
 */
public class CasesDataProvider {
    @DataProvider(name = "casesProvider")
    public static Object[][] caseProvider() throws IOException, BiffException {
        String filePath = ".\\testCases\\Case2_1.3.4.xls"; //测试案例相对路径
        Object[][] cases = ReadExcelCases.readCases(filePath);

        return cases;
    }
}
