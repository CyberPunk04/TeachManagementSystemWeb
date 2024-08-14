<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>
      <div class="base_title">学生管理</div>
    </div>
    <div class="base_query_oneLine">
      <div class="query_left">
        <button class="commButton" @click="addItem()">添加</button>
      </div>
      <div class="query_right">
        <span style="margin-top: 5px">学号或姓名</span>
        <input type="text" v-model="numName" style="margin-left: 10px; width: 230px" />
        <button style="margin-left: 5px" class="commButton" @click="doQuery()">查询</button>
        <el-button class="commButton" @click="doExport()">导出</el-button>
      </div>
    </div>

    <div class="table-content">
      <el-table
        :data="studentList.slice((pagination.currentPage - 1) * pagination.pageSize, pagination.currentPage * pagination.pageSize)"
        :header-cell-style="{
          color: '#2E2E2E',
          fontSize: '14px',
          fontWeight: '400',
          background: 'rgb(242,242,242)',
        }" :row-style="{ height: '60px' }" :cell-style="{ padding: '2px' }" style="width: 100%">
        <el-table-column label="序号" fixed="left" width="50" color="black">
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="学号" color="black" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.num }}
          </template>
        </el-table-column>
        <el-table-column label="姓名" color="black" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column label="院系" color="black" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.dept }}
          </template>
        </el-table-column>
        <el-table-column label="专业" color="black" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.major }}
          </template>
        </el-table-column>
        <el-table-column label="班级" color="black" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.className }}
          </template>
        </el-table-column>
        <el-table-column
          label="证件号码"
          color="black"
          align="center"
          width="100"
        >
          <template v-slot="scope">
            {{ scope.row.card }}
          </template>
        </el-table-column>
        <el-table-column label="性别" color="black" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.genderName }}
          </template>
        </el-table-column>
        <el-table-column label="出生日期" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.birthday }}
          </template>
        </el-table-column>
        <el-table-column label="邮箱" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.email }}
          </template>
        </el-table-column>
        <el-table-column label="电话" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
        <el-table-column label="地址" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.address }}
          </template>
        </el-table-column>
        <el-table-column label="操作" color="black" align="center" width="260">
          <template v-slot="scope">
            <el-button class="commButton" @click="editItem(scope.row)"
              >编辑</el-button
            >
            <el-button
              class="commButton"
              @click="familyMember(scope.row.studentId)"
              >家庭成员</el-button
            >
            <el-button
              class="commButton"
              @click="deleteItem(scope.row.studentId)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pagin">
      <el-pagination background style="margin-top: 15px" :current-page="pagination.currentPage"
        :page-size="pagination.pageSize" :page-sizes="[5, 10]" layout="total, sizes, prev, pager, next, jumper"
        :total="studentList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

  </div>


  <el-dialog title="学生添加修改" :width="350" :height="500" :before-close="handleClose" v-model="dialogVisible"
    :append-to-body='true'>
    <el-form :model="form" label-width="80px" ref="formRef">
      <el-form-item label="学号" prop="num" :rules="[{ required: true,validator: valinum, trigger: 'blur' }]">
        <el-input v-model="form.num" />
      </el-form-item>
      <el-form-item label="姓名" prop="name" :rules="[{ required: true, message: '请输入学生姓名', trigger: 'blur' }]">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="院系" prop="dept" :rules="[{ required: true, message: '请输入学生所在学院', trigger: 'blur' }]">
        <el-input v-model="form.dept" />
      </el-form-item>
      <el-form-item label="专业" prop="major" :rules="[{ required: true, message: '请输入学生所在专业', trigger: 'blur' }]">
        <el-input v-model="form.major" />
      </el-form-item>
      <el-form-item label="班级" prop="className" :rules="[{ required: true, message: '请输入学生所在班级', trigger: 'blur' }]">
        <el-input v-model="form.className" />
      </el-form-item>
      <el-form-item label="证件号码"  prop="card" :rules="[{ required: true,validator: valicard, trigger: 'blur' }]">
        <el-input v-model="form.card" />
      </el-form-item>
      <el-form-item label="性别" prop="gender" :rules="[{ required: true, message: '请选择学生性别', trigger: 'change' }]">
        <el-select v-model="form.gender" placeholder="请选择性别" style="width: 97%">
              <el-option v-for="item in genderList" :key="item.value" :label="item.title" :value="item.value">
              </el-option>
            </el-select>
      </el-form-item>
      <el-form-item label="出生日期">
            <el-date-picker
              v-model="birthday"
              type="date"
              style="width: 100%"
              placeholder="选择出生日期"
            />
      </el-form-item>
      <el-form-item label="邮箱"  prop="email" :rules="[{ required: true, message: '请输入学生邮箱', trigger: 'blur' }]">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="电话" prop="phone" :rules="[{ required: true,validator: valiphone, trigger: 'blur' }]">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="地址" prop="address" :rules="[{ required: true, message: '请输入学生地址', trigger: 'blur' }]">
        <el-input v-model="form.address" />
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
</template>

<script lang="ts">
import {type OptionItem,type StudentItem } from '~/models/general'
import { defineComponent } from 'vue'
import { getDictionaryOptionItemList } from "~/services/systemServ";
import {getStudentList,studentDelete,studentEditSave } from '~/services/personServ'
import { message, messageConform } from '~/tools/messageBox'
import { formatDate } from "~/tools/comMethod";
import router from "~/router";
import { downloadPost } from "~/services/genServ";
export default defineComponent({
  data: () => ({
    numName: '',

    studentId: null as number|null,
    studentList: [] as StudentItem[],
    birthday: new Date(),
    deleteId: -1,
    gender: {} as OptionItem,
    genderList: [] as OptionItem[],
    form: {} as StudentItem,
    pagination: {
      currentPage: 1,
      pageSize: 10,
      total: 0,
    },
    dialogVisible: false,

  }),
  //初始加载一次
  async created() {
    this.query()
    this.genderList = await getDictionaryOptionItemList("XBM");

  },

  methods: {
    valinum(rule: any,value: any ,callback: (error?: Error) => void) {
      const regex = /^\d+$/;
      if (!regex.test(value)||this.form.num.length!=12) {
        callback(new Error('学号必须为数字,且为12位'));
      } else {
        callback();
      }
    },
    valicard(rule: any,value: any ,callback: (error?: Error) => void) {
      const regex = /^\d+$/;
      if (!regex.test(value)||this.form.card.length!=18) {
        callback(new Error('请输入完整身份证号'));
      } else {
        callback();
      }
    },
    valiphone(rule: any,value: any ,callback: (error?: Error) => void) {
      const regex = /^\d+$/;
      if (!regex.test(value)||this.form.phone.length!=11) {
        callback(new Error('请输入完整手机号'));
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
      this.studentList = await getStudentList(this.numName)

    },
    //添加学生,显示对话框
    addItem() {
      this.form = {} as StudentItem
      this.dialogVisible = true;
    },
    //编辑课程,显示对话框
    editItem(item: StudentItem) {
      this.form = item
      this.dialogVisible = true;
    },
    //关闭对话框
    close() {
      this.dialogVisible = false;
    },
    //确认对话框
    async confirm() {


      this.form.birthday = formatDate(this.birthday);
      const res = await studentEditSave(this.form.studentId,this.form)
      if (res.code == 0) {
        message(this, '保存成功')
        this.query()
      } else {
        message(this, res.msg)
      }
      this.close()

    },
    //查询家庭信息
    familyMember(studentId: number) {
      router.push({
        name: "FamilyMember",
        query: { studentId: studentId },
      });
    },
    //删除学生
    async deleteItem(studentId: number) {
      const result = await messageConform('确认删除吗?')
      if (!result) {
        return
      }
      const res = await studentDelete(studentId)
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
        // 导出学生信息
        async doExport() {
      const res = await downloadPost(
        "/api/student/getStudentListExcl",
        "学生.xlsx",
        {
          numName: this.numName,
        }
      );
      if (res != 200) {
        message(this, "导出失败！");
      }
    },
  }
})
</script>
<style></style>
