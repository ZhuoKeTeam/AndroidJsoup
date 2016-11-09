package com.team.zhuoke.androidjsoup.db.interfaces;

import com.team.zhuoke.androidjsoup.db.AbstractStorage;
import com.team.zhuoke.androidjsoup.db.query.Page;
import com.team.zhuoke.androidjsoup.db.query.QueryBuilder;
import com.team.zhuoke.androidjsoup.db.query.SqlQueryBuilder;

import java.util.List;


/**
 * 
 * 基本的信息服务
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface IBaseService
{
    /**
     * 保存对象.
     * 更新或保存,会附加相关的初始信息，操作时间，版本号等.
     * @param t 要保存的对象.
     * @return 相应的对象.
     */
    public <T extends AbstractStorage> T save(T t);
    
    /**
     * 保存对象.
     * 更新或保存,仅简单的保存，但是会有后续的广播信息.
     * @param t 要保存的对象.
     * @return 相应的对象.
     */
    public <T extends AbstractStorage> T saveSimple(T t);
    
    /**
     * 保存对象.
     * 原样保存或更新，不修改任何属性.
     * @param t 要保存的对象.
     * @return 相应的对象.
     */
    public <T extends AbstractStorage> T saveProtocol(T t);
    
    /**
     * 根据类名,主键,删除相应的对象.
     * @param clazz 类名.
     * @param id 主键值.
     * @return 相应的对象.
     */
    public <T extends AbstractStorage> T remove(Class<T> clazz, String id);
    
    /**
     * 删除对象.
     * @param t 要删除的对象.
     * @return 相应的对象.
     */
    public <T extends AbstractStorage> T remove(T t);
    
    /**
     * 根据类名,主键,得到相应的对象.
     * @param clazz 类名.
     * @param id 主键值.
     * @return 相应的对象.
     */
    public <T extends AbstractStorage> T get(Class<T> clazz, String id);
    
    /**
     * 查询数据.
     * @param clazz 要查询的数据对象.
     * @param queryBuilder 查询构造器.
     * @return 查得的结果.
     */
    public <T extends DataInit> List<T> query(Class<T> clazz, QueryBuilder queryBuilder);
    
    /**
     * 得到所有的类对象.
     * @param <T> 型别.
     * @param clazz 类名.
     * @return 得到的对象.
     */
    <T extends DataInit> List<T> getAll(Class<T> clazz);
    
    /**
     * 统计记录数.
     * @param clazz 要统计的类.
     * @param queryBuilder 查询条件.
     * @return 统计结果.
     */
    public <T extends AbstractStorage> int count(Class<T> clazz, QueryBuilder queryBuilder);
    
    <T> T selectOne(Class<T> clazz, SqlQueryBuilder sqb, IExecuteCallback callback);
    
    <T> T selectOne(Class<T> clazz, String sql, String[] params, IExecuteCallback callback);
    
    <T> List<T> selectList(Class<T> clazz, String sql, String[] params, IExecuteCallback callback);
    
    <T extends DataInit> List<T> selectList(Class<T> clazz, String sql, String[] params);
    
    <T> List<T> selectList(Class<T> clazz, SqlQueryBuilder sqlQueryBuilder, IExecuteCallback callback);
    
    <T extends DataInit> List<T> selectList(Class<T> clazz, SqlQueryBuilder sqlQueryBuilder);
    
    /**
     * 查询分页数据.
     * @param clazz 要查询的数据类型.
     * @param queryBuilder 查询构造器.
     * @param pageNum 查获取的数据页码.
     * @param pageSize 每页记录数.
     * @return 查得的分页数据.
     */
    public <T extends DataInit> Page<T> query(Class<T> clazz, QueryBuilder queryBuilder, int pageNum, int pageSize);
    
    public <T extends DataInit> List<T> queryForList(Class<T> clazz, QueryBuilder qb, int first, int maxCount);
    
    public void clearCache();
}
