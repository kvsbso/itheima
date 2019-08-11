package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;

import java.util.List; /**
 * 检查组服务接口
 */
public interface CheckGroupService {

    /**
     * 新增检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, List<Integer> checkitemIds);

    /**
     * 检查组分页
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @return
     */
    Result findPage(String queryString, Integer currentPage, Integer pageSize);

    /**
     * 根据检查组id查询检查组对象
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询检查项ids
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 编辑检查项
     * @param checkitemIds
     * @param checkGroup
     */
    void edit(Integer[] checkitemIds, CheckGroup checkGroup);

    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();
}
