package com.example.k_mbti.dao;

import com.example.k_mbti.dto.InquiryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryDao {

    List<InquiryDto> findAll();

    void insertInquiry(InquiryDto inquiry);

    InquiryDto findById(Long id);

    void updateInquiry(InquiryDto inquiry); 
     void deleteInquiry(Long id);// ✅ 추가
    void updateWriterNickname(@Param("oldNickname") String oldNickname,
                              @Param("newNickname") String newNickname);
}
