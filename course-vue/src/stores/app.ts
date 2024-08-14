// Utilities
import { PiniaPluginContext, defineStore } from "pinia";
import { userLoginReq } from "../services/userServ";
import { type MenuInfo, type UserInfo, SystemConfig, NotificationMsg, ConfirmMsg } from "../models/general";
import { getMenuList } from "../services/mainServ";
import { messageConform } from "~/tools/messageBox";
const defaultNaviList: MenuInfo[] = [];

export const useAppStore = defineStore("app", {
  state: () => ({
    usernameSave: "",
    passwordSave: "",
    remember: false,
    userInfo: {
      loggedIn: false,
      username: "",
      perName: "",
      jwtToken: "",
      id: 0,
      roles: "",
      password: "",
    } as UserInfo,

    systemConfig: {
      naviList: defaultNaviList,
      showLeftMeun: false,
      leftList: defaultNaviList,
      id: null,
    } as SystemConfig,
  }),
  actions: {
    async login(username: string, password: string): Promise<void> {
      const res = await userLoginReq(username, password);
      this.userInfo = {
        loggedIn: true,
        username: res.username,
        perName: res.perName,
        jwtToken: res.accessToken,
        id: res.id,
        roles: res.roles,
        password: password,
      };
      this.setNavi();
    },
    logout() {
      this.userInfo = {
        loggedIn: false,
        username: "",
        perName: "",
        jwtToken: "",
        roles: "",
        id: 0,
        password: "",
      };
      this.systemConfig.naviList = [];
    },
    async setNavi() {
      this.systemConfig.naviList = await getMenuList();
      console.log(this.systemConfig.naviList);
      this.systemConfig.leftList = this.systemConfig.naviList[0].sList;
      this.systemConfig.showLeftMeun = true;
      this.systemConfig.id = this.systemConfig.naviList[0].id;
    },
    saveAccount(username: string, password: string) {
      this.usernameSave = username;
      this.passwordSave = password;
      this.remember = true;
    },
    removeAccount() {
      this.usernameSave = "";
      this.passwordSave = "";
      this.remember = false;
    },
    //选中模块菜单，设置左侧菜单数据
    setLeftList(id: number) {
      let i: number;
      for (i = 0; i < this.systemConfig.naviList.length; i++) {
        if (this.systemConfig.naviList[i].id === id) {
          this.systemConfig.leftList = this.systemConfig.naviList[i].sList;
          this.systemConfig.id = id;
          break;
        }
      }
    },
    notificationMsg: {
      show: false,
      msg: ''
    } as NotificationMsg,
    confirmMsg: {
      show: false,
      msg: '',
      confirm: () => {
        alert('resolve')
      }
    } as ConfirmMsg,
    message(msg: string) {
      this.notificationMsg = {
        show: true,
        msg: msg
      }
    },
    async confirm(msg: string, confirm: Function) {
      this.confirmMsg = {
        show: true,
        msg: msg,
        confirm: confirm
      }
      const res = await messageConform(msg)
      if (!res) {
        return
      }
      confirm()
    }
  },
  persist: {
    storage: localStorage,
    // debug: true,
    // afterRestore: (context: PiniaPluginContext) => {
    //   console.log(context);
    // },
  },
});
