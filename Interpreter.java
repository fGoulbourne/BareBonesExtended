import java.util.*;

public class Interpreter {
  private static String[] doc;
  private HashMap<String, Integer> variables = new HashMap<>();
  private int arrayLength = 0;
  private static final String[] bc = {"while", "for", "if"};
  private static final List<String> blockCreators = Arrays.asList(bc);

  public Interpreter(String[] docText) {
    doc = docText;

  }

  //splits the line from the string into each key part
  //includes trimming excess whitespace
  private String[] parseLine(int i,  String[] document) {
    String trimDoc = document[i].trim();
    return trimDoc.split("\s|;");
  }

  //Selects an operation and calls the correct method
  //the inputs determine the line itself, split by spaces
  //the line number of the line relative to the code block
  //and the code block used
  private void operation(String[] line, int lineNum, String[] document) {
    switch (line[0]) {
      case "clear" -> clearVar(line[1]);
      case "incr" -> incrVar(line[1]);
      case "decr" -> decrVar(line[1]);
      case "while" -> whileVar(line, lineNum, document);
      case "add" -> addVar(line[1], line[2]);
      case "sub" -> subVar(line[1], line[2]);
      case "mod" -> modVar(line[1], line[2]);
      case "for" -> forVar(line, lineNum, document);
      case "set" -> setVar(line[1], line[2]);
      case "if" -> ifVar(line, lineNum, document);
    }


  }
  private void clearVar(String var) {
    variables.put(var, 0);

  }

  private void setVar(String var, String var2String) {

    Integer var2;
    //sets var2 to be either an integer,
    //or a variable already defined
    try {
      var2 = Integer.parseInt(var2String);
    } catch(Exception e) {
      var2 = variables.get(var2String);
    }

    variables.put(var, var2);
  }

  private void incrVar(String var) {
    Integer value = variables.get(var);
    if(value == null){
      variables.put(var, 1);

    } else {
      variables.put(var, value + 1);

    }
  }

  private void decrVar(String var) {
    Integer value = variables.get(var);
    if(value == null || value <= 1) {
      variables.put(var, 0);

    } else {
      variables.put(var, value - 1);

    }
  }

  private void addVar(String var1, String var2String) {
    Integer value = variables.get(var1);
    Integer var2;
    //sets var2 to be either an integer,
    //or a variable already defined
    try {
      var2 = Integer.parseInt(var2String);
    } catch(Exception e) {
      var2 = variables.get(var2String);
    }


    if(value == null){
      variables.put(var1, var2);

    } else {
      variables.put(var1, value + var2);

    }
  }

  private void subVar(String var1, String var2String) {
    Integer value = variables.get(var1);
    Integer var2;
    //sets var2 to be either an integer,
    //or a variable already defined
    try {
      var2 = Integer.parseInt(var2String);
    } catch(Exception e) {
      var2 = variables.get(var2String);
    }


    if(value == null || value < var2){
      variables.put(var1, 0);

    } else {
      variables.put(var1, value - var2);

    }
  }

  private void modVar(String var1, String var2String) {
    Integer value = variables.get(var1);
    Integer var2;
    //sets var2 to be either an integer,
    //or a variable already defined
    try {
      var2 = Integer.parseInt(var2String);
    } catch(Exception e) {
      var2 = variables.get(var2String);
    }


    if(value == null){
      variables.put(var1, 0);

    } else {
      variables.put(var1, value % var2);

    }
  }

  //Sets a variable and end point, lineCount determines startpoint
  private void whileVar(String[] line, int lineCount, String[] doc) {

    //block all giving the while information, including
    //where the loop starts in the code block
    //so the code doesn't reread it after the loop is over
    String var = line[1];                //Variable number

    Integer var2; //End of while
    Integer var3;  //for OR and AND statements

    try {
      var2 = Integer.parseInt(line[3]);
    } catch(Exception e) {
      var2 = variables.get(line[3]);
    }

    //sets the value of the var, however if it is not declared,
    //sets to null rather than crashing
    try{
      var3 = Integer.parseInt(line[4]);
    } catch(NumberFormatException e) {
      var3 = variables.get(line[4]);
    } catch(ArrayIndexOutOfBoundsException f){
      var3 = 0;
    }

    String[] array = generateBlock(lineCount, doc);

    //completes the while loop depending on operand
    switch (line[2]) {
      case "not" :
        while (!variables.get(var).equals(var2)) {
          calculate(array);

        }
        break;
      case "is" :
        while (variables.get(var).equals(var2)) {
          calculate(array);

        }
        break;
      case "<=" :
        while (variables.get(var) <= var2) {
          calculate(array);

        }
        break;
      case "<" :
        while (variables.get(var) < var2) {
          calculate(array);

        }
        break;
      case ">=" :
        while (variables.get(var) >= var2) {
          calculate(array);

        }
        break;
      case ">" :
        while (variables.get(var) > var2) {
          calculate(array);

        }
        break;
      case "between" :
        while ((variables.get(var) > var2 && variables.get(var) < var3 )
            || (variables.get(var) < var2 && variables.get(var) > var3 )) {

          calculate(array);

        }
        break;

    }

    arrayLength = array.length + 1;

  }

  private void forVar(String[] line, int lineCount, String[] doc) {

    //same as while loop but includes a 3rd arg

    //block all giving the while information, including
    //where the loop starts in the code block
    //so the code doesn't reread it after the loop is over

    boolean endSeg = false; //checks if we are at the end part of the loop definition
    List<String> commandList = new ArrayList<String>();

    for(String i : line){
      if(endSeg) {
        commandList.add(i);
      }

      if(i.equals(",")) {
        endSeg = true;
      }
    }
    String[] command = commandList.toArray(new String[0]);

    String var = line[1];                //Variable number

    Integer var2; //End of while
    Integer var3;  //for OR and AND statements

    try {
      var2 = Integer.parseInt(line[3]);
    } catch(Exception e) {
      var2 = variables.get(line[3]);
    }

    //sets the value of the var, however if it is not declared,
    //sets to null rather than crashing
    try{
      var3 = Integer.parseInt(line[4]);
    } catch(NumberFormatException e) {
      var3 = variables.get(line[4]);
    } catch(ArrayIndexOutOfBoundsException f){
      var3 = 0;
    }

    String[] array = generateBlock(lineCount, doc);

    //completes the while loop depending on operand
    switch (line[2]) {
      case "not" :
        while (!variables.get(var).equals(var2)) {
          calculate(array);
          operation(command, 0, command);

        }
        break;
      case "is" :
        while (variables.get(var).equals(var2)) {
          calculate(array);
          operation(command, 0, command);
        }
        break;
      case "<=" :
        while (variables.get(var) <= var2) {
          calculate(array);
          operation(command, 0, command);
        }
        break;
      case "<" :
        while (variables.get(var) < var2) {
          calculate(array);
          operation(command, 0, command);
        }
        break;
      case ">=" :
        while (variables.get(var) >= var2) {
          calculate(array);
          operation(command, 0, command);
        }
        break;
      case ">" :
        while (variables.get(var) > var2) {
          calculate(array);
          operation(command, 0, command);
        }
        break;
      case "between" :
        while ((variables.get(var) > var2 && variables.get(var) < var3 )
          || (variables.get(var) < var2 && variables.get(var) > var3 )) {

          calculate(array);
          operation(command, 0, command);
        }
        break;

    }

    arrayLength = array.length + 1;

  }

  private void ifVar(String[] line, int lineCount, String[] doc){
    String var = line[1];
    Integer var2; //End of while
    Integer var3;  //for OR and AND statements
    String[] array = generateBlock(lineCount, doc);

    try {
      var2 = Integer.parseInt(line[3]);
    } catch(Exception e) {
      var2 = variables.get(line[3]);
    }

    //sets the value of the var, however if it is not declared,
    //sets to null rather than crashing
    try{
      var3 = Integer.parseInt(line[4]);
    } catch(NumberFormatException e) {
      var3 = variables.get(line[4]);
    } catch(ArrayIndexOutOfBoundsException f){
      var3 = 0;
    }

    switch (line[2]) {
      case "not" :
        if (!variables.get(var).equals(var2)) {
          calculate(array);

        }
        break;
      case "is" :
        if (variables.get(var).equals(var2)) {
          calculate(array);

        }
        break;
      case "<=" :
        if (variables.get(var) <= var2) {
          calculate(array);

        }
        break;
      case "<" :
        if (variables.get(var) < var2) {
          calculate(array);

        }
        break;
      case ">=" :
        if (variables.get(var) >= var2) {
          calculate(array);

        }
        break;
      case ">" :
        if (variables.get(var) > var2) {
          calculate(array);

        }
        break;
      case "between" :
        if ((variables.get(var) > var2 && variables.get(var) < var3 )
          || (variables.get(var) < var2 && variables.get(var) > var3 )) {

          calculate(array);

        }
        break;

    }

    arrayLength = array.length + 1;
  }

  public String[] generateBlock(int lineCount, String[] doc) {
    lineCount++;                                    //selects next line
    List<String> loopCode = new ArrayList<String>();// for adding each new line of code
    String lineCheck = doc[lineCount];              //the line, trimmed afterwards
    String[] parsedLine = (lineCheck.trim()).split("\s|;");
    int whileCount = 0;                             //checks the end statement is the correct one

    //Generates a loop array, filling up a code block to be
    //worked on by the operator method
    while(!parsedLine[0].equals("end") || whileCount != 0) {

      loopCode.add(lineCheck);


      if(blockCreators.contains(parsedLine[0])) {
        whileCount++;
      } else if( parsedLine[0].equals("end")) {
        whileCount--;
      }
      lineCount++;
      lineCheck = doc[lineCount];
      parsedLine = (lineCheck.trim()).split("\s|;");

    }

    return loopCode.toArray(new String[0]);
  }



  //takes in a block of code, and calculates it
  //also pushes the block of code forward for further
  //looping if necessary
  public void calculate(String[] documents) {

    for(int i = 0; i<documents.length; i++) {

      operation(parseLine(i, documents), i, documents);
      if(arrayLength != 0) {
        i += arrayLength;
        arrayLength = 0;
      }

    }
  }

  //same as above, but will start the code block
  // from the imported doc and parse
  public void calculate() {

    for (int i = 0; i < doc.length; i++) {

      operation(parseLine(i, doc), i, doc);
      if(arrayLength != 0) {
        i += arrayLength;

        arrayLength = 0;
      }



    }
  }

  public HashMap getVars() {
    return variables;

  }

}