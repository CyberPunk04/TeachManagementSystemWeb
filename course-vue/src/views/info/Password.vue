<template>
  <!-- 最外层区域表单 -->
  <div class="base_form">
    <!-- 区域顶部标题 -->
    <div class="base_header">
      <div class="blue_column"></div>
      <div class="base_title">修改密码</div>
    </div>
    <!-- 页面提示 -->
    <div class="base_prompt">
      密码强度要求三种字符类型（大小写、数字、特殊字符）及以上且密码长度为8位及以上！
    </div>
    <!-- 表单内容 -->
    <div class="from-container">
      <div class="oldPassword flex-row">
        <div class="oldPass">旧密码</div>
        <div>
          <input class="inputWidth" v-model="oldPassword" type="password" />
        </div>
      </div>

      <div class="newPassword flex-row">
        <div class="newPass">请输入新密码</div>
        <div>
          <input class="inputWidth" v-model="newPassword" type="password" />
        </div>
      </div>
      <div class="alarm">*密码长度最大为20位</div>

      <div class="reconfirmP flex-row">
        <div class="rPass">再次输入新密码</div>
        <div>
          <input class="inputWidth" v-model="checkPassword" type="password" />
        </div>
      </div>
      <div class="alarm">*密码长度最大为20位</div>

      <div class="emailContent flex-row">
        <div class="email">输入绑定的邮箱</div>
        <div>
          <input class="inputWidth" v-model="email" type="text" />
        </div>
      </div>

      <div class="valiCodeContent flex-row">
        <div class="valiCode">邮箱验证码</div>
        <div>
          <input class="vailInput" v-model="valiCode" type="text" />
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
        <div class="group8 flex-col" @click="submitPassword()">
          <span class="word20">修改</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import { updatePassword, getEmailCode } from "~/services/infoServ";
import { message } from "~/tools/messageBox";
import { getPersonInfo } from "~/services/blogServ"
import { useAppStore } from "~/stores/app";
import router from "~/router";
export default defineComponent({
  data: () => ({
    passw: "password",
    oldPassword: "",
    newPassword: "",
    checkPassword: "",
    email: "",
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
      console.log(res);
      if (res.code == 0) {
        this.rightEmail = res.data.email;
      }
    },

    async getEmailCode() {
      if (this.email === undefined || this.email === "") {
        message(this, "邮箱为空");
        return;
      }
      const res = await getEmailCode({
        email: this.email,
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
    submitPassword() {
      var msg = "";
      if (this.oldPassword === undefined || this.oldPassword === "") {
        msg = "旧密码为空不能修改";
      } else if (this.newPassword === undefined || this.newPassword === "") {
        msg = "新密码为空不能修改";
      } else if (this.oldPassword === this.newPassword) {
        msg = "新密码和旧密码相同";
      } else if (this.checkPassword !== this.newPassword) {
        msg = "新密码和确认密码不相同";
      } else if (this.email === undefined || this.email === "") {
        msg = "邮箱为空不能修改";
      } else if (this.email !== this.rightEmail) {
        msg = "与绑定的邮箱不匹配";
      } else if (this.valiCode === undefined || this.valiCode === "") {
        msg = "验证码为空不能修改";
      } else {
        var typesCount = 0;
        // 检查是否包含大写字母
        if (/[A-Z]/.test(this.newPassword)) {
          typesCount++;
        }

        // 检查是否包含小写字母
        if (/[a-z]/.test(this.newPassword)) {
          typesCount++;
        }

        // 检查是否包含数字
        if (/\d/.test(this.newPassword)) {
          typesCount++;
        }

        // 检查是否包含符号
        if (/[^A-Za-z0-9]/.test(this.newPassword)) {
          typesCount++;
        }


        if (this.newPassword.length < 8) {
          msg = "密码长度必须大于等于8个字符，请重新输入！";

        } else if (typesCount < 2) {
          msg = "密码至少包含大写字母、小写字母、数字和符号两种及以上的类型，请重新输入！";
        }
      }
      if (msg !== "") {
        message(this, msg);
      } else {
        updatePassword({
          oldPassword: this.oldPassword,
          newPassword: this.newPassword,
          code: this.valiCode,
        }).then((response) => {
          if (response.code == 0) {
            message(this, "提交成功");
            setTimeout(() => {
              this.logout();
            }, 1000); // 在 1 秒后执行 this.logout 函数
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
