<template>
  <div class="row component-background">
    <div class="row">
      <div class="col-3"></div>
      <div class="col-7">
        <nav class="navbar bg-body-tertiary">
          <div class="container-fluid">
            <a class="navbar-brand" href="#">
              看看大家发生了哪些新鲜事~
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
                  <a class="nav-link" :href="`/UserProfile/${personId}`">我的空间</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="/UserList">用户列表</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="/FriendList">好友列表</a>
                </li>
              </ul>
              <form class="d-flex mt-3" role="search">
                <input class="form-control me-2" type="search" v-model="searchContent" placeholder="Search"
                  aria-label="Search">
                <button class="btn btn-outline-success" type="button" @click="search">Search</button>
              </form>
            </div>
          </div>
        </nav>
        <ul class="nav nav-tabs" id="myTab" role="tablist">
          <li class="nav-item" role="presentation">
            <button class="nav-link active  my-tab" id="all-posts-tab" data-bs-toggle="tab" data-bs-target="#allPosts"
              type="button" role="tab" aria-controls="allPosts" aria-selected="true"
              @click="getAllPosts, getFollowedPosts">全部</button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link  my-tab" id="followed-posts-tab" data-bs-toggle="tab" data-bs-target="#followedPosts"
              type="button" role="tab" aria-controls="followedPosts" aria-selected="false">关注</button>
          </li>
        </ul>

        <div class="tab-content" id="myTabContent">
          <div class="tab-pane fade show active" id="allPosts" role="tabpanel" aria-labelledby="all-posts-tab">
            <div class="card single-post" v-for="post in reversedAllPosts" :key="post.postId">
              <div class="card-header">
                <div style="display: flex; align-items: center;">
                  <img @click="routeUserProfile(post.person.personId)" class="img-field" :src="post.person.avatar"
                    style="width: 50px; height: 50px;" alt="">
                  <a @click="routeUserProfile(post.person.personId)" class="name btn btn-link">{{ post.person.name }}</a>
                </div>
                <div class="post-timestamp">
                  {{ post.created_at }}
                </div>
              </div>
              <div v-if="post.image" class="card-body" :class="post.is_expanded ? 'expanded' : 'collapsed1'">
                <div class="row">
                  <div class="col-12">
                    <div class="card-body" style="white-space: pre-wrap;">
                      {{ post.content }}
                    </div>
                  </div>
                  <div class="col-12">
                    <img class="img-fluid img-thumbnail float-start photo" style="" :src="post.image" alt="失败">
                  </div>
                </div>
              </div>
              <div v-else class="card-body" style="white-space: pre-wrap;">
                {{ post.content }}
              </div>
              <div class="card-footer text-muted">
                <a v-if="post.image" class="isExpanded" @click="post.is_expanded = !post.is_expanded">
                  {{ post.is_expanded ? "收起" : "展开" }}
                </a>
                <div class="clearfix">
                  <div class="comment-button">
                    <div v-if="post.person.num == username || username === 'admin'" class="btn-group btn-group-sm"
                      role="group" aria-label="Basic mixed styles example">
                      <a @click.prevent="delete_a_post(post.postId)" type="button" class="btn btn-link">删除</a>
                    </div>
                    <a v-if="!post.is_liked" class="btn btn-link comment-a"
                      @click.prevent="like_a_post_in_allposts(post.postId)" role="button">
                      <i class="fas fa-thumbs-up">({{ post.likes }})</i>
                    </a>
                    <a v-else class="btn btn-link comment-a" @click.prevent="cancel_a_like_in_allposts(post.postId)"
                      role="button">
                      <i class="fas fa-thumbs-up" style="color: rgb(49, 108, 244);">({{ post.likes }})</i>
                    </a>
                    <a @click.prevent="getCommentList(post.postId)" class="comment-a btn btn-link"
                      data-bs-toggle="collapse" :href="'#comment' + post.postId" role="button" aria-expanded="false"
                      :aria-controls="'comment' + post.postId"><i class="far fa-comment" />({{ post.commentNum }})</a>
                  </div>
                  <div class="collapse" :id="'comment' + post.postId">
                    <h4>共{{ post.commentNum }}条评论</h4>
                    <div class="comment-area">
                      <span>
                        <textarea v-model="commentContent" placeholder="在这里写评论.." class="form-control"
                          id="exampleFormControlTextarea1" rows="3"></textarea>
                        <div class="comment-button">
                          <a @click="send_a_comment(post.postId, commentContent)" class="send btn btn-link"
                            role="button">发送</a>
                        </div>
                      </span>
                    </div>
                    <hr style="border-width: 2px;border-color: rgb(49,108,244);">

                    <div>
                      <ul class="list-group list-group-flush">

                        <li v-for="comment in comments[post.postId]" :key="comment.commentId" class="list-group-item">
                          <div class="row">
                            <div class="col-1">
                              <img @click="routeUserProfile(comment.person.personId)" class="img-field"
                                :src="comment.person.avatar" style="width: 50px; height: 50px;" alt="">
                            </div>
                            <div class="col-11">
                              <div style="display: flex; justify-content: space-between;">
                                <a @click="routeUserProfile(comment.person.personId)" class="name btn btn-link">{{
                                  comment.person.name }}</a>
                                <div class="created_time">发布于{{ comment.created_at }}</div>
                              </div>


                              <div style="display: flex; justify-content: space-between;align-items: center;">
                                <p class="content">{{ comment.content }}</p>
                                <a v-if="comment.person.num == username || post.person.num == username || username === 'admin'"
                                  class="delete btn btn-link" @click="delete_a_comment(comment.commentId)">删除</a>

                              </div>
                            </div>
                          </div>
                        </li>

                      </ul>
                      <hr style="border-width: 2px;border-color: rgb(49,108,244);">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="tab-pane fade" id="followedPosts" role="tabpanel" aria-labelledby="followed-posts-tab">
            <div class="card single-post" v-for="post in reversedFollowedPosts" :key="post.postId">
              <div class="card-header">
                <div style="display: flex; align-items: center;">
                  <img @click="routeUserProfile(post.person.personId)" class="img-field" :src="post.person.avatar"
                    style="width: 50px; height: 50px;" alt="">
                  <a @click="routeUserProfile(post.person.personId)" class="name btn btn-link">{{ post.person.name }}</a>
                </div>
                <div class="post-timestamp">
                  {{ post.created_at }}
                </div>
              </div>
              <div v-if="post.image" class="card-body" :class="post.is_expanded ? 'expanded' : 'collapsed1'">
                <div class="row">
                  <div class="col-12">
                    <div class="card-body" style="white-space: pre-wrap;">
                      {{ post.content }}
                    </div>
                  </div>
                  <div class="col-12">
                    <img class="img-fluid img-thumbnail float-start photo" style="" :src="post.image" alt="失败">
                  </div>
                </div>
              </div>
              <div v-else class="card-body" style="white-space: pre-wrap;">
                {{ post.content }}
              </div>
              <div class="card-footer text-muted">
                <a v-if="post.image" class="isExpanded" @click="post.is_expanded = !post.is_expanded">
                  {{ post.is_expanded ? "收起" : "展开" }}
                </a>
                <div class="clearfix">
                  <div class="comment-button">
                    <div v-if="post.person.num == username || username === 'admin'" class="btn-group btn-group-sm"
                      role="group" aria-label="Basic mixed styles example">
                      <a @click.prevent="delete_a_post(post.postId)" type="button" class="btn btn-link">删除</a>
                    </div>

                    <a v-if="!post.is_liked" class="btn btn-link comment-a"
                      @click.prevent="like_a_post_in_followposts(post.postId)" role="button">
                      <i class="fas fa-thumbs-up">({{ post.likes }})</i>
                    </a>
                    <a v-else class="btn btn-link comment-a" @click.prevent="cancel_a_like_in_followposts(post.postId)"
                      role="button">
                      <i class="fas fa-thumbs-up" style="color: rgb(49, 108, 244);">({{ post.likes }})</i>
                    </a>
                    <a @click.prevent="getCommentList(post.postId)" class="comment-a btn btn-link"
                      data-bs-toggle="collapse" :href="'#comment' + post.postId" role="button" aria-expanded="false"
                      :aria-controls="'comment' + post.postId"><i class="far fa-comment" />({{ post.commentNum }})</a>
                  </div>
                  <div class="collapse" :id="'comment' + post.postId">
                    <h4>共{{ post.commentNum }}条评论</h4>
                    <div class="comment-area">
                      <span>
                        <textarea v-model="commentContent" placeholder="在这里写评论.." class="form-control"
                          id="exampleFormControlTextarea1" rows="3"></textarea>
                        <div class="comment-button">
                          <a @click="send_a_comment(post.postId, commentContent)" class="send btn btn-link"
                            role="button">发送</a>
                        </div>
                      </span>
                    </div>
                    <hr style="border-width: 2px;border-color: rgb(49,108,244);">

                    <div>
                      <ul class="list-group list-group-flush">

                        <li v-for="comment in comments[post.postId]" :key="comment.commentId" class="list-group-item">
                          <div class="row">
                            <div class="col-1">
                              <img @click="routeUserProfile(comment.person.personId)" class="img-field"
                                :src="comment.person.avatar" style="width: 50px; height: 50px;" alt="">
                            </div>
                            <div class="col-11">
                              <div style="display: flex; justify-content: space-between;">
                                <a @click="routeUserProfile(comment.person.personId)" class="name btn btn-link">{{
                                  comment.person.name }}</a>
                                <div class="created_time">发布于{{ comment.created_at }}</div>
                              </div>


                              <div style="display: flex; justify-content: space-between;align-items: center;">
                                <p class="content">{{ comment.content }}</p>
                                <a v-if="comment.person.num == username || post.person.num == username || username === 'admin'"
                                  class="delete btn btn-link" @click="delete_a_comment(comment.commentId)">删除</a>

                              </div>
                            </div>
                          </div>
                        </li>

                      </ul>
                      <hr style="border-width: 2px;border-color: rgb(49,108,244);">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
      <div class="col-2">

      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { PostItem, CommentItem } from "~/models/general";
import { useAppStore } from "~/stores/app";
import {
  getAllPost, getCommentList, getCommentNum,
  deleteComment, addComment, deletePost, getPersonInfo,
  getFollowPost, postLikes, getLikes,
} from "~/services/blogServ";
import { message, messageConform } from "~/tools/messageBox";
export default defineComponent({
  name: "BlogIndex",

  data: () => ({
    store: useAppStore(),
    commentContent: "",
    allposts: [] as PostItem[],
    followedPosts: [] as PostItem[],
    comments: {} as { [postId: number]: CommentItem[] },
    personId: 0 as number,
    searchContent: "" as string,
  }),

  created() {
    this.getAllPosts();
    this.getCurrentPerson();
    this.getFollowedPosts();
  },

  methods: {
    async search() {
      this.allposts = await getAllPost(this.searchContent);
      for (let post of this.allposts) {
        const res = await getCommentNum(post.postId);
        if (res.code == 0) {
          post.commentNum = res.data;
        }
      }

    },

    async getCurrentPerson() {
      const res = await getPersonInfo();
      if (res.code == 0) {
        this.personId = res.data.personId;
      }
    },

    routeUserProfile(personId: number) {
      this.$router.push(`/UserProfile/${personId}`);
    },
    async getAllPosts() {
      this.allposts = await getAllPost(null);
      for (let post of this.allposts) {
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

    },

    async getFollowedPosts() {
      this.followedPosts = await getFollowPost();
      for (let post of this.followedPosts) {
        const res = await getCommentNum(post.postId);
        const res2 = await getLikes(post.postId);
        if (res.code == 0) {
          post.commentNum = res.data;
        }
        if (res.code == 0) {
          post.commentNum = res.data;
        }
        if (res2.code == 0) {
          post.is_liked = true;
        } else {
          post.is_liked = false;
        }
      }
    },

    async getCommentList(postId: number) {
      this.comments[postId] = await getCommentList(postId);
    },

    async delete_a_comment(commentId: number) {
      const resuslt = await messageConform('确定要删除吗？');
      if (!resuslt) return;
      const res = await deleteComment(commentId);
      if (res.code == 0) {
        message(this, "删除成功");
        for (const postId in this.comments) {
          this.comments[postId] = this.comments[postId].filter((comment) => comment.commentId !== commentId);
        }
      }
    },
    //发送评论
    async send_a_comment(postId: number, content: string) {
      if (!content.trim()) {
        message(this, '内容不能为空');
        return;
      }
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
        message(this, "发送成功");
        this.comments[postId].push({
          commentId: res.data.commentId,
          postId: postId,
          person: {
            personId: res.data.person.personId,
            num: res.data.person.num,
            name: res.data.person.name,
            dept: res.data.person.dept,
            gender: res.data.person.gender,
            avatar: res.data.person.avatar,
          },
          content: content,
          created_at: dateTimeString,
        });
      }
      this.commentContent = "";
    },
    async delete_a_post(postId: number) {
      const resuslt = await messageConform('确定要删除吗？');
      if (!resuslt) return;
      const res = await deletePost(postId);
      if (res.code == 0) {
        message(this, "成功删除");
        this.allposts = this.allposts.filter((post) => post.postId !== postId);
      } else {
        console.error('删除帖子失败：', res);
      }
    },
    async like_a_post_in_allposts(postId: number) {
      if (this.allposts.find((post) => post.postId == postId)?.is_liked) return;
      const res = await postLikes(postId);
      if (res.code == 0) {
        message(this, "已点赞");
        this.allposts = this.allposts.map((post) => {
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

    async cancel_a_like_in_allposts(postId: number) {
      if (!this.allposts.find((post) => post.postId == postId)?.is_liked) return;
      console.log(postId);
      const res = await postLikes(postId);
      if (res.code == 0) {
        message(this, "已取消点赞");
        this.allposts = this.allposts.map((post) => {
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
    async like_a_post_in_followposts(postId: number) {
      if (this.followedPosts.find((post) => post.postId == postId)?.is_liked) return;
      const res = await postLikes(postId);
      if (res.code == 0) {
        message(this, "已点赞");
        this.followedPosts = this.followedPosts.map((post) => {
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

    async cancel_a_like_in_followposts(postId: number) {
      if (!this.followedPosts.find((post) => post.postId == postId)?.is_liked) return;
      console.log(postId);
      const res = await postLikes(postId);
      if (res.code == 0) {
        message(this, "已取消点赞");
        this.followedPosts = this.followedPosts.map((post) => {
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



  },
  computed: {
    //倒序输出帖子，最新的在最上面
    reversedAllPosts() {
      return this.allposts.slice().reverse();
    },
    reversedFollowedPosts() {
      return this.followedPosts.slice().reverse();
    },
    username() {
      return this.store.$state.userInfo.username;
    },
  },
});

</script>

<style scoped>
.isExpanded {

  cursor: pointer;

  text-decoration: underline;
  font-size: 14px;
}

.collapsed1 {
  height: 300px;
  overflow: hidden;

}

.expanded {
  height: auto;
}

.photo {
  max-width: 600px;
}

.tab-content {
  min-height: 100vh;
}

nav {
  margin-bottom: 10px;
}

.component-background {
  background: url('../../../public/background-2061910.jpg') no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.single-post {
  margin-bottom: 52px;

  border-color: black;
}

.post-timestamp {
  font-size: 12px;
  text-align: right;
  color: #999;
}

.btn-group {
  float: right;
}

.comment-button {

  text-align: right;
}

.comment-a {
  font-size: 16px;
  color: #999;
  text-decoration: none;
  /* 去掉下划线 */
}

.comment-a:hover {
  color: #007bff;

}

.send {
  font-size: 14px;
  color: #999;
  text-decoration: none;
  /* 去掉下划线 */
}

.send:hover {
  color: #007bff;

}

.created_time {
  font-size: 12px;
  color: #999;
}

.delete {
  font-size: 12px;
  color: red;
  text-decoration: none;

  /* 去掉下划线 */
}

.img-field {

  cursor: pointer;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.name {
  font-size: 18px;
  color: #080000;
  text-decoration: none;
  font-weight: bold;
  /* 去掉下划线 */

}

.name:hover {
  color: #007bff;
  /* 将 otherColor 替换为你想要的颜色 */
}

.offcanvas-title {
  color: #007bff;
}

.card {
  transition: box-shadow 0.5s ease;
  /* 阴影会在 0.3 秒内平滑地出现 */
  box-shadow: 0 0 0 rgba(0, 0, 0, 0);
  /* 默认没有阴影 */
}

.card:hover {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  /* 鼠标悬停时阴影变为 10px 模糊黑色 */
}

.my-tab {
  background-color: white;
  transition: box-shadow 0.3s ease;
}

.my-tab:hover {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.my-tab.active {
  background-color: gray;
}
</style>