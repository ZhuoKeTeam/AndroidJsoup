package com.team.zhuoke.androidjsoup.db;

import com.team.zhuoke.androidjsoup.db.table.MyData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 表名与类名的对映关系注册表类
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class TableClassRegist implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 表名和类的映射
     */
    private Map<String, Class<? extends Base>> tableClass = new HashMap<String, Class<? extends Base>>();
    
    /**
     * 类和表名的映射
     */
    private Map<Class<? extends Base>, String> classTable = new HashMap<Class<? extends Base>, String>();
    
    public TableClassRegist()
    {
        init();
    }
    
    private void init()
    {
        regist(MyData.TABLE_NAME, MyData.class);
    }
    
    /**
     * 将表名和类名注册到表名与类的对映关系中去.
     * 
     * @param tablename
     *            表名.
     * @param clazz
     *            类.
     */
    private void regist(String tablename, Class<? extends Base> clazz)
    {
        tableClass.put(tablename, clazz);
        classTable.put(clazz, tablename);
    }
    
    public Set<Map.Entry<String, Class<? extends Base>>> entrySet()
    {
        return tableClass.entrySet();
    }
    
    /**
     * 根据类得到表名.
     * @param clazz 类.
     * @return 表名.
     */
    public String getTableName(Class clazz)
    {
        return classTable.get(clazz);
    }
    
    public Class getClass(String tableName)
    {
        return tableClass.get(tableName.toUpperCase());
    }
    
}
