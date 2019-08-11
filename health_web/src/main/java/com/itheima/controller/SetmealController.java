package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐控制层管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;



    /**
     * 套餐分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            Result result = setmealService.findPage(queryPageBean.getQueryString(),queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL,e.getMessage());
        }
    }

    /**
     * 图片上传
     * MultipartFile：接收文件对象
     * 方式一：@RequestParam("imgFile") MultipartFile imgFile
     * 方式二：MultipartFile imgFile
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile imgFile){
        try {
            //1.
            String originalFilename = imgFile.getOriginalFilename();//获取原始文件名
            //2.通过规则生成文件名
            String suffix = FilenameUtils.getExtension(originalFilename);
            String newFileName = UUID.randomUUID().toString() + "." + suffix;
            System.out.println("**************************"+newFileName);
            //3.调用七牛云api
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),newFileName);
            //4.表示上传图片成功 自动上传的集合setmealPicDbResources
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);//实际存储到七牛云文件名
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 新增套餐
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            //保存套餐成功后 图片保存数据库记录 集合setmealPicResources
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return  new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }
}
