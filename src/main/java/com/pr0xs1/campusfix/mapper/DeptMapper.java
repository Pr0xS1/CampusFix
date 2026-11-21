package com.pr0xs1.campusfix.mapper;

import com.pr0xs1.campusfix.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    /**
     * 查询班组列表（不分页）
     *
     * @param name 名称模糊查询；Service 保证不为 null，空串表示查全部
     * @return 带 empCount / orderCount 的列表
     */
    @Select("SELECT d.*, " +
            "  (SELECT COUNT(*) FROM emp e " +
            "     WHERE e.dept_id = d.id) AS emp_count, " +
            "  (SELECT COUNT(*) FROM repair_order o " +
            "     JOIN emp e2 ON o.handler_id = e2.id " +
            "     WHERE e2.dept_id = d.id AND o.is_deleted = 0) AS order_count " +
            "FROM dept d WHERE 1 = 1 " +
            "  AND (#{name} = '' OR d.name LIKE CONCAT('%', #{name}, '%')) " +
            "ORDER BY d.sort ASC, d.id ASC")
    List<Dept> list(@Param("name") String name);

    @Select("SELECT * FROM dept WHERE id = #{id}")
    Dept getById(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM emp WHERE dept_id = #{id}")
    Long countEmpsByDeptId(@Param("id") Integer id);

    @Delete("DELETE FROM dept WHERE id = #{id}")
    void deleteById(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM dept WHERE name = #{name} " +
            "  AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Long countByName(@Param("name") String name, @Param("excludeId") Integer excludeId);

    @Insert("INSERT INTO dept (name, sort) " +
            "VALUES (#{name}, #{sort})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Dept dept);

    /**
     * 按 id 更新班组；create_time / update_time 交给数据库处理
     * @return 影响行数
     */
    @Update("UPDATE dept SET name = #{name}, sort = #{sort} " +
            "WHERE id = #{id}")
    int update(Dept dept);

}
