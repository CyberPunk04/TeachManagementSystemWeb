import axios, { type RawAxiosRequestHeaders } from "axios";
import { useAppStore } from "~/stores/app";
import {
  type MenuInfo,
  type DataResponse,
  type OptionItem,
  type StudentItem,
  type CourseItem,
  type ValidateCode,
  type TermItem,
  type TaskStudentItem,
  type TreeNode,
  type FamilyMemberItem,
  type ActivityItem,
  type FeeItem,
  type LeaveItem,
  type CompetitionItem,
  type InternshipItem,
  type PrizeItem,
  type LectureItem,
  type PracticeItem,
  type ResearchItem,
  type InnovationItem,
} from '~/models/general'






//一般数据请求函数，参数为url后台接口映射全路径，和data 为传递后台的数据，返回值为服务器返回的data
export async function generalRequest(
  url: string,
  data: any | null
): Promise<any> {
  const res = await axios.post(
    url,
    {
      data: data,
    },
    {
      headers: getAuthHeader(),
    }
  );
  if (res.status != 200) {
    console.log("Error: " + res.status);
    return;
  }
  return res.data;
}
//文件数据上传服务器函数，参数为url后台接口映射全路径，和data 为传递后台的数据（包括文件数据），返回值为服务器返回的data
export async function uploadRequest(
  url: string,
  data: any | null
): Promise<any> {
  const res = await axios.post(url, data, {
    headers: getAuthHeaderFile(),
  });
  if (res.status != 200) {
    console.log("Error: " + res.status);
    return;
  }
  return res.data;
}
//获取数据请求Head信息，这个主要包括了用户的jwtToken信息
export function getAuthHeader(): any {
  return {
    Authorization: "Bearer " + useAppStore().userInfo.jwtToken,
  };
}
//获取文件上传请求Head信息，这个主要包括了用户的jwtToken信息和文件上传的Content-Type信息
export function getAuthHeaderFile(): RawAxiosRequestHeaders {
  return {
    Authorization: "Bearer " + useAppStore().userInfo.jwtToken,
    "Content-Type": "multipart/form-data",
  };
}
//文件下载函数，参数为url后台接口映射全路径，和label 为下载文件的名称，和data 为传递后台的数据，返回值为服务器返回的data
export async function downloadPost(url: string, label: string, data: any) {
  const requestOptions = {
    method: "POST",
    headers: {
      "content-type": "application/json",
      Authorization: "Bearer " + useAppStore().userInfo.jwtToken,
    },
    body: JSON.stringify({
      data: data,
    }),
  };
  const res = await fetch(url, requestOptions)
    .then(async (response) => {
      const blob = await response.blob();

      // check for error response
      if (!response.ok) {
        // get error message from body or default to response status
        const error = response.status;
        return Promise.reject(error);
      }
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = label;
      link.click();
      URL.revokeObjectURL(link.href);
      return Promise.resolve(response.status);
    })
    .catch((error) => {
      console.error("There was an error!", error);
    });
  return res;
}

//clt


export async function getActivityStudentItemOptionList(numName: string | null): Promise<OptionItem[]> {
  const res = await generalRequest('/api/activity/getStudentItemOptionList', {
    numName: numName
  })
  return res.itemList as OptionItem[]
}

export async function getStudentVersionActivityList(studentId: string | null): Promise<ActivityItem[]> {
  const res = await generalRequest('/api/studentVersionActivity/getActivityList', {
    studentId: studentId
  })
  return res.data as ActivityItem[]
}

export async function getActivityList(studentId: string | null): Promise<ActivityItem[]> {
  const res = await generalRequest('/api/activity/getActivityList', {
    studentId: studentId
  })
  return res.data as ActivityItem[]
}

export async function activityDelete(activityId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/activity/activityDelete', {
    activityId: activityId
  })
  return res as DataResponse
}
export async function activitySave(data: ActivityItem): Promise<DataResponse> {
  const res = await generalRequest('/api/activity/activitySave', data)
  return res as DataResponse
}

export async function getFeeStudentItemOptionList(numName: string | null): Promise<OptionItem[]> {
  const res = await generalRequest('/api/fee/getStudentItemOptionList', {
    numName: numName
  })
  return res.itemList as OptionItem[]
}

export async function getFeeStudentVersionFeeList(studentId: string | null): Promise<any> {
  const res = await generalRequest('/api/studentVersionFee/getFeeList', {
    studentId: studentId
  })
  return res.data as ActivityItem[]
}

export async function getFeeList(studentId: string | null): Promise<any> {
  const res = await generalRequest('/api/fee/getFeeList', {
    studentId: studentId
  })
  return res.data as ActivityItem[]
}

export async function feeDelete(feeId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/fee/feeDelete', {
    feeId: feeId
  })
  return res as DataResponse
}
export async function feeSave(data: FeeItem): Promise<DataResponse> {
  const res = await generalRequest('/api/fee/feeSave', data)
  return res as DataResponse
}

export async function getLeavingStudentItemOptionList(numName: string | null): Promise<OptionItem[]> {
  const res = await generalRequest('/api/leaving/getStudentItemOptionList', {
    numName: numName
  })
  return res.itemList as OptionItem[]
}

export async function getLeavingStudentVersionLeavingList(studentId: string | null): Promise<LeaveItem[]> {
  const res = await generalRequest('/api/studentVersionLeaving/getLeavingList', {
    studentId: studentId
  })
  return res.data as LeaveItem[]
}

export async function getLeavingList(studentId: string | null): Promise<LeaveItem[]> {
  const res = await generalRequest('/api/leaving/getLeavingList', {
    studentId: studentId
  })
  return res.data as LeaveItem[]
}

export async function leavingDelete(leavingId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/leaving/leavingDelete', {
    leavingId: leavingId
  })
  return res as DataResponse
}
export async function leavingSave(data: LeaveItem): Promise<DataResponse> {
  const res = await generalRequest('/api/leaving/leavingSave', data)
  return res as DataResponse
}

export async function getCompetitionStudentVersionList(numName: string | null): Promise<CompetitionItem[]> {
  const res = await generalRequest('/api/studentVersionCompetition/getCompetitionList', {
    numName: numName
  })
  return res.data as CompetitionItem[]
}

export async function getCompetitionList(numName: string | null): Promise<CompetitionItem[]> {
  const res = await generalRequest('/api/competition/getCompetitionList', {
    numName: numName
  })
  return res.data as CompetitionItem[]
}

export async function competitionDelete(competitionId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/competition/competitionDelete', {
    competitionId: competitionId
  })
  return res as DataResponse
}
export async function competitionEditSave(data: CompetitionItem): Promise<DataResponse> {
  const res = await generalRequest('/api/competition/competitionEditSave', data)
  return res as DataResponse
}

export async function getInnovationStudentVersionInnovationList(numName: string | null): Promise<InnovationItem[]> {
  const res = await generalRequest('/api/studentVersionInnovation/getInnovationList', {
    numName: numName
  })
  return res.data as InnovationItem[]
}

export async function getInnovationList(numName: string | null): Promise<InnovationItem[]> {
  const res = await generalRequest('/api/innovation/getInnovationList', {
    numName: numName
  })
  return res.data as InnovationItem[]
}

export async function innovationDelete(innovationId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/innovation/innovationDelete', {
    innovationId: innovationId
  })
  return res as DataResponse
}
export async function innovationEditSave(data: InnovationItem): Promise<DataResponse> {
  const res = await generalRequest('/api/innovation/innovationEditSave', data)
  return res as DataResponse
}

export async function getInternshipStudentVersionInternshipList(numName: string | null): Promise<InternshipItem[]> {
  const res = await generalRequest('/api/studentVersionInternship/getInternshipList', {
    numName: numName
  })
  return res.data as InternshipItem[]
}

export async function getInternshipList(numName: string | null): Promise<InternshipItem[]> {
  const res = await generalRequest('/api/internship/getInternshipList', {
    numName: numName
  })
  return res.data as InternshipItem[]
}

export async function internshipDelete(internshipId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/internship/internshipDelete', {
    internshipId: internshipId
  })
  return res as DataResponse
}
export async function internshipEditSave(data: InternshipItem): Promise<DataResponse> {
  const res = await generalRequest('/api/internship/internshipEditSave', data)
  return res as DataResponse
}

export async function getLectureStudentVersionLectureList(numName: string | null): Promise<LectureItem[]> {
  const res = await generalRequest('/api/studentVersionLecture/getLectureList', {
    numName: numName
  })
  return res.data as LectureItem[]
}

export async function getLectureList(numName: string | null): Promise<LectureItem[]> {
  const res = await generalRequest('/api/lecture/getLectureList', {
    numName: numName
  })
  return res.data as LectureItem[]
}

export async function lectureDelete(lectureId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/lecture/lectureDelete', {
    lectureId: lectureId
  })
  return res as DataResponse
}
export async function lectureEditSave(data: LectureItem): Promise<DataResponse> {
  const res = await generalRequest('/api/lecture/lectureEditSave', data)
  return res as DataResponse
}

export async function getPracticeStudentVersionPractice(numName: string | null): Promise<PracticeItem[]> {
  const res = await generalRequest('/api/studentVersionPractice/getPracticeList', {
    numName: numName
  })
  return res.data as PracticeItem[]
}

export async function getPracticeList(numName: string | null): Promise<PracticeItem[]> {
  const res = await generalRequest('/api/practice/getPracticeList', {
    numName: numName
  })
  return res.data as PracticeItem[]
}

export async function practiceDelete(practiceId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/practice/practiceDelete', {
    practiceId: practiceId
  })
  return res as DataResponse
}
export async function practiceEditSave(data: PracticeItem): Promise<DataResponse> {
  const res = await generalRequest('/api/practice/practiceEditSave', data)
  return res as DataResponse
}

export async function getResearchStudentVersionResearch(numName: string | null): Promise<ResearchItem[]> {
  const res = await generalRequest('/api/studentVersionResearch/getResearchList', {
    numName: numName
  })
  return res.data as ResearchItem[]
}

export async function getResearchList(numName: string | null): Promise<ResearchItem[]> {
  const res = await generalRequest('/api/research/getResearchList', {
    numName: numName
  })
  return res.data as ResearchItem[]
}

export async function researchDelete(researchId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/research/researchDelete', {
    researchId: researchId
  })
  return res as DataResponse
}
export async function researchEditSave(data: ResearchItem): Promise<DataResponse> {
  const res = await generalRequest('/api/research/researchEditSave', data)
  return res as DataResponse
}

export async function getPrizeStudentVersionPrizeList(numName: string | null): Promise<PrizeItem[]> {
  const res = await generalRequest('/api/studentVersionPrize/getPrizeList', {
    numName: numName
  })
  return res.data as PrizeItem[]
}

export async function getPrizeList(numName: string | null): Promise<PrizeItem[]> {
  const res = await generalRequest('/api/prize/getPrizeList', {
    numName: numName
  })
  return res.data as PrizeItem[]
}

export async function prizeDelete(prizeId: number): Promise<DataResponse> {
  const res = await generalRequest('/api/prize/prizeDelete', {
    prizeId: prizeId
  })
  return res as DataResponse
}
export async function prizeEditSave(data: PrizeItem): Promise<DataResponse> {
  const res = await generalRequest('/api/prize/prizeEditSave', data)
  return res as DataResponse
}



