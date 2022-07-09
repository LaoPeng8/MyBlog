package com.pjj.mapper;

import com.pjj.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TypeMapper {

    int saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    List<Type> getAllType();

    List<Type> getAllTypeByTop();

    int updateType(Type type);

    int deleteType(Long id);

    int countType();

}
