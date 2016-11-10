package com.team.zhuoke.androidjsoup.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static void copyFile(File sourceFile,File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff=new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff=new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len =inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        //关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }
}
