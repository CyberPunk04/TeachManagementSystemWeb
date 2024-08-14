<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>
      <div class="base_title">课程管理</div>
    </div>
    <div class="base_query_oneLine">
      <div class="query_left">
        <button class="commButton" @click="addItem()">添加</button>
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
        <el-table-column label="教师姓名" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.teacher }}
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
        <el-table-column label="操作" color="black" align="center">
          <template v-slot="scope">
            <el-button class="commButton" @click="editItem(scope.row)">编辑</el-button>
            <el-button class="commButton" @click="deleteItem(scope.row.courseId)">删除</el-button>
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


  <el-dialog title="课程添加修改" :width="350" :height="500" :before-close="handleClose" v-model="dialogVisible"
    :append-to-body='true'>
    <el-form :model="form" label-width="80px" ref="formRef">
      <el-form-item label="课序号" prop="num" :rules="[{ required: true, message: '请输入课序号', trigger: 'blur' }]">
        <el-input v-model="form.num" />
      </el-form-item>
      <el-form-item label="课程名称" prop="name" :rules="[{ required: true, message: '请输入课程名称', trigger: 'blur' }]">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="学分" prop="credit" :rules="[{ validator: validateCredit, trigger: 'blur' }]">
        <el-input v-model="form.credit" />
      </el-form-item>

      <el-form-item label="教材名称" prop="coursePath" :rules="[{ required: true, message: '请输入教材名称', trigger: 'blur' }]">
        <el-input v-model="form.coursePath" />
      </el-form-item>
      <el-form-item label="前序课" prop="preCourseId" :rules="[{ required: true, message: '请选择前序课', trigger: 'change' }]">
        <el-select v-model="form.preCourseId">
          <el-option v-for="item in courseSelectList" :key="item.title" :value="item.id" :label="item.title"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="教师姓名" prop="teacherId" :rules="[{ required: true, message: '请选择教师姓名', trigger: 'change' }]">
        <el-select v-model="form.teacherId">
          <el-option v-for="item in teacherSelectList" :key="item.id" :value="item.id" :label="item.title"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="上课时间" prop="classTime" :rules="[{ validator: validateClassTime, trigger: 'blur' }]">
        <el-input v-model="form.classTime" />
      </el-form-item>
      <el-form-item label="开始周" prop="startWeek" :rules="[{ validator: validateWeek, trigger: 'blur' }]">
        <el-input v-model="form.startWeek" />
      </el-form-item>
      <el-form-item label="结束周" prop="endWeek" :rules="[{ validator: validateWeek, trigger: 'blur' }]">
        <el-input v-model="form.endWeek" />
      </el-form-item>
      <el-form-item label="课程类型" prop="type" :rules="[{ validator: validateCourseType, trigger: 'blur' }]">
        <el-input v-model="form.type" />
      </el-form-item>
      <el-form-item label="课容量" prop="much" :rules="[{ validator: validateCapacity, trigger: 'blur' }]">
        <el-input v-model="form.much" />
      </el-form-item>
      <el-form-item label="上课地点" prop="place" :rules="[{ required: true, message: '请输入上课地点', trigger: 'blur' }]">
        <el-input v-model="form.place" />
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
import { getCourseList, getTeacherList, courseDelete, courseSave, getCoursePageData } from '~/services/courseServ'
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
  }),
  //初始加载一次
  created() {
    this.query()
  },

  methods: {

    validateCredit(rule: any, value: any, callback: (error?: Error) => void) {
      const regex = /^\d+(\.\d{1})?$/;
      if (!regex.test(value)) {
        callback(new Error('学分必须是数字，最多一位小数'));
      } else {
        callback();
      }
    },

    validateClassTime(rule: any, value: any, callback: (error?: Error) => void) {
      const regex = /^周[一二三四五六日]第[一二三四五]节$/;
      if (!regex.test(value)) {
        callback(new Error('格式不正确，请按类似周一第一节输入'));
      } else {
        callback();
      }
    },

    validateWeek(rule: any, value: any, callback: (error?: Error) => void) {
      const regex = /^[1-9]\d*$/;
      if (!regex.test(value)) {
        callback(new Error('周数必须是正整数'));
      } else {
        const startWeek = parseInt(this.form.startWeek.toString());
        const endWeek = parseInt(this.form.endWeek.toString());

        if (isNaN(startWeek) || isNaN(endWeek)) {
          callback(new Error('请输入有效的周数'));
        } else if (endWeek <= startWeek) {
          callback(new Error('结束周必须大于开始周'));
        }
        else {
          callback();
        }
      }
    },

    validateCourseType(rule: any, value: any, callback: (error?: Error) => void) {
      const validTypes = ['必修', '选修', '任选'];
      if (!validTypes.includes(value)) {
        callback(new Error('课程类型须为必修、选修或任选之一'));
      } else {
        callback();
      }
    },

    validateCapacity(rule: any, value: any, callback: (error?: Error) => void) {
      const regex = /^[1-9]\d*$/;
      if (!regex.test(value)) {
        callback(new Error('课容量必须是正整数'));
      } else {
        callback();
      }
    },

    //前序课选择的列表
    makeSelectCourseList() {
      this.courseSelectList = []
      for (let i = 0; i < this.courseList.length; i++) {
        const item = this.courseList[i]
        this.courseSelectList.push({
          id: item.courseId,
          value: item.num,
          title: item.num + '-' + item.name
        })
      }
    },
    //教师选择列表
    makeSelectteacherList() {
      this.teacherSelectList = []
      for (let i = 0; i < this.teacherList.length; i++) {
        const tec = this.teacherList[i]
        this.teacherSelectList.push({
          id: tec.teacherId,
          value: tec.num,
          title: tec.name
        })
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
      this.courseList = await getCourseList(this.numName)
      this.teacherList = await getTeacherList(this.tecName)
      this.makeSelectCourseList()
      this.makeSelectteacherList()
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
      this.query();
      this.dialogVisible = false;
    },
    closeClassTime() {
      this.classTimeVisible = false;
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
