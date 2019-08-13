package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;

import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 检查组控制层管理
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;


    /**
     * 新增检查组
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, @RequestParam List<Integer> checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 检查组分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            Result result = checkGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        //调用service
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL, e.getMessage());
        }
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        //调用service
        try {
            List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, @RequestParam List<Integer> checkitemIds) {
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
