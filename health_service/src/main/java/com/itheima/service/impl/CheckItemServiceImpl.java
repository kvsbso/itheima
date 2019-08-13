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
 * 检查项业务层,具体实现
 */

@Transactional
@Service(interfaceClass = CheckItemService.class)//指定创建服务的包
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public Result findPage(Integer currentPage, Integer pageSize, String queryString) {
        //分页对象,调用startPage方法
        //传入两个参数,第一个为当前页,第二个为每页显示总条数
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);

        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,new PageResult(checkItemPage.getTotal(),checkItemPage.getResult() ));
    }

    /**
     * 删除检查项
     * @param id
     */
    @Override
    public void delete(Integer id) {
        int count = checkItemDao.finddeleteById(id);
        System.out.println(count);

        if (count > 0){
            throw new RuntimeException("删除检查项失败!");
        }
        checkItemDao.deleteById(id);
    }

    /**
     * 根据id查询编辑检查项数据
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 修改检查项数据
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
