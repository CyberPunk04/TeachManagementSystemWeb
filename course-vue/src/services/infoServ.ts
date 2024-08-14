import { generalRequest, uploadRequest } from "~/services/genServ";
import {
  type DataResponse,
  StudentCommentItem,
  StudentItem,
  TeacherItem
} from "~/models/general";
//修改密码
export async function updatePassword(data: Object): Promise<DataResponse> {
  const res = await generalRequest("/api/base/updatePassword", data);
  return res as DataResponse;
}
//用户获取邮箱验证码的方法
export function getEmailCode(data: Object) {
  return generalRequest("/api/base/updatePassword/emailVerify", data);
}

//用户更换邮箱时获取邮箱验证码的方法
export function getEmailCode2(data: Object) {
  return generalRequest("/api/base/updateEmail/emailVerify", data);
}

//用户更换邮箱的方法
export function updateEmail(data: Object) {
  return generalRequest("/api/base/updateEmail", data);
}

//获取学生个人简介信息后台数据请求方法
export async function getStudentIntroduceData(
  studentId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/student/getStudentIntroduceData", {
    studentId: studentId,
  });
  return res as DataResponse;
}
//获取学生照片数据后台数据请求方法
export async function getPhotoImageStr(
  fileName: String
): Promise<DataResponse> {
  const res = await generalRequest("/api/base/getPhotoImageStr", {
    fileName: fileName,
  });
  return res as DataResponse;
}

//上传学生照片数据后台数据请求方法
export async function uploadPhoto(remoteFile: string, file: any): Promise<any> {
  //console.log(remoteFile);
  //console.log(file);
  const formData = new FormData();
  formData.append("file", file);
  const res = await uploadRequest(
    "/api/base/uploadPhotoWeb?remoteFile=" + remoteFile,
    formData
  );
  return res as DataResponse;
}

//获取学生互评
export async function getCommentList(
  studentId: number | null
): Promise<StudentCommentItem[]> {
  const res = await generalRequest("/api/studentComment/getCommentList", {
    studentId: studentId,
  })
  return res.data as StudentCommentItem[];
}
//存储学生个人简介信息
export async function saveStudentIntroduce(
  studentId: number | null,
  form: StudentItem
): Promise<DataResponse> {
  const res = await generalRequest("/api/student/saveStudentIntroduce", {
    studentId: studentId,
    form: form,
  });
  return res as DataResponse;
}
//存储教师个人简介信息
export async function saveTeacherIntroduce(
  teacherId: number | null,
  form: TeacherItem
): Promise<DataResponse> {
  const res = await generalRequest("/api/teacher/saveTeacherIntroduce", {
    teacherId: teacherId,
    form: form,
  });
  return res as DataResponse;
}
//获取教师个人简介信息后台数据请求方法
export async function getTeacherIntroduceData(
  teacherId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/teacher/getTeacherIntroduceData", {
    teacherId: teacherId,
  });
  return res as DataResponse;
}




// ///////
