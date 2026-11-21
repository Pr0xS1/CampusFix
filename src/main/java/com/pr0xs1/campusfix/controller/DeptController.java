package com.pr0xs1.campusfix.controller;

import com.pr0xs1.campusfix.pojo.Dept;
import com.pr0xs1.campusfix.pojo.Result;
import com.pr0xs1.campusfix.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result list(@RequestParam(required = false) String name) {
        return Result.success(deptService.list(name));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(deptService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        deptService.deleteById(id);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody Dept dept) {
        deptService.add(dept);
        return Result.success(dept.getId());
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Dept dept) {
        dept.setId(id);
        deptService.update(dept);
        return Result.success();
    }

}
