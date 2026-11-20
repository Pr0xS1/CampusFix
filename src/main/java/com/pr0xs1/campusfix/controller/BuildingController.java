package com.pr0xs1.campusfix.controller;

import com.pr0xs1.campusfix.pojo.Building;
import com.pr0xs1.campusfix.pojo.Result;
import com.pr0xs1.campusfix.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/buildings")
    public Result page(
            @RequestParam(defaultValue = "1")  Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String campus
    ) {
        return Result.success(buildingService.pageQuery(page, pageSize, keyword, campus));
    }

    @DeleteMapping("/buildings/{id}")
    public Result delete(@PathVariable Integer id) {
        buildingService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/buildings")
    public Result add(@RequestBody Building building) {
        buildingService.add(building);
        return Result.success(building.getId());
    }
}
