package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 检查组实现类
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {


    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 保存检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, List<Integer> checkitemIds) {
        //1.保存检查组表
        checkGroupDao.add(checkGroup);
        //2.保存检查组后 checkGroup.getId()
        Integer groupId = checkGroup.getId();//检查组id
        //3.再循环往中间表插入数据 检查组id 检查项id(抽取一个公共方法出来)
        this.setCheckGroupAndCheckItem(groupId,checkitemIds);
    }

    @Override
    public Result findPage(String queryString, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);//返回结果的 插件定义Page<T>
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, new PageResult(checkGroupPage.getTotal(), checkGroupPage.getResult()));
    }

    /**
     * 根据检查组id查询检查组对象
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }
    /**
     * 根据检查组id查询检查项ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        Integer groupId = checkGroup.getId();

        //A.根据id更新检查组对象
        checkGroupDao.edit(checkGroup);
        //B.先根据检查组id将中间表已经关联的检查项记录删除
        checkGroupDao.deleteAssociation(groupId);
        //C.根据检查组id和检查项ids再重新关联
        this.setCheckGroupAndCheckItem(groupId,checkitemIds);
    }
    /**
     * 查询所有检查组数据
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 检查组检查项中间表公共方法
     * @param groupId
     * @param checkitemIds
     */
    public void setCheckGroupAndCheckItem(Integer groupId,Integer[] checkitemIds){
        //检查组ids非空判断
        if(checkitemIds != null && checkitemIds.length >0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    /**
     * 检查组检查项中间表公共方法
     */
    public void setCheckGroupAndCheckItem(Integer groupId,List<Integer> checkitemIds){
        //检查组ids非空判断
        if(checkitemIds != null && checkitemIds.size()>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
