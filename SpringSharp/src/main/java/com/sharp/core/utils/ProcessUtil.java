/**
 * 工程名称:SpringSharp
 * 文件名称:ProcessUtil.java
 * 包名称： com.sharp.core.utils
 * 创建时间: 2016年7月8日上午10:41:37
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ProcessUtil {
    
        public static void getProcessList(){
        // 创建系统进程
        ProcessBuilder pb = new ProcessBuilder("tasklist");
        Process p;
        try {
            p = pb.start();
       
            BufferedReader out = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream()), Charset.forName("GB2312")));
            BufferedReader err = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getErrorStream())));
            System.out.println("Window 系统进程列表");
            String ostr;
              
            while ((ostr = out.readLine()) != null)
            System.out.println(ostr);
            String estr = err.readLine();
            if (estr != null) {
                System.out.println("\nError Info");
                System.out.println(estr);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
        public static void main(String[] args) {
            ProcessUtil.getProcessList();
        }
        
}

