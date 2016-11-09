package com.team.zhuoke.androidjsoup.db.interfaces;

public interface IMessage
{
    /**
     * 字符串分隔符
     */
    public static final String SPLIT = "\r\n";
    
    /**
     * 多种类消息分隔
     */
    public static final String MSG_KIND_SPLIT = ";";
    
    /**
     * ID之间的分隔
     */
    public static final String MSG_ID_SPLIT = "|";
    
    /**
     * 解析ID之间的分隔
     */
    public static final String MSG_PARSE_ID_SPLIT = "[|]";
    
    /**
     * 种类与ID之间的分隔
     */
    public static final String MSG_KIND_ID_SPLIT = ",";
}
