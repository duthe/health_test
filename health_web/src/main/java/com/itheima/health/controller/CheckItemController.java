package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HEALTH_MANAGER')")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;


    /**
     * 查找所有检查项
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckItem> checkItemList = checkItemService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
    }


    /**
     * 添加检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        checkItemService.add(checkItem);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    /**
     * 分页查找检查项
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<CheckItem> checkItemPageResult = checkItemService.findByPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemPageResult);
    }


    /**
     * 根据id查找检查项
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id){
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
    }


    /**
     * 更新检查项信息
     * @param checkItem
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {
        checkItemService.update(checkItem);
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 根据id删除检查项
     * @param id
     * @return
     */
    @PostMapping
    @RequestMapping("/deleteById")
    public Result DeleteById(int id){
        checkItemService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}
