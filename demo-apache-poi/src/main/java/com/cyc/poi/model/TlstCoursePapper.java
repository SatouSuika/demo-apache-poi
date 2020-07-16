package com.cyc.poi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author cyc
 * @version 1.0
 * @since 2020/07/11
 */
@Data
@Table(name = "tlst_course_papper")
public class TlstCoursePapper {

	/**
	 * 主键
	 */
  	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 课程名称
	 */
  	@Column(name = "name")
	private String name;
	/**
	 * 分类-[表]tlst_course_type.id
	 */
  	@Column(name = "type")
	private Integer type;
	/**
	 * 订阅次数
	 */
  	@Column(name = "subscribeTimes")
	private Integer subscribeTimes;
	/**
	 * 状态 1、初始化；2、上架，3、下架-上架状态才展示
	 */
  	@Column(name = "status")
	private Integer status;
	/**
	 * section数量
	 */
  	@Column(name = "sectionCount")
	private Integer sectionCount;
	/**
	 * item数量
	 */
  	@Column(name = "itemCount")
	private Integer itemCount;
	/**
	 * 题目数量
	 */
  	@Column(name = "questionCount")
	private Integer questionCount;
	/**
	 * 是否教学卷-教学卷学生不能自学，所有试卷老师都可以布置作业
	 */
  	@Column(name = "isTeachingPapper")
	private Integer isTeachingPapper;
	/**
	 * 是否公开试卷
	 */
  	@Column(name = "isPublic")
	private Integer isPublic;
	/**
	 * 归属老师
	 */
  	@Column(name = "teacherId")
	private Integer teacherId;

}
