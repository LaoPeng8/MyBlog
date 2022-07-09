package com.pjj.service.impl;

import com.pjj.entity.Blog;
import com.pjj.entity.Comment;
import com.pjj.mapper.CommentMapper;
import com.pjj.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    private List<Comment> tempReplys = new ArrayList<>();//存放迭代找出的所有子代的集合

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {//只能查询出顶级评论, 那些在回复别人评论的评论 属于子级评论,是查询不出来的
        List<Comment> comments = commentMapper.listCommentByBlogIdAndParentNull(blogId);
        
        for(Comment comment:comments){
            Long id = comment.getId();
            String parentNickname1 = comment.getNickname();
            List<Comment> childComments = commentMapper.listCommentByBlogIdAndParentNotNull(blogId,id);
//            查询出子评论
            combineChildren(blogId, childComments, parentNickname1);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();

        }

        return comments;
    }

    private void combineChildren(Long blogId, List<Comment> childComments, String parentNickname1) {
//        判断是否有一级子评论
        if(childComments.size() > 0){
//                循环找出子评论的id
            for(Comment childComment : childComments){
                String parentNickname = childComment.getNickname();
                childComment.setParentNickname(parentNickname1);
                tempReplys.add(childComment);
                Long childId = childComment.getId();
//                    查询出子二级评论
                recursively(blogId, childId, parentNickname);
            }
        }
    }

    private void recursively(Long blogId, Long childId, String parentNickname1) {
//        根据子一级评论的id找到子二级评论
        List<Comment> replayComments = commentMapper.listCommentByBlogIdAndParentNotNull(blogId,childId);

        if(replayComments.size() > 0){
            for(Comment replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                replayComment.setParentNickname(parentNickname1);
                Long replayId = replayComment.getId();
                tempReplys.add(replayComment);
                recursively(blogId,replayId,parentNickname);
            }
        }
    }

    @Transactional
    @Override
    public int saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();//获取从页面上传递过来的需要保存的评论 的父级评论的id
        if(parentCommentId != -1){
            //如果该评论的父评论id不为-1, 则说明该评论有父评论, 则根据父评论的id查询到父评论,并且赋值给该评论对象的父评论属性
            //那么保存到 数据库的该评论的 父评论id 就是现在查询出来的这个评论的id
            comment.setParentComment(commentMapper.selectParentCommentByParentCommentId(parentCommentId));
        } else {
//            comment.setParentComment(null);
            //发现要了这句话之后   如果保存的新评论没有父评论则 新评论的父评论id为null  而正常新评论没有父评论,那么父评论的id应该为-1
            //本来   新评论没有父评论,那么父评论的id应该为-1    此处却设置为了null 导致保存到数据库时 父评论字段值为null 正常应为-1
            //所以看不太懂老师的意思就先注释了.
        }
        comment.setCreateTime(new Date());
        return commentMapper.saveComment(comment);
    }

    @Override
    public Comment selectParentCommentByParentCommentId(Long id) {
        return commentMapper.selectParentCommentByParentCommentId(id);
    }





//    这个老师用JPA写的代码,我用mybatis是真不用了, debug了好久,发现他需要获取当前评论的子评论,然后才可以将顶级评论的所有层级的子评论
//    转为同一层次下面的评论,但是不晓得他用JPA是怎么搞滴查询SQL, 反正我用mybatis查询,如果顶级评论,那么封装到java中这些顶级评论的子评论
//    我在java中还需要再次调用sql查询出顶级评论的子评论然后到java中复制给顶级评论的子评论属性. 但是老师使用的JPA貌似在查询顶级评论的同时
//    也查询出了顶级评论的子评论并且赋值到java中顶级评论的子评论的属性中了
//    反正着代码我也用不了了, 我自己搞了好久, 实在是搞不出来, 最多只能回复一层, 就是只能回复顶级评论, 但是张三不能回复,李四回复王五的评论....
//    最后就copy了别人的这个部分的代码....
//    /**
//     * 循环这个输入的 comments 然后 赋值给一个新的list ,  然后之后操作的就是这个新的list, 防止破坏之前传过来的list
//     * 之后调用combineChildren()来处理 这个 新的list
//     * @param comments 顶级评论, 那些在回复别人评论的评论 属于子级评论,是查询不出来的, 传递过来的该参数是顶级评论(顶级评论是没有父评论的)
//     * @return
//     */
//    private List<Comment> eachComment(List<Comment> comments){
//        List<Comment> commentsView = new ArrayList<>();
//        for(Comment comment : comments){
//            Comment temp = new Comment();
//            BeanUtils.copyProperties(comment,temp);
//            commentsView.add(temp);
//        }
//        combineChildren(commentsView);
//        return commentsView;
//    }
//
//    /**
//     *
//     * @param comments 顶级评论, 那些在回复别人评论的评论 属于子级评论,是查询不出来的, 传递过来的该参数是顶级评论(顶级评论是没有父评论的)
//     */
//    private void combineChildren(List<Comment> comments){
//        for(Comment comment : comments){
//            if(comment.getReplyComments()==null){
//                comment.setReplyComments(new ArrayList<Comment>());
//            }
//            List<Comment> replys1 = comment.getReplyComments();
//
//            for(Comment reply1 : replys1){
//                //循环迭代，找出子代，存放在tempReplys中
//                recursively(reply1);
//            }
//            //修改顶级节点的reply集合为迭代处理后的集合
//            comment.setReplyComments(tempReplys);
//            //清除临时存放区
//            tempReplys = new ArrayList<>();
//        }
//
//    }
//
//    /**
//     * 递归迭代
//     * @param comment 被迭代的对象
//     */
//    private void recursively(Comment comment){
//        if(comment.getReplyComments()==null){
//            comment.setReplyComments(new ArrayList<Comment>());
//        }
//        tempReplys.add(comment);//顶级节点添加到临时存放集合
//        if(comment.getReplyComments().size()>0){
//            List<Comment> replys = comment.getReplyComments();
//            for(Comment reply : replys){
//                tempReplys.add(reply);
//                if(reply.getReplyComments().size()>0){
//                    recursively(reply);
//                }
//            }
//        }
//    }
}
