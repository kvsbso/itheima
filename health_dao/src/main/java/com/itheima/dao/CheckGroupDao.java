package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map; /**
 * 检查组接口
 */
public interface CheckGroupDao {

    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 通过检查组往中间表插入数据
     * @param map
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 检查组分页
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);
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
     * 根据id更新检查组对象
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 先根据检查组id将中间表已经关联的检查项记录删除
     * @param groupId
     */
    void deleteAssociation(Integer groupId);
    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();
}
