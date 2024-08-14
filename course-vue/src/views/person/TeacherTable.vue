<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>
      <div class="base_title">教师管理</div>
    </div>
    <div class="base_query_oneLine">
      <div class="query_left">
        <button class="commButton" @click="addItem()">添加</button>



      </div>
      <div class="query_right">
        <span style="margin-top: 5px">教工号或姓名</span>
        <input type="text" v-model="numName" style="margin-left: 10px; width: 230px" />
        <button style="margin-left: 5px" class="commButton" @click="doQuery()">查询</button>

      </div>
    </div>

    <div class="table-content">
      <el-table
        :data="teacherList.slice((pagination.currentPage - 1) * pagination.pageSize, pagination.currentPage * pagination.pageSize)"
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
        <el-table-column label="工号" color="black" align="center" width="90">
          <template v-slot="scope">
            {{ scope.row.num }}
          </template>
        </el-table-column>
        <el-table-column label="姓名" color="black" align="center" width="90">
          <template v-slot="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column label="院系" color="black" align="center" width="90">
          <template v-slot="scope">
            {{ scope.row.dept }}
          </template>
        </el-table-column>
        <el-table-column label="职称" color="black" align="center" width="90">
          <template v-slot="scope">
            {{ scope.row.title }}
          </template>
        </el-table-column>
        <el-table-column label="学位" color="black" align="center" width="90">
          <template v-slot="scope">
            {{ scope.row.degree }}
          </template>
        </el-table-column>
        <el-table-column
          label="证件号码"
          color="black"
          align="center"
          width="90"
        >
          <template v-slot="scope">
            {{ scope.row.card }}
          </template>
        </el-table-column>
        <el-table-column label="性别" color="black" align="center" width="90">
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
        <el-table-column prop="studyDirection" label="研究方向">
          <template #default="scope">
            <el-button text type="primary" plain @click="showStudyDirection(scope.row)">
              详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="publications" label="论文著作">
          <template #default="scope">
            <el-button text type="primary" plain @click="showPublications(scope.row)">
              详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" color="black" align="center" width="175">
          <template v-slot="scope">
            <el-button class="commButton" @click="editItem(scope.row)"
              >详情编辑</el-button
            >
            <el-button
              class="commButton"
              @click="deleteItem(scope.row.teacherId)"
              >删除</el-button
            >

          </template>
        </el-table-column>
        
      </el-table>
    </div>

    <div class="pagin">
      <el-pagination background style="margin-top: 15px" :current-page="pagination.currentPage"
        :page-size="pagination.pageSize" :page-sizes="[5, 10]" layout="total, sizes, prev, pager, next, jumper"
        :total="teacherList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

  </div>


  <el-dialog title="教师添加修改" :width="350" :height="500" :before-close="handleClose" v-model="dialogVisible"
    :append-to-body='true'>
    <el-form :model="form" label-width="80px" ref="formRef">
      <el-form-item label="教工号" prop="num" :rules="[{ required: true,validator: valinum, trigger: 'blur' }]">
        <el-input v-model="form.num" />
      </el-form-item>
      <el-form-item label="姓名" prop="name" :rules="[{ required: true, message: '请输入教师姓名', trigger: 'blur' }]">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="院系" prop="dept" :rules="[{ required: true, message: '请输入教师所在学院', trigger: 'blur' }]">
        <el-input v-model="form.dept" />
      </el-form-item>
      <el-form-item label="职称" prop="title" :rules="[{ required: true,validator: valititle, trigger: 'blur' }]">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="学位" prop="digree" :rules="[{ required: true,validator: validegree, trigger: 'blur' }]">
        <el-input v-model="form.degree" />
      </el-form-item>
      <el-form-item label="证件号码"  prop="card" :rules="[{ required: true,validator: valicard, trigger: 'blur' }]">
        <el-input v-model="form.card" />
      </el-form-item>
      <el-form-item label="性别" prop="gender" :rules="[{ required: true, message: '请选择教师性别', trigger: 'change' }]">
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
      <el-form-item label="邮箱"  prop="email" :rules="[{ required: true, message: '请输入教师邮箱', trigger: 'blur' }]">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="电话" prop="phone" :rules="[{ required: true,validator: valiphone, trigger: 'blur' }]">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="地址" prop="address" :rules="[{ required: true, message: '请输入教师地址', trigger: 'blur' }]">
        <el-input v-model="form.address" />
      </el-form-item>
      <el-form-item label="研究方向" prop="studyDirection" :rules="[{ required: true, message: '请输入研究方向', trigger: 'blur' }]">
        <el-input
          type="textarea"
            :rows="2"
          placeholder="请输入内容"
           v-model="form.studyDirection">
          </el-input>
      </el-form-item>
      <el-form-item label="学术著作" prop="publications" :rules="[{ required: true, message: '请列举教师学术著作', trigger: 'blur' }]">
        <el-input
          type="textarea"
            :rows="2"
          placeholder="请输入内容"
           v-model="form.publications">
          </el-input>
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

  <el-dialog title="研究方向详情" :width="350" :height="500" :before-close="handleClose" v-model="studyDirectionVisible"
    :append-to-body='true'>
    <el-form :model="form" label-width="80px">
  
      <el-form-item label="研究方向">
        <el-input
          type="textarea"
            :rows="2"
          placeholder="暂无数据"
           v-model="form.studyDirection" :disabled="true">
          </el-input>
      </el-form-item>
    </el-form>
    <el-row :gutter="80" class="dialog-buttons">
      <el-col :span="12">
        <el-button @click="closeStudyDirection">关闭</el-button>
      </el-col>
    </el-row>
  </el-dialog>
  <el-dialog title="著作详情" :width="350" :height="500" :before-close="handleClose" v-model="publicationsVisible"
    :append-to-body='true'>
    <el-form :model="form" label-width="80px">
  
      <el-form-item label="学术著作">
        <el-input
          type="textarea"
            :rows="2"
          placeholder="暂无数据"
           v-model="form.publications" :disabled="true">
          </el-input>
      </el-form-item>
    </el-form>
    <el-row :gutter="80" class="dialog-buttons">
      <el-col :span="12">
        <el-button @click="closePublications">关闭</el-button>
      </el-col>
    </el-row>
  </el-dialog>

</template>

<script lang="ts">
import {type OptionItem,type TeacherItem } from '~/models/general'
import { defineComponent } from 'vue'
import { getDictionaryOptionItemList } from "~/services/systemServ";
import {getTeacherList,teacherDelete,teacherEditSave } from '~/services/personServ'
import { message, messageConform } from '~/tools/messageBox'
import { formatDate } from "~/tools/comMethod";
export default defineComponent({
  data: () => ({
    numName: '',

    teacherId: null as number|null,
    teacherList: [] as TeacherItem[],
    birthday: new Date(),
    deleteId: -1,
    gender: {} as OptionItem,
    genderList: [] as OptionItem[],
    form: {} as TeacherItem,
    pagination: {
      currentPage: 1,
      pageSize: 10,
      total: 0,
    },
    dialogVisible: false,
    studyDirectionVisible: false,
    publicationsVisible: false,
  }),
  //初始加载一次
  async created() {
    this.query()
    this.genderList = await getDictionaryOptionItemList("XBM");

  },

  methods: {
    valititle(rule: any, value: any, callback: (error?: Error) => void) {
      const validTypes = ['一级教授', '二级教授', '副教授','讲师','助教'];
      if (!validTypes.includes(value)) {
        callback(new Error('一级教授、二级教授、副教授、讲师、助教'));
      } else {
        callback();
      }
    },
    validegree(rule: any, value: any, callback: (error?: Error) => void) {
      const validTypes = ['博士', '硕士', '学士'];
      if (!validTypes.includes(value)) {
        callback(new Error('学士、硕士、博士'));
      } else {
        callback();
      }
    },
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
      this.teacherList = await getTeacherList(this.numName)

    },
    //添加学生,显示对话框
    addItem() {
      this.form = {} as TeacherItem
      this.dialogVisible = true;
    },
    //编辑课程,显示对话框
    editItem(item: TeacherItem) {
      this.form = item
      this.dialogVisible = true;
    },
    //关闭对话框
    close() {
      this.dialogVisible = false;
    },
    //确认对话框
    async confirm() {
      this.close()
      this.form.birthday = formatDate(this.birthday);
      const res = await teacherEditSave(this.form.teacherId,this.form)
      if (res.code == 0) {
        message(this, '保存成功')
        this.query()
      } else {
        message(this, res.msg)
      }
    },
    //展示研究方向
    showStudyDirection(item: TeacherItem) {
      this.form = item
      this.studyDirectionVisible = true;
    },
    //展示著作
    showPublications(item: TeacherItem) {
      this.form = item
      this.publicationsVisible = true;
    },
    //关闭对话
    closeStudyDirection() {
      this.studyDirectionVisible = false;
    },
        //关闭对话
    closePublications() {
      this.publicationsVisible = false;
    },
    //删除教师
    async deleteItem(teacherId: number) {
      const result = await messageConform('确认删除吗?')
      if (!result) {
        return
      }
      const res = await teacherDelete(teacherId)
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