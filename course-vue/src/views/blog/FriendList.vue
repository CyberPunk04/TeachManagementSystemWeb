<template>
  <div class="row component-background">
    <ContentField>
      <div class="row">
        <div class="col-2"></div>
        <div class="col-8">
          <nav class="navbar bg-body-tertiary">
            <div class="container-fluid">
              <a class="navbar-brand" href="/FriendList">
                好友列表
              </a>
              <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar"
                aria-controls="offcanvasNavbar">
                <span class="navbar-toggler-icon"></span>
              </button>
            </div>
            <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar"
              aria-labelledby="offcanvasNavbarLabel">
              <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvasNavbarLabel">仪表盘</h5>
                <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
              </div>
              <div class="offcanvas-body">
                <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
                  <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="/BlogIndex">首页</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" :href="`/UserProfile/${personId}`">我的空间</a>
                  </li>
                  <li class="nav-item" v-if="username !== 'admin'">
                    <a class="nav-link" href="/UserList">用户列表</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="/FriendList">好友列表</a>
                  </li>
                </ul>
                <form class="d-flex mt-3" role="search">
                  <input class="form-control me-2" type="search" v-model="searchContent" placeholder="Search"
                    aria-label="Search">
                  <button class="btn btn-outline-success" type="button" @click="search">Search</button>
                </form>
              </div>
            </div>
          </nav>
          <div class="card title">
            <div class="card-body">
              <div class="row">
                <div class="col-1"></div>
                <div class="col-11">
                  <div class="row">
                    <div class="col-2">用户</div>
                    <div class="col-2">院系</div>
                    <div class="col-2">类型</div>
                  </div>
                </div>
              </div>
            </div>
          </div>


          <div class="card content" v-for="user in userList" :key="user.personId"
            @click="open_user_profile(user.personId)">
            <div class="card-body">

              <div class="row">
                <!-- 头像 -->
                <div class="col-1">
                  <img class="img-fluid img-field" :src="user.avatar" alt="加载失败" />
                </div>

                <!-- 用户信息 -->
                <div class="col-11">
                  <div class="row">
                    <div class="col-2">
                      <div class="username">
                        {{ user.name }}
                      </div>

                      <div class="gender" v-if="user.gender == '1'">
                        男
                      </div>
                      <div class="gender" v-else-if="user.gender == '2'">
                        女
                      </div>
                      <div class="gender" v-else> 薛定谔的性别</div>
                    </div>
                    <div class="col-2">
                      <div class="dept">
                        {{ user.dept }}
                      </div>
                    </div>

                    <div class="col-2">
                      <div class="type" v-if="'major' in user">
                        学生
                      </div>
                      <div class="type" v-else>
                        教师
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-2"></div>
      </div>

    </ContentField>
  </div>
</template>


<script lang="ts">
import { defineComponent } from "vue";
import { StudentItem, TeacherItem } from "~/models/general";
import ContentField from "~/components/ContentField.vue";
import { getPersonInfo, getAvatar, getFriendList } from "~/services/blogServ";
import router from "~/router";
import { useAppStore } from "~/stores/app";
export default defineComponent({
  components: {
    ContentField,
  },
  data: () => ({
    studentList: [] as StudentItem[],
    teacherList: [] as TeacherItem[],
    userList: [] as (StudentItem | TeacherItem)[],
    personId: 0 as number,
    searchContent: "" as string,
    store: useAppStore(),
  }),

  computed: {
    username() {
      return this.store.$state.userInfo.username;
    },
  },

  methods: {
    async search() {
      this.userList = await getFriendList(this.searchContent);
      this.getUserAvatar();

    },
    async getCurrentPerson() {
      const res = await getPersonInfo();
      if (res.code == 0) {
        this.personId = res.data.personId;
      }
    },
    //只有在async函数中才能使用await
    // 获取分页数据
    async getUserList() {
      try {

        this.userList = await getFriendList("");
        console.log(this.userList);
        // 将两个数组合并，并随机排序
        //this.userList = [...this.studentList, ...this.teacherList].sort(() => Math.random() - 0.5);
      } catch (error) {
        console.error('获取失败：', error);
      }
    },

    //并行的获取所有用户的头像
    async getUserAvatar() {
      // 创建一个 promise 数组
      const promises = this.userList.map(user => getAvatar(user.personId));

      // 并行地获取所有用户的头像
      const results = await Promise.all(promises);

      // 更新 userList 中的 avatar
      for (let i = 0; i < results.length; i++) {
        if (results[i].code == 0) {
          this.userList[i].avatar = results[i].data;
        }
      }
    },


    open_user_profile(personId: number) {
      router.push({ name: "UserProfile", params: { personId: personId } });
    },

  },
  // created 生命周期钩子，当组件被创建时，它会自动调用 getUserList 方法，从而在页面加载时获取用户列表。
  async created() {
    await this.getUserList();
    this.getCurrentPerson();
    await this.getUserAvatar();
  },
});
</script>

<style scoped>
.component-background {
  background: url('../../../public/background-2061910.jpg') no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.content {
  margin-bottom: 20px;

  /* 小手 */
  cursor: pointer;
}

.title {
  margin-bottom: 5px;
  font-size: large;
  color: rgb(229, 130, 124);
  margin-left: -20px;

}

nav {
  margin-left: -20px;
  margin-bottom: 10px;
}


.content:hover {
  background-color: #dee3e6;
  box-shadow: 2px 2px 10px lightgray;
  transition: 500ms;
}

img {
  border-radius: 50%;
}

.img-field {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.username {
  font-weight: bold;
  height: 50%;
}

.gender {
  font-size: 12px;
  color: gray;
  height: 50%;
}
</style>