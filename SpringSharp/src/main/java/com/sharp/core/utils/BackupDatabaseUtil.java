/**
 * 工程名称:SpringSharp
 * 文件名称:BackupDatabaseUtil.java
 * 包名称： com.sharp.core.utils
 * 创建时间: 2016年7月8日上午10:24:37
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
        String user = "root"; // 数据库的用户名
        String password = "admin";// 数据库的密码
        String database = "hrtweb";// 要备份的数据库名
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
            System.out.println("已经保存到 " + filepath + " 中");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filepath;
    }
}

/**
 * 创建定时器
 */

class PickTask {
    private Timer timer = new Timer();

    private TimerTask task = new TimerTask() {
        public void run() {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String beginDate = sdf.format(date);
            String beginTime = beginDate.substring(11, 16);
            System.out.println("开始时间：" + beginDate);

            BackupDatabaseUtil bdb = new BackupDatabaseUtil();
            // 设定备份时间
            if (beginTime.equals("17:09")) {
                try {
                    bdb.backup(); // 执行文件备份
                    String dbName = bdb.backup().toString(); // 取出备份的文件名字
                    String path = "d:\\";
                    int nameNo = dbName.lastIndexOf("\\");
                    // 判断文件是否存在，如果存在，则备份成功，如果不存在则备份不成功需要重新备份
                    File file = new File(path, dbName.substring(nameNo + 1, dbName.length()));
                    if (file.exists()) {
                        System.out.println("备份成功");

                    } else {

                        System.out.println("备份失败，重新备份");
                        // 在备份未成功的情况下重新备份
                        new PickTask().start(1, 1);
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("can not find the file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("时间还不到呢，不要着急哦！");
            }
        }
    };

    // start 方法不能少，主要是schedule方法
    public void start(int delay, int internal) {
        timer.schedule(task, delay * 1000, internal * 1000);
    }

    /**
     * 测试类，执行定时备份指令
     */

    public static void main(String[] args) {
        PickTask picktask = new PickTask();
        picktask.start(1, 60); // 每60秒执行一次
    }
}
