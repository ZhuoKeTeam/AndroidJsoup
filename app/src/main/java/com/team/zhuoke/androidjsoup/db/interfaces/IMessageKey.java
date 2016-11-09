package com.team.zhuoke.androidjsoup.db.interfaces;

/**
 * 
 * 变更键值
 *
 * <p>detailed comment
 * @author ztw 2016年5月12日
 * @see
 * @since 1.0
 */
public interface IMessageKey
{
    public static final int KEY_CLOUDTASK = 1;
    
    /**
     * 维修记录置为成功或失败时通知列表进行刷新
     */
    public static final int KEY_MAINTENACE_STATUS = 2;
    
    /**
     * 获取天气
     */
    public static final int KEY_GETWEATHER = 3;
    
    /**
     * 天气获取到
     */
    public static final int KEY_GETWEATHER_ED = 4;
}
