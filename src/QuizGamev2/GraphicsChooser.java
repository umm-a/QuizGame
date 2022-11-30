package QuizGamev2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphicsChooser {

    Shapes myshapes;
    Colors mycolors = new Colors();

    List<Shapes> shapesList= new ArrayList<>();

    public GraphicsChooser(){
        //Manuellt:
       // myshapes = new Shapes7(mycolors.colormixList.get(0));

        //Slumpat:
      //  shapesList.add(new Shapes1(mycolors.colormixList.get(0)));  // samma som Shape2 - skapa en unik shape1!
        shapesList.add(new Shapes2(mycolors.colormixList.get(0)));
        shapesList.add(new Shapes3(mycolors.colormixList.get(0)));
        shapesList.add(new Shapes4(mycolors.colormixList.get(0)));
        shapesList.add(new Shapes5(mycolors.colormixList.get(0)));
        shapesList.add(new Shapes6(mycolors.colormixList.get(0)));
        shapesList.add(new Shapes7(mycolors.colormixList.get(0)));
        Collections.shuffle(shapesList);
        myshapes = shapesList.get(0);
    }

}
