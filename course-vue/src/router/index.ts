// Composables
import { createRouter, createWebHistory, RouteLocationNormalized, NavigationGuardNext } from "vue-router";
import { useAppStore } from "~/stores/app";
//路由表
const routes = [
  //当路由为空时，重定向到登录页面
  {
    path: "/",
    redirect: "/MainPage",
  },
  //登录页面
  {
    path: "/Login",
    name: "Login",
    component: () => import("~/Login.vue"),
  },
  //主页面
  {
    path: "/MainPage",
    name: "MainPage",
    component: () => import("~/views/MainPage.vue"),
  },
  {
    path: "/SystemIntroduce",
    name: "SystemIntroduce",
    component: () => import("~/views/info/SystemIntroduce.vue"),
  },
  {
    path: "/Password",
    name: "Password",
    component: () => import("~/views/info/Password.vue"),
  },
  {
    path: "/updateEmail",
    name: "updateEmail",
    component: () => import("~/views/info/UpdateEmail.vue"),
  },
  {
    path: "/StudentIntroduce",
    name: "StudentIntroduce",
    component: () => import("~/views/info/StudentIntroduce.vue"),
  },
  {
    path: "/TeacherIntroduce",
    name: "TeacherIntroduce",
    component: () => import("~/views/info/TeacherIntroduce.vue"),
  },
  {
    path: "/menu-manage-panel",
    name: "MenuManage",
    component: () => import("~/views/system/MenuManage.vue"),
  },
  {
    path: "/dictionary-manage-panel",
    name: "DictionaryManage",
    component: () => import("~/views/system/DictionaryManage.vue"),
  },

  {
    path: "/student-panel",
    name: "StudentTable",
    component: () => import("~/views/person/StudentTable.vue"),
  },
  {
    path: "/StudentInfo",
    name: "StudentInfo",
    component: () => import("~/views/person/StudentInfo.vue"),
  },
  {
    path: "/FamilyMember",
    name: "FamilyMember",
    component: () => import("~/views/person/FamilyMember.vue"),
  },

  //yxy
  // {
  //   path: "/teacher-panel",
  //   name: "AdminTeacher",
  //   component: () => import("~/views/admin/AdminTeacher.vue"),
  // },
  // {
  //   path: "/TeacherInfo",
  //   name: "AdminTeachInfo",
  //   component: () => import("~/views/admin/AdminTeachInfo.vue"),
  // },

  {
    path: "/teacher-panel",
    name: "teacherTable",
    component: () => import("~/views/person/TeacherTable.vue"),
  },
  {
    path: "/TeacherInfo",
    name: "TeacherInfo",
    component: () => import("~/views/person/TeacherInfo.vue"),
  },

  {
    path: "/course-panel",
    name: "CourseTable",
    component: () => import("~/views/teaching/CourseTable.vue"),
  },
  {
    path: "/score-table-panel",
    name: "ScoreTable",
    component: () => import("~/views/teaching/ScoreTable.vue"),
  },
  {
    path: "/TestBackground",
    name: "TestBackground",
    component: () => import("~/views/test/TestBackground.vue"),
  },
  {
    path: "/TestDiv",
    name: "TestDiv",
    component: () => import("~/views/test/TestDiv.vue"),
  },
  {
    path: "/TestComponent",
    name: "TestComponent",
    component: () => import("~/views/test/TestComponent.vue"),
  },


  //jzh
  {//有用户id参数的路由w
    path: "/UserProfile/:personId",
    name: "UserProfile",
    component: () => import("~/views/blog/UserProfile.vue"),
  },
  {
    path: "/UserList",
    name: "UserList",
    component: () => import("~/views/blog/UserList.vue"),
  },
  {
    path: "/FriendList",
    name: "FriendList",
    component: () => import("~/views/blog/FriendList.vue"),
  },
  {
    path: "/BlogIndex",
    name: "BlogIndex",
    component: () => import("~/views/blog/BlogIndex.vue")
  },
  {
    path: "/404",
    name: "404",
    component: () => import("~/views/error/404.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },

  {
    path: "/adminAtudentComment-panel",
    name: "AdminStudentComment",
    component: () => import("~/views/student/AdminStudentComment.vue"),
  },

  //gsz
  {
    path: "/StudentScore",
    name: "StudentScore",
    component: () => import("~/views/teaching/StudentScore.vue"),
  },
  {
    path: "/CourseShow",
    name: "CourseShow",
    component: () => import("~/views/teaching/CourseShow.vue"),
  },
  {
    path: "/CourseSelection",
    name: "CourseSelection",
    component: () => import("~/views/teaching/CourseSelection.vue"),
  },
  {
    path: "/TeacherCourse",
    name: "TeacherCourse",
    component: () => import("~/views/teaching/TeacherCourse.vue"),
  },
  {
    path: "/TeacherScore",
    name: "TeacherScore",
    component: () => import("~/views/teaching/TeacherScore.vue"),
  },
  //clt
  {
    path: '/activity-table',
    name: 'ActivityTable',
    component: () => import('~/views/daily/ActivityTable.vue')
  },
  {
    path: '/fee-table',
    name: 'FeeTable',
    component: () => import('~/views/daily/FeeTable.vue')
  },
  {
    path: '/leave-table',
    name: 'LeaveTable',
    component: () => import('~/views/daily/LeaveTable.vue')
  },
  {
    path: '/competition-table',
    name: 'CompetitionTable',
    component: () => import('~/views/innovationmanage/CompetitionTable.vue')
  },
  {
    path: '/innovation-table',
    name: 'InnovationTable',
    component: () => import('~/views/innovationmanage/InnovationTable.vue')
  },
  {
    path: '/internship-table',
    name: 'InternshipTable',
    component: () => import('~/views/innovationmanage/InternshipTable.vue')
  },
  {
    path: '/lecture-table',
    name: 'LectureTable',
    component: () => import('~/views/innovationmanage/LectureTable.vue')
  },
  {
    path: '/practice-table',
    name: 'PracticeTable',
    component: () => import('~/views/innovationmanage/PracticeTable.vue')
  },
  {
    path: '/research-table',
    name: 'ResearchTable',
    component: () => import('~/views/innovationmanage/ResearchTable.vue')
  },
  {
    path: '/prize-table',
    name: 'PrizeTable',
    component: () => import('~/views/honor/PrizeTable.vue')
  },

  //
  {
    path: "/student-comment-panel",
    name: "StudentComment",
    component: () => import("~/views/student/StudentComment.vue"),
  },

];
//路由创建
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

//路由守卫 未登录时跳转到登录页面
router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const store = useAppStore();
  if (!store.userInfo.loggedIn && to.path !== '/login') {
    next('/login');
  } else {
    next();
  }
});
//路由导出
export default router;
