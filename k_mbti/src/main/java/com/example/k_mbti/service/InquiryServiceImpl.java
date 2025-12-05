package com.example.k_mbti.service;

import com.example.k_mbti.dao.InquiryDao;
import com.example.k_mbti.dto.InquiryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final InquiryDao inquiryDao;

    public InquiryServiceImpl(InquiryDao inquiryDao) {
        this.inquiryDao = inquiryDao;
    }

    @Override
    public List<InquiryDto> getInquiryList() {
        return inquiryDao.findAll();
    }

    @Override
    public void writeInquiry(InquiryDto inquiry) {
        inquiryDao.insertInquiry(inquiry);
    }

    @Override
    public InquiryDto findById(Long id) {
        return inquiryDao.findById(id);
    }

    @Override
    public void updateInquiry(InquiryDto inquiry) {
        inquiryDao.updateInquiry(inquiry);   // ✅ DAO로 위임
    }

    // 아래 두 개는 역할이 겹치면 삭제해도 됨
}
