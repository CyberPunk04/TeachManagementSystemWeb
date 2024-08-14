<template>
  <div class="card content">
    <div class="card-body">
      <div class="row">
        <div v-for="post in reversedPosts" :key="post.postId">
          <div class="card single-post">
            <div v-if="post.image" class="card-body photo-content" :class="post.is_expanded ? 'expanded' : 'collapsed1'">
              <div class="row">
                <div class="col-12" style="white-space: pre-wrap;">{{ post.content }}</div>
                <div v-if="post.image">
                  <img class="img-fluid img-thumbnail float-start photo" :src="post.image" alt="失败">
                </div>
              </div>

            </div>
            <div v-else class="card-body photo-content" style="white-space: pre-wrap;">
              <div class="row">
                <div class="col-12">{{ post.content }}</div>
              </div>
            </div>
            <div class="post-timestamp">
              {{ post.created_at }}
            </div>
            <a v-if="post.image" class="isExpanded" @click="post.is_expanded = !post.is_expanded">
              {{ post.is_expanded ? "收起" : "展开" }}
            </a>
          </div>
          <div v-if="is_me || username === 'admin'" class="btn-group btn-group-sm" role="group"
            aria-label="Basic mixed styles example">
            <button @click.prevent="edit_a_post(post.postId, post.content, post.image)" type="button"
              v-if="username !== 'admin'" class="btn btn-primary">编辑</button>
            <button @click.prevent="delete_a_post(post.postId)" type="button" class="btn btn-danger">删除</button>
          </div>
          <div class="clearfix">
            <div class="comment-button">

              <a v-if="!post.is_liked" class="btn btn-link comment-a" @click.prevent="like_a_post(post.postId)"
                role="button">
                <i class="fas fa-thumbs-up">({{ post.likes }})</i>
              </a>
              <a v-else class="btn btn-link comment-a" @click.prevent="cancel_a_like(post.postId)" role="button">
                <i class="fas fa-thumbs-up" style="color: rgb(49, 108, 244);">({{ post.likes }})</i>
              </a>

              <a @click.prevent="getCommentList(post.postId)" class="comment-a btn btn-link" data-bs-toggle="collapse"
                :href="'#comment' + post.postId" role="button" aria-expanded="false"
                :aria-controls="'comment' + post.postId"><i class="far fa-comment" />({{ post.commentNum }})</a>
            </div>

            <div class="collapse" :id="'comment' + post.postId">
              <h4>共{{ post.commentNum }}条评论</h4>
              <div class="comment-area">
                <span>
                  <textarea v-model="commentContent" placeholder="在这里写评论.." class="form-control"
                    id="exampleFormControlTextarea1" rows="3"></textarea>
                  <div class="comment-button">
                    <a @click="send_a_comment(post.postId, commentContent)" class="send btn btn-link" role="button">发送</a>
                  </div>
                </span>
              </div>
              <hr style="border-width: 2px;border-color: rgb(49,108,244);">
              <!-- 评论区 -->
              <div>
                <ul class="list-group list-group-flush">

                  <li v-for="comment in comments[post.postId]" :key="comment.commentId" class="list-group-item">
                    <div class="row">
                      <div class="col-1">
                        <img @click="routeUserProfile(comment.person.personId)" class="img-field"
                          :src="comment.person.avatar" style="width: 50px; height: 50px;" alt="失败">
                      </div>
                      <div class="col-11">
                        <div style="display: flex; justify-content: space-between;">
                          <a @click="routeUserProfile(comment.person.personId)" class="name btn btn-link">{{
                            comment.person.name }}</a> <!-- 评论人 -->
                          <div class="created_time">发布于{{ comment.created_at }}</div> <!-- 评论时间 -->
                        </div>


                        <div style="display: flex; justify-content: space-between;align-items: center;">
                          <p class="content">{{ comment.content }}</p> <!-- 内容 -->
                          <a v-if="comment.person.num == username || is_me || username === 'admin'"
                            class="delete btn btn-link" @click="delete_a_comment(comment.commentId)">删除</a>
                          <!-- 删除评论 -->
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
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useAppStore } from "~/stores/app";

export default defineComponent({
  name: "UserProfilePosts",

  data: () => ({
    store: useAppStore(),
    commentContent: "",
  }),

  props: {
    posts: {
      type: Object,
      required: true,
    },
    user: {
      type: Object,
      required: true,
    },
    comments: {
      type: Object,
      required: true,
    },
    is_me: {
      type: Boolean,
      required: true,
    },
  },

  methods: {

    routeUserProfile(personId: number) {
      console.log(personId);
      this.$router.push(`/UserProfile/${personId}`);
    },
    delete_a_post(postId: number) {
      this.$emit("delete_a_post", postId);
    },
    edit_a_post(postId: number, content: string, image: string) {
      this.$emit("edit_a_post", postId, content, image);
    },
    like_a_post(postId: number) {
      console.log(postId);
      this.$emit("like_a_post", postId);
    },
    cancel_a_like(postId: number) {
      this.$emit("cancel_a_like", postId);
    },
    getCommentList(postId: number) {
      this.$emit("getCommentList", postId);
    },
    send_a_comment(postId: number, commentContent: string) {
      if (!commentContent.trim()) {
        alert('评论内容不能为空');
        return;
      }
      this.$emit("send_a_comment", postId, commentContent);
      this.commentContent = "";
    },
    delete_a_comment(commentId: number) {
      this.$emit("delete_a_comment", commentId);
    },





  },
  computed: {
    //倒序输出帖子，最新的在最上面
    reversedPosts() {
      return this.posts.slice().reverse();
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

.single-post {
  margin-bottom: 5px;
  margin-top: 12px;
  border-color: black;
}

.photo {
  max-width: 500px;
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
  font-size: 14px;
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
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;

  cursor: pointer;
}
</style>
