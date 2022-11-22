package QuizGamev2;

import java.io.Serializable;

public class Question implements Serializable {

    String category;
    String question;
    String answerCorrect;
    String answerOption2;
    String answerOption3;
    String answerOption4;
    String questionText; //todo ändrat här

    public Question(String category, String question, String answerCorrect, String answerOption2, String answerOption3, String answerOption4, String questionText) {
        this.category = category;
        this.question = question;
        this.answerCorrect = answerCorrect;
        this.answerOption2 = answerOption2;
        this.answerOption3 = answerOption3;
        this.answerOption4 = answerOption4;
        this.questionText = questionText;
    }
}
