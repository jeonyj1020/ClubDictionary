package com.example.clubdictionary;

public class GroupPostInfo {
    private String title;
    private String content;
    private String kakaoLink;
    private String publisher;
    public GroupPostInfo() {
    }

    public GroupPostInfo(String title, String content, String kakaoLink, String publisher) {
        this.title = title;
        this.content = content;
        this.kakaoLink = kakaoLink;
        this.publisher = publisher;
    }

    public String getKakaoLink() {
        return kakaoLink;
    }

    public void setKakaoLink(String kakaoLink) {
        this.kakaoLink = kakaoLink;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
