package com.example.k_mbti.service;

import com.example.k_mbti.dto.InquiryDto;

import java.util.List;

public interface InquiryService {

    List<InquiryDto> getInquiryList();     // 목록

    void writeInquiry(InquiryDto inquiry); // 작성

    InquiryDto findById(Long id);          // 상세 조회

    void updateInquiry(InquiryDto inquiry); // ✅ 수정

    void deleteInquiry(Long id);

}
