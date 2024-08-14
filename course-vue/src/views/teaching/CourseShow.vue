
<template>
    <div class="base_form">
        <div class="left-bar">
            <el-card class="left-card">
                <div slot="header" class="clearfix">
                    <span style="font-size: 28px;">我的课表</span>
                </div>
                <div class="courseshow-table" >
                    <el-table :data="courseChooseList" style="width: 100%;font-size: 18px;height: 100%;" :row-style="{ height: '80px' }" max-height="660">
                        <el-table-column type="expand">
                            <template v-slot="props">
                                <el-form label-position="left" inline class="demo-table-expand">
                                    <el-form-item label="开课周：">
                                        <span style="font-size: 18px;">{{ props.row.startWeek }}</span>
                                    </el-form-item>
                                    <el-form-item label="结课周：">
                                        <span style="font-size: 18px;">{{ props.row.endWeek }}</span>
                                    </el-form-item>
                                    <el-form-item label="教师：">
                                        <span style="font-size: 18px;">{{ props.row.teacher }}</span>
                                    </el-form-item>
                                </el-form>
                            </template>
                        </el-table-column>
                        <el-table-column label="课程" prop="name">
                        </el-table-column>
                        <el-table-column label="上课时间" prop="classTime">
                        </el-table-column>
                        <el-table-column label="上课地点" prop="place">
                        </el-table-column>
                    </el-table>

                </div>
            </el-card>
        </div>
        <div class="right-bar">
            <div>
                <el-calendar v-model="value" style="width: 100%">
                </el-calendar>
            </div>
        </div>
    </div>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import { ref } from 'vue';
import { type OptionItem, type ScoreItem, type CourseShowItem, type CourseItem } from "~/models/general";
import {  getCourseShowList, getCourseSelectionList } from "~/services/courseServ";
import { message, messageConform } from "~/tools/messageBox";
export default defineComponent({
    data: () => ({
        scoreList: [] as ScoreItem[],
        courseShowList: [] as CourseShowItem[],
        courseChooseList: [] as CourseItem[],
        studentId: null,
        courseId: null,
        editedItem: {} as ScoreItem,
        studentList: [] as OptionItem[],
        courseList: [] as OptionItem[],
        deleteId: -1,
        pagination: {
            currentPage: 1,
            pageSize: 10,
            dataTotal: 0,
        },
        dialogVisible: false,
        value1: "",
        power: 0,
        value: new Date(),
    }),
    computed: {},
    created() {
        this.initialize();
    },

    methods: {
        async initialize() {
            this.courseShowList = await getCourseShowList(null);
            this.courseChooseList = await getCourseSelectionList(null)
        },
    },
});
</script>
<style>
.left-bar {
    float: left;
    width: 55%;
    height: 100%;
    position: relative;
    overflow: auto;
    padding: 20px;
}

.right-bar {
    margin-top: 10px;
    margin-right: 50px;
    
    float: right;
    right: 10px;
    left: 10px;
    width: 38%;
    height: 100%;
    position: relative;
    overflow: auto;
    padding: 20px;
}


.el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
}

.left-card {
    height: 100%;
    position: relative;
}

.clearfix {
    height: 10%;
    padding: 10px;
}

.demo-table-expand {
    font-size: 0;
}

.demo-table-expand label {
    width: 90px;
    color: #99a9bf;
}

.demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
}

.courseshow-table>>>.el-table__row>td {
    /* 去除表格线 */
    border: none;
}

.courseshow-table>>>.el-table th.is-leaf {
    /* 去除上边框 */
    border: none;
}

.courseshow-table>>>.el-table::before {
    /* 去除下边框 */
    height: 0;
}


</style>
