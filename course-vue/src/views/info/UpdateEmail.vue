<template>
  <!-- 最外层区域表单 -->
  <div class="base_form">
    <!-- 区域顶部标题 -->
    <div class="base_header">
      <div class="blue_column"></div>
      <div class="base_title">更改邮箱</div>
    </div>
    <!-- 页面提示 -->

    <!-- 表单内容 -->
    <div class="from-container">
      <div class="oldPassword flex-row">
        <div class="oldPass">旧邮箱</div>
        <div>
          <input class="inputWidth" v-model="oldEmail" type="email" />
        </div>
      </div>
      <div class="emailContent flex-row">
        <div class="email">输入绑定的邮箱</div>
        <div>
          <input class="inputWidth" v-model="newEmail" type="email" />
        </div>
      </div>

      <div class="valiCodeContent flex-row">
        <div class="valiCode">邮箱验证码</div>
        <div>
          <input class="vailInput" v-model="valiCode" />
        </div>
        <span>
          <div class="flex-row">
            <div class="vailCodeBtn flex-col" @click="getEmailCode">
              <span class="word19">发送验证码</span>
            </div>
          </div>
        </span>
      </div>




      <div class="main8 flex-row">
        <div class="group8 flex-col" @click="submitEmail()">
          <span class="word20">修改</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import { updateEmail, getEmailCode, getEmailCode2 } from "~/services/infoServ";
import { message } from "~/tools/messageBox";
import { getPersonInfo } from "~/services/blogServ"
import { useAppStore } from "~/stores/app";
import router from "~/router";
export default defineComponent({
  data: () => ({
    passw: "password",
    oldEmail: "",
    newEmail: "",
    rightEmail: "",
    valiCode: "",
    msg: "",
  }),
  created() {
    this.getEmail();
  },
  methods: {
    //获取绑定的邮箱
    async getEmail() {
      const res = await getPersonInfo();
      if (res.code == 0) {
        this.rightEmail = res.data.email;
      }
    },

    async getEmailCode() {
      if (this.oldEmail === undefined || this.newEmail === "") {
        message(this, "邮箱为空");
        return;
      }
      const res = await getEmailCode2({
        oldEmail: this.oldEmail,
        newEmail: this.newEmail,
      });
      if (res.code == 0) {
        message(this, "验证码已发送");
      } else {
        message(this, res.msg);
      }
    },

    logout() {
      const store = useAppStore();
      store.logout();
      router.push("/Login");
    },
    // 提交密码
    submitEmail() {
      var msg = "";
      if (this.oldEmail === undefined || this.oldEmail === "") {
        msg = "邮箱为空不能修改";
      } else if (this.oldEmail !== this.rightEmail) {
        msg = "与绑定的邮箱不匹配";
      } else if (this.newEmail === undefined || this.newEmail === "") {
        msg = "新邮箱为空不能修改";
      } else if (this.newEmail === this.oldEmail) {
        msg = "新邮箱和旧邮箱相同";
      } else if (this.valiCode === undefined || this.valiCode === "") {
        msg = "验证码为空不能修改";
      }

      if (msg !== "") {
        message(this, msg);
      } else {
        updateEmail({
          oldEmail: this.oldEmail,
          newEmail: this.newEmail,
          code: this.valiCode,
        }).then((response) => {
          if (response.code == 0) {
            message(this, "提交成功");
            router.push("/MainPage");
          } else {
            message(this, response.msg);
          }
        });
      }
    },
  },
});
</script>
<style>
.alarm {
  margin-left: 594px;
  margin-top: 8px;
  font-family: PingFangTC-Regular;
  font-size: 12px;
  color: #930e14;
  letter-spacing: 0;
  font-weight: 400;
}

.newPassword {
  margin-left: 485px;
  margin-top: 34px;
}

.reconfirmP {
  margin-left: 470px;
  margin-top: 9px;
}

.emailContent {
  margin-left: 470px;
  margin-top: 9px;
}

.valiCodeContent {
  margin-left: 470px;
  margin-top: 19px;
}

.inputWidth {
  width: 478px;
}

.vailInput {
  width: 380px;
}

input {
  height: 42px;
  line-height: 42px;
  border-radius: 0px;
}

.rPass {
  width: 120px;
  height: 42px;
  font-size: 14px;
  font-family: PingFangTC-Regular, PingFangTC;
  font-weight: 400;
  color: #202020;
  line-height: 42px;
  text-align: center;
  margin-right: 6px;
}

.email {
  width: 120px;
  height: 42px;
  font-size: 14px;
  font-family: PingFangTC-Regular, PingFangTC;
  font-weight: 400;
  color: #202020;
  line-height: 42px;
  text-align: center;
  margin-right: 6px;
}

.valiCode {
  width: 120px;
  height: 42px;
  font-size: 14px;
  font-family: PingFangTC-Regular, PingFangTC;
  font-weight: 400;
  color: #202020;
  line-height: 42px;
  text-align: center;
  margin-right: 6px;
}

.newPass {
  width: 100px;
  height: 42px;
  font-size: 14px;
  font-family: PingFangTC-Regular, PingFangTC;
  font-weight: 400;
  color: #202020;
  line-height: 42px;
  text-align: center;
  margin-right: 12px;
}

.oldPass {
  text-align: center;
  width: 42px;
  height: 42px;
  font-size: 14px;
  font-family: PingFangTC-Regular, PingFangTC;
  font-weight: 400;
  color: #202020;
  line-height: 42px;
  margin-right: 12px;
}

.oldPassword {
  margin-left: 542px;
  margin-top: 125px;
}

.main8 {
  width: 640px;
  height: 42px;
  margin: 42px 0 0 457px;
  border: none;
}

.group8 {
  background-color: rgba(147, 14, 20, 1);
  border-radius: 4px;
  height: 42px;
  width: 311px;
  cursor: pointer;
}

.vailCodeBtn {
  background-color: rgba(147, 14, 20, 1);

  border-radius: 4px;
  height: 42px;
  width: 95px;
  margin-left: 3px;
  cursor: pointer;
}

.word19 {
  width: 28px;
  height: 14px;
  overflow-wrap: break-word;
  color: rgba(255, 255, 255, 1);
  font-size: 14px;
  font-family: STHeitiSC-Medium;
  text-align: left;
  white-space: nowrap;
  line-height: 14px;
  display: block;
  margin: 15px 0 0 10px;
}

.word20 {
  width: 28px;
  height: 14px;
  overflow-wrap: break-word;
  color: rgba(255, 255, 255, 1);
  font-size: 14px;
  font-family: STHeitiSC-Medium;
  text-align: left;
  white-space: nowrap;
  line-height: 14px;
  display: block;
  margin: 15px 0 0 140px;
}

.group9 {
  background-color: rgba(226, 227, 228, 1);
  border-radius: 4px;
  height: 42px;
  width: 311px;
}

.info8 {
  width: 28px;
  height: 14px;
  overflow-wrap: break-word;
  color: rgba(131, 131, 131, 1);
  font-size: 14px;
  font-family: SourceHanSansCN-Regular;
  text-align: left;
  white-space: nowrap;
  line-height: 14px;
  display: block;
  margin: 15px 0 0 140px;
}

.flex-col {
  display: flex;
  flex-direction: column;
}

.flex-row {
  display: flex;
  flex-direction: row;
}

.justify-between {
  display: flex;
  justify-content: space-between;
}
</style>
