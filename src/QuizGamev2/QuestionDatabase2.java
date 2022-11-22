package QuizGamev2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionDatabase2 implements Serializable {

    List<Question> questionlist;
    List<String> categoryList;
    String filename = "src/QuizGamev2/Questionfile.txt";


    public QuestionDatabase2(){
        questionlist = loadQuestions(filename);
        categoryList = createCategorylist(questionlist);

        System.out.println(categoryList);
    }

    //laddar frågor från textfil, detta kan i ett senare skede expanderas till egna filer per kategori
    public List<Question> loadQuestions(String filename){
        List<Question> mylist = new ArrayList<>();
        String temp;
        try(BufferedReader buf = new BufferedReader(new FileReader(filename))){
            while((temp = buf.readLine()) != null){
            String[] arr = temp.split(",");
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = arr[i].trim();
                }
            mylist.add(new Question(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mylist;
    }


    //skapar en lista med enbart kategorier utifrån tillgängliga frågor
    public List<String> createCategorylist(List<Question> qlist){
        List<String> mycategorylist = new ArrayList<>();
        for(Question q: questionlist){
            if(!mycategorylist.contains(q.category)){
                mycategorylist.add(q.category);
            }
        }
        mycategorylist.add("CATEGORIES");
        return mycategorylist;
    }

    //genererar en random question efter vald kategori
    public Question generateRandomQuestion(String myCategory){
        List<Question> myQuestionList = new ArrayList<>();
        for(Question q:questionlist){
            if(myCategory.equals(q.category)){
                myQuestionList.add(q);
            }
        }
        Collections.shuffle(myQuestionList);
        return myQuestionList.get(0);
    }
}
