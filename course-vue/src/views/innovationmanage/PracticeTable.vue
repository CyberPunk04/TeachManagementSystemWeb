<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>

      <div class="base_title">实践活动</div>
    </div>

    <div class="base_query_oneLine">
      <div class="query_left">
        <template v-if="isShowView()">
          <button class="commButton" @click="addItem()">添加</button>
        </template>
      </div>
      <div class="query_right">
        <span style="margin-top: 5px">实践活动名称</span>
        <input type="text" v-model="numName" style="margin-left: 10px; width: 230px" />
        <button style="margin-left: 5px" class="commButton" @click="query()">查询</button>
      </div>
    </div>
<!--    <div class="table_center" style="margin-top: 5px">
      <table class="content">
        <tr class="table_th">
          <td>实践活动名称</td>
          <td>学生姓名</td>
          <td>实践活动时间</td>
          <td>实践活动内容</td>
          &lt;!&ndash;          <td>前序课</td>&ndash;&gt;
          <template v-if="isShowView()">
            <td>操作</td>
          </template>
        </tr>
        <tr v-for="item in practiceList" :key="item.practiceId">
          <td>{{ item.practiceName }}</td>
          <td>{{ item.student }}</td>
          <td>{{ item.practiceTime }}</td>
          <td>{{ item.practiceContent }}</td>
          &lt;!&ndash;          <td>{{ item.preCourse }}</td>&ndash;&gt;
          <template v-if="isShowView()">
            <td>
              <button class="table_edit_button" @click="editItem(item)">编辑</button>
              <button class="table_delete_button" @click="deleteItem(item.practiceId)">删除</button>
            </td>
          </template>
        </tr>
      </table>
    </div>-->
    <div class="table-content">
      <el-table :data="practiceList" :header-cell-style="{
        color: '#2E2E2E',
        fontSize: '15px',
        fontWeight: '400',
        background: 'rgb(242,242,242)',
      }" :row-style="{ height: '60px' }" :cell-style="{ padding: '2px' }" style="width: 100%">
        <!-- <el-table-column label="序号" fixed="left" width="50" color="black">
            <template v-slot="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>-->
        <!--
          <td>实践活动名称</td>
          <td>学生姓名</td>
          <td>实践活动时间</td>
          <td>实践活动内容</td>-->
        <el-table-column label="实践活动名称" color="black" align="center" >
          <template v-slot="scope">
            {{ scope.row.practiceName }}
          </template>
        </el-table-column>
        <el-table-column label="学生姓名" color="black" align="center" >
          <template v-slot="scope">
            {{ scope.row.student }}
          </template>
        </el-table-column>
        <!--        <el-table-column label="学生班级" color="black" align="center" >
                  <template v-slot="scope">
                    {{ scope.row.className }}
                  </template>
                </el-table-column>-->
        <el-table-column label="实践活动时间" color="black" align="center" width="200" >
          <template v-slot="scope">
            {{ scope.row.practiceTime }}
          </template>
        </el-table-column>
        <el-table-column label="实践活动内容" color="black" align="center" >
          <template v-slot="scope">
            {{ scope.row.practiceContent }}
          </template>
        </el-table-column>
        <template v-if="isShowView()">
        <el-table-column label="操作" color="black" align="center" width="260">
          <template v-slot="scope">
            <el-button class="commButton" @click="editItem(scope.row)">编辑</el-button>
            <el-button class="commButton" @click="deleteItem(scope.row.practiceId)">删除</el-button>
          </template>
        </el-table-column>
        </template>
      </el-table>
      <!-- 分页区域 -->
      <div class="pagin">
        <el-pagination background style="margin-top: 15px" :total="pagination.dataTotal"
                       :current-page="pagination.currentPage" :page-size="pagination.pageSize" @current-change="handleChangePage"
                       layout="total, prev, pager, next, jumper" />
      </div>
    </div>
  </div>
<!--  <dialog id="favDialog" onclose="close()"
    style="position: absolute; top: 300px; left: 300px; width: 382px; height: 300px">
    <div class="base_title">实践添加修改对话框</div>
    <div class="dialog-div" style="margin-top: 5px">
      <table class="content">
        <tr>
          <td colspan="1" style="text-align: right">学号</td>
          <td colspan="1">
            <select style="width: 100%" class="commInput" v-model="form.num">
              <option value="0">请选择...</option>
              <option v-for="item in studentList" :key="item.id" :value="item.value">
                {{ item.title }}
              </option>
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="1" style="text-align: right">实践活动名称</td>
          <td colspan="1"><input v-model="form.practiceName" style="width: 97%" /></td>
        </tr>
        <tr>
          <td colspan="1" style="text-align: right">实践活动时间</td>
          <td colspan="1">
            <el-date-picker v-model="form.practiceTime" type="datetime" size="small" placeholder="请选择讲座时间"
              format="YYYY/MM/DD hh:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
          </td>
        </tr>
        <tr>
          <td colspan="1" style="text-align: right">实践活动内容</td>
          <td colspan="1">
            <input v-model="form.practiceContent" style="width: 97%" />
          </td>
        </tr>
        &lt;!&ndash;        <tr>
                  <td colspan="1" style="text-align: right">前序课</td>
                  <td colspan="1">
                    <select class="commInput" v-model="form.preCourseId">
                      <option v-for="item in courseSelectList" :key="item.id" :value="item.id">
                        {{ item.title }}
                      </option>
                    </select>
                  </td>
                </tr>&ndash;&gt;

        <tr>
          <td colspan="2">
            <button class="commButton" @click="close()" style="margin-right: 30px">取消</button>
            <button class="commButton" @click="confirm()">确认</button>
          </td>
        </tr>
      </table>
    </div>
  </dialog>-->

  <DialogForm :dialog-visible="dialogVisible" title="实践活动添加修改" labelWidth="100px"
              :form="form" :form-item="formItem" @save="confirm"
              :selectOptions="selectOptions" @close="close"></DialogForm>
</template>

<script lang="ts">
import { mapState } from 'pinia'
import {ActivityItem, type OptionItem, type PracticeItem, ROLE_STUDENT} from '~/models/general'
import { defineComponent } from 'vue'
import { useAppStore } from '~/stores/app'
import { getPracticeList, getPracticeStudentVersionPractice, getActivityStudentItemOptionList, practiceDelete, practiceEditSave } from '~/services/genServ'
import { getDialog, getOptionItem } from '~/tools/comMethod'
import {FormModel} from "~/models/formModel";
const store = useAppStore()
export default defineComponent({
  data: () => ({
    numName: '',
    studentList: [] as OptionItem[],
    practiceList: [] as PracticeItem[],
    deleteId: -1,
    form: {} as PracticeItem,
    pagination: {
      currentPage: 1,
      pageSize: 10,
      dataTotal: 1,
    },
    dialogVisible: false,
    formItem:[
      {label:'学生姓名', labelKey:'num',itemType:'select',optionKey:'value'},
      {label:'实践活动名称', labelKey:'practiceName'},
      {label:'实践活动时间', labelKey:'practiceTime',itemType:'datetime'},
      {label:'实践活动内容', labelKey:'practiceContent',itemType:'textarea'},
    ] as FormModel[],
  }),
  computed: {
    ...mapState(useAppStore, ['userInfo']),
    selectOptions:(props:any)=>{
      return {'num':props.studentList}
    }
  },
  created() {
    this.query()
  },

  methods: {
    isShowView() {
      return this.userInfo.roles !== ROLE_STUDENT
    },
    async makeSelectCourseList() {
      this.studentList = await getActivityStudentItemOptionList('')
    },
    async query() {
      if (this.isShowView()) {
        this.practiceList = await getPracticeList(this.numName)
      } else {
        this.practiceList = await getPracticeStudentVersionPractice(this.numName)
      }

      await this.makeSelectCourseList()
    },
    addItem() {
      this.form = {} as PracticeItem
      //getDialog('favDialog').show()
      this.dialogVisible=true
    },
    editItem(item: PracticeItem) {
      this.form = item
      // getDialog('favDialog').show()
      this.dialogVisible=true
    },
    close() {
      //getDialog('favDialog').close()
      this.dialogVisible=false
    },
    // 分页请求
    handleChangePage(val: number) {
      this.pagination.currentPage = val;
      this.query();
    },
    async confirm() {
      this.close()
      console.log(this.form)
      this.form.studentId = getOptionItem(this.studentList, this.form.num).id
      const res = await practiceEditSave(this.form)
      if (res.code == 0) {
        store.message('保存成功')
        this.query()
      } else {
        store.message(res.msg)
      }
    },
    deleteItem(courseId: number) {
      this.deleteId = courseId
      store.confirm('确认删除？', this.deleteConfirm)
    },
    async deleteConfirm() {
      const res = await practiceDelete(this.deleteId)
      if (res.code == 0) {
        store.message('删除成功')
        this.query()
      } else {
        store.message(res.msg)
      }
    }
  }
})
</script>
<style></style>
