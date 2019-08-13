package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
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


    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        try {
            String originalFilename = imgFile.getOriginalFilename();

            String extension = FilenameUtils.getExtension(originalFilename);
            String s = UUID.randomUUID().toString() + "." + extension;
            //调用七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),s);

            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,s);

            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,s);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,setmeal.getImg());
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 套餐分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            Result result = setmealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
}
