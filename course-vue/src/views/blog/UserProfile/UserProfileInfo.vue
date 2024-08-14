<template>
  <div class="card">
    <div class="card-body">
      <div class="row">
        <div class="col-4">
          <div class="avatar-area">
            <img class="img-fluid img-field" :src="avatar" alt="加载失败">
            <form v-if="is_me" id="form_submit_photo" class="form-horizontal" role="form" method="post"
              enctype="multipart/form-data">

              <label class="btn btn-default btn-file">
                更新头像
                <input id="ingredient_file" type="file" accept="image/*" style="display: none;" name="photo"
                  @change="uploadAvatar($event)">
              </label>
            </form>

          </div>
        </div>

        <div class="col-8">
          <div class="Username">{{ user.name }}</div>
          <div class="fans">粉丝数: {{ user.followers }}</div>
          <button v-if="!user.is_followed" @click="follow" type="button" class="btn btn-primary">+关注</button>
          <button v-else type="button" @click="unfollow" class="btn btn-primary">取消关注</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { uploadAvatar } from "~/services/blogServ";

export default defineComponent({

  name: "UserProfileInfo",

  data: () => ({
    avatarURL: "",
  }),

  props: {
    user: {
      type: Object,
      required: true,
    },
    avatar: {
      type: String,
      required: true,

    },
    is_me: {
      type: Boolean,
      required: true,
    },
  },

  // methods 中的函数不会像 computed 属性那样被缓存，
  // 所以如果需要频繁地重新计算某些值,需要使用 computed 属性。
  computed: {

  },

  methods: {
    follow() {

      this.$emit("follow");
    },
    unfollow() {

      this.$emit("unfollow");
    },
    // 上传头像
    uploadAvatar(e: any) {
      this.$emit("uploadAvatar", e);
    },



  }

});

</script>

<style scoped>
img {
  border-radius: 50%;

}

.avatar {
  margin-top: 10px;
}

.avatar-area {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.img-field {
  max-width: px;
}

img {
  border-radius: 50%;
}

.Username {
  font-size: 17px;
  font-weight: bold;
}

.fans {
  font-size: 12px;
  color: gray
}

button {
  padding: 2px 4px;
  font-size: 12px;
}

.btn-file input[type=file] {
  position: absolute;
  min-width: 100%;
  min-height: 100%;
  font-size: 100px;
  text-align: right;
  filter: alpha(opacity=0);
  overflow: hidden;
  border-radius: 4px;
  color: #333;
  border: 1px solid #000000;

  padding: 6px 12px;
  font-size: 14px;
  font-weight: 400;
  line-height: 1.42857143;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  -ms-touch-action: manipulation;
  touch-action: manipulation;
  cursor: pointer;
}

form {
  background: rgb(245, 242, 242);
  display: block;
  margin-top: 1em;
  border-radius: 15px;

}
</style>
