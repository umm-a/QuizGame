package QuizGamev2;

public class ServerGameEngine{




    ServerPlayer player1;
    ServerPlayer player2;

    String nickName1;
    String nickName2;


    ServerPlayer currentPlayer;

    QuestionDatabase2 questionDatabase2;


    public ServerGameEngine(QuestionDatabase2 questionDatabase2){
        this.questionDatabase2 = questionDatabase2;

    }



}
