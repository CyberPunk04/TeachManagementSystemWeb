<template>
  <nav class="navbar bg-body-tertiary">
    <div class="container-fluid">
      <a class="navbar-brand">
        个人主页
      </a>
      <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar"
        aria-controls="offcanvasNavbar">
        <span class="navbar-toggler-icon"></span>
      </button>
    </div>
    <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
      <div class="offcanvas-header">
        <h5 class="offcanvas-title" id="offcanvasNavbarLabel">仪表盘</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
      </div>
      <div class="offcanvas-body">
        <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="/BlogIndex">首页</a>
          </li>
          <li class="nav-item" v-if="username !== 'admin'">
            <a class="nav-link" :href="`/UserProfile/${userId}`">我的空间</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/UserList">用户列表</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/FriendList">好友列表</a>
          </li>
        </ul>

      </div>
    </div>
  </nav>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <UserProfileInfo v-if="!loading" @follow="follow" @unfollow="unfollow" @uploadAvatar="uploadAvatar" :user="user"
          :avatar="avatar" :is_me="is_me" />

        <UserProfileWrite :is_editing="is_editing" :content1="postContent" @uploadIamge="uploadIamge" v-if="is_me"
          @post_a_post="post_a_post" @save_a_post="save_a_post" />
      </div>
      <div class="col-9">
        <UserProfilePosts :comments="comments" :posts="posts" :user="user" :is_me="is_me" @delete_a_post="delete_a_post"
          @edit_a_post="edit_a_post" @like_a_post="like_a_post" @cancel_a_like="cancel_a_like"
          @getCommentList="getCommentList" @send_a_comment="send_a_comment" @delete_a_comment="delete_a_comment"
          :key="postsUpdateTime" />
      </div>
    </div>
  </ContentField>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import ContentField from "~/components/ContentField.vue";
import UserProfileInfo from "./UserProfile/UserProfileInfo.vue";
import UserProfilePosts from "./UserProfile/UserProfilePosts.vue";
import UserProfileWrite from "./UserProfile/UserProfileWrite.vue";
import { useRoute } from "vue-router";
import { StudentItem, TeacherItem, PostItem, CommentItem } from "~/models/general";
import { getStudentInfo, getTeacherInfo } from "~/services/personServ";
import {
  getPostList, deletePost,
  createPost, editPost,
  getFansNum, follow, getFollowStatus,
  getCommentList, addComment,
  deleteComment, getCommentNum, getPersonInfo,
  getAvatar, uploadAvatar, postLikes, getLikes
} from "~/services/blogServ";

import { useAppStore } from "~/stores/app";
import axios from "axios";
import { message, messageConform } from "~/tools/messageBox";

export default defineComponent({

  components: {
    ContentField,
    UserProfileInfo,
    UserProfilePosts,
    UserProfileWrite,

  },
  // setup() {
  //   //获取路由上的personId参数
  //   const route = useRoute();
  //   personId = route.params.personId;
  //   return { personId };
  // },
  data: () => ({
    user: {} as StudentItem | TeacherItem,//储存所查看的页面的用户信息
    posts: [] as PostItem[],
    comments: {} as { [postId: number]: CommentItem[] },
    commentNum: 0 as number,
    personId: null as number | null,//路由用
    loading: true,
    store: useAppStore(),
    postContent: "",

    postId: 0 as number,
    is_editing: false,
    postsUpdateTime: 0 as number,//时间戳，用于强制刷新
    isProcessing: false, // 新增状态变量
    userId: 0 as number,
    avatar: "" as string,
    image: "" as string,
  }),

  created() {
    this.getUserProfile();
    this.getPostList();
    this.getCurrentPerson();
    this.getAvatar();

  },

  //computed方法，自动计算
  computed: {
    is_me() {
      return this.user.num === this.store.$state.userInfo.username;
    },
    username() {
      return this.store.$state.userInfo.username;
    },
  },
  // watch: {
  //   postsLength() {
  //     this.count = this.posts.length as number;
  //   },
  // },
  methods: {

    async getCurrentPerson() {
      const res = await getPersonInfo();
      if (res.code == 0) {
        this.userId = res.data.personId;
      }
    },

    //获取某个用户的帖子列表
    async getPostList() {
      try {
        this.posts = await getPostList(this.personId);
        for (let post of this.posts) {
          const res = await getCommentNum(post.postId);
          const res2 = await getLikes(post.postId);
          if (res.code == 0) {
            post.commentNum = res.data;
          }
          if (res2.code == 0) {
            post.is_liked = true;
          } else {
            post.is_liked = false;
          }

        }
      } catch (error) {
        console.error('获取帖子失败：', error);
      }
    },

    //获取根据路由某个用户信息
    async getUserProfile() {
      const route = useRoute();
      this.personId = Number(route.params.personId);
      this.user = await getStudentInfo(null, this.personId);
      if (this.user == null) {
        this.user = await getTeacherInfo(null, this.personId);
      }
      //获取粉丝数
      this.user.followers = await getFansNum(this.personId);
      //获取关注状态
      const res = await getFollowStatus(this.personId);

      if (res.code == 0) {
        this.user.is_followed = true;
      } else {
        this.user.is_followed = false;
      }

      this.loading = false;
    },


    async follow() {
      if (this.is_me) {
        message(this, '不能关注自己');
        return;
      }
      if (this.user.is_followed || this.isProcessing) return;

      const res = await follow(this.personId);
      if (res.code == 0) {
        message(this, '关注成功');
        this.user.is_followed = true;
        this.user.followers++;
      } else {
        message(this, '关注失败!');
      }
      this.isProcessing = false;
    },
    async unfollow() {
      if (!this.user.is_followed || this.isProcessing) return;
      const res = await follow(this.personId);
      if (res.code == 0) {
        message(this, '取消关注成功');
        this.user.is_followed = false;
        this.user.followers--;
      } else {
        message(this, '取消关注失败');
      }
      this.isProcessing = false;
    },
    async delete_a_post(postId: number) {
      const resuslt = await messageConform('确定要删除吗？');
      if (!resuslt) return;
      const res = await deletePost(postId);
      if (res.code == 0) {
        message(this, '删除成功');
        this.posts = this.posts.filter((post) => post.postId !== postId);
      } else {
        message(this, '删除帖子失败!');
      }
    },

    //发帖
    async post_a_post(content: string) {
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const date = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');

      const dateTimeString = `${year}/${month}/${date} ${hours}:${minutes}:${seconds}`;

      const res = await createPost(content, dateTimeString, this.personId, this.image);
      if (res.code == 0) {
        message(this, '发帖成功');
        this.posts.push({
          postId: res.data.postId,
          content: content,
          created_at: dateTimeString,
          person: {
            personId: res.data.person.personId,
            num: res.data.person.num,
            name: res.data.person.name,
            dept: res.data.person.dept,
            gender: res.data.person.gender,
            avatar: res.data.person.avatar
          },
          commentNum: 0,
          image: this.image,
          likes: 0,
          is_liked: false,
          is_expanded: false,
        });
        this.postsUpdateTime = Date.now();
      } else {
        message(this, '发帖失败!');
      }

    },

    //编辑帖子
    async save_a_post(content: string) {
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const date = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');

      const dateTimeString = `编辑于${year}/${month}/${date} ${hours}:${minutes}:${seconds}`;

      const res = await editPost(this.postId, content, dateTimeString, this.image);

      if (res.code == 0) {
        message(this, '编辑成功');
        this.posts = this.posts.map((post) => {
          if (post.postId == this.postId) {
            post.content = content;
            post.created_at = dateTimeString;
            post.image = this.image;
          }
          return post;
        });
        this.is_editing = false;
        this.postsUpdateTime = Date.now();
      } else {
        message(this, '编辑失败!');
      }
    },

    //编辑帖子
    edit_a_post(postId: number, content: string, image: string) {
      this.postContent = content;
      this.is_editing = true;
      this.postId = postId;
      this.image = image;
    },

    //展示某个帖子的评论列表
    async getCommentList(postId: number) {
      this.comments[postId] = await getCommentList(postId);
    },

    //发送评论
    async send_a_comment(postId: number, content: string) {
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const date = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');

      const dateTimeString = `${year}/${month}/${date} ${hours}:${minutes}:${seconds}`;
      const res = await addComment(postId, content, dateTimeString);
      if (res.code == 0) {
        message(this, '发送成功');
        this.comments[postId].push({
          commentId: res.data.commentId,
          postId: postId,
          person: {
            personId: res.data.person.personId,
            num: res.data.person.num,
            name: res.data.person.name,
            dept: res.data.person.dept,
            gender: res.data.person.gender,
            avatar: res.data.person.avatar
          },
          content: content,
          created_at: dateTimeString,
        });
      } else {
        message(this, '评论失败!');
      }
    },
    async delete_a_comment(commentId: number) {
      const resuslt = await messageConform('确定要删除吗？');
      if (!resuslt) return;
      const res = await deleteComment(commentId);
      if (res.code == 0) {

        message(this, '删除成功');
        for (const postId in this.comments) {
          this.comments[postId] = this.comments[postId].filter((comment) => comment.commentId !== commentId);
        }
      }
    },
    async getAvatar() {
      const res = await getAvatar(this.personId);
      if (res.code == 0) {
        this.avatar = res.data;
      }
    },
    // 上传头像
    async uploadAvatar(e: any) {

      var formData = new FormData();

      formData.append("file", e.target.files[0]);
      formData.append("smfile", e.target.files[0]);
      axios.post('/avatar', formData, {
        headers: {

          'Content-Type': 'multipart/form-data',
          //smm
          'Authorization': 'Suk2roaGQDuftfckQCX4uEBFnM64agN8',
          //free
          //'Accept': 'application/json',
          //"Authorization": "Bearer " + "47|zmBkbJQaj3uHyor1h1KBdSzplLaygvko0O79b2lX"
        },
      }).then((res) => {
        console.log(res);
        //smms
        if (res.status == 200) {
          if (res.data.code == "image_repeated") {
            this.avatar = res.data.images;
            this.storeAvatar();
          } else {
            this.avatar = res.data.data.url;
            this.storeAvatar();
          }
        } else {
          message(this, res.data.message);
        }


        //free
        // if (res.data.status == true) {
        //   this.avatarURL = res.data.links.url;
        //   this.storeAvatar();
        // } else {
        //   message(this, res.data.message);
        // }

      });

    },
    async storeAvatar() {
      const res = await uploadAvatar(this.avatar);
      if (res.code == 0) {
        message(this, "上传成功");
        this.avatar = this.avatar;
      } else {
        message(this, "上传失败");
        this.avatar = "";
      }
    },


    async uploadIamge(e: any) {
      var formData = new FormData();
      formData.append("file", e.target.files[0]);
      formData.append("smfile", e.target.files[0]);
      axios.post('/avatar', formData, {
        headers: {

          'Content-Type': 'multipart/form-data',
          //smm
          'Authorization': 'Suk2roaGQDuftfckQCX4uEBFnM64agN8',
          //free
          //'Accept': 'application/json',
          //"Authorization": "Bearer " + "47|zmBkbJQaj3uHyor1h1KBdSzplLaygvko0O79b2lX"
        },
      }).then((res) => {

        //smms
        if (res.status == 200) {
          message(this, "上传成功");
          if (res.data.code == "image_repeated") {
            this.image = res.data.images;
          } else {
            this.image = res.data.data.url;
          }
        } else {
          message(this, res.data.message);
        }


        //free
        // if (res.data.status == true) {
        //   this.avatarURL = res.data.links.url;
        //   this.storeAvatar();
        // } else {
        //   message(this, res.data.message);
        // }

      });
    },
    async like_a_post(postId: number) {
      if (this.posts.find((post) => post.postId == postId)?.is_liked) return;
      const res = await postLikes(postId);
      if (res.code == 0) {
        message(this, "点赞成功");
        this.posts = this.posts.map((post) => {
          if (post.postId == postId) {
            post.likes++;
            post.is_liked = true;
          }
          return post;
        });
      } else {
        message(this, "点赞失败");
      }
    },

    async cancel_a_like(postId: number) {
      if (!this.posts.find((post) => post.postId == postId)?.is_liked) return;
      console.log(postId);
      const res = await postLikes(postId);
      if (res.code == 0) {
        message(this, "取消点赞成功");
        this.posts = this.posts.map((post) => {
          if (post.postId == postId) {
            post.likes--;
            post.is_liked = false;
          }
          return post;
        });
      } else {
        message(this, "取消点赞失败");
      }
    },
  }
});
</script>

<style scoped></style>