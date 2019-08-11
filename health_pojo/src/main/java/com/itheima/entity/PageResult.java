package com.itheima.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装对象
 * 页面上会使用分页组件
 *    此组件只需要返回两个数据
 *    total：总记录数
 *    rows: 当前页码数据
 */
public class PageResult implements Serializable{
    private Long total;//总记录数
    private List rows;//当前页结果
    public PageResult(Long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public List getRows() {
        return rows;
    }
    public void setRows(List rows) {
        this.rows = rows;
    }
}
