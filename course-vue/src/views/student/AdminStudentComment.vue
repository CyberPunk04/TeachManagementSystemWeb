<template>
    <div class="base_form">
      <div class="base_header">
        <div class="blue_column"></div>
        <div class="base_title">互评管理</div>
      </div>
      <div class="base_query_oneLine">
        <div class="query_right">
          <span style="margin-top: 5px">学号或姓名</span>
          <input type="text" v-model="numName" style="margin-left: 10px; width: 230px" />
          <button style="margin-left: 5px" class="commButton" @click="doQuery()">查询</button>

        </div>
      </div>
  
      <div class="table-content">
        <el-table
          :data="studentCommentList.slice((pagination.currentPage - 1) * pagination.pageSize, pagination.currentPage * pagination.pageSize)"
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
          <el-table-column label="评论学生" color="black" align="center" width="200">
            <template v-slot="scope">
              {{ scope.row.commenter.name}}
            </template>
          </el-table-column>
          <el-table-column label="被评论学生" color="black" align="center" width="200">
            <template v-slot="scope">
              {{ scope.row.commented.name }}
            </template>
          </el-table-column>
          <el-table-column label="评分" color="black" align="center" width="100">
            <template v-slot="scope">
              {{ scope.row.value }}
            </template>
          </el-table-column>
          <el-table-column label="标签" color="black" align="center" width="250">
            <template v-slot="scope">
              {{ scope.row.tags }}
            </template>
          </el-table-column>

          <el-table-column prop="comment" label="评论内容">
          <template #default="scope">
            <el-button text type="primary" plain @click="showClassTime(scope.row)">
              详情
            </el-button>
          </template>
        </el-table-column>
          
          <!-- <el-table-column label="详细评论" color="black" align="center">
            <template #default="scope">
              <el-button text type="primary" plain @click="showDescription(scope.row.comment); descriptionVisible = true">
                详情
              </el-button>
            </template> -->
          <!-- </el-table-column> -->
          <el-table-column label="操作" color="black" align="center" width="250">
            <template v-slot="scope">

              <el-button class="commButton" @click="deleteItem(scope.row.studentCommentId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
  
      <div class="pagin">
        <el-pagination background style="margin-top: 15px" :current-page="pagination.currentPage"
          :page-size="pagination.pageSize" :page-sizes="[5, 10]" layout="total, sizes, prev, pager, next, jumper"
          :total="studentCommentList.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
  
    </div>
    <el-dialog title="评论详情" :width="350" :height="500" :before-close="handleClose" v-model="dialogVisible"
    :append-to-body='true'>
    <el-form :model="form" label-width="80px">
        <el-form-item label="评论学生学号">
        <el-input v-model="form.commenter.num" :disabled="true"/>
      </el-form-item>
      <el-form-item label="评论学生">
        <el-input v-model="form.commenter.name" :disabled="true"/>
      </el-form-item>
      <el-form-item label="被评论学生学号">
        <el-input v-model="form.commented.num" :disabled="true"/>
      </el-form-item>
      <el-form-item label="被评论学生">
        <el-input v-model="form.commented.name" :disabled="true"/>
      </el-form-item>
      <el-form-item label="评分">
        <el-input v-model="form.value" :disabled="true"/>
      </el-form-item>

      <el-form-item label="标签">
        <el-input
          type="textarea"
            :rows="2"
          placeholder="暂无数据"
           v-model="form.tags" :disabled="true">
          </el-input>
      </el-form-item>
      <el-form-item label="评论内容">
        <el-input
          type="textarea"
            :rows="4"
          placeholder="暂未有数据"
           v-model="form.comment" :disabled="true">
          </el-input>
      </el-form-item>
    </el-form>
    <el-row :gutter="80" class="dialog-buttons">
      <el-col :span="12">
        <el-button  @click="confirm">关闭</el-button>
      </el-col>
    </el-row>
  </el-dialog>

  

  </template>
  
  <script lang="ts">
  import { type StudentCommentItem, type OptionItem,type StudentItem} from '~/models/general'
  import { defineComponent } from 'vue'
  import { getStudentCommentList,studentCommentDelete,getStudentComments } from '~/services/teachingServ'
  import { message, messageConform } from '~/tools/messageBox'
import { number } from 'echarts'
  export default defineComponent({
    data: () => ({
      numName: '',
      commentedId: number||null,
      studentCommentList: [] as StudentCommentItem[],
      student:[] as StudentItem [],
      deleteId: -1,
      form: {} as StudentCommentItem,
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
      dialogVisible: false,

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
      async doQuery() {
        this.pagination.currentPage = 1;
        this.studentCommentList = await getStudentComments(this.numName)
      },
      // 分页请求
      handleChangePage(val: number) {
        this.pagination.currentPage = val;
        this.query();
      },
      // 获取分页数据
      async query() {
        this.studentCommentList = await getStudentCommentList();
        console.log(this.studentCommentList);
      },
      editItem(item: StudentCommentItem) {
      this.form = item
      this.dialogVisible = true;
    },
    //直接关闭，管理员只能删除查看，不能修改内容
    confirm() {
      this.dialogVisible = false;
    },
      //删除评论
      async deleteItem(studentCommentId: number) {
        const result = await messageConform('确认删除吗?')
        if (!result) {
          return
        }
        const res = await studentCommentDelete(studentCommentId)
        console.log(res);
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
      showClassTime(item: StudentCommentItem) {
      this.form = item
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
  