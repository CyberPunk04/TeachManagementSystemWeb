<template>
    <div class="base_form">
        <div class="base_header">
            <div class="blue_column"></div>
            <div class="base_title">授课查看</div>
        </div>
        <div class="base_query_oneLine">
            <div class="query_left">
            </div>
            <div class="query_right">
                <span style="margin-top: 5px">课序号或课程名称</span>
                <input type="text" v-model="numName" style="margin-left: 10px; width: 230px" />
                <button style="margin-left: 5px" class="commButton" @click="doQuery()">查询</button>
            </div>
        </div>

        <div class="table-content">
            <el-table
                :data="courseList.slice((pagination.currentPage - 1) * pagination.pageSize, pagination.currentPage * pagination.pageSize)"
                :header-cell-style="{
                    color: '#2E2E2E',
                    fontSize: '15px',
                    fontWeight: '400',
                    background: 'rgb(242,242,242)',
                }" :row-style="{ height: '60px' }" :cell-style="{ padding: '2px' }" style="width: 100%">
                <el-table-column label="课序号" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.num }}
                    </template>
                </el-table-column>
                <el-table-column label="课程名称" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.name }}
                    </template>
                </el-table-column>
                <el-table-column label="学分" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.credit }}
                    </template>
                </el-table-column>
                <el-table-column label="教材名称" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.coursePath }}
                    </template>
                </el-table-column>
                <el-table-column label="前序课" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.preCourse }}
                    </template>
                </el-table-column>
                <el-table-column prop="classTime" label="上课时间" align="center">
                    <template #default="scope">
                        <el-button text type="primary" plain @click="showClassTime(scope.row)">
                            详情
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column label="上课地点" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.place }}
                    </template>
                </el-table-column>
                <el-table-column label="课程类型" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.type }}
                    </template>
                </el-table-column>
                <el-table-column label="课容量" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.much }}
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="paginationClass">
            <el-pagination background style="margin-top: 15px" :current-page="pagination.currentPage"
                :page-size="pagination.pageSize" :page-sizes="[5, 10]" layout="total, sizes, prev, pager, next, jumper"
                :total="courseList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>

    </div>

    <el-dialog title="课程时间详情" :width="350" :height="500" :before-close="handleClose" v-model="classTimeVisible"
        :append-to-body='true'>
        <el-form :model="form" label-width="80px">
            <el-form-item label="上课时间">
                <el-input v-model="form.classTime" :disabled="true" />
            </el-form-item>
            <el-form-item label="开始周">
                <el-input v-model="form.startWeek" :disabled="true" />
            </el-form-item>
            <el-form-item label="结束周">
                <el-input v-model="form.endWeek" :disabled="true" />
            </el-form-item>
        </el-form>
        <el-row :gutter="80" class="dialog-buttons">
            <el-col :span="12">
                <el-button @click="closeClassTime">关闭</el-button>
            </el-col>
        </el-row>
    </el-dialog>
</template>

<script lang="ts">
import { type CourseItem, type OptionItem, type TeacherItem } from '~/models/general'
import { defineComponent } from 'vue'
import { getTeacherCourseList, getTeacherList, courseDelete, courseSave, getCoursePageData } from '~/services/courseServ'
import { message, messageConform } from '~/tools/messageBox'
export default defineComponent({
    data: () => ({
        numName: '',
        tecName: '',
        courseList: [] as CourseItem[],
        teacherList: [] as TeacherItem[],
        courseSelectList: [] as OptionItem[],
        teacherSelectList: [] as OptionItem[],
        deleteId: -1,
        form: {} as CourseItem,
        pagination: {
            currentPage: 1,
            pageSize: 10,
            total: 0,
        },
        dialogVisible: false,
        classTimeVisible: false,
        descriptionVisible: false,
        userId: window.localStorage.getItem("userId"),
    }),
    //初始加载一次
    created() {
        this.query()
    },

    methods: {
        //每个页面最多行
        handleSizeChange(val: number) {
            this.pagination.pageSize = val;
        },
        // 当前页
        handleCurrentChange(val: number) {
            this.pagination.currentPage = val;
        },
        // 查询请求
        doQuery() {
            this.pagination.currentPage = 1;
            this.query();
        },
        // 分页请求
        handleChangePage(val: number) {
            this.pagination.currentPage = val;
            this.query();
        },
        // 获取分页数据
        async query() {
            this.courseList = await getTeacherCourseList(this.numName)
            this.teacherList = await getTeacherList(this.tecName)
        },
        //添加课程,显示对话框
        addItem() {
            this.form = {} as CourseItem
            this.dialogVisible = true;
        },
        //编辑课程,显示对话框
        editItem(item: CourseItem) {
            this.form = item
            this.dialogVisible = true;
        },
        showClassTime(item: CourseItem) {
            this.form = item
            this.classTimeVisible = true;
        },
        //关闭对话框
        close() {
            this.dialogVisible = false;
        },
        closeClassTime() {
            this.classTimeVisible = false;
        },
        //确认对话框
        async confirm() {
            this.close()
            const res = await courseSave(this.form)
            if (res.code == 0) {
                message(this, '保存成功')
                this.query()
            } else {
                message(this, res.msg)
            }
        },
        //删除课程
        async deleteItem(courseId: number) {
            const result = await messageConform('确认删除吗?')
            if (!result) {
                return
            }
            const res = await courseDelete(courseId)
            if (res.code == 0) {
                message(this, '删除成功')
                this.query()
            } else {
                message(this, res.msg)
            }
        },
        openDialog() {
            this.dialogVisible = true;
        },
        closeDialog() {
            this.dialogVisible = false;
        },
        handleClose(done: () => void) {
            done();
        },
    }
})
</script>
<style></style>
