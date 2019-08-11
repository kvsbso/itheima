package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.apache.ibatis.annotations.Param;
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
     * @param checkGroup  检查组对象
     * @param checkitemIds  检查项ids
     * @return  返回成功或失败
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody CheckGroup checkGroup,@RequestParam List<Integer> checkitemIds){
        //控制层一般都是接收请求 和响应数据
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return  new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
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
            Result result = checkGroupService.findPage(queryPageBean.getQueryString(),queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL,e.getMessage());
        }
    }

    /**
     * 根据检查组id查询检查组对象
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组id查询检查项ids
     */
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId",method = RequestMethod.GET)
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
            List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_IDS_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_IDS_FAIL);
        }
    }

    /**
     * 编辑检查组 edit
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(Integer[] checkitemIds,@RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.edit(checkitemIds,checkGroup);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询所有检查组
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

}
