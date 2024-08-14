<template>
  <div class="base_form">
    <div class="div1">
      <div class="block1">
        <input type="text" />
        <input type="text" />
        <input type="text" />
      </div>
      <div class="block2"></div>
      <div class="block3"></div>
    </div>
    <div class="div1">
      <div class="block1"></div>
      <div class="block2"></div>
      <div class="block3"></div>
    </div>
    <div class="div1">
      <div class="block1"></div>
      <div class="block2"></div>
      <div class="block3"></div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  name: "TestDiv",
  data() {
    return {};
  },
});
</script>
<style scoped>
.div1 {
  height: 20%;
  display: flex;
  flex-direction: row;
  border: solid 1px;
  padding: 0px;
}
.div1 .block1 {
  margin: 0px;
  width: 30%;
  height: 100%;
  border: solid 1px;
}
.div1 .block2 {
  width: 30%;
  height: 80%;
  border: solid 1px;
}
.div1 .block3 {
  width: 40%;
  height: 80%;
  border: solid 1px;
}
</style>
