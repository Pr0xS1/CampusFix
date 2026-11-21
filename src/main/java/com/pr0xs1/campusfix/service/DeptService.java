package com.pr0xs1.campusfix.service;

import com.pr0xs1.campusfix.pojo.Dept;

import java.util.List;

public interface DeptService {
    /** 查询班组列表
     * @param name 名称模糊查询，可为 null
     * @return 按 sort 升序，含 empCount / orderCount
     */
    List<Dept> list(String name);

    void deleteById(Integer id);

    void add(Dept dept);

    Dept getById(Integer id);

    void update(Dept dept);
}
