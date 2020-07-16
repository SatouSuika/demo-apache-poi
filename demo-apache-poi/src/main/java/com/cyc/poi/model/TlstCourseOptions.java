package com.cyc.poi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author cyc
 * @version 1.0
 * @since 2020/07/11
 */
@Data
@Table(name = "tlst_course_options")
public class TlstCourseOptions {

	/**
	 * 主键
	 */
  	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 展示顺序-对应item中的排序
	 */
  	@Column(name = "`index`")
	private Integer index;
	/**
	 * 归属section-[表]tlst_subscribe_section.id
	 */
  	@Column(name = "sectionId")
	private Integer sectionId;
	/**
	 * 归属item-[表]tlst_subscribe_item.id
	 */
  	@Column(name = "itemId")
	private Integer itemId;
	/**
	 * A选项
	 */
  	@Column(name = "A")
	private String A;
	/**
	 * B选项
	 */
  	@Column(name = "B")
	private String B;
	/**
	 * C选项
	 */
  	@Column(name = "C")
	private String C;
	/**
	 * D选项
	 */
  	@Column(name = "D")
	private String D;
	/**
	 * 正确答案
	 */
  	@Column(name = "rightAnswer")
	private String rightAnswer;
	/**
	 * 提示
	 */
  	@Column(name = "tips")
	private String tips;
	/**
	 * 分值
	 */
  	@Column(name = "score")
	private Integer score;

}
