package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 业务处理层的接口
 */
public interface CheckItemService {
    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    Result findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 删除检查项
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id查询编辑检查项数据
     * @param id
     * @return
     */
    CheckItem findById(Integer id);

    /**
     * 修改检查项数据
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

}
