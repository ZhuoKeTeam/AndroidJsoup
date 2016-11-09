package com.team.zhuoke.androidjsoup.db.base;


import android.content.ContentValues;
import android.database.Cursor;

import com.team.zhuoke.androidjsoup.db.Base;

/**
 * 
 * 任务表基础类
 *
 * <p>detailed comment
 * @author ztw 2016年5月12日
 * @see
 * @since 1.0
 */
public class BaseMyData extends Base
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 表名
     */
    public static final String TABLE_NAME = "SHARES";

    /**
     * NoteId字段
     */
    public static final String NOTEID = "NOTEID";

    /**
     * 预期收益字段
     */
    public static final String EXPECTED = "EXPECTED";

    /**
     * 当前收益字段
     */
    public static final String CURRENT = "CURRENT";

    /**
     * 止损字段
     */
    public static final String LOSS = "LOSS";

    /**
     * 价格字段
     */
    public static final String PRICE = "PRICE";

    /**
     * NoteId
     */
    private String noteId;

    /**
     * 预期收益
     */
    private String expected;
    /**
     *当前收益
     */
    private String current;
    /**
     *止损
     */
    private String loss;
    /**
     *价格
     */
    private String price;

    /**
     * 得到NoteId
     * @return
     */
    public String getNoteId() {
        return noteId;
    }

    /**
     *设置NoteId
     * @param noteId
     */
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
    /**
     *得到预期收益
     * @return
     */
    public String getExpected() {
        return expected;
    }

    /**
     * 设置预期收益
     * @param expected
     */
    public void setExpected(String expected) {
        this.expected = expected;
    }
    /**
     *得到当前收益
     * @return
     */
    public String getCurrent() {
        return current;
    }

    /**
     * 设置当前收益
     * @param current
     */
    public void setCurrent(String current) {
        this.current = current;
    }
    /**
     *得到止损
     * @return
     */
    public String getLoss() {
        return loss;
    }

    /**
     * 设置止损
     * @param loss
     */
    public void setLoss(String loss) {
        this.loss = loss;
    }
    /**
     *得到价格
     * @return
     */
    public String getPrice() {
        return price;
    }

    /**
     * 设置价格
     * @param price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    protected void processContentValues(ContentValues contentValues)
    {
        put(contentValues, NOTEID, noteId);
        put(contentValues, EXPECTED, expected);
        put(contentValues, CURRENT, current);
        put(contentValues, LOSS, loss);
        put(contentValues, PRICE, price);
    }
    
    @Override
    protected void doInit(Cursor cursor)
    {
        super.doInit(cursor);
        noteId = cursor.getString(cursor.getColumnIndex(NOTEID));
        expected = cursor.getString(cursor.getColumnIndex(EXPECTED));
        current = cursor.getString(cursor.getColumnIndex(CURRENT));
        loss = cursor.getString(cursor.getColumnIndex(LOSS));
        price = cursor.getString(cursor.getColumnIndex(PRICE));
    }
    
    @Override
    public String getTableName()
    {
        return TABLE_NAME;
    }
    
}
