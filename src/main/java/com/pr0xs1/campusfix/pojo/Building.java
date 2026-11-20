package com.pr0xs1.campusfix.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class Building {
    /** 主键 */
    private Integer id;

    /** 楼宇名称，唯一 */
    private String name;

    /** 楼宇编号，如 A1、B2 */
    private String code;

    /** 所属校区 */
    private String campus;

    /** 楼层数 */
    private Integer floors;

    /** 状态：1 启用　0 停用 */
    private Integer status;

    /** 创建时间，由数据库默认值填充 */
    private LocalDateTime createTime;

    /** 更新时间，由数据库 ON UPDATE 自动刷新 */
    private LocalDateTime updateTime;

    private Integer monthCount;

}
