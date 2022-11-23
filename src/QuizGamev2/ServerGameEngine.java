package QuizGamev2;

import QuizGame.PlayerClient;

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

    public void separateScoreString(String pointString){
        String[] scoreString = pointString.split("," );
        boolean isCorrectAnswer = Boolean.parseBoolean(scoreString[1]);
        ServerPlayer player = new ServerPlayer(scoreString[0]); //lägg till en ny construktor i Serverplayer? Blir det samma player då?
        if (isCorrectAnswer == true){
            player.points ++;
            scoreToString(player.points);
        }
        else {
            scoreToString(player.points);
        }
    }

    public int countScore(int state, boolean isCorrectAnswer, ServerPlayer player){
        if (state == 3  && isCorrectAnswer == true){
            player.points++;
            scoreToString(player.points);
        }
        else if (state == 4){
            scoreToString(player.points);
        }
        return player.points;
    }

    public String scoreToString(int points){

        String pointString = "Your score: " + points;

        return pointString;

    }






}
