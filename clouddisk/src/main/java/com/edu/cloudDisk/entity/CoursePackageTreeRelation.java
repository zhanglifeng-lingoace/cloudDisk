package com.edu.cloudDisk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程包和课包树关系表
 * </p>
 *
 * @author zhanglf
 * @since 2022-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CoursePackageTreeRelation implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键id自增
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * course_package.id
     */
    private Long packageId;

    /**
     * 课包树的id,一个课包树可能由一个或者多个课程树构成
     */
    private String packageTreeId;

    /**
     * 使用原始课程树的根id
     */
    private Long courseRootId;

    /**
     * 上课时长，单位-分钟
     */
    private Integer duration;

    /**
     * 课程树下的lesson总数量
     */
    private Integer lessonNum;

    /**
     * 状态: 1-有效，2-归档
     */
    private Integer status;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人用户名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人ID
     */
    private Long updaterId;

    /**
     * 修改人用户名
     */
    private String updaterName;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
