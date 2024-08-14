<template>
    <div class="base_form">
        <div class="base_header">
            <div class="blue_column"></div>
            <div class="base_title">请假审批</div>
        </div>
        <div class="base_query_oneLine">
            <div class="query_left">
            </div>
            <div class="query_right">
                
            </div>
        </div>

        <div class="table-content">
            <el-table
                :data="leavingList.slice((pagination.currentPage - 1) * pagination.pageSize, pagination.currentPage * pagination.pageSize)"
                :header-cell-style="{
                    color: '#2E2E2E',
                    fontSize: '15px',
                    fontWeight: '400',
                    background: 'rgb(242,242,242)',
                }" :row-style="{ height: '60px' }" :cell-style="{ padding: '2px' }" style="width: 100%">
                <el-table-column label="学生姓名" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.studentName }}
                    </template>
                </el-table-column>
                <el-table-column label="目的地" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.destination }}
                    </template>
                </el-table-column>
                <el-table-column label="请假时间" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.leavingTime }}
                    </template>
                </el-table-column>
                <el-table-column label="开始时间" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.startTime }}
                    </template>
                </el-table-column>
                <el-table-column label="结束时间" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.endTime }}
                    </template>
                </el-table-column>
                <el-table-column label="请假原因" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.reason }}
                    </template>
                </el-table-column>
                
                <el-table-column label="操作" color="black" align="center">
                    <template v-slot="scope">
                        <el-button class="commButton" @click="confirmA(scope.row)">同意</el-button>
                        <el-button class="commButton" @click="confirmB(scope.row)">不同意</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="paginationClass">
            <el-pagination background style="margin-top: 15px" :current-page="pagination.currentPage"
                :page-size="pagination.pageSize" :page-sizes="[5, 10]" layout="total, sizes, prev, pager, next, jumper"
                :total="leavingList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
    </div>
</template>

<script lang="ts">
import { LeaveItem, type CourseItem, type OptionItem, type TeacherItem } from '~/models/general'
import { defineComponent } from 'vue'
import { getCourseList, getTeacherList, courseDelete, courseSave, getCoursePageData,leavingSaveA,leavingSaveB } from '~/services/courseServ'
import { getLeavingList } from '~/services/genServ'
import { message, messageConform } from '~/tools/messageBox'
export default defineComponent({
    data: () => ({
        numName: '',
        tecName: '',
        leavingList: [] as LeaveItem[],
        teacherList: [] as TeacherItem[],
        courseSelectList: [] as OptionItem[],
        teacherSelectList: [] as OptionItem[],
        deleteId: -1,
        form: {} as LeaveItem,
        pagination: {
            currentPage: 1,
            pageSize: 10,
            total: 0,
        },
        dialogVisible: false,
        classTimeVisible: false,
        descriptionVisible: false,
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
            this.leavingList = await getLeavingList(this.numName)
          
        },
        
        //编辑课程,显示对话框
       async confirmA(item: LeaveItem) {
            this.form = item
            const res = await leavingSaveA(this.form)
            if (res.code == 0) {
                message(this, '保存成功')
                this.query()
            } else {
                message(this, res.msg)
            }
        },
        async confirmB(item: LeaveItem) {
            this.form = item
            const res = await leavingSaveB(this.form)
            if (res.code == 0) {
                message(this, '保存成功')
                this.query()
            } else {
                message(this, res.msg)
            }
        },
        //关闭对话框
        close() {
            this.query();
            this.dialogVisible = false;
        },
        closeClassTime() {
            this.classTimeVisible = false;
        },
        handleClose(done: () => void) {
            done();
        },
    }
})
</script>
<style></style>
