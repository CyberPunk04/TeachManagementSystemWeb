<template>
  <div class="edit-field">
    <div class="form-floating">
      <textarea v-model="content" class="form-control" placeholder="请编辑文字" id="text" style="height: 240px;"></textarea>
      <label for="text">编辑区</label>
      <button v-if="!is_editing" @click.prevent="post_a_post" type="button" class="btn btn-outline-primary">发送</button>
      <button v-else @click.prevent="save_a_post" type="button" class="btn btn-outline-success">保存</button>
      <form id="form_submit_photo" class="form-horizontal" role="form" method="post" enctype="multipart/form-data">
        <label v-if="!is_editing" class="btn btn-default btn-file">
          上传图片
          <input id="ingredient_file" type="file" accept="image/*" style="display: none;" name="photo"
            @change="uploadIamge($event)">
        </label>
        <label v-else class="btn btn-default btn-file">
          编辑图片
          <input id="ingredient_file" type="file" accept="image/*" style="display: none;" name="photo"
            @change="uploadIamge($event)">
        </label>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "UserProfileWrite",
  data: () => ({
    content: "",
  }),

  props: {
    content1: {
      type: String,
      required: false,
    },
    is_editing: {
      type: Boolean,
      required: true,
    },
  },

  watch: {
    content1(content1: string) {
      this.content = content1;
    },
  },

  methods: {
    post_a_post() {
      if (!this.content.trim()) {
        alert('帖子内容不能为空');
        return;
      }
      this.$emit("post_a_post", this.content);
      this.content = "";
    },
    save_a_post() {
      this.$emit("save_a_post", this.content);
      this.content = "";
    },
    uploadIamge(e: any) {
      console.log("2");
      this.$emit("uploadIamge", e);
    },
  }
});
</script>

<style scoped>
form {

  background: rgb(171, 169, 169);
  display: block;
  margin-top: 5px;
  border-color: 1px solid rgb(49, 108, 244);
  border-radius: 15px;
  float: right;
}

form:hover {
  background: rgb(49, 108, 244);
}

.edit-field {
  margin-top: 10px;
}

button {
  margin-top: 10px;
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
</style>