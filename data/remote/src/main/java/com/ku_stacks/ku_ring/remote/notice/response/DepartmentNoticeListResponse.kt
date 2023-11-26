package com.ku_stacks.ku_ring.remote.notice.response

data class DepartmentNoticeListResponse(
    val code: Int,
    val message: String,
    val data: List<DepartmentNoticeResponse>,
) {
    companion object {
        fun mockSucceeded(dataSize: Int) = DepartmentNoticeListResponse(
            code = 200,
            message = "공지 조회에 성공하였습니다",
            data = (1..dataSize).map {
                DepartmentNoticeResponse(
                    articleId = it.toString(),
                    postedDate = "2023-05-02",
                    url = "http://cse.konkuk.ac.kr/noticeView.do?siteId=CSE&boardSeq=882&menuSeq=6097&seq=182677",
                    subject = "2023학년도 진로총조사 설문 요청",
                    category = "department",
                    important = false
                )
            }
        )

        fun mockEmpty() = DepartmentNoticeListResponse(
            code = 200,
            message = "공지 조회에 성공하였습니다",
            data = emptyList()
        )
    }
}