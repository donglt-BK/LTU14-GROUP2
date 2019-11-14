package com.bk.olympia.model;

import com.bk.olympia.DatabaseImport;

public class Trivia {
    private String category;
    private String difficulty;
    private String question;

    private String correct_answer;
    private String[] incorrect_answers;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDifficulty() {
        return DatabaseImport.convertDifficulty(difficulty);
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return DatabaseImport.convertHtmlCharacters(question);
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correct_answer = correctAnswer;
    }

    public String[] getIncorrectAnswers() {
        return incorrect_answers;
    }

    public void setIncorrectAnswers(String[] incorrectAnswers) {
        this.incorrect_answers = incorrectAnswers;
    }
}