package com.team.zhuoke.androidjsoup.db.table;

import com.team.zhuoke.androidjsoup.db.base.BaseMyData;
import com.team.zhuoke.androidjsoup.db.interfaces.IChangeObject;
import com.team.zhuoke.androidjsoup.db.interfaces.IOp;

/**
 * 
 * 任务表
 *
 * <p>detailed comment
 * @author ztw 2016年5月12日
 * @see
 * @since 1.0
 */
public class MyData extends BaseMyData implements IOp, IChangeObject
{
    
    @Override
    public void setOpUserId(String opUserId)
    {
        
    }
    
    @Override
    public boolean isChanged(Object newObject)
    {
        if (newObject == null)
        {
            return true;
        }
        if (!(newObject instanceof MyData))
        {
            return false;
        }
        MyData task = (MyData)newObject;
        StringBuffer sBufferThis = new StringBuffer();
        StringBuffer sBufferOld = new StringBuffer();
        sBufferThis.append(this.getId())
            .append(",")
            .append(this.getIsValid())
            .append(",")
            .append(this.getCreateTime())
            .append(",")
            .append(this.getLastVer());
        sBufferOld.append(task.getId())
            .append(",")
            .append(task.getIsValid())
            .append(",")
            .append(task.getCreateTime())
            .append(",")
            .append(task.getLastVer());
        return sBufferThis.toString().equals(sBufferOld.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyData))
            return false;
        if (getClass() != obj.getClass())
        {
            return false;
        }
        MyData myData = (MyData)obj;
        return myData.getNoteId().equals(getNoteId());
    }

    @Override
    public String toString() {
        return "BaseMyData{" +
                "current='" + current + '\'' +
                ", noteId='" + noteId + '\'' +
                ", expected='" + expected + '\'' +
                ", loss='" + loss + '\'' +
                ", price='" + price + '\'' +
                ", endTime='" + endTime + '\'' +
                ", page_address='" + pageAddress + '\'' +
                '}';
    }
}
