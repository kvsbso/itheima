package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项控制层管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(CheckItemController.class);

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody CheckItem checkItem){
        //调用service服务保存数据
        try {
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    /**
     * 检查项分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')") //当前用户访问findPage需要CHECKITEM_QUERY权限
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            //error  warn info debug
            logger.info("**********************************info**********************************");
            logger.error("**********************************error**********************************");
            logger.warn("**********************************warn**********************************");
            logger.debug("**********************************debug**********************************");
            logger.trace("**********************************trace**********************************");
            //info:一般输入输出
            //error:异常的时候
            //warn:业务警告
            //debug:开发阶段 需要输出数据的地方打印
            //trace最详细的 了解即可
            Result result = checkItemService.findPage(queryPageBean.getQueryString(),queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(e.getMessage());
            //失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL,e.getMessage());
        }
    }


    /**
     * 检查项删除
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){
        try {
            checkItemService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL,e.getMessage());
        }
    }


    /**
     * 根据检查项id查询检查项数据
     * @return
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){
        try {
            CheckItem checkItem =  checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }
        catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL,e.getMessage());
        }
    }


    /**
     * 编辑检查项数据
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        }
        catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL,e.getMessage());
        }
    }


    /**
     * 查询所有检查项数据
     * @return
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }
        catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL,e.getMessage());
        }
    }
}
