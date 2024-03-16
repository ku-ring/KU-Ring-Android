package com.ku_stacks.ku_ring.local;

import com.ku_stacks.ku_ring.local.entity.DepartmentEntity;
import com.ku_stacks.ku_ring.local.entity.NoticeEntity;
import com.ku_stacks.ku_ring.local.entity.PushEntity;

public class LocalFixtures {
    public static DepartmentEntity departmentEntity() {
        return new DepartmentEntity(
                "smart_ict_convergence",
                "sicte",
                "스마트ICT융합공학과",
                false
        );
    }

    public static NoticeEntity noticeEntity() {
        return new NoticeEntity(
                "5b4a11b",
                "bachelor",
                "",
                "2023학년도 전과 선발자 안내",
                "20230208",
                "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
                false,
                false,
                false,
                false,
                false
        );
    }

    public static NoticeEntity readNoticeEntity() {
        return new NoticeEntity(
                "5b4a11b",
                "bachelor",
                "",
                "2023학년도 전과 선발자 안내",
                "20230208",
                "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
                false,
                true,
                false,
                false,
                false
        );
    }

    public static NoticeEntity departmentNoticeEntity() {
        return new NoticeEntity(
                "182677",
                "department",
                "cse",
                "2023학년도 진로총조사 설문 요청",
                "2023-05-02",
                "http://cse.konkuk.ac.kr/noticeView.do?siteId=CSE&boardSeq=882&menuSeq=6097&seq=182677",
                false,
                false,
                false,
                false,
                false
        );
    }

    public static PushEntity pushEntity() {
        return new PushEntity(
                "ababab",
                "bachelor",
                "2022-01-14 00:50:33",
                "실감미디어 혁신 공유대학 융합전공 안내",
                "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=ababab",
                true,
                "20220114-005036"
        );
    }
}
