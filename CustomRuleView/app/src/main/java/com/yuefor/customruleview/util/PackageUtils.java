package com.yuefor.customruleview.util;

import com.yuefor.customruleview.CustomApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PackageUtils {


    public static String getAssetsString(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(CustomApplication.getGlobalContext().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null)
                result.append(line);
            bufReader.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
