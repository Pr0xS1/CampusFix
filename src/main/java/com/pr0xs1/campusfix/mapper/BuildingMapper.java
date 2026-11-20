package com.pr0xs1.campusfix.mapper;

import com.pr0xs1.campusfix.pojo.Building;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BuildingMapper {
    /**
     * 查询总数
     *
     * @param keyword 关键字
     * @param campus  校区
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM building b WHERE 1 = 1 " +
            "  AND (b.name LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR b.code LIKE CONCAT('%', #{keyword}, '%')) " +
            "  AND (#{campus} = '' OR b.campus = #{campus})")
    Long count(@Param("keyword") String keyword, @Param("campus") String campus);
    /**
     * 分页查询
     *
     * @param keyword 关键字
     * @param campus  校区
     * @param offset  偏移量
     * @param pageSize 页大小
     * @return 查询结果
     */
    @Select("SELECT b.*, " +
            "  (SELECT COUNT(*) FROM repair_order o " +
            "     WHERE o.building_id = b.id AND o.is_deleted = 0 " +
            "       AND o.create_time >= DATE_FORMAT(CURDATE(), '%Y-%m-01') " +
            "       AND o.create_time <  DATE_FORMAT(CURDATE() + INTERVAL 1 MONTH, '%Y-%m-01')" +
            "  ) AS month_count " +
            "FROM building b WHERE 1 = 1 " +
            "  AND (b.name LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR b.code LIKE CONCAT('%', #{keyword}, '%')) " +
            "  AND (#{campus} = '' OR b.campus = #{campus}) " +
            "ORDER BY b.id ASC LIMIT #{offset}, #{pageSize}")
    List<Building> list(@Param("keyword") String keyword, @Param("campus") String campus, @Param("offset") int offset, @Param("pageSize") Integer pageSize);

    @Select("SELECT * FROM building WHERE id = #{id}")
    Building getById(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM repair_order WHERE building_id = #{id} AND is_deleted = 0")
    Long countOrdersByBuildingId(@Param("id") Integer id);

    @Delete("DELETE FROM building WHERE id = #{id}")
    void deleteById(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM building WHERE name = #{name} " +
            "  AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Long countByName(@Param("name") String name, @Param("excludeId") Integer excludeId);

    @Insert("INSERT INTO building (name, code, campus, floors, status) " +
            "VALUES (#{name}, #{code}, #{campus}, #{floors}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Building building);
}
