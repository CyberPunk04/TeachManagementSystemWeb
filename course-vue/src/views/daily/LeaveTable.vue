<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>

      <div class="base_title">请假管理</div>
    </div>

    <div class="base_query_oneLine">
      <div class="query_left">
        <template v-if="isShowView()">
          <button class="commButton" @click="addItem()">添加</button>
        </template>
      </div>
      <div class="query_right">
        <span style="margin-top: 5px">学生</span>
        <select class="commInput" v-model="studentId">
          <option value="0">请选择...</option>
          <option v-for="item in studentList" :key="item.id" :value="item.id">
            {{ item.title }}
          </option>
        </select>
        <button style="margin-left: 5px" class="commButton" @click="query()">查询</button>
      </div>
    </div>


    <div class="table-content">
      <el-table :data="leavingList" :header-cell-style="{
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
        <el-table-column label="校区" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.campus }}
          </template>
        </el-table-column>
        <el-table-column label="目的地" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.destination }}
          </template>
        </el-table-column>
        <el-table-column label="请假原因" color="black" align="center">
          <template v-slot="scope">
            {{ scope.row.reason }}
          </template>
        </el-table-column>
        <el-table-column label="请假时间" color="black" align="center" width="200">
          <template v-slot="scope">
            {{ scope.row.leavingTime }}
          </template>
        </el-table-column>
        <el-table-column label="开始时间" color="black" align="center" width="200">
          <template v-slot="scope">
            {{ scope.row.startTime }}
          </template>
        </el-table-column>
        <el-table-column label="结束时间" color="black" align="center" width="200">
          <template v-slot="scope">
            {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <template v-if="isShowView()">
          <el-table-column label="操作" color="black" align="center" width="260">
            <template v-slot="scope">
              <el-button class="commButton" @click="editItem(scope.row)">编辑</el-button>
              <el-button class="commButton" @click="deleteItem(scope.row.leavingId)">删除</el-button>
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


  <DialogForm :dialog-visible="dialogVisible" title="请假添加修改" :form="form" :form-item="formItem" @save="confirm"
    :selectOptions="selectOptions" @close="close">

  </DialogForm>
</template>

<script lang="ts">
import { mapState } from 'pinia'
import { type OptionItem, type LeaveItem, ROLE_STUDENT, ActivityItem } from '~/models/general'
import { defineComponent } from 'vue'
import { useAppStore } from '~/stores/app'
import { getLeavingStudentItemOptionList, getLeavingStudentVersionLeavingList, getLeavingList, leavingDelete, leavingSave } from '~/services/genServ'
import { getDialog } from '~/tools/comMethod'
import { FormModel } from "~/models/formModel";
const store = useAppStore()
export default defineComponent({
  data: () => ({
    studentId: '',
    studentList: [] as OptionItem[],
    leavingList: [] as LeaveItem[],
    deleteId: -1,
    form: {} as LeaveItem,
    pagination: {
      currentPage: 1,
      pageSize: 10,
      dataTotal: 1,
    },
    dialogVisible: false,
    formItem: [
      { label: '学生姓名', labelKey: 'studentId', itemType: 'select', optionKey: 'id' },
      { label: '校区', labelKey: 'campus' },
      { label: '目的地', labelKey: 'destination', },
      { label: '请假原因', labelKey: 'reason' },
      { label: '请假时间', labelKey: 'leavingTime', itemType: 'datetime' },
      { label: '开始时间', labelKey: 'startTime', itemType: 'datetime' },
      { label: '结束时间', labelKey: 'endTime', itemType: 'datetime' },
    ] as FormModel[],
  }),
  computed: {
    ...mapState(useAppStore, ['userInfo']),
    selectOptions: (props: any) => {
      return { 'studentId': props.studentList }
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
      this.studentList = await getLeavingStudentItemOptionList('')
    },
    async query() {
      if (this.isShowView()) {
        this.leavingList = await getLeavingList(this.studentId)
      } else {
        this.leavingList = await getLeavingStudentVersionLeavingList(this.studentId)
      }

      await this.makeSelectCourseList()
    },
    addItem() {
      this.form = {} as LeaveItem
      this.dialogVisible = true
    },
    editItem(item: LeaveItem) {
      this.form = item
      this.dialogVisible = true
    },
    close() {
      this.dialogVisible = false
    },
    // 分页请求
    handleChangePage(val: number) {
      this.pagination.currentPage = val;
      this.query();
    },
    async confirm() {
      this.close()
      console.log(this.form)
      const res = await leavingSave(this.form)
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
      const res = await leavingDelete(this.deleteId)
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
