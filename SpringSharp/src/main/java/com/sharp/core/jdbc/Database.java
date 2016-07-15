package com.sharp.core.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ������: Database
 * �����������ݿ�����
 * ����ʱ��: 2016-7-4 ����03:25:34
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class Database
{


    public Database()
    {
        super();
    }
    

    /**
     * �������ƣ�get_jdbc_connection
     * ������������jdbc�ķ�ʽ��ȡ���ݿ�����
     * �����ˣ� ������
     * ����ʱ�䣺2016-7-4 ����03:29:46
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
     * �������ƣ�getConnection
     * ������������ȡ���ݿ�����
     * �����ˣ� ������
     * ����ʱ�䣺2016-7-4 ����03:29:38
     * @return
     * @throws Exception
     * @since JDK 1.5
     */
    public static Connection getConnection() throws Exception{
    	 Connection conn = null;
    	 try {
    		 conn = get_jdbc_connection();
		} catch (Exception e) {
			System.out.println("��ȡ���ݿ����ӳ���");
			throw e;
		}
    	return conn;
    }
    
    public static void main(String[] args) {
    	 Connection conn = null;
    	try {
    		conn = getConnection();
		} catch (Exception e) {
			System.out.println("��ȡ���ݿ��쳣");
			e.printStackTrace();
		}finally{
			if(conn == null){
				System.out.println("δ��ȡ�����ݿ�����");
			}else{
				System.out.println("�ɹ���ȡ���ݿ�����");
			}
		}
	}

}