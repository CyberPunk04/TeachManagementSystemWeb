<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>
      <div class="base_title">成绩管理</div>
    </div>

    <div class="base_query_oneLine">
      <div class="query_left">
        <button class="commButton" @click="addItem()">添加</button>
      </div>
      <div class="query_right">
        <!--<span style="margin-top: 5px;margin-right: 8px ;">教师录入权限：</span>
        <el-tooltip :content="value1" placement="top">
          <el-switch v-model="value1" @change="sendPower" active-color="#13ce66" inactive-color="#ff4949"
            active-value="教师可录入成绩" inactive-value="教师不可录入成绩">
          </el-switch>
        </el-tooltip>-->
        <span style="margin-top: 5px;margin-left: 20px;margin-right: 8px ;">学生</span>
        <select class="commInput" v-model="studentId">
          <option value="0">请选择...</option>
          <option v-for="item in studentList" :key="item.id" :value="item.id">
            {{ item.title }}
          </option>
        </select>
        <span style="margin-top: 5px;margin-left: 15px;margin-right: 8px">课程</span>
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
        <el-table-column label="操作" color="black" align="center">
          <template v-slot="scope">
            <el-button class="commButton" @click="editItem(scope.row)">修改</el-button>
            <el-button class="commButton" @click="deleteItem(scope.row.scoreId)">删除</el-button>
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

  <el-dialog title="成绩添加修改" :width="350" :height="500" :before-close="handleClose" v-model="dialogVisible"
    :append-to-body='true'>
    <el-form :model="editedItem" label-width="80px" ref="formRefA">
      <el-form-item label="学生" prop="studentId" :rules="[{ required: true, message: '请选择学生', trigger: 'change' }]">
        <el-select v-model="editedItem.studentId" placeholder="请选择" ref="selectRef">
          <el-option v-for="item in studentList" :key="item.id" :label="item.title" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课程" prop="courseId" :rules="[{ required: true, message: '请选择课程', trigger: 'change' }]">
        <el-select v-model="editedItem.courseId" placeholder="请选择" ref="selectRef">
          <el-option v-for="item in courseList" :key="item.id" :label="item.title" :value="item.value">
          </el-option>
        </el-select>
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
        <el-button type="primary" @click="trySaveA">确认</el-button>
      </el-col>
    </el-row>
  </el-dialog>
  <el-dialog title="成绩添加修改" :width="350" :height="500" :before-close="handleClose" v-model="editVisible"
    :append-to-body='true'>
    <el-form :model="editedItem" label-width="80px" ref="formRefB">
      <el-form-item label="学生" prop="studentId">
        <el-select v-model="editedItem.studentId" placeholder="请选择" ref="selectRef" :disabled="true">
          <el-option v-for="item in studentList" :key="item.id" :label="item.title" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课程" prop="courseId">
        <el-select v-model="editedItem.courseId" placeholder="请选择" ref="selectRef" :disabled="true">
          <el-option v-for="item in courseList" :key="item.id" :label="item.title" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="成绩" prop="mark" :rules="[{ validator: validateMarkB, trigger: 'blur' }]">
        <el-input v-model="editedItem.mark" />
      </el-form-item>
    </el-form>
    <el-row :gutter="80" class="dialog-buttons">
      <el-col :span="12">
        <el-button @click="close">取消</el-button>
      </el-col>
      <el-col :span="12">
        <el-button type="primary" @click="trySaveB">确认</el-button>
      </el-col>
    </el-row>
  </el-dialog>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import { ref } from 'vue';
import {
  getScoreList,
  getStudentItemOptionList,
  getCourseItemOptionList,
  scoreSave,
  scoreDelete,
} from "~/services/teachingServ";
import { type OptionItem, type ScoreItem } from "~/models/general";
import { getScorePageData, teacherPowerSave, scoreNoSave } from "~/services/courseServ";
import { message, messageConform } from "~/tools/messageBox";
export default defineComponent({
  data: () => ({
    scoreList: [] as ScoreItem[],
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
    editVisible: false,
    value1: "",
    power: 0,
  }),
  computed: {},
  created() {
    this.initialize();
  },

  methods: {
    // 初始化,获取学生选择项列表和课程选择项列表
    async initialize() {
      this.scoreList = await getScoreList(this.studentId, this.courseId);
      this.studentList = await getStudentItemOptionList();
      this.courseList = await getCourseItemOptionList();
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
    validateMark(rule: any, value: any, callback: (error?: Error) => void) {
      const regex = /^[0-9]\d*$/;
      const intValue = parseInt(value);

      if (!regex.test(value) || intValue < 0 || intValue > 100) {
        callback(new Error('须为0到100之间的正整数'));
      } else {
        callback();
      }
    },
    validateMarkB(rule: any, value: any, callback: (error?: Error) => void) {
      const regex = /^[0-9]\d*$/;
      const intValue = parseInt(value);

      if (!regex.test(value) || intValue < 0 || intValue > 100) {
        callback(new Error('须为0到100之间的正整数'));
      } else {
        callback();
      }
    },

    //每个页面最多行
    handleSizeChange(val: number) {
      this.pagination.pageSize = val;
    },
    // 当前页
    handleCurrentChange(val: number) {
      this.pagination.currentPage = val;
    },

    // 添加成绩,显示成绩修改对画框
    addItem() {
      this.editedItem = {} as ScoreItem;
      this.dialogVisible = true;
    },
    // 编辑成绩,显示成绩修改对画框
    editItem(item: ScoreItem) {
      this.editedItem = item;
      this.editVisible = true;
    },
    // 关闭成绩修改对话框
    close() {
      this.dialogVisible = false;
      this.editVisible = false;
      this.initialize();
    },
    trySaveA() {
      const formRef = this.$refs.formRefA as unknown as { validate: (callback: (valid: boolean) => void) => void };

      // 在保存之前进行验证
      formRef.validate((valid) => {
        if (valid) {
          // 验证通过，执行保存操作
          this.confirmA();
        } else {
          // 验证失败，不执行保存操作
          message(this, '请检查输入的错误');
        }
      });
    },
    trySaveB() {
      const formRef = this.$refs.formRefB as unknown as { validate: (callback: (valid: boolean) => void) => void };

      // 在保存之前进行验证
      formRef.validate((valid) => {
        if (valid) {
          // 验证通过，执行保存操作
          this.confirmB();
        } else {
          // 验证失败，不执行保存操作
          message(this, '请检查输入的错误');
        }
      });
    },
    // 确认成绩修改对话框
    async confirmA() {
      this.close();
      const res = await scoreNoSave(
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
    async confirmB() {
      this.close();
      const res = await scoreSave(
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
    // 删除成绩
    async deleteItem(scoreId: number) {
      const result = await messageConform("确认删除吗?");
      if (!result) {
        return;
      }
      const res = await scoreDelete(scoreId);
      if (res.code == 0) {
        message(this, "删除成功");
        this.query();
      } else {
        message(this, res.msg);
      }
    },
    handleClose(done: () => void) {
      done();
    },
    async sendPower() {
      /*if (this.value1 = "教师可录入成绩") {
        this.power = 1;
      } else {
        this.power = 0;
      }*/
      const res = await teacherPowerSave(this.value1);
    }
  },
});
</script>
