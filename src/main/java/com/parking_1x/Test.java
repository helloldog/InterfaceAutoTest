package com.parking_1x;

import com.google.gson.JsonObject;
import com.parking_1x.utils.BaseLib;
import com.parking_1x.utils.DataInitialize;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ConnectionConfig.connectionConfig;

/**
 * Created by weijl on 2019-6-27.
 */
public class Test {


    public static void main(String[] args) throws ParseException {

        RestAssured.config = RestAssured.config()
                .connectionConfig(connectionConfig().closeIdleConnectionsAfterEachResponse());
    }
}
