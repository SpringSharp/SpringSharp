/**
 * ��������:SpringSharp
 * �ļ�����:ProcessUtil.java
 * �����ƣ� com.sharp.core.utils
 * ����ʱ��: 2016��7��8������10:41:37
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
        // ����ϵͳ����
        ProcessBuilder pb = new ProcessBuilder("tasklist");
        Process p;
        try {
            p = pb.start();
       
            BufferedReader out = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream()), Charset.forName("GB2312")));
            BufferedReader err = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getErrorStream())));
            System.out.println("Window ϵͳ�����б�");
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

