package com.ku_stacks.ku_ring.remote;

import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest;
import com.ku_stacks.ku_ring.remote.notice.response.CategoryResponse;
import com.ku_stacks.ku_ring.remote.notice.response.NoticeListResponse;
import com.ku_stacks.ku_ring.remote.notice.response.NoticeResponse;
import com.ku_stacks.ku_ring.remote.notice.response.SubscribeListResponse;
import com.ku_stacks.ku_ring.remote.util.DefaultResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RemoteFixtures {

    /*
    fun subscribeListResponse() = SubscribeListResponse(
            "성공", 200, listOf(
                CategoryResponse("student", "stu", "학생"),
                CategoryResponse("employment", "emp", "취창업"),
            )
        )
     */

    public static SubscribeListResponse subscribeListResponse() {
        ArrayList<CategoryResponse> list = new ArrayList<>();
        list.add(new CategoryResponse("student", "stu", "학생"));
        list.add(new CategoryResponse("employment", "emp", "취창업"));
        return new SubscribeListResponse("성공", 200, list);
    }

    public static SubscribeRequest subscribeRequest() {
        return new SubscribeRequest(Arrays.asList("bachelor", "scholarship"));
    }

    public static DefaultResponse defaultResponse() {
        return new DefaultResponse(200, "성공", null);
    }

    public static NoticeResponse noticeResponse() {
        return new NoticeResponse(
                "5b4a11b",
                "20220203",
                "2022학년도 1학기 재입학 합격자 유의사항 안내",
                "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
                "bachelor",
                false
        );
    }

    public static NoticeListResponse noticeListResponse() {
        return new NoticeListResponse(
                "성공",
                200,
                Collections.singletonList(noticeResponse())
        );
    }
}
