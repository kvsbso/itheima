package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 清理图片任务类
 */
@Component
public class ClearImg {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理图片任务方法
     * 1.删除redis中图片记录
     * 2.删除七牛云上图片
     */
    public void deleteImg(){
        //1.获取redis中两个集合中的数据 相减
        //sdiff 参数1：自动上传图片的记录 - 参数2：确定上传图片的记录 ==差值结果
        Set<String> setImg = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //2.得到垃圾图片集合
        for (String img : setImg) {
            // 1.删除七牛云上图片
            QiniuUtils.deleteFileFromQiniu(img);
            //2.删除redis中图片记录  参数1:需要删除数据集合名称 参数2:value值
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,img);
            System.out.println("删除成功了。。。。");
        }

    }

}
