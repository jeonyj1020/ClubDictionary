package com.example.clubdictionary.ClubPage;

public class sampleText {
    String question, answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public sampleText() {
    }

    public sampleText(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}