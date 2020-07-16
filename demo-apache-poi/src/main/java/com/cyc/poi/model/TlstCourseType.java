package com.cyc.poi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author cyc
 * @version 1.0
 * @since 2020/07/11
 */
@Data
@Table(name = "tlst_course_type")
public class TlstCourseType {

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

}
