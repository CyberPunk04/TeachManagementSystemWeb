<template>
  <div style="height: 100%">
    <!-- 当登录成功后显示此DIV -->
    <div style="height: 100%" v-if="userInfo.username &&
      userInfo.username.length > 0 &&
      systemConfig.showLeftMeun
      ">
      <el-container style="height: 100%">
        <!-- 主页面的头部区域 -->
        <el-header height="40" class="topHeader">
          <div class="outer1 flex-col">
            <div class="block1 justify-between">
              <div class="flex-row">

                <!-- 导航栏图片 -->
                <div class="section1 flex-col"></div>
                <div class="section2 flex-col"></div>
                <div class="box1 flex-col"></div>
              </div>
              <div class="flex-row">

                <!-- 头像 -->
                <img class="icon1" referrerpolicy="no-referrer" :src="avatar" @click="avatarRouter()" />
                <div class="section3 flex-col justify-between" @click="avatarRouter()">
                  <span class="word1">您好～</span>
                  <span class="info">{{ userInfo.perName }}({{ userInfo.username }})
                    {{ currentTime }}</span>
                </div>
                <div class="section4 flex-col"></div>

                <div class="section5" @click="refreshPage">

                  <div class="shuxinImage"></div>
                  <div class="shuaxin">刷新</div>
                </div>

                <div class="section6" @click="logout()">
                  <div class="tuichuImage"></div>
                  <div class="shuaxin">退出</div>

                </div>
              </div>
            </div>
          </div>

          <el-menu background-color="rgb(147,14,20)" text-color="rgb(255,255,255)" active-text-color="rgb(255,255,255)"
            mode="horizontal" router:false style="height: 46px;">
            <template v-for="(v, i) in systemConfig.naviList" :key="i">
              <el-menu-item :index="'/' + v.name" @click="getSecondList(v.id), routerName(v.name)">{{ v.title
              }}</el-menu-item>
            </template>
          </el-menu>
        </el-header>
        <!-- 主页面的中心区域 -->
        <el-container>
          <!-- 中心区域的菜单区域 -->
          <el-aside style="width: 170px; height: 100%" v-if="showSecondNav">
            <el-scrollbar class="scrollar" v-if="showLeftList">
              <el-menu background-color="rgb(44,48,57)" text-color="rgb(255,255,255)" active-text-color="rgb(255,255,255)"
                router :default-openeds="['0']">
                <template v-for="(v, i) in systemConfig.leftList" :key="i">
                  <template v-if="v.sList.length > 0">
                    <el-sub-menu :index="i.toString()">
                      <template v-slot:title>
                        <i class="el-icon-location"></i>
                        <span>{{ v.title }}</span>
                      </template>
                      <el-menu-item-group v-for="(item, j) in v.sList" :key="j">
                        <el-menu-item :index="'/' + item.name" @click="routerName(item.name)">{{ item.title
                        }}</el-menu-item>
                      </el-menu-item-group>
                    </el-sub-menu>
                  </template>
                  <template v-else>
                    <el-menu-item :index="'/' + v.name" @click="routerName(v.name)">
                      <i class="el-icon-menu"></i>
                      <span>{{ v.title }}</span>
                    </el-menu-item>
                  </template>
                </template>
              </el-menu>
            </el-scrollbar>
            <!-- 判断是不是博客页面，展示二级菜单 -->
            <el-scrollbar class="scrollar" v-if="!showLeftList">
              <el-menu background-color="rgb(44,48,57)" text-color="rgb(255,255,255)" active-text-color="rgb(255,255,255)"
                router :default-openeds="['0']">
                <template v-for="(v, i) in leList" :key="i">
                  <template v-if="v.sList.length > 0">
                    <el-sub-menu :index="i.toString()">
                      <template v-slot:title>
                        <i class="el-icon-location"></i>
                        <span>{{ v.title }}</span>
                      </template>
                      <el-menu-item-group v-for="(item, j) in v.sList" :key="j">
                        <el-menu-item :index="'/' + item.name" @click="routerName(item.name)">{{ item.title
                        }}</el-menu-item>
                      </el-menu-item-group>
                    </el-sub-menu>
                  </template>
                  <template v-else>
                    <el-menu-item :index="'/' + v.name" @click="routerName(v.name)">
                      <i class="el-icon-menu"></i>
                      <span>{{ v.title }}</span>
                    </el-menu-item>
                  </template>
                </template>
              </el-menu>
            </el-scrollbar>
          </el-aside>
          <!--   router-view 控制显示VUE页面，VUE会根据路由路径不同.vue文件的内容，我们设计的所有vue文件内容都在这里渲染 -->
          <el-main>
            <router-view :key="$route.fullPath" />
          </el-main>
        </el-container>
        <el-footer>
          <footer>
            <div class="jumbotron jumbotron-fluid bg-secondary  mt-5 mb-0">
              <div class="container">
                <div class="row">
                  <div class="col-12 col-sm-12 col-md-12 col-lg-4 col-xl-4 cizgi">
                    <div class="card bg-secondary border-0">
                      <div class="card-body text-light text-center">
                        <p class="d-inline lead" style="font-size: medium;">版权所有 © 山东大学软件学院2023<br>
                          <a href="https://www.sdu.edu.cn/index.htm" class="text-light d-block lead">官网</a>
                        </p>

                      </div>
                    </div>
                  </div>
                  <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-4 cizgi"> </div>
                  <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-4 cizgi">
                    <div class="card bg-secondary border-0">
                      <div class="card-body text-center">
                        <h5 class="card-title text-white display-4" style="font-size:medium">联系我们</h5>
                        <a class="text-light d-block lead" style="margin-left: -20px" href="#"><i
                            class="fa fa-phone mr-2"></i>（86）-531-88395114 </a>
                        <a class="text-light d-block lead" href="#"><i
                            class="fa fa-envelope mr-2"></i>zhihang1256@outlook.com</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </footer>
          <!--# Footer #-->
        </el-footer>
      </el-container>

    </div>
    <!--  当登录前显示此DIV router-view 控制显示VUE页面，VUE会根据路由路径不同.vue文件的内容， 初始路由路径为/，显示Login.vue内容 -->
    <div style="height: 100%" v-else>
      <router-view :key="$route.fullPath" />
    </div>
  </div>
</template>

<script lang="ts">
// 引入使用的组件、函数和数据接口
import { mapState } from "pinia";
import { defineComponent } from "vue";
import { useAppStore } from "~/stores/app";
import router from "~/router";
import { type MenuInfo } from "~/models/general";
import { formatTime } from "~/tools/comMethod";
import { getAvatar, getPersonInfo } from "~/services/blogServ";
// vue3中新增了 defineComponent ，它并没有实现任何的逻辑，只是把接收的 Object 直接返回，它的存在就是完全为了服务 TypeScript 而存在的。
// 我都知道普通的组件就是一个普通的对象，既然是一个普通的对象，那自然就不会获得自动的提示，
export default defineComponent({
  // templte中使用的数据
  data: () => ({
    isCollapse: false,
    leList: [] as MenuInfo[],
    funId: "",
    showLeftList: true,
    timer: null as any,
    currentTime: formatTime(new Date()),
    currentFristNavId: Number(localStorage.getItem("currentFristNavId") || 0),
    personId: 0 as number,
    avatar: "",
  }),
  created() {
    this.getCurrentPerson();
  },
  //生命周期函数  mounted() 在实例挂载之后调用， 设置定期刷新控制台时间
  mounted() {

    if (this.timer) {
      clearInterval(this.timer);
    } else {
      this.timer = setInterval(() => {
        this.currentTime = formatTime(new Date());
      }, 1000);
    }
  },
  // 生命周期函数  unmounted() 在实例销毁之后调用，清除定期刷新控制台时间
  unmounted() {
    clearInterval(this.timer);
  },
  // 计算属性 用于计算属性，计算属性的结果会被缓存，除非依赖的响应式属性变化才会重新计算 当用户信息和菜单更新后页面会重新显然，显示主界面
  computed: {
    ...mapState(useAppStore, ["systemConfig"]),
    ...mapState(useAppStore, ["userInfo"]),
    showSecondNav() {
      return this.currentFristNavId !== 6;;
    },
  },
  methods: {
    // 刷新页面
    refreshPage(): void {
      location.reload();
    },
    // 退出登录
    logout() {
      const store = useAppStore();
      store.logout();
      router.push("/Login");
    },
    // 获取二级菜单
    getSecondList(id: number) {
      const store = useAppStore();
      store.setLeftList(id);
      this.currentFristNavId = id;
      localStorage.setItem("currentFristNavId", id.toString());
      let route = "";
      switch (id) {
        case 6:
          route = "/BlogIndex";
          break;
        default:
          route = "/MainPage";
      }

      router.push(route);
    },
    // 路由跳转 如果路由名为ProjectHtml或者ProjectVideo则打开新的窗口 其他的路由跳转到对应的页面
    routerName(name: string) {
      if (name == null || name == undefined || name == "") {
        return;
      }
      if (name == "ProjectHtml") {
        this.openProjectHtml();
        return;
      }
      if (name == "ProjectVideo") {
        this.openProjectVideo();
        return;
      }
      if (name == "Introduce") {
        this.avatarRouter();
        return;
      }
      router.push("/" + name);
    },
    // 打开新窗口 显示projectDoc.html内容，这个文件在public目录下,同学要修改该页面的内容，作为项目详细介绍的页面，用于检查作业的老师直接打开该页面即可看到作业的详细介绍
    openProjectHtml() {
      window.open("projectDoc.html");
    },
    // 打开新窗口 显示projectVideo.html内容，这个文件在public目录下,同学要录制项目特色视频，作为项目特色介绍视频包房，用于检查作业的老师直接打开该页面查看项目特色
    openProjectVideo() {
      window.open("projectVideo.html");
    },
    //获取头像
    async getAvatar() {
      const res = await getAvatar(this.personId);
      if (res.code == 0) {
        this.avatar = res.data;
      }
    },
    async getCurrentPerson() {
      const res = await getPersonInfo();
      if (res.code == 0) {
        this.personId = res.data.personId;
        this.getAvatar();
      }
    },
    avatarRouter() {
      //根据当前用户的角色跳转到不同的页面
      if (this.userInfo.roles === "ROLE_STUDENT") {
        router.push("/StudentIntroduce");
      } else if (this.userInfo.roles === "ROLE_TEACHER") {
        router.push("/TeacherIntroduce");
      } else if (this.userInfo.roles === "ROLE_ADMIN") {
        router.push("/MainPage");
      }

    }
  },
});
</script>
<!-- 这个是系统主页面的样式，同学可以根据自己的喜好修改 -->
<style lang="scss" scoped>
.el-menu-item.is-active {
  background-color: rgb(105, 74, 74) !important;
}

.el-menu--horizontal>.el-menu-item {
  height: 44px;
  line-height: 44px;
  font-family: SourceHanSansCN-Regular;
  font-size: 16px;
  color: #ffffff;
  letter-spacing: 0;
  font-weight: 400;
}

.el-menu--horizontal>.el-menu-item.is-active {
  background-color: rgb(197, 24, 32) !important;
}

.topHeader {
  padding: 0px;
}

.m-header {
  height: 50px !important;
  background-color: rgb(255, 255, 255);
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.header {
  display: flex;
  flex-direction: column;
}

.right {
  cursor: pointer;
  display: flex;
  flex-direction: row;
}

.scrollar {
  height: 100%;
  overflow-x: hidden;
  background-color: rgb(44, 48, 57);
}

.pername {
  margin-top: 15px;
  font-size: 20px;
  color: gray;
  font-weight: bold;
}

.titile {
  font-size: 29px;
  color: #0067ff;
  letter-spacing: 0;
  font-weight: bold;
  background-color: #ffffff;
  width: 180px;
  height: 40px;
  margin-top: 10px;
  margin-left: 40px;
}

.right-header {
  font-family: PingFangSC-Regular;
  font-size: 14px;
  color: #47474a;
  letter-spacing: 0;
  font-weight: 400;
  margin-top: 20px;
  margin-right: 30px;
}

.shuxian {
  margin-top: 5px;
  margin-bottom: 5px;
  height: 50px;
  width: 1px;
  background: #cfd2d7;
  margin-right: 30px;
}

.tuichu {
  font-family: PingFangSC-Regular;
  font-size: 14px;
  color: #47474a;
  letter-spacing: 0;
  font-weight: 400;
  margin-top: 20px;
  margin-right: 30px;
}

.icon_tuichu {
  margin-top: 23px;
  margin-right: 10px;
  background: url("/loginOut.png");
  width: 14px;
  height: 14px;
}

.image {
  width: 30px;
  margin-top: 15px;
  margin-right: 20px;
  height: 30px;
}

.search {
  margin-top: 23px;
  margin-right: 30px;
  background: url("/serch.png");
  width: 18px;
  height: 18px;
}

.question {
  margin-top: 23px;
  margin-right: 30px;
  background: url("/question.png");
  width: 18px;
  height: 18px;
}

.alarm {
  margin-top: 23px;
  margin-right: 80px;
  background: url("/alarm.png");
  width: 18px;
  height: 18px;
}

.leftIcon {
  cursor: pointer;
  margin-top: 23px;
  margin-left: 18px;
  background: url("/leftright.png");
  width: 18px;
  height: 18px;
}

.outer1 {
  background-color: rgba(255, 255, 255, 1);
  height: 70px;
  width: 100%;
}

.block1 {
  width: 95%;
  height: 44px;
  margin: 13px 0 0 35px;
}

.section1 {
  width: 135px;
  height: 40px;
  background: url("../../public/sdu.png") 100% no-repeat;
  background-size: 100% 100%;
  /* 添加这一行 */
  margin-top: 2px;
}

.section2 {
  background-color: rgba(147, 14, 20, 1);
  width: 1px;
  height: 35px;
  margin: 5px 0 0 26px;
}

.txt1 {
  width: 216px;
  height: 29px;
  overflow-wrap: break-word;
  color: rgba(147, 14, 20, 1);
  font-size: 24px;
  font-family: jiangxizhuokai;
  text-align: right;
  white-space: nowrap;
  line-height: 29px;
  display: block;
  margin: 8px 0 0 19px;
}

.icon1 {
  width: 44px;
  height: 44px;
  cursor: pointer;
  //圆形
  border-radius: 50%;
}

.section3 {
  width: 140px;
  height: 33px;
  margin: 7px 10px 0 18px;
  background-color: rgba(255, 255, 255, 1);
  cursor: pointer;
}

.word1 {
  width: 36px;
  height: 12px;
  overflow-wrap: break-word;
  color: #2d2d2d;
  font-size: 12px;
  font-family: SourceHanSansCN-Regular;
  text-align: right;
  white-space: nowrap;
  line-height: 12px;
  display: block;
}

.info {
  width: 140px;
  height: 14px;
  font-size: 12px;
  overflow-wrap: break-word;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;
  color: #2d2d2d;
  white-space: nowrap;
  line-height: 21px;
}

.section4 {
  background-color: rgba(229, 227, 227, 1);
  width: 1px;
  height: 33px;
  margin: 7px 0 0 25px;
}

.section5 {
  width: 20px;
  height: 33px;
  margin: 9px 0 0 35px;
  cursor: pointer;
}

.section6 {
  width: 20px;
  height: 32px;
  margin: 9px 0 0 25px;
  cursor: pointer;
}

.info2 {
  width: 20px;
  height: 10px;
  overflow-wrap: break-word;
  color: rgba(45, 45, 45, 1);
  font-size: 10px;
  font-family: SourceHanSansCN-Regular;
  text-align: right;
  white-space: nowrap;
  line-height: 10px;
  margin-top: 8px;
  display: block;
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

.shuaxin {
  width: 20px;
  height: 10px;
  overflow-wrap: break-word;
  color: rgba(45, 45, 45, 1);
  font-size: 10px;
  text-align: right;
  white-space: nowrap;
  line-height: 10px;
  margin-top: 8px;
}

.shuxinImage {
  width: 16px;
  height: 14px;
  background: url("/refresh.png");
  margin-left: 3px;
}

.tuichuImage {
  width: 14px;
  height: 14px;
  background: url("/tuichu.png");
  margin-left: 5px;
}

.box1 {
  width: 135px;
  height: 40px;
  background: url("../../public/logo1.png") 100% no-repeat;
  background-size: 100% 100%;
  /* 添加这一行 */
  margin-top: 2px;
  margin-left: 10px;
}

footer {
  margin-top: -22px;
  margin-left: -10px;
}

footer a.text-light:hover {

  color: #fed136 !important;
  text-decoration: none;
}

footer a.text-light {
  font-size: small;
  color: #ffffff !important;
  text-decoration: none;
}


footer .cizgi {
  border-right: 1px solid #535e67;
}

@media (max-width: 992px) {
  footer .cizgi {
    border-right: none;
  }
}
</style>
