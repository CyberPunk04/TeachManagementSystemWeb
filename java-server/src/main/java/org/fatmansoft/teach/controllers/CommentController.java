package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.PostRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.fatmansoft.teach.repository.CommentRepository;
import org.fatmansoft.teach.repository.UserRepository;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserRepository userRepository;

    public Map getMapFromComment(Comment c) {
        Map m = new HashMap();
        Person p;
        Post post;
        if(c == null)
            return null;

        m.put("commentId", c.getCommentId());
        m.put("content", c.getContent());
        m.put("created_at", c.getCreated_at());
        p = c.getPerson();
        if(p == null)
            return m;
        m.put("person", p);
        post=c.getPost();
        if(post==null)
            return m;
        m.put("postId", post.getPostId());

        return m;
    }

    public List getCommentMapList(Integer postId) {
        List dataList = new ArrayList();
        List<Comment> cList = commentRepository.findByPostPostId(postId); //数据库查询操作
        if (cList == null || cList.size() == 0)
            return dataList;
        for (int i = 0; i < cList.size(); i++) {
            dataList.add(getMapFromComment(cList.get(i)));
        }
        return dataList;
    }

    //根据postId获取评论列表
    @PostMapping("/getCommentList")
    public DataResponse getCommentList(@Valid@RequestBody DataRequest dataRequest) {
        Integer postId = dataRequest.getInteger("postId");
        List dataList = getCommentMapList(postId);
        return CommonMethod.getReturnData(dataList);
    }

    //添加评论
    //返回值是一个Comment对象
    @PostMapping("/addComment")
    public DataResponse addComment (@Valid@RequestBody DataRequest dataRequest) {
        Integer postId = dataRequest.getInteger("postId");
        String content = dataRequest.getString("content");
        String created_at = dataRequest.getString("created_at");

        Integer personId = null;
        Integer userId = CommonMethod.getUserId();
        Optional<User> op = Optional.empty();
        if (userId != null) {
            op = userRepository.findById(userId);
        }
        if(op.isPresent()){
            User user = op.get();
            personId = user.getPerson().getPersonId();
        }
        Comment c = new Comment();
        c.setContent(content);
        c.setCreated_at(created_at);

        Optional<Post> op1 = postRepository.findById(postId);
        Optional<Person> op2 = personRepository.findById(personId);
        Post post = new Post();
        Person person = new Person();
        if(op1.isPresent() && op2.isPresent()) {
            post = op1.get();
            person = op2.get();
        }
        c.setPost(post);
        c.setPerson(person);
        commentRepository.save(c);
        return CommonMethod.getReturnData(c);
    }

    //删除评论
    @PostMapping("/deleteComment")
    public DataResponse deleteComment(@Valid@RequestBody DataRequest dataRequest) {
        Integer commentId = dataRequest.getInteger("commentId");
        Comment c = null;
        Optional<Comment> op;
        if (commentId != null) {
            op = commentRepository.findById(commentId);
            if (op.isPresent()) {
                c = op.get();
            }
        }
        if (c != null) {
            commentRepository.delete(c);
        }
        return CommonMethod.getReturnMessageOK();
    }

    //统计一个帖子的评论数量
    @PostMapping("/getCommentNum")
    public DataResponse getCommentNum(@Valid@RequestBody DataRequest dataRequest) {
        Integer postId = dataRequest.getInteger("postId");

        Integer commentNum = commentRepository.countByPostId(postId);
        return CommonMethod.getReturnData(commentNum);
    }
}
