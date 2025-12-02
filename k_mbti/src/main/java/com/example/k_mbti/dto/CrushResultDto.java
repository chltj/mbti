package com.example.k_mbti.dto; // ← 패키지 경로는 프로젝트에 맞게 수정해줘

public class CrushResultDto {

    private int score;                 // 0 ~ 100
    private String level;              // 예: "관심 많음 / 썸 느낌 💓"
    private String comment;            // 전체 코멘트

    private String replySpeedLabel;    // 답장 속도 라벨
    private String frequencyLabel;     // 대화 빈도 라벨
    private String initiativeLabel;    // 먼저 말 거는 비율 라벨
    private String warmthLabel;        // 말투/이모티콘 따뜻함 라벨

    public CrushResultDto() {
    }

    public CrushResultDto(int score, String level, String comment,
                          String replySpeedLabel, String frequencyLabel,
                          String initiativeLabel, String warmthLabel) {
        this.score = score;
        this.level = level;
        this.comment = comment;
        this.replySpeedLabel = replySpeedLabel;
        this.frequencyLabel = frequencyLabel;
        this.initiativeLabel = initiativeLabel;
        this.warmthLabel = warmthLabel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReplySpeedLabel() {
        return replySpeedLabel;
    }

    public void setReplySpeedLabel(String replySpeedLabel) {
        this.replySpeedLabel = replySpeedLabel;
    }

    public String getFrequencyLabel() {
        return frequencyLabel;
    }

    public void setFrequencyLabel(String frequencyLabel) {
        this.frequencyLabel = frequencyLabel;
    }

    public String getInitiativeLabel() {
        return initiativeLabel;
    }

    public void setInitiativeLabel(String initiativeLabel) {
        this.initiativeLabel = initiativeLabel;
    }

    public String getWarmthLabel() {
        return warmthLabel;
    }

    public void setWarmthLabel(String warmthLabel) {
        this.warmthLabel = warmthLabel;
    }
}
