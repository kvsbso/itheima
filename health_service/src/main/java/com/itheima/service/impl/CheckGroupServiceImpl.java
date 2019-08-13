package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass =CheckGroupService.class )
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, List<Integer> checkitemIds) {
        //保存检查组表
        checkGroupDao.add(checkGroup);
        //获取检查组id
        Integer groupid = checkGroup.getId();
        //循环往中间表插入数据
        this.setCheckGroupAndCheckItem(groupid,checkitemIds );

    }

    /**
     * 检查组分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public Result findPage(Integer currentPage, Integer pageSize, String queryString) {
        //传入两个参数,第一个为当前页,第二个为每页显示总条数
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> checkGroups = checkGroupDao.selectByCondition(queryString);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,new PageResult(checkGroups.getTotal(),checkGroups.getResult()));
    }
    /*
    * @Override
    public Result findPage(String queryString, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);//返回结果的 插件定义Page<T>
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, new PageResult(checkGroupPage.getTotal(), checkGroupPage.getResult()));
    }*/

    /**
     * 根据id查询所有检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组的id,查询所有中间表所有关联的ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组信息
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, List<Integer> checkitemIds) {
        //修改检查组数据
        checkGroupDao.edit(checkGroup);
        //获取检查组id
        Integer groupid = checkGroup.getId();
        //清空中间表的id
        checkGroupDao.deleteAssociation(groupid);
        //循环往中间表插入数据
        this.setCheckGroupAndCheckItem(groupid,checkitemIds );
    }

    /**
     * 查询所有检查组数据
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    public void setCheckGroupAndCheckItem(Integer groupid, List<Integer> checkitemIds){
        if (checkitemIds != null && checkitemIds.size()>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("groupid", groupid);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }

    }

    /*
    * public void setCheckGroupAndCheckItem(Integer groupId,List<Integer> checkitemIds){
        //检查组ids非空判断
        if(checkitemIds != null && checkitemIds.size()>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }*/
}
