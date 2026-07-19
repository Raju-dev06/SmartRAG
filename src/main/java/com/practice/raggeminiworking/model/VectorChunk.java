package com.practice.raggeminiworking.model;

import java.util.List;

public class VectorChunk {

    private Long id;
    private String sessionId;
    private String text;
    private List<Double> embedding;

    private String sourceType;
    private String fileName;

    private Integer vectorRank;
    private Integer keywordRank;
    private Double rrfScore;

    public VectorChunk() {}

    public VectorChunk(Long id, String sessionId, String text, List<Double> embedding) {
        this.id = id;
        this.sessionId = sessionId;
        this.text = text;
        this.embedding = embedding;
    }

    public Long getId() { return id; }
    public String getSessionId() { return sessionId; }
    public String getText() { return text; }
    public List<Double> getEmbedding() { return embedding; }
    public String getSourceType() { return sourceType; }
    public String getFileName() { return fileName; }
    public Integer getVectorRank() { return vectorRank; }
    public Integer getKeywordRank() { return keywordRank; }
    public Double getRrfScore() { return rrfScore; }

    public void setId(Long id) { this.id = id; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public void setText(String text) { this.text = text; }
    public void setEmbedding(List<Double> embedding) { this.embedding = embedding; }
    public void setSourceType(String sourceType) { this.sourceType =sourceType; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setVectorRank(Integer vectorRank) { this.vectorRank = vectorRank; }
    public void setKeywordRank(Integer keywordRank) { this.keywordRank = keywordRank; }
    public void setRrfScore(Double rrfScore) { this.rrfScore = rrfScore; }
}