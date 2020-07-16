package com.cyc.poi.Mapper;

import com.cyc.poi.model.TlstCourseItem;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

@org.apache.ibatis.annotations.Mapper
@Component
public interface TlstCourseItemMapper extends Mapper<TlstCourseItem>, IdsMapper<TlstCourseItem>, InsertListMapper<TlstCourseItem> {
}
