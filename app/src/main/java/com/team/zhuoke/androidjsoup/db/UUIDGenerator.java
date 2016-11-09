package com.team.zhuoke.androidjsoup.db;

import java.io.Serializable;

/**
 * 
 * 用于生成ID
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class UUIDGenerator
{
    private static final int ID_LENGTH = 36;
    
    private static final int INT_BIT = 8;
    
    private static final int STR_BIT = 8;
    
    private static final int SHORT_BIT = 4;
    
    private static final int JVM_BIT = 8;
    
    private static final int HI = 32;
    
    private String entityId = "10000000";
    
    private static short counter = (short)0;
    
    private static final int JVM = (int)(System.currentTimeMillis() >>> JVM_BIT);
    
    /**
     * 构造ID生成器.
     */
    public UUIDGenerator()
    {
    }
    
    /**
     * 得到JVM相关信息. <br>
     * 这里是JVM启动时间.
     * 
     * @return JVM相关信息.
     */
    protected int getJVM()
    {
        return JVM;
    }
    
    /**
     * 得到当前计数. <br>
     * 可防重复.
     * 
     * @return 当前计数.
     */
    protected short getCount()
    {
        synchronized (UUIDGenerator.class)
        {
            if (counter < 0)
            {
                counter = 0;
            }
            return counter++;
        }
    }
    
    /**
     * 得到高位时间.
     * 
     * @return 高位时间.
     */
    protected short getHiTime()
    {
        return (short)(System.currentTimeMillis() >>> HI);
    }
    
    /**
     * 得到低位时间.
     * 
     * @return 低位时间.
     */
    protected int getLoTime()
    {
        return (int)System.currentTimeMillis();
    }
    
    /**
     * 格式化int型数据. <br>
     * 占八位.
     * 
     * @param intval
     *            int型的值.
     * @return 格式化后的字符串.
     */
    protected String format(int intval)
    {
        String formatted = Integer.toHexString(intval);
        StringBuilder buf = new StringBuilder("00000000");
        buf.replace(INT_BIT - formatted.length(), INT_BIT, formatted);
        return buf.toString();
    }
    
    /**
     * 格式化short型数据. <br>
     * 占四位.
     * 
     * @param shortval
     *            short型的值.
     * @return 格式化后的字符串.
     */
    protected String format(short shortval)
    {
        String formatted = Integer.toHexString(shortval);
        StringBuilder buf = new StringBuilder("0000");
        buf.replace(SHORT_BIT - formatted.length(), SHORT_BIT, formatted);
        return buf.toString();
    }
    
    /**
     * 得到一个UUID.
     * 
     * @return 新的UUID.
     */
    public Serializable generate()
    {
        return new StringBuilder(ID_LENGTH).append(entityId)
            .append(format(getJVM()))
            .append(format(getHiTime()))
            .append(format(getLoTime()))
            .append(format(getCount()))
            .toString();
    }
    
    /**
     * 格式化字符串型数据. <br>
     * 占八位.
     * 
     * @param stringval
     *            字符串型的值.
     * @return 格式化后的结果.
     */
    protected String format(String stringval)
    {
        if (stringval == null)
        {
            stringval = "";
        }
        stringval = stringval.length() > STR_BIT ? stringval.substring(stringval.length() - STR_BIT, stringval.length())
            : stringval;
        StringBuilder buf = new StringBuilder("00000000");
        buf.replace(STR_BIT - stringval.length(), STR_BIT, stringval);
        return buf.toString();
    }
}
