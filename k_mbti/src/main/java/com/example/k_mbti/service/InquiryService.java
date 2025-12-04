package com.example.k_mbti.service;

import com.example.k_mbti.dto.InquiryDto;

import java.util.List;

public interface InquiryService {

    List<InquiryDto> getInquiryList();

    void writeInquiry(InquiryDto inquiry);

    InquiryDto getInquiry(Long id);
}