package com.ku_stacks.ku_ring.domain;

import java.util.ArrayList;

public class DomainFixtures {
    public static Notice notice() {
        return new Notice(
                "20220203",
                "2022학년도 1학기 재입학 합격자 유의사항 안내",
                "bachelor",
                "",
                "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5b4a11b",
                "5b4a11b",
                true,
                false,
                false,
                false,
                false,
                new ArrayList<>()
        );
    }

    public static Department department() {
        return new Department(
                "smart_ict_convergence",
                "sicte",
                "스마트ICT융합공학과",
                false,
                false,
                false
        );
    }
}
