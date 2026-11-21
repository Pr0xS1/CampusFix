package com.pr0xs1.campusfix.service.impl;

import com.pr0xs1.campusfix.exception.BusinessException;
import com.pr0xs1.campusfix.mapper.DeptMapper;
import com.pr0xs1.campusfix.pojo.Dept;
import com.pr0xs1.campusfix.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> list(String name) {
        name = (name == null) ? "" : name;
        return deptMapper.list(name);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new BusinessException("班组 id 不能为空");
        }

        // 2. 是否存在（不存在的 id 不能静默成功）
        Dept dept = deptMapper.getById(id);
        if (dept == null) {
            throw new BusinessException("班组不存在");
        }

        // 3. 名下是否还有维修工
        Long empCount = deptMapper.countEmpsByDeptId(id);
        if (empCount != null && empCount > 0) {
            throw new BusinessException("该班组下还有维修工，请先调整归属");
        }

        // 4. 执行删除
        deptMapper.deleteById(id);
    }

    @Override
    public void add(Dept dept) {
        if (dept == null) {
            throw new BusinessException("请求参数不能为空");
        }

        // ---------- 1. 字段校验 ----------
        validateFields(dept);

        // ---------- 2. 名称唯一（新增时排除 id 传 null）----------
        Long sameName = deptMapper.countByName(dept.getName(), null);
        if (sameName != null && sameName > 0) {
            throw new BusinessException("班组名称已存在");
        }

        // ---------- 3. 插入 ----------
        try {
            deptMapper.insert(dept);
        } catch (DuplicateKeyException e) {
            // 并发下两个请求同时通过上面的检查，由唯一索引兜住
            throw new BusinessException("班组名称已存在");
        }
    }

    @Override
    public Dept getById(Integer id) {
        Dept dept = deptMapper.getById(id);
        if (dept == null) {
            throw new BusinessException("班组不存在");
        }
        return dept;
    }

    @Override
    public void update(Dept dept) {
        if (dept == null || dept.getId() == null) {
            throw new BusinessException("班组 id 不能为空");
        }

        // ---------- 1. 存在性：先确认这条还在 ----------
        Dept exists = deptMapper.getById(dept.getId());
        if (exists == null) {
            throw new BusinessException("班组不存在");
        }

        // ---------- 2. 字段校验（与新增完全一致）----------
        validateFields(dept);

        // ---------- 3. 名称唯一：必须排除自身 id ----------
        Long sameName = deptMapper.countByName(dept.getName(), dept.getId());
        if (sameName != null && sameName > 0) {
            throw new BusinessException("班组名称已存在");
        }

        // ---------- 4. 执行更新 ----------
        int rows;
        try {
            rows = deptMapper.update(dept);
        } catch (DuplicateKeyException e) {
            // 并发：另一个请求刚把名字改成同一个
            throw new BusinessException("班组名称已存在");
        }
        if (rows == 0) {
            // 上面查到还在，update 却一行没动，说明这几毫秒内被别人删了
            throw new BusinessException("班组不存在或已被删除");
        }
    }

    /** 新增与修改共用的字段校验：名称非空、长度、排序号范围，并把 name 去空格 */
    private void validateFields(Dept dept) {
        String name = trimToNull(dept.getName());
        if (name == null) {
            throw new BusinessException("班组名称不能为空");
        }
        if (name.length() > 20) {
            throw new BusinessException("班组名称最长 20 个字符");
        }
        dept.setName(name);

        Integer sort = dept.getSort();
        if (sort == null) {
            dept.setSort(0);                  // 不传按 0 排最前
        } else if (sort < 0 || sort > 9999) {
            throw new BusinessException("排序号必须是 0~9999 之间的整数");
        }
    }
    /** 去首尾空格，空串转 null */
    private String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }


}
