package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.*;
import java.util.List;

/**
 * StudentController 主要是为学生管理数据管理提供的Web请求服务
 */

// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController{
    //Java 对象的注入 我们定义的这下Java的操作对象都不能自己管理是由有Spring框架来管理的， StudentController 中要使用StudentRepository接口的实现类对象，
    // 需要下列方式注入，否则无法使用， studentRepository 相当于StudentRepository接口实现对象的一个引用，由框架完成对这个引用的赋值，
    // StudentController中的方法可以直接使用
    @Autowired
    private  PostRepository postRepository;
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //用户数据操作自动注入
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private PostLikesRepository postLikesRepository;


    public Map getMapFromPost(Post p) {
        Map m = new HashMap();

        if(p == null)
            return null;
        Person person = p.getPerson();

        m.put("content",p.getContent());
        m.put("created_at",p.getCreated_at());
        m.put("postId",p.getPostId());
        m.put("image",p.getImage());
        m.put("likes",p.getLikes());
        if(person == null)
            return m;

        m.put("person",person);

        return m;
    }
    /**
     * 根据用户id获取所有帖子
     *
     * @param personId 输入参数
     * @return Map List 集合
     */
    public List getPostMapList(String personId) {
        List dataList = new ArrayList();
        List<Post> pList = postRepository.findByPersonPersonId(Integer.valueOf(personId));  //数据库查询操作
        if (pList == null || pList.size() == 0)
            return dataList;

        for (int i = 0; i < pList.size(); i++) {
            dataList.add(getMapFromPost(pList.get(i)));
        }
        return dataList;
    }

    /*
    根据用户id获取所有帖子
    * */
    @PostMapping("/getPostList")
    public DataResponse getPostList(@Valid @RequestBody DataRequest dataRequest) {
        String personId = dataRequest.getString("personId");
        List dataList = getPostMapList(personId);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }


    /**
     * 根据输入的Post.Conetent和Post.person.personName查询帖子
     * @param dataRequest
     * @return
     */
    @PostMapping("/getAllPost")
    public DataResponse getAllPost(@Valid @RequestBody DataRequest dataRequest) {
        String content = dataRequest.getString("contentPersonName");
        List <Post> pList = null;
        List dataList = new ArrayList();
        pList = postRepository.findPostListByContentOrPerson(content);
        for(int i = 0; i < pList.size(); i++){
            dataList.add(getMapFromPost(pList.get(i)));
        }
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * 获取关注的人的帖子
     * @param dataRequest
     * @return
     */
    @PostMapping("/getFollowPost")
    public DataResponse getFollowPost(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> op;
        Person person = new Person();
        if(userId != null){
             op = userRepository.findById(userId);
             person = new Person();
            if(op.isPresent()) {
                User user = op.get();
                person = user.getPerson();
            }
        }
        List<Person> followingList = followRepository.findFollowingByFollower(person.getPersonId());
        List dataList = new ArrayList();
        for(int i = 0; i < followingList.size(); i++){
            List<Post> pList = postRepository.findByPersonPersonId(followingList.get(i).getPersonId());
            if (pList == null || pList.size() == 0)
                continue;
            for (int j = 0; j < pList.size(); j++) {
                dataList.add(getMapFromPost(pList.get(j)));
            }
        }

        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list

    }

    //统计发了多少帖子
    @PostMapping("/countPost")
    public DataResponse countPost(@Valid @RequestBody DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        int count = postRepository.countPostByPersonId(personId);
        return CommonMethod.getReturnData(count);
    }


    //把该帖子的评论同时删除
    @PostMapping("/postDelete")
    public DataResponse postDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer postId = dataRequest.getInteger("postId");
        Post p = null;
        Optional<Post> op;

        if (postId != null) {
            op = postRepository.findById(postId);   //查询获得实体对象
            if (op.isPresent()) {
                p = op.get();
            }
        }
        List<Comment> cList = commentRepository.findByPostPostId(postId);
        if (cList != null && cList.size() > 0) {
            for (int i = 0; i < cList.size(); i++) {
                commentRepository.delete(cList.get(i));
            }
        }

        if (p != null) {
            postRepository.delete(p);
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }


    /**
     * 创建一个帖子
     * */

    @PostMapping("/postSave")

    public DataResponse postSave(@Valid @RequestBody DataRequest dataRequest) {

        Integer personId = dataRequest.getInteger("personId");
        String content = dataRequest.getString("content");
        String created_at = dataRequest.getString("created_at");
        String image = dataRequest.getString("image");

        Post p = new Post();

        Person person = null;


        Optional<Person> t = personRepository.findById(personId);
        if (t.isPresent()) {
            person = t.get();
        }
        p.setCreated_at(created_at);
        p.setContent(content);
        p.setPerson(person);

        p.setImage(image);
        p.setLikes(0);
        postRepository.save(p);

        //返回最新的post信息
        return CommonMethod.getReturnData(getMapFromPost(p));

    }

    /**
     * 更新一个帖子
     * */

    @PostMapping("/postEdit")
    public DataResponse postEdit(@Valid @RequestBody DataRequest dataRequest) {
        Integer postId = dataRequest.getInteger("postId");
        String content = dataRequest.getString("content");
        String created_at = dataRequest.getString("created_at");
        String image = dataRequest.getString("image");

        Post p = null;
        Optional<Post> op;

        if (postId != null) {
            op = postRepository.findById(postId);   //查询获得实体对象
            if (op.isPresent()) {
                p = op.get();
            }
        }

        if (p != null) {
            p.setCreated_at(created_at);
            p.setContent(content);
            p.setImage(image);
            postRepository.save(p);
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    //查询是否以及点赞某个帖子
    @PostMapping("/getLikes")
    public DataResponse getLikes(@Valid @RequestBody DataRequest dataRequest) {
        Integer postId = dataRequest.getInteger("postId");
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

        PostLikes existingPostLikes = postLikesRepository.findByPostIdAndPersonId(postId, personId);
        if(existingPostLikes == null) {
            return CommonMethod.getReturnMessageError("未点赞");
        }else{
            return CommonMethod.getReturnMessageOK();
        }
    }


    // 点赞或取消点赞的方法
    @PostMapping("/postLikes")
    public DataResponse postLikes(@Valid@RequestBody DataRequest dataRequest) {
        Integer postId = dataRequest.getInteger("postId");
        Integer personId = null;
        Integer userId = CommonMethod.getUserId();
        Optional<User> op = Optional.empty();
        Person person = new Person();
        if (userId != null) {
            op = userRepository.findById(userId);
        }
        if(op.isPresent()){
            User user = op.get();
            person = user.getPerson();
            personId = person.getPersonId();
        }
        System.out.println(postId);
        Optional<Post> op3 = postRepository.findById(postId);
        Post p = new Post();
        if(op3.isPresent()){
            p = op3.get();
        }

        PostLikes existingPostLikes = postLikesRepository.findByPostIdAndPersonId(postId, personId);
        if(existingPostLikes == null) {
            //如果未点赞，则点赞
            int count = p.getLikes();
            p.setLikes(count + 1);

            PostLikes newPostLikes = new PostLikes();
            Optional<Post> op1 = postRepository.findById(postId);
            Post post = new Post();

            if (op1.isPresent()) {
                post = op1.get();
            }
            newPostLikes.setPost(post);
            newPostLikes.setPerson(person);
            postLikesRepository.save(newPostLikes);
            return CommonMethod.getReturnMessageOK();
        }else{
            int count = p.getLikes();
            p.setLikes(count - 1);
            //如果已点赞，则取消点赞
            postLikesRepository.delete(existingPostLikes);
            return CommonMethod.getReturnMessageOK("取消点赞成功");
        }

    }



    //返回当前用户信息为了确定当前用户的personID
    @PostMapping("/getPersonInfo")
    public DataResponse getPersonInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> op = Optional.empty();
        if (userId != null) {
            op = userRepository.findById(userId);
        }
        Person person = new Person();
        if (op.isPresent()) {
            User user = op.get();
            person = user.getPerson();

        }
        return CommonMethod.getReturnData(person);
    }

    //上传用户头像
    @PostMapping("/uploadAvatar")
    public DataResponse uploadAvatar(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> op = Optional.empty();
        if (userId != null) {
            op = userRepository.findById(userId);
        }
        Person person = new Person();
        if (op.isPresent()) {
            User user = op.get();
            person = user.getPerson();
        }
        String avatar = dataRequest.getString("avatar");
        person.setAvatar(avatar);
        personRepository.save(person);
        return CommonMethod.getReturnMessageOK();


    }


    //获取某个用户的头像
    @PostMapping("/getAvatar")
    public DataResponse getAvatar(@Valid @RequestBody DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        Optional<Person> op = Optional.empty();
        if (personId != null) {
            op = personRepository.findById(personId);
        }
        Person person = new Person();
        if (op.isPresent()) {
            person = op.get();
        }
        return CommonMethod.getReturnData(person.getAvatar());
    }
}
