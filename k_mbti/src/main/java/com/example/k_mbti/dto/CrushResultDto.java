package com.example.k_mbti.dto;

public class CrushResultDto {

    private int score;
    private String level;
    private String comment;

    // 7개의 세부 라벨 필드
    private String replySpeedLabel;
    private String frequencyLabel;
    private String initiativeLabel;
    private String warmthLabel;
    private String lengthLabel;
    private String overlapLabel;
    private String ratioLabel;

    public CrushResultDto() {
    }

    // 10개 인자를 받는 생성자 (CrushService 호환)
    public CrushResultDto(int score, String level, String comment,
                          String replySpeedLabel, String frequencyLabel,
                          String initiativeLabel, String warmthLabel,
                          String lengthLabel, String overlapLabel, String ratioLabel) {
        this.score = score;
        this.level = level;
        this.comment = comment;
        this.replySpeedLabel = replySpeedLabel;
        this.frequencyLabel = frequencyLabel;
        this.initiativeLabel = initiativeLabel;
        this.warmthLabel = warmthLabel;
        this.lengthLabel = lengthLabel;
        this.overlapLabel = overlapLabel;
        this.ratioLabel = ratioLabel;
    }

    // --- Getter 메소드 (Controller 및 View에서 사용) ---
    public int getScore() { return score; }
    public String getLevel() { return level; }
    public String getComment() { return comment; }
    public String getReplySpeedLabel() { return replySpeedLabel; }
    public String getFrequencyLabel() { return frequencyLabel; }
    public String getInitiativeLabel() { return initiativeLabel; }
    public String getWarmthLabel() { return warmthLabel; }
    public String getLengthLabel() { return lengthLabel; }
    public String getOverlapLabel() { return overlapLabel; }
    public String getRatioLabel() { return ratioLabel; }
    
    // Setter 메소드는 필요에 따라 추가하십시오.
}