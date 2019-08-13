package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项持久层
 */

public interface CheckItemDao {
    //新增检查项
    void add(CheckItem checkItem);

    //检查项分页
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 删除检查项
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询中间表是否存在
     * @param id
     * @return
     */
    int finddeleteById(Integer id);

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
