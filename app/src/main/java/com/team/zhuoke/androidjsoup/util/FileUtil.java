package com.team.zhuoke.androidjsoup.util;

import android.text.TextUtils;

import java.io.File;

public class FileUtil
{
    public static void createFile(File file)
    {
        try
        {
            file.createNewFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static File makeDirIfNotExist(File folder)
    {
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        return folder;
    }
    
    /**
     * 根据路径删除文件
     * @param path
     */
    public static void delFileByPath(String path)
    {
        if (!TextUtils.isEmpty(path))
        {
            File file = new File(path);
            if (file.exists())
            {
                file.delete();
            }
        }
    }
    
    /**
     * 根据路径删除文件
     * @param path
     */
    public static File getFileByPath(String path)
    {
        if (!TextUtils.isEmpty(path))
        {
            return new File(path);
        }
        return null;
    }
}
