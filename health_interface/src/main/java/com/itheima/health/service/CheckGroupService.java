package com.itheima.health.service;

import com.itheima.health.pojo.CheckGroup;

public interface CheckGroupService {



    void add(CheckGroup checkGroup, Integer[] checkitemIds);
}