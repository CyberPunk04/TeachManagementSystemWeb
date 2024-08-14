//请求数据接口定义 用于请求数据时的参数的定义
//export 表示输出，其他文件可以引用
export interface DataRequest {
  data: any
}

//请求数据接口定义 用于请求数据时的返回值的定义
export interface DataResponse {
  data: any
  code: number
  msg: string
}

// 用于文件数据结构的定义
export interface FileInfo {
  url: string
  name: string
}
//用于系统提示信息的定义
export interface NotificationMsg {
  show: boolean
  msg: string
}
//用于系统确认信息的定义
export interface ConfirmMsg {
  show: boolean
  msg: string
  confirm: Function
}
//用于WebStocet状态数据信息的定义
export interface WebSocketState {
  data: Object
  webSocket: WebSocket
}
//用于聊天信息的定义
export interface ChatInfo {
  userId: number
  username: string
  avatar: string
  content: string
}
//用于验证码信息的定义
export interface ValidateCode {
  validateCodeId: number
  img: string
}
//用于菜单项信息的定义
export interface MenuInfo {
  id: number
  name: string
  title: string
  sList: MenuInfo[]
}
//用于选择项信息的定义
export interface OptionItem {
  id: number
  value: string
  title: string
}
//用于树节点信息的定义
export interface TreeNode {
  pid: number | null
  id: number
  value: string
  title: string
  label: string
  userTypeIds: string
  parentTitle: string
  children: TreeNode[]
  isLeaf: number | null
}
//用于系统配置信息的定义
export interface SystemConfig {
  naviList: MenuInfo[]
  showLeftMeun: boolean
  leftList: MenuInfo[]
  id: number | null
}
//用于用户信息的定义
export interface UserInfo {
  loggedIn: boolean
  username: string
  perName: string
  jwtToken: string
  id: number
  roles: string
  password: string
}
//用于Chart数据信息的定义
export interface ChartItem {
  value: string[]
  label1: string[]
  label2: string[]
}
//用于UserOnline数据信息的定义
export interface UserOnlineItem {
  total: number
  monthCount: number
  dayCount: number
}
//用于学生选择数据信息的定义
export interface Student {
  id: number
  num: string
  name: string
}
//用于学生数据信息的定义
export interface StudentItem {
  studentId: number
  personId: number
  num: string
  name: string
  dept: string
  major: string
  className: string
  card: string
  gender: string
  genderName: string
  birthday: string
  email: string
  phone: string
  address: string
  //临时加入为了调试
  is_followed: boolean
  followers: number
  avatar: string
}

//用于成绩数据信息的定义
export interface ScoreItem {
  scoreId: number
  studentId: number
  courseId: number
  studentNum: string
  studentName: string
  className: string
  courseNum: string
  courseName: string
  credit: number
  mark: number
  ranking: number
}
//用于学生团队数据信息的定义
export interface StudentTermItem {
  taskStudentId: number
  termId: number
  studentId: number
  num: string
  name: string
  className: string
  email: string
  phone: string
  address: string
  teachNo: number
  teamId: number
  teamNo: string
  weight: number
  teamScore: number
  uploader: string
  uploadTime: string
  birthday: string
  attachId: number
}
//用于团队学生数据信息的定义
export interface TermStudentItem {
  studentId: number
  num: string
  name: string
  className: string
  teachNo: string
  weight: number
  score: number
  level: string
}
//用于团队数据信息的定义
export interface TermItem {
  termId: number
  select: boolean
  teamNo: string
  taskStudentId1: number
  student1: string
  weight1: number
  taskStudentId2: number
  student2: string
  weight2: number
  taskStudentId3: number
  student3: string
  weight3: number
  taskStudentId4: number
  student4: string
  weight4: number
  taskStudentId5: number
  student5: string
  weight5: number
  teamWeight: number
  teamScore: number
  uploader: string
  uploadTime: string
}
//用于学科学生数据信息的定义
export interface TaskStudentItem {
  taskStudentId: number
  studentId: number
  num: string
  name: string
  className: string
  teachNo: string
  teamNo: number
  teamScore: number
  weight: number
  score: number
  level: string
}
//用于家庭成员数据信息的定义
export interface FamilyMemberItem {
  memberId: number
  studentId: number
  relation: string
  name: string
  gender: string
  age: number
  unit: string
}
//用于教师数据信息的定义
export interface TeacherItem {
  teacherId: number
  personId: number
  num: string
  name: string
  dept: string
  title: string
  degree: string
  card: string
  gender: string
  genderName: string
  birthday: string
  email: string
  phone: string
  address: string
  studyDirection: string
  publications: string
  //临时加入为了调试
  is_followed: boolean
  followers: number
  avatar: string
}

// //用于博客功能帖子的定义
export interface PostItem {
  postId: number
  content: string
  person: {
    personId: number
    num: string//学工号
    name: string
    dept: string
    gender: number
    avatar: string
  }
  created_at: string
  image: string
  commentNum: number
  likes: number
  is_liked: boolean,
  is_expanded: boolean,
}
//用于博客功能评论的定义
export interface CommentItem {
  commentId: number
  postId: number
  person: {
    personId: number
    num: string//学工号
    name: string
    dept: string
    gender: number
    avatar: string
  }
  content: string
  created_at: string
}


//yxy 学生互评
export interface StudentCommentItem {
  StudentcommentId: number;
  commenter: {
    num: number;
    name: string;
  };
  commented: {
    num: number;
    name: string;
  };
  commenterId: number;
  commentedId: number;
  tags: string;
  comment: string;
  value: number;
}
//gsz
//用于课程数据信息的定义 gsz
//用于成绩数据信息的定义
export interface ScoreItem {
  scoreId: number
  studentId: number
  courseId: number
  studentNum: string
  studentName: string
  className: string
  courseNum: string
  courseName: string
  credit: number
  mark: number
  ranking: number
}
export interface CourseItem {
  courseId: number
  num: string
  name: string
  credit: number
  coursePath: string
  preCourse: string
  preCourseId: number
  teacher: string
  teacherId: number
  type: string
  place: string
  much: number
  startWeek: number
  endWeek: number
  classTime: string
  selectionNumber: number
  last: number
}
//用于教师端成绩数据信息的定义
export interface TeacherScoreItem {
  num: number
  scoreId: number
  studentId: number
  courseId: number
  courseSelectionId: number
  studentNum: string
  student: string
  className: string
  courseNum: string
  course: string
  teacher: string
  answer: string
  mark: number
  ranking: number
}




//clt
export interface ActivityItem {
  activityId: number,
  studentId: number,
  studentNum: number,
  studentName: string,
  activityName: string,
  activityType: string,
  activityTime: string,
  activityLocation: string
}

export interface FeeItem {
  feeId: number,
  studentId: number,
  studentName: string,
  className: string,
  day: string,
  money: string
}

export interface LeaveItem {
  leavingId: number,
  studentId: number,
  studentName: string,
  className: string,
  campus: string,
  destination: string,
  reason: string,
  leavingTime: string,
  startTime: string,
  endTime: string
}

export interface CompetitionItem {
  competitionId: number,
  studentId: number,
  num: string,
  student: string,
  className: string,
  competitionTime: string,
  competitionName: string,
  competitionLevel: string,
  competitionPrize: string
}

export interface InnovationItem {
  innovationId: number,
  studentId: number,
  num: string,
  student: string,
  className: string,
  innovationName: string,
  innovationTime: string,
  teamName: string,
  teamNum: number,
  teamPosition: string,
  innovationContent: string
}

export interface InternshipItem {
  internshipId: number,
  studentId: number,
  num: string,
  student: string,
  className: string,
  company: string,
  startTime: string,
  internshipPosition: string,
  length: string
}


export interface LectureItem {
  lectureId: number,
  studentId: number,
  num: string,
  student: string,
  className: string,
  lectureTime: string,
  lectureName: string,
  lecturePosition: string,
  lectureLeader: string
}

export interface PracticeItem {
  practiceId: number,
  studentId: number,
  num: string,
  student: string,
  className: string,
  practiceTime: string,
  practiceName: string,
  practiceContent: string
}

export interface ResearchItem {
  researchId: number,
  studentId: number,
  num: string,
  student: string,
  className: string,
  researchTime: string,
  researchName: string,
  researchContent: string
}

export interface PrizeItem {
  prizeId: number,
  studentId: number,
  num: string,
  student: string,
  className: string,
  gotTime: string,
  prizeName: string,
  prizeType: string,
  prizeLevel: string
}

export const ROLE_STUDENT = 'ROLE_STUDENT'
///////////////////////

export interface CheckedItem {
  tags: string;
  checked: boolean;
}



