package com.pr0xs1.campusfix.service;

import com.pr0xs1.campusfix.pojo.Building;
import com.pr0xs1.campusfix.pojo.PageResult;

import java.util.List;

public interface BuildingService {
    /** 分页查询建筑信息
     * @param page 当前页码
     * @param pageSize 每页显示条数
     * @param keyword 查询关键字
     * @param campus 校区
     * @return PageResult 分页结果
     */
    PageResult pageQuery(Integer page, Integer pageSize, String keyword, String campus);

    void deleteById(Integer id);

    void add(Building building);

    List<Building> listOptions();

    Building getById(Integer id);

    void update(Building building);
}
