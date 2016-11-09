package com.team.zhuoke.androidjsoup.db.query;

import java.util.List;

/**
 * 
 * 分页
 * <T>分页结果集里的对象类型
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class Page<T>
{
    /**
     * 默认每页记录条数
     */
    public static final int DEFAULT_PAGE_SIZE = 50;
    
    /**
     * 显示几个页码
     */
    public static final int DEFAULT_PAGE_NUM = 8;
    
    /**
     * 每页记录条数
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    
    /**
     * 显示第几页
     */
    private int showPage;
    
    /**
     * 总页数
     */
    private int pageCount;
    
    /**
     * 总记录数
     */
    /**
     * 总记录数
     */
    private int recordCount;
    
    /**
     * 分页结果记录集
     */
    private List<T> records;
    
    /**
     * 显示页码数量
     */
    private int showPageNum = DEFAULT_PAGE_NUM;
    
    /**
     * 默认构造器.
     */
    public Page()
    {
        showPage = 1;
    }
    
    /**
     * 得到每页记录条数.
     * 
     * @return 每页记录条数.
     */
    public int getPageSize()
    {
        return pageSize;
    }
    
    /**
     * 设置每页记录条数.
     * 
     * @param pageSize
     *            每页记录条数.
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }
    
    /**
     * 得到总页数.
     * 
     * @return 总页数.
     */
    public int getPageCount()
    {
        return pageCount;
    }
    
    /**
     * 设置总页数.
     * 
     * @param pageCount
     *            总页数.
     */
    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }
    
    /**
     * 得到显示第几页.
     * 
     * @return 显示第几页.
     */
    public int getShowPage()
    {
        return showPage;
    }
    
    /**
     * 设置显示第几页.
     * 
     * @param showPage
     *            显示页数.
     */
    public void setShowPage(int showPage)
    {
        if (showPage < 1)
        {
            this.showPage = 1;
        }
        else
        {
            this.showPage = showPage;
        }
    }
    
    /**
     * 得到显示页码数量.
     * 
     * @return 显示页码数量.
     */
    public int getShowPageNum()
    {
        return showPageNum;
    }
    
    /**
     * 设置显示页码数量.
     * 
     * @param showPageNum
     *            显示页码数量.
     */
    public void setShowPageNum(int showPageNum)
    {
        if (showPageNum < 1)
        {
            this.showPageNum = 1;
        }
        else
        {
            this.showPageNum = showPageNum;
        }
    }
    
    /**
     * 得到当前开始页数. <br>
     * 在显示部分页的情况下使用.
     * 
     * @return 开始页数.
     */
    public int getFromPage()
    {
        int pc = (showPage + showPageNum - 1) / showPageNum;
        
        int fPage = (pc - 1) * showPageNum + 1;
        fPage = fPage > pageCount ? pageCount : fPage;
        return fPage < 1 ? 1 : fPage;
    }
    
    /**
     * 得到当前结束页数. <br>
     * 在显示部分页的情况下使用.
     * 
     * @return 结束页数.
     */
    public int getToPage()
    {
        int pc = (showPage + showPageNum - 1) / showPageNum;
        int fPage = pc * showPageNum;
        fPage = fPage > pageCount ? pageCount : fPage;
        return fPage < 1 ? 1 : fPage;
        
    }
    
    /**
     * 得到总记录数.
     * 
     * @return 总记录数.
     */
    public int getRecordCount()
    {
        return recordCount;
    }
    
    /**
     * 设置总记录数.
     * 
     * @param recordCount
     *            总记录数.
     */
    public void setRecordCount(int recordCount)
    {
        this.recordCount = recordCount;
    }
    
    /**
     * 得到分页结果记录集.
     * 
     * @return 分页结果记录集.
     */
    public List<T> getRecords()
    {
        return records;
    }
    
    /**
     * 设置分页结果记录集.
     * 
     * @param records
     *            分页结果记录集.
     */
    public void setRecords(List<T> records)
    {
        this.records = records;
    }
    
    /**
     * 得到下一页页数.
     * 
     * @return 下一页页数.
     */
    public int getNextPage()
    {
        int ri = showPage + 1;
        return ri >= pageCount ? pageCount : ri;
        
    }
    
    /**
     * 得到上一页页数.
     * 
     * @return 上一页页数.
     */
    public int getPrePage()
    {
        int ri = showPage - 1;
        return ri <= 0 ? 1 : ri;
    }
    
    /**
     * 得到取数据的开始记录位置.
     * 
     * @return 取数据的开始记录位置.
     */
    public int getFirstResult()
    {
        int first = (showPage - 1) * pageSize;
        return first < 0 ? 0 : first;
    }
    
    /**
     * 执行分页计算.
     */
    public void page()
    {
        pageCount = recordCount / pageSize + (recordCount % pageSize == 0 ? 0 : 1);
    }
}
