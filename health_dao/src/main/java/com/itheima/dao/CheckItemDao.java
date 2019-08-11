package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项持久层 接口
 */
public interface CheckItemDao {
    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 根据条件 查询分页数据
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);
    /**
     * 检查项删除
     * @return
     */
    void deleteById(Integer id);

    /**
     * 根据检查项id查询中间表数据
     * @param itemId
     * @return
     */
    int findCountByCheckItemId(Integer itemId);
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
