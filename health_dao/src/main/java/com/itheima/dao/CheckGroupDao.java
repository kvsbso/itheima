package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 中间包插入数据
     * @param map
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 检查组分页
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);


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
     * 修改检查组数据
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 清空中间表绑定的id
     * @param groupid
     */
    void deleteAssociation(Integer groupid);

    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();

}
