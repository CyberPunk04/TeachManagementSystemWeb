<template>
    <div class="base_form">
        <div class="base_header">
            <div class="blue_column"></div>
            <div class="base_title">课程及成绩查询</div>
        </div>

        <div class="base_query_oneLine">
            <div class="query_left">

            </div>
            <div class="query_right">
                <span style="margin-top: 5px">课程</span>
                <select class="commInput" v-model="courseId">
                    <option value="0">请选择...</option>
                    <option v-for="item in courseList" :key="item.id" :value="item.id">
                        {{ item.title }}
                    </option>
                </select>
                <button style="margin-left: 5px" class="commButton" @click="query()">
                    查询
                </button>
            </div>
        </div>
        <!--
        <div class="table_center" style="margin-top: 5px">
            <table class="content">
                <tr class="table_th">
                    <td>学号</td>
                    <td>姓名</td>
                    <td>班级</td>
                    <td>课序号</td>
                    <td>课程名称</td>
                    <td>学分</td>
                    <td>成绩</td>
                </tr>
                <tr v-for="item in scoreList" :key="item.scoreId">
                    <td>{{ item.studentNum }}</td>
                    <td>{{ item.studentName }}</td>
                    <td>{{ item.className }}</td>
                    <td>{{ item.courseNum }}</td>
                    <td>{{ item.courseName }}</td>
                    <td>{{ item.credit }}</td>
                    <td>{{ item.mark }}</td>
                </tr>
            </table>
        </div>
        -->

        <div class="table-content">
            <el-table
                :data="scoreList.slice((pagination.currentPage - 1) * pagination.pageSize, pagination.currentPage * pagination.pageSize)"
                :header-cell-style="{
                    color: '#2E2E2E',
                    fontSize: '15px',
                    fontWeight: '400',
                    background: 'rgb(242,242,242)',
                }" :row-style="{ height: '60px' }" :cell-style="{ padding: '2px' }" style="width: 100%">
                <el-table-column label="学号" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.studentNum }}
                    </template>
                </el-table-column>
                <el-table-column label="姓名" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.studentName }}
                    </template>
                </el-table-column>
                <el-table-column label="班级" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.className }}
                    </template>
                </el-table-column>
                <el-table-column label="课序号" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.courseNum }}
                    </template>
                </el-table-column>
                <el-table-column label="课程名称" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.courseName }}
                    </template>
                </el-table-column>
                <el-table-column label="学分" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.credit }}
                    </template>
                </el-table-column>
                <el-table-column label="分数" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.mark }}
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 分页区域 -->
        <div class="paginationClass">
            <el-pagination background style="margin-top: 15px" :current-page="pagination.currentPage"
                :page-size="pagination.pageSize" :page-sizes="[5, 10]" layout="total, sizes, prev, pager, next, jumper"
                :total="courseList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
    </div>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import {
    getScoreList,
    getStudentItemOptionList,
    getCourseItemOptionList,
    scoreSave,
    scoreDelete,
} from "~/services/teachingServ";
import {
    getStudentIntroduceData,
    getPhotoImageStr,
    uploadPhoto,
} from "~/services/infoServ";
import { getScorePageData, getStudentCourseItemOptionList } from "~/services/courseServ";
import { type OptionItem, type ScoreItem, type StudentItem } from "~/models/general";
import { message, messageConform } from "~/tools/messageBox";
export default defineComponent({
    data: () => ({
        scoreList: [] as ScoreItem[],
        studentId: null as number | null,
        courseId: null,
        info: {} as StudentItem,
        editedItem: {} as ScoreItem,
        studentList: [] as OptionItem[],
        courseList: [] as OptionItem[],
        courseSelectionList: [] as OptionItem[],
        deleteId: -1,
        pagination: {
            currentPage: 1,
            pageSize: 10,
            dataTotal: 0,
        },
    }),
    computed: {},
    created() {
        this.initialize();
    },

    methods: {
        // 初始化,获取学生选择项列表和课程选择项列表
        async initialize() {
            let res = await getStudentIntroduceData(this.studentId);
            this.info = res.data.info;
            this.studentId = this.info.studentId;
            this.scoreList = await getScoreList(this.studentId, this.courseId);
            this.studentList = await getStudentItemOptionList();
            this.courseList = await getStudentCourseItemOptionList();
        },
        //每个页面最多行
        handleSizeChange(val: number) {
            this.pagination.pageSize = val;
        },
        // 当前页
        handleCurrentChange(val: number) {
            this.pagination.currentPage = val;
        },
        // 查询
        async query() {
            this.scoreList = await getScoreList(this.studentId, this.courseId);
            this.pagination.currentPage = 1;
            const res = await getScorePageData(
                this.studentId,
                this.courseId,
                this.pagination.currentPage
            );
            this.pagination.dataTotal = res.data.dataTotal;
            this.pagination.pageSize = res.data.pageSize;
            this.scoreList = res.data.dataList;
        },
        // 分页请求
        handleChangePage(val: number) {
            this.pagination.currentPage = val;
            this.initialize();
        },

    },
});
</script>