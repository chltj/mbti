package com.example.k_mbti.dao;
import com.example.k_mbti.dto.InquiryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryDao {

    List<InquiryDto> findAll();

    void insertInquiry(InquiryDto inquiry);

    InquiryDto findById(Long id);
}