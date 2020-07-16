package com.cyc.poi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author cyc
 * @version 1.0
 * @since 2020/07/11
 */
@Data
@Table(name = "tlst_course_section")
public class TlstCourseSection {

	/**
	 * 主键
	 */
  	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 试卷名称
	 */
  	@Column(name = "name")
	private String name;
	/**
	 * 展示顺序
	 */
  	@Column(name = "`index`")
	private Integer index;
	/**
	 * 创建时间
	 */
  	@Column(name = "createDatetime")
	private Date createDatetime;
	/**
	 * 情景数
	 */
  	@Column(name = "itemCount")
	private Integer itemCount;
	/**
	 * 归属套题卷-[表]tlst_course_papper.id
	 */
  	@Column(name = "papperId")
	private Integer papperId;

}
