<template>
  <div class="flex-container">
    <section class="webflow-auth-sidebar" data-standalone-ad="">
      <div :class="`sidebar-image-${pageType}`"></div>
    </section>

    <section>
      <div class="container-fluid" v-if="pageType == 1">
        <div class="loginContent">
          <div class="title">
            <h4>登录到教务系统</h4>
          </div>
          <div class="line-with-text">
            <span>使用学工号登录</span>
          </div>
          <div class="main">
            <form class="row g-3" @submit.prevent="loginSubmit">
              <div class="col-12">
                <label for="username" class="form-label login">用户名</label>
                <input v-model="username" type="text" class="form-control form-control-lg login1" id="username">
              </div>
              <div class="col-12">
                <label for="password" class="form-label login">
                  密码
                  <a class="forget" @click="forgetPass">密码忘记了？</a>
                </label>
                <input type="password" v-model="password" class="form-control form-control-lg login1" id="password">
              </div>
              <div class="col-12">
                <label for="valiCode" class="form-label login">验证码</label>
                <div class="input-and-button">
                  <div class="row">
                    <div class="col-9"><input type="text" v-model="valiCode" class="form-control form-control-lg login1"
                        id="valiCode">
                    </div>
                    <div class="col-3">
                      <!-- <input class="btn3" type="submit" value="发送验证码"> -->
                      <img @click="changeValiCode()" class="img1 w-100 h-100" referrerpolicy="no-referrer" :src="img"
                        alt="验证码" />
                    </div>
                  </div>

                </div>
              </div>

              <div class="col-12">
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" v-model="remember" id="gridCheck">
                  <label class="form-check-label" for="gridCheck">
                    记住我
                  </label>
                </div>
              </div>
              <div class="col-12">
                <input class="btn2" type="submit" value="Sign In">
              </div>
            </form>
            <p class="">
              没有账户？
              <a class="signup" @click="pageRole()">注册</a>
            </p>
          </div>
        </div>
      </div>

      <!-- 密码重置 -->

      <div class="container-fluid" v-if="pageType == 2">
        <div class="loginContent">
          <div class="title">
            <h2>密码忘记了?</h2>
          </div>
          <div class="password-instructions">
            <p>输入用户名和该账户绑定的邮箱，我们将向您发送重置密码的邮件。</p>

          </div>
          <div class="main">
            <form class="row g-3">
              <div class="col-12">
                <label for="username" class="form-label login">用户名</label>
                <input v-model="username" type="text" class="form-control form-control-lg login1" id="username">
              </div>

              <div class="col-12">
                <label for="email" class="form-label login">
                  邮箱
                </label>
                <input type="email" v-model="email" class="form-control form-control-lg login1" id="email">
              </div>
              <div class="col-12">
                <label for="valiCode" class="form-label login">验证码</label>
                <div class="input-and-button">
                  <div class="row">
                    <div class="col-9"><input type="text" v-model="valiCode" class="form-control form-control-lg login1"
                        id="valiCode">
                    </div>
                    <div class="col-3">
                      <!-- <input class="btn3" type="submit" value="发送验证码"> -->
                      <img @click="changeValiCode()" class="img1 w-100 h-100" referrerpolicy="no-referrer" :src="img"
                        alt="验证码" />
                    </div>
                  </div>

                </div>
              </div>
              <div class="col-12">
                <input class="btn2" @click="initPassword()" type="button" value="Send Reset Instructions">
              </div>
            </form>
            <p class="">

              <a class="signup" @click="backLogin()">返回登录</a>
            </p>
          </div>
        </div>
      </div>
      <!-- 选择角色 -->
      <div class="container-fluid" v-if="pageType == 3">
        <div class="loginContent">
          <div class="title">
            <h4>注册到教务系统</h4>
          </div>
          <div class="password-instructions">
            <p>请选择您的身份以继续</p>

          </div>
          <div class="main">
            <form class="row g-3">

              <div class="col-12">
                <input class="btn2" @click="registerPageAndChooseRole('ADMIN')" type="button" value="Admin">
              </div>

              <div class="line-with-text">
                <span>or</span>
              </div>

              <div class="col-12">
                <input class="btn4" @click="registerPageAndChooseRole('STUDENT')" type="button" value="Student">
              </div>

              <div class="line-with-text">
                <span>or</span>
              </div>

              <div class="col-12">
                <input class="btn2" @click="registerPageAndChooseRole('TEACHER')" type="button" value="Teacher">
              </div>
            </form>
            <p class="">
              已经有账户？
              <a class="signup" @click="backLogin()">登录</a>
            </p>
          </div>
        </div>
      </div>
      <!-- 用户注册 -->
      <div class="container-fluid" v-if="pageType == 4">
        <div class="loginContent">
          <div class="title">
            <h4>注册到教务系统</h4>
          </div>
          <div class="main">
            <form class="row g-3" @submit.prevent="register">
              <div class="col-6">
                <label for="perName" class="form-label login">姓名</label>
                <input v-model="perName" type="text" class="form-control form-control-lg login1" id="perName">
              </div>
              <div class="col-6">
                <label for="username" class="form-label login">用户名</label>
                <input v-model="username" type="text" class="form-control form-control-lg login1" id="username">
              </div>
              <div class="col-12">
                <label for="password" class="form-label login">
                  密码
                </label>
                <input type="password" v-model="password" class="form-control form-control-lg login1" id="password">
              </div>
              <div class="col-12">
                <label for="email" class="form-label login">
                  邮箱
                </label>
                <input type="email" v-model="email" class="form-control form-control-lg login1" id="email">
              </div>
              <div class="col-12">
                <label for="valiCode" class="form-label login">验证码</label>

                <div class="row">
                  <div class="col-9"><input type="text" v-model="emailCode" class="form-control form-control-lg login1"
                      id="valiCode">
                  </div>
                  <div class="col-3">
                    <input class="btn3" type="button" @click="sendVali()" :disabled="isSending" :value="buttonText">

                  </div>
                </div>
              </div>
              <div class="col-12">
                <input class="btn2" type="submit" value="Sign Up">
              </div>
            </form>
            <p class="">
              已经有账户？
              <a class="signup" @click="backLogin()">登录</a>
            </p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useAppStore } from "~/stores/app";
import {
  getValidateCode,
  testValidateInfo,
  resetPassWord,
  registerUser,
  getEmailCode
} from "~/services/mainServ";
import { message } from "~/tools/messageBox";
import router from "~/router";
import { Base64 } from "js-base64";

import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

const USER_KEY = "UserKey";
export default defineComponent({
  //返回的数据，templte中使用的数据, 并设置初始值
  data: () => ({
    // username: '2022030001',
    // username: 'admin',
    // password: '123456',
    username: "",
    password: "",
    valiCode: "",
    emailCode: "",
    pageType: 1,
    id: 0,
    jwt: "",
    img: "",
    funId: "",
    vueName: "",
    loginCode: "",
    email: "",
    messageCode: "",
    showSlider: false,
    remember: true,
    role: "STUDENT",
    perName: "",

    isSending: false,
    countdown: 60,
    buttonText: '发送验证码',
  }),
  //页面加载前执行的函数 设置初始为登录界面， 不需要手动调用beforeMount函数，vue会自动调用
  beforeMount() {
    this.pageType = 1;
  },
  //页面加载后执行的函数， 执行性一次， 从后台请求验证码，从浏览器获取上次登录的用户信息作为用户和密码的初始值，不需要手动调用created函数，vue会自动调用
  async created() {
    const res = await getValidateCode();
    this.id = res.validateCodeId;
    this.img = res.img;
    const store = useAppStore();
    if (store.remember) {
      this.username = Base64.decode(store.usernameSave);
      this.password = Base64.decode(store.passwordSave);
      this.remember = true;
    } else {
      this.username = "";
      this.password = "";
      this.remember = false;

    }
  },
  //页面加载后执行的函数， 执行性多次
  methods: {
    //刷新验证码
    async changeValiCode() {
      const res = await getValidateCode();
      this.id = res.validateCodeId;
      this.img = res.img;
      this.valiCode = "";
    },
    //返回登录界面
    backLogin() {
      this.username = "";
      this.password = "";
      this.valiCode = "";
      this.pageType = 1;
    },
    //忘记密码 显示密码设置表单显示
    forgetPass() {
      this.username = "";
      this.password = "";
      this.valiCode = "";
      this.pageType = 2;
    },
    pageRole() {
      this.username = "";
      this.password = "";
      this.valiCode = "";
      this.emailCode = "";
      this.role = "";
      this.pageType = 3;
    },
    chooseRole(role: string) {
      this.role = role;
    },
    //用户注册 显示用户注册表单
    pageRegister() {
      this.username = "";
      this.password = "";
      this.valiCode = "";
      this.emailCode = "";
      this.pageType = 4;
    },
    registerPageAndChooseRole(role: string) {
      this.pageRegister();
      console.log(role);
      this.chooseRole(role);
    },
    // 初始密码 请求后台服务，将发送初始密码到邮箱
    async initPassword() {
      let res = await testValidateInfo({
        validateCodeId: this.id,
        validateCode: this.valiCode,
      });
      if (res.code != 0) {
        message(this, res.msg);
        this.changeValiCode();//刷新验证码
        return;
      }
      if (this.username == "" || this.username == undefined) {
        message(this, "账号为空,请填写账号");
        return;
      }
      if (this.email == "" || this.email == undefined) {
        message(this, "邮箱为空,请填写邮箱");
        return;
      }
      res = await resetPassWord({
        username: this.username,
        email: this.email,
      });
      if (res.code == 0) {
        message(this, "初始密码已发送至您的邮箱，请注意查收");
        this.changeValiCode();
        this.pageType = 1;
      } else {
        message(this, res.msg);
      }
    },
    //发送验证码 请求后台服务，将发送验证码到邮箱
    async sendVali() {
      this.isSending = true;
      this.buttonText = `${this.countdown}秒后重新发送`;
      let res = await getEmailCode({
        email: this.email,
      });
      if (res.code == 0) {
        message(this, "验证码已发送至您的邮箱，请注意查收");
      } else {
        message(this, res.msg);
      }
      let intervalId = setInterval(() => {
        this.countdown--;
        this.buttonText = `${this.countdown}秒后重新发送`;

        if (this.countdown === 0) {
          clearInterval(intervalId);
          this.isSending = false;
          this.buttonText = '发送验证码';
          this.countdown = 60;
        }
      }, 1000);
    },
    //用户注册 请求后台服务，将用户注册信息发送到后台，后台添加账户人员教师或学生信息
    async register() {

      if (this.username == "" || this.username == undefined) {
        message(this, "账号为空,请填写账号");
        return;
      }
      if (this.password == "" || this.password == undefined) {
        message(this, "密码为空,请填写密码");
        return;
      }
      if (this.perName == "" || this.perName == undefined) {
        message(this, "姓名为空,请填写姓名");
        return;
      }
      if (this.email == "" || this.email == undefined) {
        message(this, "邮箱为空,请填写邮箱");
        return;
      }
      if (this.emailCode == "" || this.emailCode == undefined) {
        message(this, "验证码为空,请填写验证码");
        return;
      }
      if (this.role == "" || this.role == undefined) {
        message(this, "角色为空,请选择角色");
        return;
      }

      var typesCount = 0;
      // 检查是否包含大写字母
      if (/[A-Z]/.test(this.password)) {
        typesCount++;
      }

      // 检查是否包含小写字母
      if (/[a-z]/.test(this.password)) {
        typesCount++;
      }

      // 检查是否包含数字
      if (/\d/.test(this.password)) {
        typesCount++;
      }

      // 检查是否包含符号
      if (/[^A-Za-z0-9]/.test(this.password)) {
        typesCount++;
      }


      if (this.password.length < 8) {
        message(this, "密码长度必须大于等于8个字符，请重新输入！");
        return;
      }
      // 检查类型数量是否大于等于2
      if (typesCount < 2) {
        message(this, "密码至少包含大写字母、小写字母、数字和符号两种及以上的类型，请重新输入！");
        return;
      }



      let res = await registerUser({
        username: this.username,
        password: this.password,
        perName: this.perName,
        email: this.email,
        code: this.emailCode,
        role: this.role,
      });
      if (res.code == 0) {
        message(this, "已注册成功！");
        this.changeValiCode();
        this.pageType = 1;
      } else {
        message(this, res.msg);
      }
    },
    //登录请求后台服务，将用户登录信息发送到后台，后台验证用户信息，返回jwt
    async loginSubmit() {
      const res = await testValidateInfo({
        validateCodeId: this.id,
        validateCode: this.valiCode,
      });
      if (res.code != 0) {
        message(this, res.msg);
        this.changeValiCode();
        return;
      }
      if (this.username == "" || this.username == undefined) {
        message(this, "用户名为空");
      } else if (this.password == "" || this.password == undefined) {
        message(this, "密码为空");
      } else {
        const store = useAppStore();
        try {
          //登录成功后，将用户信息保存到store中，将用户信息保存到浏览器中
          await store.login(this.username, this.password);
          await store.setNavi();
          if (this.remember) {
            store.saveAccount(
              Base64.encode(this.username),
              Base64.encode(this.password)
            );
          } else {
            store.removeAccount();
          }
          router.push("/MainPage");
        } catch (err) {
          message(this, "登录失败!");
        }
      }
    },
  },
});
</script>

<style scoped>
.flex-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  /* 让.flex-container占满整个视口的高度 */
}

.flex-container>section:not(.sidebar-image) {
  flex-grow: 1;
}



.sidebar-image-1 {
  width: 75%;
  height: 100vh;
  background-image: url('https://s2.loli.net/2023/12/20/JGE5MTXlKRSfBOH.jpg');
  background-position: left center;
  background-size: cover;
  background-repeat: no-repeat;

}




.sidebar-image-2 {
  width: 75%;
  height: 100vh;
  background-image: url('https://s2.loli.net/2023/12/20/KIwQYt3THgf8oS4.jpg');
  background-position: left center;
  background-size: cover;
  background-repeat: no-repeat;

}

.sidebar-image-3 {
  width: 75%;
  height: 100vh;
  background-image: url('https://s2.loli.net/2023/12/20/cxm7DboNLjy4dGp.jpg');
  background-position: left center;
  background-size: cover;
  background-repeat: no-repeat;

}

.sidebar-image-4 {
  width: 75%;
  height: 100vh;
  background-image: url('https://s2.loli.net/2023/12/20/AaZnd4Wz52xCqN9.jpg');
  background-position: left center;
  background-size: cover;
  background-repeat: no-repeat;

}

@media (max-width: 1000px) {

  .sidebar-image-1,
  .sidebar-image-2,
  .sidebar-image-3,
  .sidebar-image-4 {
    display: none;
  }
}

p {
  padding: 0;
  border: 0;

  margin: 20px 0 0;
  font: normal 14px/20px "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  color: #3d3d4e;
  text-align: center;
}

.signup {
  margin-left: 5px;
  line-height: 0;
  vertical-align: baseline;
  color: #0d0c22;
  text-decoration: underline;
  cursor: pointer;
}

.btn2 {
  --btn-bg-color: #0d0c22;
  --btn-bg-color-hover: #565564;
  --btn-text-color: #fff;
  --btn-text-color-hover: #fff;
  --btn-border-color: var(--btn-bg-color);
  --btn-border-color-hover: var(--btn-bg-color-hover);
  --btn-icon-size: 16px;
  display: inline-flex;
  position: relative;
  box-sizing: border-box;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  width: var(--btn-width, min-content);
  height: var(--btn-height);
  padding: 0 var(--btn-padding);
  transition: 0.15s cubic-bezier(0.32, 0, 0.59, 0.03);
  transition-property: color, background-color, border, border-radius, visibility;
  border: var(--btn-border-width, 1.5px) solid var(--btn-border-color, var(--btn-bg-color));
  border-radius: var(--btn-border-radius, 9999999px);
  background-color: var(--btn-bg-color);
  color: var(--btn-text-color);
  font-family: inherit;
  font-size: var(--btn-font-size);
  font-weight: var(--btn-font-weight, 600);
  line-height: 1;
  text-decoration: none;
  white-space: nowrap;
  cursor: pointer;
  --btn-height: 56px;
  --btn-padding: 24px;
  --btn-font-size: 14px;
  --btn-width: 100%;
  margin-top: 20px;
}

.btn2:hover {
  background-color: var(--btn-bg-color-hover);
  color: var(--btn-text-color-hover);
  border-color: var(--btn-border-color-hover);
}

.btn4 {
  --btn-bg-color: #ffffff;
  --btn-bg-color-hover: #ffffff;
  --btn-text-color: #000000;
  --btn-text-color-hover: #000000;
  --btn-border-color: #b6b5bc;
  --btn-border-color-hover: #000000;
  --btn-icon-size: 16px;
  display: inline-flex;
  position: relative;
  box-sizing: border-box;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  width: var(--btn-width, min-content);
  height: var(--btn-height);
  padding: 0 var(--btn-padding);
  transition: 0.15s cubic-bezier(0.32, 0, 0.59, 0.03);
  transition-property: color, background-color, border, border-radius, visibility;
  border: var(--btn-border-width, 1.5px) solid var(--btn-border-color, var(--btn-bg-color));
  border-radius: var(--btn-border-radius, 9999999px);
  background-color: var(--btn-bg-color);
  color: var(--btn-text-color);
  font-family: inherit;
  font-size: var(--btn-font-size);
  font-weight: var(--btn-font-weight, 600);
  line-height: 1;
  text-decoration: none;
  white-space: nowrap;
  cursor: pointer;
  --btn-height: 56px;
  --btn-padding: 24px;
  --btn-font-size: 14px;
  --btn-width: 100%;
  margin-top: 20px;
}

.btn4:hover {
  background-color: var(--btn-bg-color-hover);
  color: var(--btn-text-color-hover);
  border-color: var(--btn-border-color-hover);
}

.input-and-button {

  justify-content: space-between;
  align-items: stretch;
}

image {
  height: auto;
  /* 让图片等比例缩放以适应元素的大小 */
  object-fit: scale-down;
}

.btn3 {
  --btn-bg-color: #2e4154;
  --btn-bg-color-hover: #565564;
  --btn-text-color: #fff;
  --btn-text-color-hover: #fff;
  --btn-border-color: var(--btn-bg-color);
  --btn-border-color-hover: var(--btn-bg-color-hover);
  --btn-icon-size: 16px;
  display: inline-flex;
  position: relative;
  box-sizing: border-box;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  width: var(--btn-width, min-content);
  height: var(--btn-height);
  padding: 0 var(--btn-padding);
  transition: 0.15s cubic-bezier(0.32, 0, 0.59, 0.03);
  transition-property: color, background-color, border, border-radius, visibility;
  border: var(--btn-border-width, 1.5px) solid var(--btn-border-color, var(--btn-bg-color));
  border-radius: var(--btn-border-radius, 9999999px);
  background-color: var(--btn-bg-color);
  color: var(--btn-text-color);
  font-family: inherit;
  font-size: var(--btn-font-size);
  font-weight: var(--btn-font-weight, 600);
  line-height: 3;
  text-decoration: none;
  white-space: nowrap;
  cursor: pointer;

  --btn-padding: 24px;
  --btn-font-size: 14px;
  --btn-width: 100%;
  width: 100px;
}

.btn3:hover {
  background-color: var(--btn-bg-color-hover);
  color: var(--btn-text-color-hover);
  border-color: var(--btn-border-color-hover);
}

label {
  display: block;
  margin: 14px 0 4px;
  color: #0d0c22;
  font: bold 15px/24px "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;

  font-kerning: auto;
  font-optical-sizing: auto;
  font-weight: bold;
  font-size: 15px;
  line-height: 24px;
  font-family: "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.forget {
  position: relative;
  top: 6px;
  float: right;
  color: #0d0c22;
  font-size: 14px;
  font-weight: normal;
  text-decoration: underline !important;
  /* 添加下划线 */
  cursor: pointer;
  /* 鼠标悬停时显示小手 */
}


.login1:hover {
  border-color: rgba(0, 0, 0, 0.1);
  background-color: #fff;
  -webkit-box-shadow: 0 0 0 4px rgba(193, 208, 242);
  box-shadow: 0 0 0 4px rgba(193, 208, 242);
}

.login {

  font-size: 15px;
  font-weight: bold;
}

.loginContent {
  width: 100%;
  max-width: 416px;
  margin: auto;
}

.title {

  font-weight: bold;
  text-align: left;
  margin-top: 180px;
  margin-bottom: 20px;
}

.line-with-text {
  margin-top: 30px;
  margin-bottom: 0px;
  display: flex;
  align-items: center;
  text-align: center;
  color: #bebdbd;
  font-size: 12px;
}

.line-with-text::before,
.line-with-text::after {
  content: '';
  flex: 1;
  border-bottom: 1px solid #c9c5c5;
}

.line-with-text::before {
  margin-right: .5em;
}

.line-with-text::after {
  margin-left: .5em;
}

h2 {

  color: #0d0c22;

  vertical-align: baseline;
  margin-bottom: 40px;
  font: bold 24px/29px "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.password-instructions {

  color: #0d0c22;
  font-size: 100%;
  margin-bottom: 26px;
}

p {

  color: #000;
  text-align: left;
  font: normal 14px/20px "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
}
</style>