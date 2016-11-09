package com.team.zhuoke.androidjsoup.db;


import com.team.zhuoke.androidjsoup.db.query.Op;

/**
 * 
 * 数据更新操作，仅对单对象
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class Storage
{
    public Op op;
    
    public AbstractStorage object;
    
    public Storage(Op op, AbstractStorage object)
    {
        this.op = op;
        this.object = object;
    }
    
    public Op getOp()
    {
        return op;
    }
    
    public AbstractStorage getObject()
    {
        return object;
    }
}
