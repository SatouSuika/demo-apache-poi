package com.cyc.poi.Mapper;

import com.cyc.poi.model.TlstCourseType;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

@org.apache.ibatis.annotations.Mapper
@Component
public interface TlstCourseTypeMapper extends Mapper<TlstCourseType>, IdsMapper<TlstCourseType>, InsertListMapper<TlstCourseType> {
}
