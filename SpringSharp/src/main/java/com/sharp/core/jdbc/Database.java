package com.sharp.core.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 类名称: Database
 * 类描述：数据库连接
 * 创建时间: 2016-7-4 下午03:25:34
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class Database
{


    public Database()
    {
        super();
    }
    

    /**
     * 方法名称：get_jdbc_connection
     * 方法描述：以jdbc的方式获取数据库连接
     * 创建人： 邢凌霄
     * 创建时间：2016-7-4 下午03:29:46
     * @return
     * @throws Exception
     * @since JDK 1.5
     */
    public static Connection get_jdbc_connection() throws Exception
    {
        Connection conn = null;
        try
        {
            String DB_IP = "10.16.122.232";
            String DB_SID = "orcl";
            String DB_USERNAME = "yl20151230";
            String DB_PASSWORD = "yl20151230";
            String DB_PORT = "1521";

            System.out.println(" Enter method -- Database.get_jdbc_connection()");

            String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
            String dbURL = "jdbc:oracle:thin:@" + DB_IP + ":" + DB_PORT + ":" + DB_SID;

            System.out.println("dbURL = " + dbURL);
            System.out.println("DB_USERNAME = " + DB_USERNAME);
            System.out.println("DB_PASSWORD = " + DB_PASSWORD);

            Class.forName(jdbcDriver).newInstance();
            conn = DriverManager.getConnection(dbURL, DB_USERNAME, DB_PASSWORD);
        }
        catch (SQLException sqe)
        {
            System.out.println("connect db failed by oracle jdbc driver. " + sqe.toString());
            throw sqe;
        }
        return conn;
    }
    
    /**
     * 方法名称：getConnection
     * 方法描述：获取数据库连接
     * 创建人： 邢凌霄
     * 创建时间：2016-7-4 下午03:29:38
     * @return
     * @throws Exception
     * @since JDK 1.5
     */
    public static Connection getConnection() throws Exception{
    	 Connection conn = null;
    	 try {
    		 conn = get_jdbc_connection();
		} catch (Exception e) {
			System.out.println("获取数据库连接出错");
			throw e;
		}
    	return conn;
    }
    
    public static void main(String[] args) {
    	 Connection conn = null;
    	try {
    		conn = getConnection();
		} catch (Exception e) {
			System.out.println("获取数据库异常");
			e.printStackTrace();
		}finally{
			if(conn == null){
				System.out.println("未获取到数据库连接");
			}else{
				System.out.println("成功获取数据库连接");
			}
		}
	}

}