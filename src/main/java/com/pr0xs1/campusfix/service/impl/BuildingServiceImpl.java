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
        keyword = (keyword == null) ? "" : keyword;
        campus  = (campus == null) ? ""  : campus;
        page = (page == null || page < 1) ? 1 : page;
        pageSize = (pageSize == null || pageSize < 1 || pageSize > 100) ? 10 : pageSize;
        Long total = buildingMapper.count(keyword, campus);
        if (total == null || total == 0) {
            return new PageResult(0L, Collections.emptyList());   // 没数据就不用查第二次
        }
        int offset = (page - 1) * pageSize;
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
        if (building == null) {
            throw new BusinessException("请求参数不能为空");
        }

        // ---------- 1. 字段校验 ----------
        validateFields(building);

        // ---------- 2. 名称唯一（新增时排除 id 传 null）----------
        Long sameName = buildingMapper.countByName(building.getName(), null);
        if (sameName != null && sameName > 0) {
            throw new BusinessException("楼宇名称「" + building.getName() + "」已存在");
        }

        // ---------- 3. 插入 ----------
        try {
            buildingMapper.insert(building);
        } catch (DuplicateKeyException e) {
            // 并发下两个请求同时通过上面的检查，由唯一索引兜住
            throw new BusinessException("楼宇名称「" + building.getName() + "」已存在");
        }
    }

    @Override
    public List<Building> listOptions() {
        return buildingMapper.listOptions();
    }

    @Override
    public Building getById(Integer id) {
        Building building = buildingMapper.getById(id);
        if (building == null) {
            throw new BusinessException("楼宇不存在");
        }
        return building;
    }

    @Override
    public void update(Building building) {
        if (building == null || building.getId() == null) {
            throw new BusinessException("楼宇 id 不能为空");
        }

        // ---------- 1. 存在性：先确认这条还在 ----------
        Building exists = buildingMapper.getById(building.getId());
        if (exists == null) {
            throw new BusinessException("楼宇不存在");
        }

        // ---------- 2. 字段校验（与新增完全一致）----------
        validateFields(building);

        // ---------- 3. 名称唯一：必须排除自身 id ----------
        Long sameName = buildingMapper.countByName(building.getName(), building.getId());
        if (sameName != null && sameName > 0) {
            throw new BusinessException("楼宇名称「" + building.getName() + "」已存在");
        }

        // ---------- 4. 执行更新 ----------
        int rows;
        try {
            rows = buildingMapper.update(building);
        } catch (DuplicateKeyException e) {
            // 并发：另一个请求刚把名字改成同一个
            throw new BusinessException("楼宇名称「" + building.getName() + "」已存在");
        }
        if (rows == 0) {
            // 上面查到还在，update 却一行没动，说明这几毫秒内被别人删了
            throw new BusinessException("楼宇不存在或已被删除");
        }
    }

    /** 新增与修改共用的字段校验：非空、长度、范围，并把 name/code/campus 去空格 */
    private void validateFields(Building building) {
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
        if (campus.length() > 20) {
            throw new BusinessException("所属校区最长 20 个字符");
        }
        building.setCampus(campus);

        Integer floors = building.getFloors();
        if (floors == null || floors < 1 || floors > 100) {
            throw new BusinessException("楼层数必须是 1~100 之间的整数");
        }

        if (building.getStatus() == null) {
            building.setStatus(1);            // 不传视为启用
        } else if (building.getStatus() != 0 && building.getStatus() != 1) {
            throw new BusinessException("状态只能是 0（停用）或 1（启用）");
        }
    }
    /** 去首尾空格，空串转 null */
    private String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }


}
