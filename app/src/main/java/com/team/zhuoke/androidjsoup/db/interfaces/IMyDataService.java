package com.team.zhuoke.androidjsoup.db.interfaces;


import com.team.zhuoke.androidjsoup.db.table.MyData;

import java.util.List;

/**
 * 
 * 任务处理服务
 *
 * <p>detailed comment
 * @author ztw 2016年5月12日
 * @see
 * @since 1.0
 */
public interface IMyDataService
{
    /**
     * 添加一条记录
     * @param myData
     * @return
     */
    public MyData addMyData(MyData myData);

    /**
     * 批量添加记录
     * @param myDatas
     * @return
     */
    public void addMyData(List<MyData> myDatas);

    /**
     * 根据ID更新一条记录
     * @param myDataId
     * @param myData
     * @return 被更新的对象
     */
    public MyData updateMyData(String myDataId, MyData myData);

    /**
     * 删除一条记录
     * @param myDataId
     */
    public void deleteMyData(String myDataId);
}
