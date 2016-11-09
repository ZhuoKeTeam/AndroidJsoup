package com.team.zhuoke.androidjsoup.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.team.zhuoke.androidjsoup.db.interfaces.DataInit;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 可持久化积累抽象
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public abstract class AbstractStorage implements Cloneable, Serializable, DataInit
{
    /**
     * [serialVersionUID]
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * ID对应的字段
     */
    public static final String ID = "ID";
    
    /**
     * 版本号对应的字段
     */
    public static final String LASTVER = "LASTVER";
    
    /**
     * 是否有效对应的字段
     */
    public static final String ISVALID = "ISVALID";
    
    /**
     * 创建时间对应的字段
     */
    public static final String CREATETIME = "CREATETIME";
    
    /**
     * 修改时间对应的字段
     */
    public static final String OPTIME = "OPTIME";
    
    /**
     * 是
     */
    public static final Short TRUE = (short)1;
    
    /**
     * 否
     */
    public static final Short FALSE = (short)0;
    
    /**
     * ID
     */
    private String id;
    
    /**
     * 版本号
     */
    private Integer lastVer;
    
    /**
     * 是否有效
     */
    private Short isValid;
    
    private Long createTime;
    
    private Long opTime;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public Integer getLastVer()
    {
        return lastVer;
    }
    
    public void setLastVer(Integer lastVer)
    {
        this.lastVer = lastVer;
    }
    
    public Long getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Long createTime)
    {
        this.createTime = createTime;
    }
    
    public Long getOpTime()
    {
        return opTime;
    }
    
    public void setOpTime(Long opTime)
    {
        this.opTime = opTime;
    }
    
    /**
     * 得到是否有效.
     *
     * @return 是否有效.
     */
    public Short getIsValid()
    {
        return isValid;
    }
    
    /**
     * 设置是否有效.
     *
     * @param isValid 是否有效.
     */
    public void setIsValid(Short isValid)
    {
        this.isValid = isValid;
    }
    
    /**
     * 增加版本号.
     */
    public void increaseVersion()
    {
        lastVer++;
    }
    
    /**
     * 得到上下文内容.
     *
     * @return 上下文内容.
     */
    protected ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(ISVALID, isValid);
        contentValues.put(LASTVER, lastVer);
        contentValues.put(CREATETIME, createTime);
        contentValues.put(OPTIME, opTime);
        processContentValues(contentValues);
        return contentValues;
    }
    
    /**
     * 置入值.
     *
     * @param contentValues 内容容器.
     * @param key           键 .
     * @param value         值.
     */
    protected void put(ContentValues contentValues, String key, Object value)
    {
        if (value == null)
        {
            contentValues.putNull(key);
        }
        else if (value instanceof String)
        {
            contentValues.put(key, (String)value);
        }
        else if (value instanceof Long)
        {
            contentValues.put(key, (Long)value);
        }
        else if (value instanceof Integer)
        {
            contentValues.put(key, (Integer)value);
        }
        else if (value instanceof Short)
        {
            contentValues.put(key, (Short)value);
        }
        else if (value instanceof byte[])
        {
            contentValues.put(key, (byte[])value);
        }
        else if (value instanceof Byte)
        {
            contentValues.put(key, (Byte)value);
        }
        else if (value instanceof Boolean)
        {
            contentValues.put(key, (Boolean)value);
        }
        else if (value instanceof Float)
        {
            contentValues.put(key, (Float)value);
        }
        else if (value instanceof Double)
        {
            contentValues.put(key, (Double)value);
        }
        else if (value instanceof Date)
        {
            Date date = (Date)value;
            contentValues.put(key, date.getTime());
        }
    }
    
    /**
     * 保存.
     */
    public void save(SQLiteDatabase db)
    {
        db.insert(getTableName(), null, getContentValues());
    }
    
    /**
     * 更新.
     */
    public void update(SQLiteDatabase db)
    {
        db.update(getTableName(), getContentValues(), "ID=?", new String[] {id});
    }
    
    /**
     * 删除.
     */
    public void delete(SQLiteDatabase db)
    {
        db.delete(getTableName(), "ID=?", new String[] {id});
    }
    
    /**
     * 利用游标初始化对象.
     */
    public void init(Cursor cursor)
    {
        id = cursor.getString(cursor.getColumnIndex(ID));
        isValid = cursor.getShort(cursor.getColumnIndex(ISVALID));
        lastVer = cursor.getInt(cursor.getColumnIndex(LASTVER));
        createTime = cursor.getLong(cursor.getColumnIndex(CREATETIME));
        opTime = cursor.getLong(cursor.getColumnIndex(OPTIME));
        doInit(cursor);
    }
    
    /**
     * 处理内容容器.
     *
     * @param contentValues 内容容器.
     */
    protected abstract void processContentValues(ContentValues contentValues);
    
    /**
     * 得到表名.
     *
     * @return 得到表名.
     */
    public abstract String getTableName();
    
    /**
     * 初始化对象.
     *
     * @param cursor 游标对象.
     */
    protected void doInit(Cursor cursor)
    {
        
    }
    
    @Override
    public Object clone()
        throws CloneNotSupportedException
    {
        return super.clone();
    }
}
