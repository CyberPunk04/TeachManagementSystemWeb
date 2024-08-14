import { generalRequest, uploadRequest } from "~/services/genServ";
import {
  CommentItem,
  StudentItem,
  TeacherItem,
  type DataResponse,
  type PostItem,
} from "~/models/general";

//返回某个用户的帖子列表
export async function getPostList(
  personId: number | null
): Promise<PostItem[]> {
  const res = await generalRequest("/api/post/getPostList", {
    personId: personId,
  });
  return res.data as PostItem[];
}

//创建帖子
export async function createPost(
  content: string | null,
  created_at: string | null,
  personId: number | null,
  image: string | null,
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/postSave", {
    content: content,
    created_at: created_at,
    personId: personId,
    image,
  });
  //返回成功信号
  return res as DataResponse;//成功值为code=0
}

//修改帖子
export async function editPost(
  postId: number | null,
  content: string | null,
  created_at: string | null,
  image: string | null,
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/postEdit", {
    postId: postId,
    content: content,
    created_at: created_at,
    image,
  });
  //返回成功信号
  return res as DataResponse;//成功值为code=0
}

//删除帖子
export async function deletePost(
  postId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/postDelete", {
    postId: postId,
  });
  //返回成功信号
  return res as DataResponse;//成功值为code=0
}

//统计发了多少帖子
export async function countPost(
  personId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/countPost", {
    personId: personId,
  });
  //返回成功信号
  return res as DataResponse;//返回帖子数
}

//获取对某个帖子的点赞状态
export async function getLikes(
  postId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/getLikes", {
    postId: postId,
  });
  //返回成功信号
  return res as DataResponse;//点赞返回code=0,未点赞返回code=1
}

//点赞功能,如果点赞了就取消点赞,如果没点赞就点赞
export async function postLikes(
  postId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/postLikes", {
    postId: postId,
  });
  return res as DataResponse;//成功值为code=0
}

//获取关注状态
export async function getFollowStatus(

  followingId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/follow/getFollow", {
    followingId: followingId,
  });
  //返回成功信号
  return res as DataResponse;//关注返回code=0,未关注返回code=1
}

//关注功能,如果关注了就取消关注,如果没关注就关注
export async function follow(
  followingId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/follow/updateFollow", {

    followingId: followingId,
  });
  return res as DataResponse;//成功值为code=0
}

//获取粉丝数
export async function getFansNum(
  personId: number | null
): Promise<number> {
  const res = await generalRequest("/api/follow/getFansNum", {
    personId: personId,
  });
  //返回成功信号
  return res.data as number;//返回粉丝数
}

//发送一个评论
export async function addComment(
  postId: number | null,
  content: string | null,
  created_at: string | null,
): Promise<DataResponse> {
  const res = await generalRequest("/api/comment/addComment", {
    postId: postId,
    content: content,
    created_at: created_at,
  });
  //返回成功信号
  return res as DataResponse;//成功值为code=0
}

//获取一个帖子的评论列表
export async function getCommentList(
  postId: number | null
): Promise<CommentItem[]> {
  const res = await generalRequest("/api/comment/getCommentList", {
    postId: postId,
  });
  return res.data as CommentItem[];
}
//删除一个评论
export async function deleteComment(
  commentId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/comment/deleteComment", {
    commentId: commentId,
  });
  //返回成功信号
  return res as DataResponse;//成功值为code=0
}

//统计一个帖子的评论数
export async function getCommentNum(
  postId: number | null
): Promise<DataResponse> {
  const res = await generalRequest("/api/comment/getCommentNum", {
    postId: postId,
  });
  //返回成功信号
  return res as DataResponse;//返回评论数
}

//获取所有帖子列表或者根据内容和发帖人搜索帖子
export async function getAllPost(
  contentPersonName: string | null,
): Promise<PostItem[]> {
  const res = await generalRequest("/api/post/getAllPost", {
    contentPersonName: contentPersonName,
  });
  return res.data as PostItem[];
}

//获取关注的人的帖子列表
export async function getFollowPost(
): Promise<PostItem[]> {
  const res = await generalRequest("/api/post/getFollowPost", {
  });
  return res.data as PostItem[];
}

//返回当前用户信息用于是否判断是否是我自己。
export async function getPersonInfo(
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/getPersonInfo", {
  });
  return res as DataResponse;
}

//获取某个用户的头像
export async function getAvatar(
  personId: number | null,
): Promise<DataResponse> {
  const res = await generalRequest("/api/post/getAvatar", {
    personId: personId,
  });
  return res as DataResponse;
}

//上传图片到后端
export async function uploadAvatar(
  avatar: string,
): Promise<any> {
  const res = await generalRequest("/api/post/uploadAvatar", {
    avatar: avatar,
  });

  return res as DataResponse;
}

//获取好友列表
export async function getFriendList(
  numName: string | null,
): Promise<any> {
  const res = await generalRequest("/api/follow/getFriendList", {
    numName,
  });

  return res.data as StudentItem | TeacherItem;
}