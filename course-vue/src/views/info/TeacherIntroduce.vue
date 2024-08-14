<template>
    <div class="base_form">
        <div class="base_header">
            <div class="blue_column"></div>
            <div class="base_title">个人主页</div>
        </div>

        <div class="teacher_info">
            <div class="top_bar">
                <div class="left">
                    <h6>基本信息</h6>
                </div>
                <div class="right">
                    <el-button class="right-button" @click="editInfo(info)">修改信息</el-button>
                </div>
            </div>
            <hr>
            <div style="display: flex;justify-content: center;align-items: center;">
                <table class="info_content">
                    <tr>
                        <td class="tag">工号</td>
                        <td class="info_td">{{ info.num }}</td>
                        <td class="tag">姓名</td>
                        <td class="info_td">{{ info.name }}</td>
                        <td class="tag">学院</td>
                        <td class="info_td">{{ info.dept }}</td>
                        <td rowspan="3">
                            <div class="image-container">
                                <img :src="imgStr" alt="个人照片" width="200" />
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="tag">职称</td>
                        <td class="info_td">{{ info.title }}</td>
                        <td class="tag">学位</td>
                        <td class="info_td">{{ info.degree }}</td>
                        <td class="tag">证件号码</td>
                        <td class="info_td">{{ info.card }}</td>
                    </tr>
                    <tr>
                        <td class="tag">性别</td>
                        <td class="info_td">{{ info.genderName }}</td>
                        <td class="tag">出生日期</td>
                        <td class="info_td">{{ info.birthday }}</td>
                        <td class="tag">邮箱</td>
                        <td class="info_td">{{ info.email }}</td>
                    </tr>
                    <tr>
                        <td class="tag">电话</td>
                        <td class="info_td">{{ info.phone }}</td>
                        <td class="tag">地址</td>
                        <td class="info_td" colspan="3">{{ info.address }}</td>
                        <td>
                            <el-button style="margin-left: 5px" @click="downloadPdf()">附件下载</el-button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div id="course_center">
            <div class="top_bar">
                <div class="left">
                    <h6>我的课程</h6>
                </div>
                <div class="right">
                    <el-button class="right-button" @click="gotoCourse(info.teacherId)">课程管理</el-button>
                </div>
            </div>
            <hr>
            <div id="courseContent">
                <table id="courseTable">
                    <tr style="background-color: cornsilk;">
                        <td class="courseTd">课序号</td>
                        <td class="courseTd">课程名程</td>
                        <td class="courseTd">学分</td>
                        <td class="courseTd">课程类型</td>
                        <td class="courseTd">教材名称</td>
                        <td class="courseTd">起始周</td>
                        <td class="courseTd">结束周</td>
                        <td class="courseTd">上课时间</td>
                        <td class="courseTd">上课地点</td>
                        <td class="courseTd">课程容量</td>
                    </tr>
                    <tr class="scoreTr" v-for="item in courseList" :key="item.courseId">
                        <td class="courseTd">{{ item.num }}</td>
                        <td class="courseTd">{{ item.name }}</td>
                        <td class="courseTd">{{ item.credit }}</td>
                        <td class="courseTd">{{ item.type }}</td>
                        <td class="courseTd">{{ item.coursePath }}</td>
                        <td class="courseTd">{{ item.startWeek }}</td>
                        <td class="courseTd">{{ item.endWeek }}</td>
                        <td class="courseTd">{{ item.classTime }}</td>
                        <td class="courseTd">{{ item.place }}</td>
                        <td class="courseTd">{{ item.much }}</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="others">
            <div class="container">
                <div class="container1">
                    <div id="blog" class="part1" style="width: 600px;">
                        <div class="top_bar">
                            <div class="left">
                                <h6>我的博客</h6>
                            </div>
                            <div class="right">
                                <el-button class="right-button" @click="gotoBlog()">我的博客</el-button>
                            </div>
                        </div>
                        <hr>
                        <div class="card-blog" @click="gotoBlog()">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-4">
                                        <div class="avatar_container">
                                            <img class="img-fluid img-field" :src="user.avatar" alt="加载失败">
                                        </div>
                                    </div>
                                    <div class="col-8">
                                        <div class="Username">{{ user.name }}</div>
                                        <div class="fans">粉丝数: {{ user.followers }}</div>
                                        <div class="fans">总计发帖数量: {{ countPost }}</div>
                                        <hr>
                                        <div class="fans">欢迎关注哦~</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container2">
                    <div id="studyDirection" class="part2" style="width: 500px;">
                        <div class="top_bar">
                            <div class="left">
                                <h6>研究方向</h6>
                            </div>
                            <div class="right">
                                <el-button class="right-button" @click="editStudyDirection()">编辑</el-button>
                            </div>
                        </div>
                        <hr>
                        <div>
                            <div class="two">
                                {{ info.studyDirection }}
                            </div>
                        </div>
                    </div>
                    <div id="publications" class="part2" style="width: 500px;">
                        <div class="top_bar">
                            <div class="left">
                                <h6>论文著作</h6>
                            </div>
                            <div class="right">
                                <el-button class="right-button" @click="editPublications()">编辑</el-button>
                            </div>
                        </div>
                        <hr>
                        <div>
                            <div class="two">
                                {{ info.publications }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--教师研究方向编辑对话框-->>
    <dialog id="editStudyDirection" onclose="close()" style="
        position: absolute;
        top: 300px;
        left: 300px;
        width: 300px;
        height: 350px;    
    ">
        <div>
            <div class="base_title">
                研究方向修改
            </div>
            <textarea v-model="info.studyDirection" class="form-control" placeholder="请编辑文字" id="text"
                style="height: 240px;">{{ info.studyDirection }}</textarea>
        </div>
        <div class="button-container">
            <el-button class="commButton" @click="confirm()">确认</el-button>
            <el-button class="commButton" style="margin-left: 20px;" @click="close1()">取消</el-button>
        </div>
    </dialog>
    <!--教师论文著作编辑对话框-->>
    <dialog id="editPublications" onclose="close()" style="
        position: absolute;
        top: 300px;
        left: 300px;
        width: 300px;
        height: 400px;    
    ">
        <div>
            <div class="base_title">
                论文著作修改
            </div>
            <textarea v-model="info.publications" class="form-control" placeholder="请编辑文字" id="text"
                style="height: 240px;">{{ info.publications }}</textarea>
        </div>
        <div class="button-container">
            <el-button class="commButton" @click="confirm()">确认</el-button>
            <el-button class="commButton" style="margin-left: 20px;" @click="close2()">取消</el-button>
        </div>
    </dialog>
    <!-- 教师端简单信息修改对话框显示 -->
    <dialog id="editDialog" onclose="close()" style="
        position: absolute;
        top: 300px;
        left: 300px;
        width: 600px;
        height: 750px;
    ">
        <div class="base_title">个人信息修改</div>
        <div class="dialog-div" style="margin-top: 5px">
            <div>
                <div>
                    <div id="change-image">
                        <img id="person-image" :src="imgStr" alt="个人照片" width="200" />
                    </div>
                    <div class="button-container">
                        <input type="file" id="file" accept=".jpg" />
                        <el-button @click="uploadFile()">更改头像</el-button>
                    </div>
                </div>
                <table class="dialog-content">
                    <tr>
                        <td class="tag" style="border-radius: 0px;" colspan="1">工号</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.num }}
                        </td>
                        <td class="tag" colspan="1" style="border-radius: 0px;">姓名</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.name }}
                        </td>
                    </tr>
                    <tr>
                        <td class="tag" colspan="1" style="border-radius: 0px;">学院</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.dept }}
                        </td>
                        <td class="tag" colspan="1" style="border-radius: 0px;">职称</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.title }}
                        </td>
                    </tr>
                    <tr>
                        <td class="tag" colspan="1" style="border-radius: 0px;">学位</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.degree }}
                        </td>
                        <td class="tag" colspan="1" style="border-radius: 0px;">证件号码</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.card }}
                        </td>
                    </tr>
                    <tr>
                        <td class="tag" colspan="1" style="border-radius: 0px;">性别</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.genderName }}
                        </td>
                        <td class="tag" colspan="1" style="border-radius: 0px;">出生日期</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.birthday }}
                        </td>
                    </tr>
                    <tr>
                        <td class="tag" colspan="1" style="border-radius: 0px;">邮箱</td>
                        <td class="info_td" style="border-radius: 0px;">
                            {{ info.email }}
                        </td>
                        <td class="tag" colspan="1" style="border-radius: 0px;">电话</td>
                        <td class="info_td" style="border-radius: 0px;">
                            <input v-model="info.phone" class="commInput" />
                        </td>
                    </tr>
                    <tr>
                        <td class="tag" colspan="1" style="border-radius: 0px;">地址</td>
                        <td class="info_td" colspan="3" style="border-radius: 0px;">
                            <input v-model="info.address" class="commInput" style="text-align: left;width:350px;" />
                        </td>
                    </tr>
                </table>
            </div>
            <div class="button-container">
                <el-button class="commButton" @click="confirm()">确认</el-button>
                <el-button class="commButton" style="margin-left: 20px;" @click="close()">取消</el-button>
            </div>
        </div>
    </dialog>
</template>
<script lang="ts" scoped>
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { defineComponent } from "vue";
import {
    type TeacherItem,
    type StudentItem,
    type CourseItem,
    type PostItem,
} from "~/models/general";
import { downloadPost, getAuthHeader } from "~/services/genServ";
import {
    getTeacherIntroduceData,
    saveTeacherIntroduce,
    getPhotoImageStr,
    uploadPhoto,
} from "~/services/infoServ";
import {
    getPersonInfo, getFansNum, getAvatar, countPost
} from "~/services/blogServ";
import { message } from "~/tools/messageBox";
import { getStudentInfo, getTeacherInfo } from "~/services/personServ";
import router from "~/router";

export default defineComponent({
    data: () => ({
        authHeader: getAuthHeader(),
        valid: false,
        uploadIsLoading: false,
        teacherId: null as number | null,
        imgStr: '',
        info: {} as TeacherItem,
        courseList: [] as CourseItem[],
        nameRules: [],
        emailRules: [],
        //博客组件展示个人信息
        user: {} as StudentItem | TeacherItem,
        loading: true,
        countPost: 0 as number,
        commentContent: "",
        allposts: [] as PostItem[],
        followedPosts: [] as PostItem[],
        personId: 0 as number,
    }),
    async created() {
        let res = await getTeacherIntroduceData(this.teacherId)
        this.info = res.data.info
        this.teacherId = this.info.teacherId
        this.courseList = res.data.courseList
        res = await getPhotoImageStr('photo/' + this.info.personId + '.jpg')
        this.imgStr = res.data;
        this.getCurrentPerson();

    },


    methods: {
        //导出为PDF
        exportToPDF() {
            // 获取要导出的 div 元素
            const divToExport = document.getElementById('PDF');
            if (divToExport) {
                // 使用 html2canvas 将 div 转换为 Canvas
                html2canvas(divToExport).then((canvas) => {
                    // 创建 PDF 对象
                    const pdf = new jsPDF('p', 'mm', 'a4');

                    // 获取 Canvas 的图像数据
                    const imgData = canvas.toDataURL('image/png');

                    // 将图像添加到 PDF 中
                    pdf.addImage(imgData, 'PNG', 0, 0, 210, 70);

                    // 保存 PDF 文件
                    pdf.save('教师基本信息.pdf');
                });
            } else {
                console.error('无法找到要导出的 div 元素');
            }
        },
        //进入课程管理界面
        gotoCourse(teacherId: number) {
            router.push({
                name: "CourseTable",
                // 传递参数
                query: { teacherId: teacherId },
            });
        },

        //博客展示
        async getCurrentPerson() {
            const res = await getPersonInfo();
            if (res.code == 0) {
                this.personId = res.data.personId;
                this.getUserProfile();

            }
        },
        //加载博客个人信息
        async getUserProfile() {
            this.user = await getStudentInfo(null, this.personId);
            if (this.user == null) {
                this.user = await getTeacherInfo(null, this.personId);
            }
            //获取粉丝数
            this.user.followers = await getFansNum(this.personId);
            this.loading = false;
            this.getAvatar();
            this.getCountPost();
        },
        //获取头像
        async getAvatar() {
            const res = await getAvatar(this.personId);
            if (res.code == 0) {
                this.user.avatar = res.data;
            }
        },
        //统计帖子数目
        async getCountPost() {
            const res = await countPost(this.personId);
            if (res.code == 0) {
                this.countPost = res.data;
            }
        },

        //进入博客界面
        gotoBlog() {
            router.push({ name: "UserProfile", params: { personId: this.personId } });
        },

        //上传个人照片
        async uploadFile() {
            const file = document.querySelector('#file') as any
            if (file.files == null || file.files.length == 0) {
                alert('请选择文件！')
                return
            }
            const res = await uploadPhoto('photo/' + this.info.personId + '.jpg', file)
            if (res.code === 200) {
                alert('上传成功')
            } else {
                alert('上传失败')
            }
        },

        //导出为PDF
        downloadPdf() {
            const res = downloadPost('/api/teacher/getTeacherIntroduceItextPdf', this.info.num + '.pdf', {
                teacherId: this.teacherId
            })
            console.log(res)
        },

        // 上传成功回调函数
        onSuccess(res: any) {
            if (res.code == 0) {
                message(this, "上传成功！");
            } else {
                message(this, res.msg);
            }
        },

        //修改个人信息
        editInfo(item: TeacherItem) {
            this.info = item;
            const dialog = document.getElementById("editDialog") as HTMLDialogElement;
            dialog.show();
        },

        //修改研究方向
        editStudyDirection() {
            const dialog = document.getElementById("editStudyDirection") as HTMLDialogElement;
            dialog.show();
        },

        //修改论文著作
        editPublications() {
            const dialog = document.getElementById("editPublications") as HTMLDialogElement;
            dialog.show();
        },

        //关闭对话框
        close() {
            const dialog = document.getElementById("editDialog") as HTMLDialogElement;
            dialog.close();
        },
        close1() {
            const dialog = document.getElementById("editStudyDirection") as HTMLDialogElement;
            dialog.close();
        },
        close2() {
            const dialog = document.getElementById("editPublications") as HTMLDialogElement;
            dialog.close();
        },
        //保存修改
        async confirm() {
            this.close();
            this.close1();
            this.close2();
            let flag = false;
            let c;
            for (let i = 0; i < this.info.phone.length; i++) {
                c = this.info.phone[i];
                if (c < '0' || c > '9') {
                    flag = true;
                }
            }
            if (this.info.phone == '' || this.info.phone == null || this.info.phone == undefined || this.info.phone.length != 11 || flag) {
                message(this, "请输入正确格式的号码")
                this.info.phone = '';
                return
            }
            const res = await saveTeacherIntroduce(this.teacherId, this.info);

            if (res.code == 0) {
                message(this, "保存成功");
            } else {
                message(this, res.msg);
            }
        },
    }
})
</script>
<style scoped>
img {
    border-radius: 50%;
}

.img-field {
    margin-left: 40px;
    max-width: 80px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.fans {
    font-size: 12px;
    color: gray
}

/*最大div*/
.base_form {
    background-color: #f1ebeb;
}

.top_bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

h6 {
    height: 35px;
    display: flex;
    margin-top: 10px;
    margin-left: 20px;
    /*justify-content: space-between;*/
    align-items: center;
}

.right-button {
    margin-right: 20px;
}

hr {
    border: none;
    border-top: 1px solid #b4b1b1;
    margin: 1px 0;
}

/*——————————————————————————————第一块：个人基本信息————————————————————————————*/
.teacher_info {
    background-color: white;
    border-radius: 8px;
    margin-bottom: 20px;
    margin-top: 10px;
    margin-left: 100px;
    margin-right: 100px;
    box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.3);
}

/*基本信息表格*/
.info_content {
    border-collapse: separate;
    border-spacing: 10px 10px;
    border-color: transparent;
    margin-top: 8px;
    margin-bottom: 15px;
    margin-left: 30px;
    margin-right: 30px;
}

td {
    text-align: center;
    width: 80px;
    height: 50px;
    border: none;
}

.tag {
    background-color: whitesmoke;
    border-radius: 10px;
    padding: 0px;
    /*font-weight: bold;*/
}

.info_td {
    width: 150px;
    height: 50px;
    border: none;
    border-radius: 10px;
    background-color: rgba(247, 240, 244, 0.651);
    transition: transform 0.3s, box-shadow 0.3s;
}

.info_td:hover {
    background-color: #fce9e9;
    transition: ease;
    transform: scale(1.06);
    box-shadow: 0 0 10px rgba(54, 54, 54, 0.3);
}

.image-container {
    display: inline-block;
    position: relative;
    transition: transform 0.3s, box-shadow 0.3s;
}

.image-container:hover {
    transform: scale(1.06);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

#change-image {
    display: flex;
    justify-content: center;
    align-items: center;
    width: auto;
    /* 为了使外层 div 铺满整个视口高度，可以根据实际情况调整 */
}

#person-image {
    display: inline-block;
    position: relative;
    transition: transform 0.3s, box-shadow 0.3s;
}

#person-image:hover {
    transform: scale(1.06);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

/*————————————————————————————————我的课程——————————————————————————————*/
#course_center {
    background-color: white;
    border-radius: 8px;
    margin-bottom: 20px;
    margin-top: 20px;
    margin-left: 100px;
    margin-right: 100px;
    box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.3);
}

#courseContent {
    /*用来装scoreTable和myChartPie*/
    display: flex;
}

#courseTable {
    flex: 1;
    width: 500px;
    margin-top: 10px;
    margin-left: 30px;
    margin-right: 30px;
    margin-bottom: 30px;
}

.scoreTr {
    background-color: ghostwhite;
}

.scoreTr:hover {
    background-color: white;
    transition: ease;
}

.scoreTd {
    text-align: center;
    width: 10px;
    height: 10px;
    border: none;
}

/*——————————————————————博客+研究方向+论文著作—————————————————————————————— */
.two {
    margin-left: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
    margin-right: 20px;
}

.others {
    background-color: transparent;
}

.container {
    display: flex;
}

.container1 {
    flex: 1;
    background-color: transparent;
}

.container2 {
    flex: 1;
    background-color: transparent;
}

.part1 {
    background-color: white;
    border-radius: 8px;
    margin-bottom: 20px;
    margin-top: 20px;
    margin-left: 100px;
    margin-right: 10px;
    box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.3);
}

.card-blog {
    margin-top: 5px;
    transition: box-shadow 0.5s ease;
    /* 阴影会在 0.3 秒内平滑地出现 */
    box-shadow: 0 0 0 rgba(0, 0, 0, 0);
    cursor: pointer;
}

.card-blog:hover {
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    /* 鼠标悬停时阴影变为 10px 模糊黑色 */
}

.part2 {
    background-color: white;
    border-radius: 8px;
    margin-bottom: 20px;
    margin-top: 20px;
    margin-left: 10px;
    margin-right: 10px;
    box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.3);
}

.dialog-content {
    margin-top: 10px;
    margin-left: 20px;
    margin-right: 20px;
    margin-bottom: 30px;
}

.button-container {
    display: flex;
    justify-content: center;
    align-items: center;
}

.left-right {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.inline-container {
    display: flex;
    align-items: center;
}

#file {
    border: #b4b1b1;
}
</style>
  
