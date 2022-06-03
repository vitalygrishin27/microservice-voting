package com.voting.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.bom.Jury;
import com.voting.bom.Member;
import com.voting.bom.Performance;
import com.voting.exception.CategoryException;
import com.voting.exception.JuryException;
import com.voting.exception.MemberException;
import com.voting.repository.CategoryRepo;
import com.voting.repository.MemberRepo;
import com.voting.service.CategoryService;
import com.voting.service.MemberService;
import com.voting.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberRepo repo;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PerformanceService performanceService;

    @Value("${maxUploadFileSizePlayerPhoto}")
    private Long maxUploadFileSizePlayerPhoto;

    @Override
    public Long create(Member member, Object performancesData) {
        try {
            checkFieldsAndSetFile(member, performancesData);
            repo.saveAndFlush(member);
        } catch (Exception e) {
            throw new CategoryException(e.getMessage());
        }
        return member.getId();
    }

    @Override
    public void delete(Long memberId) {
        repo.delete(getMemberIfExists(memberId));
    }

    @Override
    public Long update(Member member, Object performancesData) {
        try {
            checkFieldsAndSetFile(member, performancesData);
            repo.saveAndFlush(member);
        } catch (Exception e) {
            throw new CategoryException(e.getMessage());
        }
        return member.getId();
    }

    @Override
    public List<Member> getAll() {
        return repo.findAll();
    }

    private void checkFieldsAndSetFile(Member member, Object performancesData) throws IOException {
        if (member.getFirstName().equals("") ||
                member.getLastName().equals("")) throw new MemberException("Mandatory fields are required");
        if (member.getFile() != null && member.getFile().getSize() > maxUploadFileSizePlayerPhoto)
            throw new MemberException("File size is too large. Allowed to " + maxUploadFileSizePlayerPhoto);
        if (member.getFile() != null)
            member.setPhoto("data:image/jpeg;base64, " + Base64Utils.encodeToString(member.getFile().getBytes()));

        if (performancesData != null) {
            List<Performance> performancesFromRequest = Arrays.asList(new ObjectMapper().readValue((String) performancesData, Performance[].class));
            if (member.getId() != null) {
                performanceService.getByMember(member).forEach(performance -> {
                    if (!performancesFromRequest.contains(performance)) {
                        performanceService.deleteById(performance.getId());
                    }
                });
            }
            for (Performance item : performancesFromRequest
            ) {
                if (item.getCategoryId() != null)
                    item.setCategory(categoryService.getCategoryIfExists(item.getCategoryId()));
                item.setMember(member);
                item.setContest(member.getContest());
                if (member.getId() != null) performanceService.create(item);
            }
            member.setPerformances(performancesFromRequest);
        } else {
            performanceService.deleteAllByMember(member);
        }
    }

    private Member getMemberIfExists(Long memberId) {
        Optional<Member> optionalMember = repo.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new MemberException(String.format("Member with id=%s not found", memberId));
        }
        return optionalMember.get();
    }
}
