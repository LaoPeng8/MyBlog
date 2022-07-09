package com.pjj.mapper;

import com.pjj.entity.Blog;
import com.pjj.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {

    int saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    List<Tag> getAllTag();

    int updateTag(Tag tag);

    int deleteTag(Long id);

    int countTag();

    List<Tag> getAllTagByTop();

    Blog getDetailedBlog(Long id);

}
