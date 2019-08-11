package com.itheima.constant;

/**
 * RedisConstant常量类
 * 为图片上传记录
 *  key value
 *  key：setmealPicResources setmealPicDbResources
 *  value:集合
 */
public class RedisConstant {
    //套餐图片所有图片名称
    public static final String SETMEAL_PIC_RESOURCES = "setmealPicResources";
    //套餐图片保存在数据库中的图片名称
    public static final String SETMEAL_PIC_DB_RESOURCES = "setmealPicDbResources";
}