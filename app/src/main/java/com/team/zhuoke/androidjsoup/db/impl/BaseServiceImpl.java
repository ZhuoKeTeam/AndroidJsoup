package com.team.zhuoke.androidjsoup.db.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.team.zhuoke.androidjsoup.db.AbstractStorage;
import com.team.zhuoke.androidjsoup.db.Base;
import com.team.zhuoke.androidjsoup.db.DBUtils;
import com.team.zhuoke.androidjsoup.db.StorageService;
import com.team.zhuoke.androidjsoup.db.TableClassRegist;
import com.team.zhuoke.androidjsoup.db.interfaces.DataInit;
import com.team.zhuoke.androidjsoup.db.interfaces.IBaseService;
import com.team.zhuoke.androidjsoup.db.interfaces.IChangeObject;
import com.team.zhuoke.androidjsoup.db.interfaces.IDatabaseProvider;
import com.team.zhuoke.androidjsoup.db.interfaces.IExecuteCallback;
import com.team.zhuoke.androidjsoup.db.interfaces.ISignId;
import com.team.zhuoke.androidjsoup.db.query.BetweenValue;
import com.team.zhuoke.androidjsoup.db.query.ConditionValue;
import com.team.zhuoke.androidjsoup.db.query.Criteria;
import com.team.zhuoke.androidjsoup.db.query.InValue;
import com.team.zhuoke.androidjsoup.db.query.Page;
import com.team.zhuoke.androidjsoup.db.query.QueryBuilder;
import com.team.zhuoke.androidjsoup.db.query.SqlQueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 基础服务
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class BaseServiceImpl implements IBaseService
{
    private static final String LOAD_SQL = "select * from %s where id=?";
    
    private TableClassRegist tableClassRegist;
    
    private StorageService storageService;
    
    private IDatabaseProvider databaseProvider;
    
    private boolean isMonitorChange = false;
    
    private DBUtils dbUtils = null;
    
    public BaseServiceImpl(TableClassRegist tableClassRegist, StorageService storageService,
        IDatabaseProvider databaseProvider)
    {
        super();
        this.tableClassRegist = tableClassRegist;
        this.storageService = storageService;
        this.databaseProvider = databaseProvider;
        this.dbUtils = DBUtils.getInstance();
    }
    
    private void init(AbstractStorage storageObject)
    {
        storageObject.setId((String)dbUtils.getIdGenerator().generate());
        storageObject.setLastVer(1);
        storageObject.setIsValid(Base.TRUE);
    }
    
    private void afterInsert(AbstractStorage storageObject)
    {
        isMonitorChange = dbUtils.getSimpleObjectChangedRegist().isMonitorChange();
        if (isMonitorChange && storageObject instanceof IChangeObject)
        {
            dbUtils.getSimpleObjectChangedRegist().doChange(storageObject);
        }
    }
    
    private void beforeUpdate(AbstractStorage storageObject)
    {
        isMonitorChange = dbUtils.getSimpleObjectChangedRegist().isMonitorChange();
        if (isMonitorChange && storageObject instanceof IChangeObject)
        {
            Object oldObject = get(storageObject.getClass(), storageObject.getId());
            IChangeObject changeObject = (IChangeObject)storageObject;
            IChangeObject oldObject2 = (IChangeObject)oldObject;
            if (oldObject2 == null)
            {
                dbUtils.getSimpleObjectChangedRegist().doChange(storageObject);
            }
            else if (oldObject2.isChanged(changeObject))
            {
                dbUtils.getSimpleObjectChangedRegist().doChange(storageObject);
            }
        }
    }
    
    private void afterSave(AbstractStorage storageObject)
    {
    }
    
    @Override
    public <T extends AbstractStorage> T save(T t)
    {
        t.setOpTime(System.currentTimeMillis());
        if (t instanceof ISignId)
        {
            T oldObject = (T)storageService.get(t.getClass(), t.getId());
            if (oldObject != null)
            {
                if (dbUtils.getSimpleObjectChangedRegist().isMonitorChange() && t instanceof IChangeObject)
                {
                    IChangeObject changeObject = (IChangeObject)t;
                    IChangeObject oldObject2 = (IChangeObject)oldObject;
                    if (oldObject2.isChanged(changeObject))
                    {
                        dbUtils.getSimpleObjectChangedRegist().doChange(t);
                    }
                }
                t.increaseVersion();
                storageService.update(t);
            }
            else
            {
                Long createTime = t.getCreateTime();
                if (createTime == null || createTime == 0)
                {
                    t.setCreateTime(System.currentTimeMillis());
                }
                t.setLastVer(1);
                t.setIsValid(Base.TRUE);
                storageService.save(t);
                afterInsert(t);
            }
        }
        else
        {
            if (t.getId() == null)
            {
                Long createTime = t.getCreateTime();
                if (createTime == null || createTime == 0)
                {
                    t.setCreateTime(System.currentTimeMillis());
                }
                init(t);
                storageService.save(t);
                afterInsert(t);
            }
            else
            {
                t.increaseVersion();
                beforeUpdate(t);
                storageService.update(t);
            }
        }
        afterSave(t);
        return t;
    }
    
    @Override
    public <T extends AbstractStorage> T saveSimple(T t)
    {
        if (t.getId() == null)
        {
            Long createTime = t.getCreateTime();
            if (createTime == null || createTime == 0)
            {
                t.setCreateTime(System.currentTimeMillis());
            }
            t.setLastVer(1);
            t.setIsValid(Base.TRUE);
            storageService.save(t);
            afterInsert(t);
        }
        else
        {
            t.increaseVersion();
            beforeUpdate(t);
            storageService.update(t);
        }
        afterSave(t);
        return t;
    }
    
    @Override
    public <T extends AbstractStorage> T saveProtocol(T t)
    {
        T oldObject = (T)storageService.get(t.getClass(), t.getId());
        if (oldObject != null)
        {
            storageService.update(t);
            afterInsert(t);
        }
        else
        {
            storageService.save(t);
            afterInsert(t);
        }
        return t;
    }
    
    @Override
    public <T extends AbstractStorage> T remove(Class<T> clazz, String id)
    {
        return storageService.remove(clazz, id);
    }
    
    @Override
    public <T extends AbstractStorage> T remove(T t)
    {
        return storageService.remove(t);
    }
    
    @Override
    public <T extends AbstractStorage> T get(Class<T> clazz, String id)
    {
        return storageService.get(clazz, id);
    }
    
    private String getLoadSql(String tableName)
    {
        return String.format(LOAD_SQL, tableName);
    }
    
    /** {@inheritDoc} */
    @Override
    public <T extends DataInit> List<T> query(Class<T> clazz, QueryBuilder queryBuilder)
    {
        String tableName = tableClassRegist.getTableName(clazz);
        if (tableName == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<String> params = processParameter(queryBuilder, sb);
        
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(tableName);
        if (sb.length() > 0)
        {
            sql.append(" where ").append(sb);
        }
        sql.append(generatorOrderBy(queryBuilder));
        SQLiteDatabase database = databaseProvider.getReadableDatabase(clazz);
        Cursor cursor = database.rawQuery(sql.toString(), (String[])params.toArray(new String[params.size()]));
        try
        {
            if (cursor.getCount() > 0)
            {
                List<T> result = loadData(clazz, cursor);
                return result;
            }
            else
            {
                return Collections.EMPTY_LIST;
            }
        }
        finally
        {
            cursor.close();
        }
    }
    
    /**
     * @param <T>
     * @param clazz
     * @param cursor
     * @return
     */
    private <T extends DataInit> List<T> loadData(Class<T> clazz, Cursor cursor)
    {
        List<T> result = new ArrayList<T>();
        while (!cursor.isLast())
        {
            cursor.moveToNext();
            try
            {
                T t = clazz.newInstance();
                if (t instanceof Base)
                {
                    Base base = (Base)t;
                    base.init(cursor);
                    result.add(t);
                }
            }
            catch (IllegalAccessException e)
            {
            }
            catch (InstantiationException e)
            {
            }
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public <T extends AbstractStorage> int count(Class<T> clazz, QueryBuilder queryBuilder)
    {
        String tableName = tableClassRegist.getTableName(clazz);
        if (tableName == null)
        {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        List<String> params = processParameter(queryBuilder, sb);
        
        StringBuilder sql = new StringBuilder();
        sql.append("select COUNT(*) from ").append(tableName);
        if (sb.length() > 0)
        {
            sql.append(" where ").append(sb);
        }
        Cursor cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql.toString(),
            (String[])params.toArray(new String[params.size()]));
        try
        {
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                return cursor.getInt(0);
            }
            else
            {
                return 0;
            }
        }
        finally
        {
            cursor.close();
        }
    }
    
    /**
     * 生成排序语句.
     * 
     * @param queryBuilder
     *            查询构造器.
     * @return 排序语句.
     */
    private String generatorOrderBy(QueryBuilder queryBuilder)
    {
        List<String> orderBys = queryBuilder.getOrderBy();
        if (orderBys == null || orderBys.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" order by ");
        for (String orderBy : orderBys)
        {
            sb.append(orderBy).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    /**
     * 处理生成条件及语句.
     * 
     * @param queryBuilder
     *            查询构造器.
     * @param sb
     *            语句结果.
     * @return 条件参数.
     */
    private List<String> processParameter(QueryBuilder queryBuilder, StringBuilder sb)
    {
        List<Criteria> criterias = queryBuilder.getCriterias();
        List<String> params = new ArrayList<String>();
        for (Criteria criteria : criterias)
        {
            if (criteria.isValid())
            {
                sb.append("(");
                List<String> criteriaWithoutValues = criteria.getCriteriaWithoutValue();
                if (criteriaWithoutValues != null && !criteriaWithoutValues.isEmpty())
                {
                    for (String whereCause : criteriaWithoutValues)
                    {
                        sb.append(whereCause).append(" and ");
                    }
                }
                List<ConditionValue> criteriaWithSingleValues = criteria.getCriteriaWithSingleValue();
                if (criteriaWithSingleValues != null && !criteriaWithSingleValues.isEmpty())
                {
                    for (ConditionValue conditionValue : criteriaWithSingleValues)
                    {
                        sb.append(conditionValue.getCondition()).append("? and ");
                        params.add(conditionValue.getValue());
                    }
                }
                List<InValue> criteriaWithListValue = criteria.getCriteriaWithListValue();
                if (criteriaWithListValue != null && !criteriaWithListValue.isEmpty())
                {
                    for (InValue inValue : criteriaWithListValue)
                    {
                        sb.append(inValue.getCondition());
                        List<String> values = inValue.getValues();
                        sb.append("(");
                        
                        for (String value : values)
                        {
                            sb.append("?,");
                            params.add(value);
                        }
                        sb.replace(sb.length() - 1, sb.length(), ")");
                        sb.append(" and ");
                    }
                }
                List<BetweenValue> criteriaWithBetweenValues = criteria.getCriteriaWithBetweenValue();
                if (criteriaWithBetweenValues != null && !criteriaWithBetweenValues.isEmpty())
                {
                    for (BetweenValue betweenValue : criteriaWithBetweenValues)
                    {
                        sb.append(betweenValue.getCondition()).append(" ? and ? ");
                        sb.append(" and ");
                        params.add(betweenValue.getBegin());
                        params.add(betweenValue.getEnd());
                    }
                }
                sb.replace(sb.length() - 5, sb.length(), "");
                sb.append(")");
                sb.append(" or ");
            }
        }
        if (sb.length() > 4)
        {
            sb.replace(sb.length() - 4, sb.length(), "");
        }
        return params;
    }
    
    /** {@inheritDoc} */
    @Override
    public <T extends DataInit> Page<T> query(Class<T> clazz, QueryBuilder queryBuilder, int pageNum, int pageSize)
    {
        String tableName = tableClassRegist.getTableName(clazz);
        if (tableName == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<String> params = processParameter(queryBuilder, sb);
        
        StringBuilder countSql = new StringBuilder();
        countSql.append("select COUNT(*) from ").append(tableName);
        if (sb.length() > 0)
        {
            countSql.append(" where ").append(sb);
        }
        Cursor cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(countSql.toString(),
            (String[])params.toArray(new String[params.size()]));
        int recordCount = 0;
        try
        {
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                recordCount = cursor.getInt(0);
            }
        }
        finally
        {
            cursor.close();
        }
        Page<T> page = new Page<T>();
        page.setRecordCount(recordCount);
        page.setPageSize(pageSize);
        page.page();
        page.setShowPage(pageNum);
        
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(tableName);
        if (sb.length() > 0)
        {
            sql.append(" where ").append(sb);
        }
        
        sql.append(generatorOrderBy(queryBuilder));
        sql.append(" limit ").append(pageSize).append(" offset ").append(page.getFirstResult());
        
        cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql.toString(),
            (String[])params.toArray(new String[params.size()]));
        try
        {
            if (cursor.getCount() > 0)
            {
                List<T> result = loadData(clazz, cursor);
                page.setRecords(result);
            }
            return page;
        }
        finally
        {
            cursor.close();
        }
    }
    
    @Override
    public <T extends DataInit> List<T> getAll(Class<T> clazz)
    {
        String tableName = tableClassRegist.getTableName(clazz);
        if (tableName == null)
        {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(tableName).append(" where isvalid=1");
        SQLiteDatabase database = databaseProvider.getReadableDatabase(clazz);
        Cursor cursor = database.rawQuery(sql.toString(), null);
        try
        {
            if (cursor.getCount() > 0)
            {
                List<T> result = loadData(clazz, cursor);
                return result;
            }
            else
            {
                return null;
            }
        }
        finally
        {
            cursor.close();
        }
    }
    
    @Override
    public <T> T selectOne(Class<T> clazz, String sql, String[] params, IExecuteCallback callback)
    {
        Cursor cursor = null;
        if (params != null && params.length > 0)
        {
            cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql, params);
        }
        else
        {
            cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql, null);
        }
        try
        {
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                return (T)callback.execute(cursor);
            }
            else
            {
                return null;
            }
        }
        finally
        {
            cursor.close();
        }
    }
    
    @Override
    public <T> List<T> selectList(Class<T> clazz, String sql, String[] params, IExecuteCallback callback)
    {
        Cursor cursor = null;
        if (params != null && params.length > 0)
        {
            cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql, params);
        }
        else
        {
            cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql, null);
        }
        try
        {
            if (cursor.getCount() > 0)
            {
                List<T> result = new ArrayList<T>();
                while (!cursor.isLast())
                {
                    cursor.moveToNext();
                    T t = (T)callback.execute(cursor);
                    result.add(t);
                }
                return result;
            }
            else
            {
                return Collections.EMPTY_LIST;
            }
        }
        finally
        {
            cursor.close();
        }
    }
    
    @Override
    public <T extends DataInit> List<T> selectList(Class<T> clazz, String sql, String[] params)
    {
        Cursor cursor = null;
        if (params != null && params.length > 0)
        {
            cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql, params);
        }
        else
        {
            cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql, null);
        }
        try
        {
            if (cursor.getCount() > 0)
            {
                List<T> result = new ArrayList<T>();
                while (!cursor.isLast())
                {
                    cursor.moveToNext();
                    try
                    {
                        T di = clazz.newInstance();
                        di.init(cursor);
                        result.add(di);
                    }
                    catch (IllegalAccessException e)
                    {
                    }
                    catch (InstantiationException e)
                    {
                    }
                }
                return result;
            }
            else
            {
                return Collections.EMPTY_LIST;
            }
        }
        finally
        {
            cursor.close();
        }
    }
    
    @Override
    public <T> List<T> selectList(Class<T> clazz, SqlQueryBuilder sqlQueryBuilder, IExecuteCallback callback)
    {
        String sql = sqlQueryBuilder.getSql();
        String[] params = sqlQueryBuilder.getParams();
        return selectList(clazz, sql, params, callback);
    }
    
    @Override
    public <T extends DataInit> List<T> selectList(Class<T> clazz, SqlQueryBuilder sqlQueryBuilder)
    {
        String sql = sqlQueryBuilder.getSql();
        String[] params = sqlQueryBuilder.getParams();
        return selectList(clazz, sql, params);
    }
    
    @Override
    public <T> T selectOne(Class<T> clazz, SqlQueryBuilder sqb, IExecuteCallback callback)
    {
        return selectOne(clazz, sqb.getSql(), sqb.getParams(), callback);
    }
    
    @Override
    public <T extends DataInit> List<T> queryForList(Class<T> clazz, QueryBuilder qb, int first, int maxCount)
    {
        String tableName = tableClassRegist.getTableName(clazz);
        if (tableName == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<String> params = processParameter(qb, sb);
        
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(tableName);
        if (sb.length() > 0)
        {
            sql.append(" where ").append(sb);
        }
        
        sql.append(generatorOrderBy(qb));
        sql.append(" limit ").append(maxCount).append(" offset ").append(first);
        
        Cursor cursor = databaseProvider.getReadableDatabase(clazz).rawQuery(sql.toString(),
            (String[])params.toArray(new String[params.size()]));
        try
        {
            if (cursor.getCount() > 0)
            {
                return loadData(clazz, cursor);
            }
            return Collections.EMPTY_LIST;
        }
        finally
        {
            cursor.close();
        }
    }
    
    @Override
    public void clearCache()
    {
        if (storageService != null)
        {
            storageService.clear();
        }
    }
}
