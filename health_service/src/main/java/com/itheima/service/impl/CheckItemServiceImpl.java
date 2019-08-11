package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项业务层 具体实现
 */
@Transactional
@Service(interfaceClass = CheckItemService.class) //指定创建服务的包
public class CheckItemServiceImpl implements CheckItemService {


    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     *
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页查询
     * Result:
     * {
     * flag:true
     * message:'xxxxx',
     * data:{
     * total:100,
     * rows:[
     * {xxxxxx},
     * {yyyyyy}
     * ]
     * }
     * }
     */
    @Override
    public Result findPage(String queryString, Integer currentPage, Integer pageSize) {
        //PageHelper分页对象 调用静态方法startPage实现分页
        //传入两个参数 PageNum 当前页面 pageSize:每页显示记录数
        PageHelper.startPage(currentPage, pageSize);
        //紧跟着第二行就是需要分页的查询语句 select * from t_checkitem limit 0,10
        //pageHelper插件拦截查询表语句，动态为语句添加分页条件，查询分页数据
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);//返回结果的 插件定义Page<T>
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, new PageResult(checkItemPage.getTotal(), checkItemPage.getResult()));
    }

    /**
     * 检查项删除
     *
     * @return
     */
    @Override
    public void deleteById(Integer itemId) {
        //1.根据检查项id查询检查项和检查组的中间表
        int count = checkItemDao.findCountByCheckItemId(itemId);
        //2.如果数据存入抛出异常
        if (count > 0) {
            throw new RuntimeException("检查项和检查组已经关联，无法删除");
        }
        //3.如果数据不存在直接删除
        checkItemDao.deleteById(itemId);
    }

    /**
     * 根据检查项id查询检查项数据
     *
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 编辑检查项数据
     *
     * @return
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
