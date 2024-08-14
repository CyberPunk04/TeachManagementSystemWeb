<template>
  <div class="container content_field">
    <!-- 一个自适应容器 -->
    <div class="card">
      <div class="card-body">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "ContentField",
});


</script>

<style scoped>
div.content_field {
  margin-top: 20px;

}

.card {

  min-height: 800px;
  background-color: rgba(255, 255, 255, 0.5);
  /* 白色背景，50% 透明度 */
}
</style>