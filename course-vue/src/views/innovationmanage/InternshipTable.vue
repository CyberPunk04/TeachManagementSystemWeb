<template>
  <div class="base_form">
    <div class="base_header">
      <div class="blue_column"></div>

      <div class="base_title">校外实习</div>
    </div>

    <div class="base_query_oneLine">
      <div class="query_left">
        <template v-if="isShowView()">
          <button class="commButton" @click="addItem()">添加</button>
        </template>
      </div>
      <div class="query_right">
        <span style="margin-top: 5px">实习公司</span>
        <input type="text" v-model="numName" style="margin-left: 10px; width: 230px" />
        <button style="margin-left: 5px" class="commButton" @click="query()">查询</button>
      </div>
    </div>
    <div class="table-content">
      <el-table :data="innovationList" :header-cell-style="{
        color: '#2E2E2E',
        fontSize: '15px',
        fontWeight: '400',
        background: 'rgb(242,242,242)',
      }" :row-style="{ height: '60px' }" :cell-style="{ padding: '2px' }" style="width: 100%">
        <el-table-column label="实习公司" color="black" align="center" >
          <template v-slot="scope">
            {{ scope.row.company }}
          </template>
        </el-table-column>
        <el-table-column label="学生姓名" color="black" align="center" >
          <template v-slot="scope">
            {{ scope.row.student }}
          </template>
        </el-table-column>
        <el-table-column label="实习开始时间" color="black" align="center" width="200" >
          <template v-slot="scope">
            {{ scope.row.startTime }}
          </template>
        </el-table-column>
        <el-table-column label="实习职务" color="black" align="center" >
          <template v-slot="scope">
            {{ scope.row.internshipPosition }}
          </template>
        </el-table-column>
        <el-table-column label="实习时长" color="black" align="center" >
          <template v-slot="scope">
            {{ scope.row.length }}
          </template>
        </el-table-column>
        <template v-if="isShowView()">
        <el-table-column label="操作" color="black" align="center" width="260">
          <template v-slot="scope">
            <el-button class="commButton" @click="editItem(scope.row)">编辑</el-button>
            <el-button class="commButton" @click="deleteItem(scope.row.internshipId)">删除</el-button>
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
  <DialogForm :dialog-visible="dialogVisible" title="实习添加修改" labelWidth="100px"
              :form="form" :form-item="formItem" @save="confirm"
              :selectOptions="selectOptions" @close="close"></DialogForm>
</template>

<script lang="ts">
import { mapState } from 'pinia'
import {type OptionItem, type InternshipItem, ROLE_STUDENT, ActivityItem} from '~/models/general'
import { defineComponent } from 'vue'
import { useAppStore } from "~/stores/app"
import {
  getInternshipList,
  getInternshipStudentVersionInternshipList,
  getActivityStudentItemOptionList,
  internshipDelete,
  internshipEditSave,
} from '~/services/genServ'
import { getDialog, getOptionItem } from '~/tools/comMethod'
import {FormModel} from "~/models/formModel";
const store = useAppStore()
export default defineComponent({
  data: () => ({
    numName: '',
    studentList: [] as OptionItem[],
    innovationList: [] as InternshipItem[],
    deleteId: -1,
    form: {} as InternshipItem,
    pagination: {
      currentPage: 1,
      pageSize: 10,
      dataTotal: 1,
    },
    dialogVisible: false,
    formItem:[
      {label:'学生姓名', labelKey:'num',itemType:'select',optionKey:'value'},
      {label:'实习公司', labelKey:'company'},
      {label:'实习开始时间', labelKey:'startTime',itemType:'datetime'},
      {label:'实习职务', labelKey:'internshipPosition'},
      {label:'实习时长', labelKey:'length',},
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
        this.innovationList = await getInternshipList(this.numName)
      } else {
        this.innovationList = await getInternshipStudentVersionInternshipList(this.numName)
      }

      await this.makeSelectCourseList()
    },
    addItem() {
      this.form = {} as InternshipItem
      //getDialog('favDialog').show()
      this.dialogVisible=true
    },
    editItem(item: InternshipItem) {
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
      const res = await internshipEditSave(this.form)
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
      const res = await internshipDelete(this.deleteId)
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
