package com.bk.olympia.model;

import com.bk.olympia.DatabaseImport;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Trivia implements Comparable<Trivia> {
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
        return DatabaseImport.convertHtmlCharacters(correct_answer);
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correct_answer = correctAnswer;
    }

    public String[] getIncorrectAnswers() {
        return Arrays.stream(incorrect_answers).map(DatabaseImport::convertHtmlCharacters).toArray(String[]::new);
    }

    public void setIncorrectAnswers(String[] incorrectAnswers) {
        this.incorrect_answers = incorrectAnswers;
    }

    public String[] getAllAnswers() {
        List<String> answers = Arrays.stream(getIncorrectAnswers()).collect(Collectors.toList());
        answers.add(getCorrectAnswer());
        Collections.shuffle(answers);
        return answers.toArray(new String[0]);
    }

    @Override
    public int compareTo(@NotNull Trivia o) {
        return this.getQuestion().compareTo(o.getQuestion());
    }
}
