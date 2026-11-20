package com.pr0xs1.campusfix.service.impl;

import com.pr0xs1.campusfix.exception.BusinessException;
import com.pr0xs1.campusfix.mapper.BuildingMapper;
import com.pr0xs1.campusfix.pojo.Building;
import com.pr0xs1.campusfix.pojo.PageResult;
import com.pr0xs1.campusfix.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public PageResult pageQuery(Integer page, Integer pageSize, String keyword, String campus) {
        Long total = buildingMapper.count(keyword, campus);
        if (total == null || total == 0) {
            return new PageResult(0L, Collections.emptyList());   // 没数据就不用查第二次
        }
        int offset = (page - 1) * pageSize;
        keyword = (keyword == null) ? "" : keyword;
        campus  = (campus == null) ? ""  : campus;
        List<Building> rows = buildingMapper.list(keyword, campus, offset, pageSize);
        return new PageResult(total, rows);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new BusinessException("楼宇 id 不能为空");
        }

        // 2. 是否存在（不存在的 id 不能静默成功）
        Building building = buildingMapper.getById(id);
        if (building == null) {
            throw new BusinessException("楼宇不存在");
        }

        // 3. 是否被报修单引用（只算未逻辑删除的单）
        Long orderCount = buildingMapper.countOrdersByBuildingId(id);
        if (orderCount != null && orderCount > 0) {
            throw new BusinessException(
                    "「" + building.getName() + "」下还有 " + orderCount + " 条报修记录，不能删除");
        }

        // 4. 执行删除
        buildingMapper.deleteById(id);
    }

    @Override
    public void add(Building building) {
        // ---------- 1. 基础校验 ----------
        if (building == null) {
            throw new BusinessException("请求参数不能为空");
        }

        String name = trimToNull(building.getName());
        if (name == null) {
            throw new BusinessException("楼宇名称不能为空");
        }
        if (name.length() > 30) {
            throw new BusinessException("楼宇名称最长 30 个字符");
        }
        building.setName(name);

        String code = trimToNull(building.getCode());
        if (code != null && code.length() > 20) {
            throw new BusinessException("楼宇编号最长 20 个字符");
        }
        building.setCode(code);

        String campus = trimToNull(building.getCampus());
        if (campus == null) {
            throw new BusinessException("所属校区不能为空");
        }
        building.setCampus(campus);

        Integer floors = building.getFloors();
        if (floors == null || floors < 1 || floors > 100) {
            throw new BusinessException("楼层数必须是 1~100 之间的整数");
        }

        // 状态不传默认启用
        if (building.getStatus() == null) {
            building.setStatus(1);
        } else if (building.getStatus() != 0 && building.getStatus() != 1) {
            throw new BusinessException("状态只能是 0（停用）或 1（启用）");
        }

        // ---------- 2. 名称唯一 ----------
        Long sameName = buildingMapper.countByName(name, null);
        if (sameName != null && sameName > 0) {
            throw new BusinessException("楼宇名称「" + name + "」已存在");
        }

        // ---------- 3. 插入 ----------
        try {
            buildingMapper.insert(building);
        } catch (DuplicateKeyException e) {
            // 并发下两个请求同时通过上面的检查，由唯一索引兜住
            throw new BusinessException("楼宇名称「" + name + "」已存在");
        }
    }

    /** 去首尾空格，空串转 null */
    private String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
