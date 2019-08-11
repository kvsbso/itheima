package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 业务处理层 服务接口
 */
public interface CheckItemService {
    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @return
     */
    Result findPage(String queryString, Integer currentPage, Integer pageSize);
    /**
     * 检查项删除
     * @return
     */
    void deleteById(Integer id);
    /**
     * 根据检查项id查询检查项数据
     * @return
     */
    CheckItem findById(Integer id);
    /**
     * 编辑检查项数据
     * @return
     */
    void edit(CheckItem checkItem);

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();
}
