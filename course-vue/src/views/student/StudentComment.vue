<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>
      <div class="base_title">学生互评</div>
      <div class="app-container" style="margin-top: 20px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card>
              <div style="margin-top: 20px;font-size: x-large;text-align: center">同学列表</div>
              <el-button class="button" v-for="(item, index) in classmates" :key="index" type="primary"
                :plain="buttonIndex !== index" @click="buttonChange(index)">
                {{ item.name }}
              </el-button>
            </el-card>
          </el-col>
          <el-col :span="18">
            <el-card class="box-card" v-if="buttonIndex !== null">
              <div style="float:left">
                <span style="color: #00BFFF;font-weight: bold;font-size: x-large">同学打分</span>
              </div>
              <div style="text-align:right; float:right">
                <el-button style="margin-right:10px" type="primary" @click="Comment()">提交
                </el-button>
              </div>
              <el-divider style="clear: both" />
              <el-rate v-model="value" :icon-classes="iconClasses" void-icon-class="icon-rate-face-off"
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']">
              </el-rate>
              <el-divider style="clear: both" />
              <div style="float:left">
                <span style="color: #00BFFF;font-weight: bold;font-size: x-large">同学标签</span>
              </div>


              <el-divider style="clear: both" />
              <div style="margin-top: 10px">
                <el-check-tag v-for="(item, index) in Tags" :key="index" class="mx-1" size="large"
                  :checked="checkedS[index].checked" @change="onChange(index)"
                  style="margin-left: 40px;font-size: large;font-weight: bold" effect="light" round>
                  {{ item }}
                </el-check-tag>
              </div>
              <el-divider />
              <div class="clearfix">
                <div style="float:left">
                  <span style="color: #00BFFF;font-weight: bold;font-size: x-large">同学评价</span>
                </div>
              </div>
              <el-divider style="clear: both" />
              <el-input style="margin-top: 5px;font-size: large" v-model="textarea"
                :autosize="{ minRows: 4, maxRows: 10 }" type="textarea" />
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import {
  addComment,
  getClassmates,
  studentCommentDelete
}
  from '~/services/teachingServ';
import { type StudentItem, type CheckedItem } from "~/models/general";
import { ElMessage, ElMessageBox } from "element-plus";
import { message, messageConform } from '~/tools/messageBox'
export default defineComponent({
  data: () => ({

    value: 0,
    className: '',
    iconClasses: ['icon-rate-face-1', 'icon-rate-face-2', 'icon-rate-face-3'],
    commentedId: 0,
    comment: "",
    tags: '',
    textarea: '',
    Tags: ['大帅哥', '666', '交际花',
      '阳光', '自信', '坚持', '学霸',
      '运动达人', '社牛', '土豪', '游戏狂',
      '数码迷', '美食家', '文青', '歌王', '交际小能手',
      '宿舍原住民'],
    checked: false,
    checkedS: [] as CheckedItem[],
    buttonIndex: 0,
    classmates: [] as StudentItem[],
    studentIndex: '',
    checkTags: [] as String[],
  }),
  created() {
    this.Classmates();
    this.Tags.forEach((item, index) => {
      let form = {} as CheckedItem;
      form.checked = false;
      this.checkedS[index] = form;
    })
  },
  methods: {
    onChange(index: number) {
      console.log(index, this.checkedS[index])
      this.checkedS[index].checked = !this.checkedS[index].checked
    },
    async Classmates() {
      this.classmates = await getClassmates(this.className);
    },
    buttonChange(index: number) {
      this.buttonIndex = index;
      console.log(this.buttonIndex);
      //手动初始化
      this.textarea = '',
        this.Tags.forEach((item, index) => {
          let form = {} as CheckedItem;
          form.checked = false;
          this.checkedS[index] = form;
        })
    },
    async Comment() {
      if (!this.textarea) {
        message(this, '请评论后提交')
      }
      else {
        this.commentedId = this.classmates[this.buttonIndex].studentId
        this.Tags.forEach((item, index) => {
          if (this.checkedS[index].checked) {
            this.checkTags.push(item);

          }
        })
        this.tags = this.checkTags.join(",");

        this.comment = this.textarea
        const res = await addComment(
          this.commentedId,
          this.comment,
          this.tags,
          this.value,
        )
        this.value = 0;
        this.Tags.forEach((item, index) => {
          let form = {} as CheckedItem;
          form.checked = false;
          this.checkedS[index] = form;
        })
        this.checkTags.length = 0;
        this.textarea = '';
        if (res.code !== 0) {
          message(this, res.msg);
          return;
        }

        if (res.code == 0) {
          message(this, "提交成功");

        } else {
          message(this, res.msg);
        }

      }
    }

  }
})


</script>
<style lang="scss" scoped>
.userSettingStyle {
  background-color: #fff;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  padding: 12px;

  ::v-deep .el-tabs__content {
    display: block !important;

    ::v-deep.el-tab-pane {
      padding: 8px 30px;
    }
  }

}

.settingsStyleTitle {
  margin-bottom: 12px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  font-size: 25px;
  line-height: 28px;
  text-align: left;
}

::v-deep .box-card {
  height: auto;
}

.button {
  margin-top: 20px;
  margin-left: 10%;
  width: 80%;
  size: auto;
  font-size: large;
}

.div1 {
  font-size: large;
  float: left;
  clear: both;
  margin-top: 5px;
  margin-left: 10px;
  margin-bottom: 5px;
  width: 50%;
}

.div2 {
  font-size: large;
  float: left;
  margin-top: 5px;
  margin-bottom: 5px;
}
</style>