package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImg {

    @Autowired
    private JedisPool jedisPool;

    public void deleteImg(){
        //获取redis中两个集合,相减
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //遍历集合
        for (String s : sdiff) {
            //删除七牛云图片
            QiniuUtils.deleteFileFromQiniu(s);
            //删除redis图片
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
        }
    }
}
