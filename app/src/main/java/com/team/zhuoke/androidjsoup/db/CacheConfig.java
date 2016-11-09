package com.team.zhuoke.androidjsoup.db;

import com.team.zhuoke.androidjsoup.db.table.MyData;

/**
 * 
 * 配置缓存
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class CacheConfig
{
    private StorageService storageService;
    
    public CacheConfig(StorageService storageService)
    {
        super();
        this.storageService = storageService;
    }
    
    public void config()
    {
        storageService.initCache(MyData.class, 300);
    }
}
