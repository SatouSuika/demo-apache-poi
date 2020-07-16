package com.cyc.poi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author cyc
 * @version 1.0
 * @since 2020/07/11
 */
@Data
@Table(name = "tlst_course_item")
public class TlstCourseItem {

	/**
	 * 主键
	 */
  	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 名称
	 */
  	@Column(name = "name")
	private String name;
	/**
	 * 展示顺序
	 */
  	@Column(name = "`index`")
	private Integer index;
	/**
	 * 音频文件路径
	 */
  	@Column(name = "audioPath")
	private String audioPath;
	/**
	 * 归属section-[表]tlst_course_section.id
	 */
  	@Column(name = "sectionId")
	private Integer sectionId;
	/**
	 * 题型 1、选择题，2：填空题，3：听写题
	 */
  	@Column(name = "type")
	private Integer type;
	/**
	 * 问题文本
	 */
  	@Column(name = "questionText")
	private String questionText;
	/**
	 * 听力原文
	 */
  	@Column(name = "listenText")
	private String listenText;

}
