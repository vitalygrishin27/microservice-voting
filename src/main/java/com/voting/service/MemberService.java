package com.voting.service;

import com.voting.bom.Member;

import java.util.List;

public interface MemberService {
    Long create (Member member, Object performancesData);

    void delete(Long memberId);

    Long update(Member member, Object performanceData);

    List<Member> getAll();
}
