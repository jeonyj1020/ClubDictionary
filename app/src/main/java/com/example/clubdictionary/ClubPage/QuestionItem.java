package com.example.clubdictionary.ClubPage;

public class QuestionItem {

    String question;
    String answer;
    String questionId;
    String answerId;
    String itemId;

    public QuestionItem() {
    }

    public QuestionItem(String question, String answer, String questionId, String answerId, String itemId) {
        this.question = question;
        this.answer = answer;
        this.questionId = questionId;
        this.answerId = answerId;
        this.itemId = itemId;
    }

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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


}
