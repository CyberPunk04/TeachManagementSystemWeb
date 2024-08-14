import axios, { type RawAxiosRequestHeaders } from "axios";
import { useAppStore } from "~/stores/app";
import { generalRequest } from "~/services/genServ";
import {
    type DataResponse,
    type CourseItem,
    type OptionItem,
    type TeacherItem,
    type PowerItem,
    type TeacherScoreItem,
    type CourseShowItem,
    type Try,
    type LeaveItem
} from "~/models/general";

//获取课程数据表后台数据请求方法
export async function getCourseList(
    numName: string | null
): Promise<CourseItem[]> {
    const res = await generalRequest("/api/course/getCourseList", {
        numName: numName,
    });
    return res.data as CourseItem[];
}
export async function getTeacherCourseList(
    numName: string | null
): Promise<CourseItem[]> {
    const res = await generalRequest("/api/teacherCourse/getCourseList", {
        numName: numName,
    });
    return res.data as CourseItem[];
}
//学生获取已选课列表
export async function getCourseSelectionList(
    numName: string | null
): Promise<CourseItem[]> {
    const res = await generalRequest("/api/courseSelection/getCourseSelectionList", {
        numName: numName,
    });
    return res.data as CourseItem[];
}
//删除课程后台数据请求方法
export async function courseDelete(courseId: number): Promise<DataResponse> {
    const res = await generalRequest("/api/course/courseDelete", {
        courseId: courseId,
    });
    return res as DataResponse;
}
//删除课程后台数据请求方法
export async function courseSelectionDelete(courseId: number): Promise<DataResponse> {
    const res = await generalRequest("/api/courseSelection/courseSelectionDelete", {
        courseId: courseId,
    });
    return res as DataResponse;
}
//课程保存后台数据请求方法
export async function courseSave(data: CourseItem): Promise<DataResponse> {
    const res = await generalRequest("/api/course/courseSave", data);
    return res as DataResponse;
}
//课程保存后台数据请求方法
export async function courseSelectionSave(data: CourseItem): Promise<DataResponse> {
    const res = await generalRequest("/api/courseSelection/courseSave", data);
    return res as DataResponse;
}
//获取课程列表分页数据后台数据请求方法
export async function getCoursePageData(
    numName: String | null,
    currentPage: number
): Promise<DataResponse> {
    const res = await generalRequest("/api/student/getStudentPageData", {
        numName: numName,
        currentPage: currentPage,
    });
    return res as DataResponse;
}
//获取分数列表分页数据后台数据请求方法
export async function getScorePageData(
    studentId: Number | null,
    courseId: Number | null,
    currentPage: number
): Promise<DataResponse> {
    const res = await generalRequest("/api/score/getScorePageData", {
        studentId: studentId,
        courseId: courseId,
        currentPage: currentPage,
    });
    return res as DataResponse;
}
//获取成绩列表后台数据请求方法
export async function getScoreList(
    studentId: number | null,
    courseId: number | null
): Promise<[]> {
    const res = await generalRequest("/api/score/getScoreList", {
        studentId: studentId,
        courseId: courseId,
    });
    return res.data as [];
}
//获取学生选择项列表后台数据请求方法
export async function getStudentItemOptionList(): Promise<OptionItem[]> {
    const res = await generalRequest("/api/score/getStudentItemOptionList", null);
    return res.itemList as OptionItem[];
}
//获取课程选择项列表后台数据请求方法
export async function getCourseItemOptionList(): Promise<OptionItem[]> {
    const res = await generalRequest("/api/score/getCourseItemOptionList", null);
    return res.itemList as OptionItem[];
}
//成绩保存后台数据请求方法
export async function scoreSave(
    scoreId: number | null,
    studentId: number,
    courseId: number,
    mark: number
): Promise<DataResponse> {
    const res = await generalRequest("/api/score/scoreSave", {
        scoreId: scoreId,
        studentId: studentId,
        courseId: courseId,
        mark: mark,
    });
    return res as DataResponse;
}
//成绩删除后台数据请求方法
export async function scoreDelete(scoreId: number): Promise<DataResponse> {
    const res = await generalRequest("/api/score/scoreDelete", {
        scoreId: scoreId,
    });
    return res as DataResponse;
}
export async function getTeacherList(
    tecName: string | null
): Promise<TeacherItem[]> {
    const res = await generalRequest("/api/teacher/getTeacherList", {
        numName: tecName,
    });
    return res.data as TeacherItem[];
}
export async function teacherPowerSave(value: string): Promise<DataResponse> {
    const res = await generalRequest("/api/teacher/teacherPower", {
        value: value,
    });
    return res as DataResponse;
}
//获取教师评分权限
export async function getPower(
    power: Number | null,
): Promise<PowerItem[]> {
    const res = await generalRequest("/api/teacher/getPower", {
        power: power
    });
    return res as PowerItem[];
}
//成绩保存后台数据请求方法
export async function teacherScoreSave(
    scoreId: number | null,
    studentId: number,
    courseId: number,
    mark: number
): Promise<DataResponse> {
    const res = await generalRequest("/api/teacherScore/scoreSave", {
        scoreId: scoreId,
        studentId: studentId,
        courseId: courseId,
        mark: mark,
    });
    return res as DataResponse;
}
export async function getTeacherScoreList(
    numName: string | null
): Promise<TeacherScoreItem[]> {
    const res = await generalRequest("/api/teacherScore/getCourseSelectionList", {
        numName: numName,
    });
    return res.data as TeacherScoreItem[];
}
//获取课程选择项列表后台数据请求方法
export async function getStudentCourseItemOptionList(): Promise<OptionItem[]> {
    const res = await generalRequest("/api/studentCourse/getCourseItemOptionList", null);
    return res.itemList as OptionItem[];
}
//获取分数列表分页数据后台数据请求方法
export async function getPowr(
    studentId: Number | null,
    courseId: Number | null,
    currentPage: number
): Promise<DataResponse> {
    const res = await generalRequest("/api/score/getScorePageData", {
        studentId: studentId,
        courseId: courseId,
        currentPage: currentPage,
    });
    return res as DataResponse;
}
export async function scoreNoSave(
    scoreId: number | null,
    studentId: number,
    courseId: number,
    mark: number
): Promise<DataResponse> {
    const res = await generalRequest("/api/score/scoreNoSave", {
        scoreId: scoreId,
        studentId: studentId,
        courseId: courseId,
        mark: mark,
    });
    return res as DataResponse;
}
export async function getCourseShowList(
    numName: string | null
): Promise<CourseShowItem[]> {
    const res = await generalRequest("/api/courseShow/getCourseSelectionData", {
        numName: numName,
    });
    return res.data as CourseShowItem[];
}
export async function leavingSaveA(data: LeaveItem): Promise<DataResponse> {
    const res = await generalRequest('/api/leavingNew/leavingSaveA', data)
    return res as DataResponse
}
export async function leavingSaveB(data: LeaveItem): Promise<DataResponse> {
    const res = await generalRequest('/api/leavingNew/leavingSaveB', data)
    return res as DataResponse
}