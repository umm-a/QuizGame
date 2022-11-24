package QuizGamev2;

import java.util.ArrayList;
import java.util.List;

public class ServerGameEngine{




    ServerPlayer player1;
    ServerPlayer player2;

    String nickName1;
    String nickName2;


    ServerPlayer currentPlayer;
    List<Question> tempQuestionList = new ArrayList<>();

    QuestionDatabase2 questionDatabase2;


    public ServerGameEngine(QuestionDatabase2 questionDatabase2){
        this.questionDatabase2 = questionDatabase2;

    }
    public void addQuestionToList(Question question){
        this.tempQuestionList.add(question);
    }
    public void removeContentsFromQuestionList(){
        for (Question q: tempQuestionList) {
            this.tempQuestionList.remove(q);
        }
    }
    public Question getFromQuestionList(int i){
        return tempQuestionList.get(i);
    }


}
