package com.cyc.poi.Mapper;

import com.cyc.poi.model.TlstCoursePapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

@org.apache.ibatis.annotations.Mapper
@Component
public interface TlstCoursePapperMapper extends Mapper<TlstCoursePapper>, IdsMapper<TlstCoursePapper>, InsertListMapper<TlstCoursePapper> {
}
