package com.pr0xs1.campusfix.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    /** 主键 */
    private Integer id;
    /** 登录用户名，唯一 */
    private String username;
    /**密码 */
    private String password;
    /** 真实姓名 */
    private String name;
    /** 性别：1 男　2 女 */
    private Integer gender;
    /** 头像相对路径 */
    private String image;
    /** 工种：1 电工 2 水暖 3 木工 4 多媒体 5 网络 6 空调；仅维修工有值 */
    private Integer job;
    /** 加入日期 */
    private LocalDate entryDate;
    /** 所属班组，仅维修工有值 */
    private Integer deptId;
    /** 联系电话 */
    private String phone;
    /** 角色：1 管理员　2 维修工　3 报修人 */
    private Integer role;
    /** 状态：1 在职　0 停用 */
    private Integer status;
    /** 创建时间，由数据库默认值填充 */
    private LocalDateTime createTime;
    /** 更新时间，由数据库 ON UPDATE 自动刷新 */
    private LocalDateTime updateTime;
}
