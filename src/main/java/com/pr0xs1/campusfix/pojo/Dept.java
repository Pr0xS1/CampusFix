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
public class Dept {
    /** 主键 */
    private Integer id;

    /** 班组名称，唯一 */
    private String name;

    /** 排序号，越小越靠前 */
    private Integer sort;

    /** 创建时间，由数据库默认值填充 */
    private LocalDateTime createTime;

    /** 更新时间，由数据库 ON UPDATE 自动刷新 */
    private LocalDateTime updateTime;

    private Long empCount;

    private Long orderCount;

}
