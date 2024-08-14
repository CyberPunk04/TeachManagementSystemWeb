<template>
    <div class="base_form">
        <div class="base_header">
            <div class="blue_column"></div>
            <div class="base_title">成绩录入</div>
        </div>

        <div class="base_query_oneLine">
            <div class="query_left">
            </div>
            <div class="query_right">
                <span style="margin-top: 5px">学生姓名或课程名称</span>
                <input type="text" v-model="numName" style="margin-left: 10px; width: 230px" />
                <button style="margin-left: 5px" class="commButton" @click="query()">查询</button>
            </div>
        </div>

        <!--
    <div class="table_center" style="margin-top: 5px">
      <table class="content">
        <tr class="table_th">
          <td>学号</td>
          <td>姓名</td>
          <td>班级</td>
          <td>课程号</td>
          <td>课程名</td>
          <td>学分</td>
          <td>成绩</td>
          <td>操作</td>
        </tr>
        <tr v-for="item in scoreList" :key="item.scoreId">
          <td>{{ item.studentNum }}</td>
          <td>{{ item.studentName }}</td>
          <td>{{ item.className }}</td>
          <td>{{ item.courseNum }}</td>
          <td>{{ item.courseName }}</td>
          <td>{{ item.credit }}</td>
          <td>{{ item.mark }}</td>
          <td>
            <button class="table_edit_button" @click="editItem(item)">
              编辑
            </button>
            <button class="table_delete_button" @click="deleteItem(item.scoreId)">
              删除
            </button>
          </td>
        </tr>
      </table>
    </div>-->

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
                        {{ scope.row.num }}
                    </template>
                </el-table-column>
                <el-table-column label="姓名" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.student }}
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
                        {{ scope.row.course }}
                    </template>
                </el-table-column>
                <el-table-column label="录入状态" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.answer }}
                    </template>
                </el-table-column>
                <el-table-column label="分数" color="black" align="center">
                    <template v-slot="scope">
                        {{ scope.row.mark }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" color="black" align="center">
                    <template v-slot="scope">
                        <el-button class="commButton" @click="editItem(scope.row)">{{ scope.row.answer == '已录入分数' ? '修改' :
                            '录入' }} </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="paginationClass">
            <el-pagination background style="margin-top: 15px" :current-page="pagination.currentPage"
                :page-size="pagination.pageSize" :page-sizes="[5, 10]" layout="total, sizes, prev, pager, next, jumper"
                :total="scoreList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
    </div>

    <el-dialog title="成绩录入" :width="350" :height="500" :before-close="handleClose" v-model="editVisible"
        :append-to-body='true'>
        <el-form :model="editedItem" label-width="80px" ref="formRef">
            <el-form-item label="学号" prop="num">
                <el-input v-model="editedItem.num" :disabled="true" />
            </el-form-item>
            <el-form-item label="学生姓名" prop="student">
                <el-input v-model="editedItem.student" :disabled="true" />
            </el-form-item>
            <el-form-item label="课程" prop="course">
                <el-input v-model="editedItem.course" :disabled="true" />
            </el-form-item>
            <el-form-item label="成绩" prop="mark" :rules="[{ validator: validateMark, trigger: 'blur' }]">
                <el-input v-model="editedItem.mark" />
            </el-form-item>
        </el-form>
        <el-row :gutter="80" class="dialog-buttons">
            <el-col :span="12">
                <el-button @click="close">取消</el-button>
            </el-col>
            <el-col :span="12">
                <el-button type="primary" @click="trySave">确认</el-button>
            </el-col>
        </el-row>
    </el-dialog>

    <el-dialog title="当前时期无法录入成绩" :width="350" :height="500" :before-close="handleClose" v-model="powerVisible"
        :append-to-body='true'></el-dialog>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import { type OptionItem, type TeacherScoreItem } from "~/models/general";
import { getPower, teacherScoreSave, getTeacherScoreList } from "~/services/courseServ";
import { message, } from "~/tools/messageBox";
export default defineComponent({
    data: () => ({
        scoreList: [] as TeacherScoreItem[],
        studentId: null,
        courseId: null,
        editedItem: {} as TeacherScoreItem,
        studentList: [] as OptionItem[],
        courseList: [] as OptionItem[],
        deleteId: -1,
        powerList: [],
        pagination: {
            currentPage: 1,
            pageSize: 10,
            dataTotal: 0,
        },
        dialogVisible: false,
        editVisible: false,
        value1: "",
        numName: '',
        powerVisible: false,

    }),
    computed: {},
    created() {
        this.initialize();
    },
    methods: {
        // 初始化,获取学生选择项列表和课程选择项列表
        async initialize() {
            this.powerList = await getPower(null);
            this.scoreList = await getTeacherScoreList(this.numName);
            //this.power = this.powerList[0].power;
        },
        // 查询
        async query() {
            this.scoreList = await getTeacherScoreList(this.numName);
        },
        //每个页面最多行
        handleSizeChange(val: number) {
            this.pagination.pageSize = val;
        },
        // 当前页
        handleCurrentChange(val: number) {
            this.pagination.currentPage = val;
        },
        validateMark(rule: any, value: any, callback: (error?: Error) => void) {
            const regex = /^[0-9]\d*$/;
            const intValue = parseInt(value);

            if (!regex.test(value) || intValue < 0 || intValue > 100) {
                callback(new Error('须为0到100之间的正整数'));
            } else {
                callback();
            }
        },
        trySave() {
            const formRef = this.$refs.formRef as unknown as { validate: (callback: (valid: boolean) => void) => void };

            // 在保存之前进行验证
            formRef.validate((valid) => {
                if (valid) {
                    // 验证通过，执行保存操作
                    this.confirm();
                } else {
                    // 验证失败，不执行保存操作
                    message(this, '请检查输入的错误');
                }
            });
        },
        // 编辑成绩,显示成绩修改对画框
        editItem(item: TeacherScoreItem) {
            // if (this.powerList[0].power == 1) {
            this.editedItem = item;
            this.editVisible = true;
            //} else {
            //this.powerVisible = true;
            //}
        },
        // 关闭成绩修改对话框
        close() {
            this.editVisible = false;
        },
        // 确认成绩修改对话框
        async confirm() {
            this.close();
            const res = await teacherScoreSave(
                this.editedItem.scoreId,
                this.editedItem.studentId,
                this.editedItem.courseId,
                this.editedItem.mark
            );
            if (res.code == 0) {
                message(this, "保存成功");
                this.query();
            } else {
                message(this, res.msg);
            }
        },

        handleClose(done: () => void) {
            done();
        },
    },
});
</script>
