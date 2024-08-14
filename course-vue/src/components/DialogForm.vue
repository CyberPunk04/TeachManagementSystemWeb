<template>
  <el-dialog ref="favDialog" v-model="dialogVisible" :title="title" :width="550" :height="500"
  :before-close="handleClose">
    <el-form :model="form" :label-width="labelWidth">
      <el-form-item v-for="(item,index) in formItem" :key="index">
        <el-form-item :label="item.label" >
          <template v-if="!item.itemType||item.itemType==='input'">
            <el-input v-model="form[item.labelKey]"></el-input>
          </template>
          <template v-if="item.itemType==='textarea'">
            <el-input v-model="form[item.labelKey]" type="textarea" :autosize="{ minRows: 3, maxRows: 6 }"></el-input>
          </template>

          <template v-if="item.itemType==='select'">
          <el-select v-model="form[item.labelKey]" :placeholder="`请选择${item.label}`">
            <el-option v-for="selectItem in selectOptions[item.labelKey]" :key="selectItem.id" :value="selectItem[item.optionKey]" :label="selectItem.title"></el-option>
          </el-select>
          </template>
          <template v-if="item.itemType==='datetime'">
              <el-date-picker v-model="form[item.labelKey]" type="datetime" size="small" placeholder="请选择活动时间"
                              format="YYYY/MM/DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
          </template>
          <template v-if="item.itemType==='date'">
            <el-date-picker v-model="form[item.labelKey]" type="date" size="small" placeholder="请选择活动日期"
                            format="YYYY/MM/DD" value-format="YYYY-MM-DD" />
          </template>
        </el-form-item>
      </el-form-item>
      <el-row :gutter="16" class="dialog-buttons">
        <el-col :span="10"/>
        <el-col :span="8">
          <el-button type="primary" @click="confirm">确认</el-button>
          <el-button @click="close">取消</el-button>
        </el-col>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent,PropType,toRefs } from "vue";
import {type FormModel} from "~/models/formModel";
export default defineComponent({
  name: "DialogForm",
  props: {
    dialogVisible:{
        type: [Boolean] as PropType<boolean>,
        default: false,
    },
    title:{
      type:[String] as PropType<string>,
      default:'课程添加修改'
    },
    form:{
      type: Object as PropType<any>,
      default:{}
    },
    formItem:{
      type:Array as PropType<FormModel[]>,
      default:[]
    },
    labelWidth:{
      type:String as PropType<string>,
      default:'80px'
    },
    selectOptions:{
      type:Object as PropType<FromOption>,
      default:{}
    }
  },
  setup(props, { emit }){
    const {form}=toRefs(props)
    function handleClose(done: () => void){
      // 处理关闭逻辑
      done();
    }
    //关闭对话框
    close=()=>{
      emit('close')
    }
    //确认对话框
    confirm=async () =>{
      emit('save',form.value)
    }
    return {handleClose,close,confirm}
  }
})
</script>

<style scoped>

</style>