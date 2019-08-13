package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    /**
     * 新增检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, List<Integer> checkitemIds);

    /**
     * 检查组分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    Result findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据id查询所有检查组
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组的id,查询所有中间表所有关联的ids
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 修改检查组信息
     * @param checkGroup
     * @param checkitemIds
     */
    void edit(CheckGroup checkGroup, List<Integer> checkitemIds);

    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();

}
