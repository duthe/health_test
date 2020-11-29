package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;


    /**
     * 添加检查组 需要有添加检查组权限
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupService.add(checkGroup, checkitemIds);
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询 需要有查询检查组权限
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findByPage")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckGroup> checkGroupPageResult = checkGroupService.findByPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupPageResult);
    }


    /**
     * 根据id查找检查组 需要有查询检查组权限
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public Result findById(int id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
    }

    /**
     * 根据检查组id查找关联的检查项 需要有查询检查组权限  需要有查询检查项权限？
     * @param checkGroupId
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int checkGroupId){
        List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(checkGroupId);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
    }


    /**
     * 更新检查组信息 需要有编辑检查组权限
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupService.update(checkGroup, checkitemIds);
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);

    }

    /**
     * 根据id删除检查组
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(int id) {
            checkGroupService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }


    /**
     * 查找所有检查组
     * @return 返回检查组集合
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    }



}
