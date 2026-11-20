package com.pr0xs1.campusfix.controller;

import com.pr0xs1.campusfix.pojo.Building;
import com.pr0xs1.campusfix.pojo.Result;
import com.pr0xs1.campusfix.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buildings")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @GetMapping
    public Result page(
            @RequestParam(defaultValue = "1")  Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String campus
    ) {
        return Result.success(buildingService.pageQuery(page, pageSize, keyword, campus));
    }

    @GetMapping("/options")
    public Result options() {
        return Result.success(buildingService.listOptions());
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(buildingService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        buildingService.deleteById(id);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody Building building) {
        buildingService.add(building);
        return Result.success(building.getId());
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Building building) {
        building.setId(id);
        buildingService.update(building);
        return Result.success();
    }

}
