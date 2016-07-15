/**
 * ��������:SpringSharp
 * �ļ�����:BackupDatabaseUtil.java
 * �����ƣ� com.sharp.core.utils
 * ����ʱ��: 2016��7��8������10:24:37
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
 */
package com.sharp.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BackupDatabaseUtil {
    public String backup() throws IOException {
        String user = "root"; // ���ݿ���û���
        String password = "admin";// ���ݿ������
        String database = "hrtweb";// Ҫ���ݵ����ݿ���
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filepath = "d:\\" + sdf.format(date) + ".sql";
        File file = new File("d:\\", sdf.format(date) + ".sql");
        if (!file.exists()) {
            file.createNewFile();
        }
        String stmt1 = "mysqldump " + database + " -h 127.0.0.1 " + " -u " + user + " -p" + password
                + " --default-character-set=gbk --result-file=" + filepath;
        try {
            Runtime.getRuntime().exec(stmt1);
            System.out.println("�Ѿ����浽 " + filepath + " ��");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filepath;
    }
}

/**
 * ������ʱ��
 */

class PickTask {
    private Timer timer = new Timer();

    private TimerTask task = new TimerTask() {
        public void run() {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String beginDate = sdf.format(date);
            String beginTime = beginDate.substring(11, 16);
            System.out.println("��ʼʱ�䣺" + beginDate);

            BackupDatabaseUtil bdb = new BackupDatabaseUtil();
            // �趨����ʱ��
            if (beginTime.equals("17:09")) {
                try {
                    bdb.backup(); // ִ���ļ�����
                    String dbName = bdb.backup().toString(); // ȡ�����ݵ��ļ�����
                    String path = "d:\\";
                    int nameNo = dbName.lastIndexOf("\\");
                    // �ж��ļ��Ƿ���ڣ�������ڣ��򱸷ݳɹ�������������򱸷ݲ��ɹ���Ҫ���±���
                    File file = new File(path, dbName.substring(nameNo + 1, dbName.length()));
                    if (file.exists()) {
                        System.out.println("���ݳɹ�");

                    } else {

                        System.out.println("����ʧ�ܣ����±���");
                        // �ڱ���δ�ɹ�����������±���
                        new PickTask().start(1, 1);
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("can not find the file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("ʱ�仹�����أ���Ҫ�ż�Ŷ��");
            }
        }
    };

    // start ���������٣���Ҫ��schedule����
    public void start(int delay, int internal) {
        timer.schedule(task, delay * 1000, internal * 1000);
    }

    /**
     * �����ִ࣬�ж�ʱ����ָ��
     */

    public static void main(String[] args) {
        PickTask picktask = new PickTask();
        picktask.start(1, 60); // ÿ60��ִ��һ��
    }
}
