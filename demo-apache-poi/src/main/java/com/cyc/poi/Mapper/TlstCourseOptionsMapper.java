package com.cyc.poi.Mapper;

import com.cyc.poi.model.TlstCourseOptions;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Component
public interface TlstCourseOptionsMapper extends Mapper<TlstCourseOptions>, IdsMapper<TlstCourseOptions>, InsertListMapper<TlstCourseOptions> {

}
