/**
 * 工程名称:SpringSharp
 * 文件名称:PagerTypeConstant.java
 * 包名称： com.sharp.core.sysframe.constant
 * 创建时间: 2016年7月11日下午5:30:58
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.constant;
public class PagerTypeConstant {
    public final static long STRING = 1;
    public final static long DATE = 2;
    public final static long DATETIME = 3;
    public final static long AMOUNT_2 = 4;
    public final static long AMOUNT_6 = 5;
    public final static long LONG = 6;
    public final static long DOUBLE = 7;
    public final static long ARRAY = 8;
    public final static long LIST = 9;
    public final static long MAP = 10;
    public final static long FUNCTION = 11;
    
    public final static String LOGOTYPE = "####";
    
    public static String getType(long type)
    {
        String result;
        switch((int)type)
        {
            case (int)STRING:
                result = "$.suggest.Type.STRING";
                break;
            case (int)DATE:
                result = "$.suggest.Type.DATE";
                break;
            case (int)DATETIME:
                result = "$.suggest.Type.DATETIME";
                break;
            case (int)AMOUNT_2:
                result = "$.suggest.Type.AMOUNT_2";
                break;
            case (int)AMOUNT_6:
                result = "$.suggest.Type.AMOUNT_6";
                break;
            case (int)LONG:
                result = "$.suggest.Type.LONG";
                break;
            case (int)DOUBLE:
                result = "$.suggest.Type.DOUBLE";
                break;      
            case (int)ARRAY:
                result = "$.suggest.Type.ARRAY";
                break;
            case (int)LIST:
                result = "$.suggest.Type.LIST";
                break;      
            case (int)MAP:
                result = "$.suggest.Type.MAP";
                break;
            case (int)FUNCTION:
                result = "$.suggest.Type.FUNCTION";
                break;              
            default:
                result = "$.suggest.Type.STRING";
                break;
                
        }
        return result;
    }
    
}

