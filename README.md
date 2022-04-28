# compilers_project

## Members:
  1. Mostafa Mahmoud Mostafa
  2. Mohamed Ali Mohamed
  3. Ahmed Mostafa Murad
  4. Mohamed Hani Abo El-Ela

## Week 1:

  * Use ANTLR Java grammar.
  * Test the grammar on a simple Java program to show the parse tree.
  * Show the starting rule of the grammar (compilationUnit).
    * As an example input file we considered that simple java program
      ![input_file1](https://user-images.githubusercontent.com/48108210/164872839-c563b693-f7da-4f68-ac77-d69fd6ff2de6.PNG)
    * The parse tree of that sample java program would be:
      ![parse_tree1](https://user-images.githubusercontent.com/48108210/164872848-b8336766-685e-40d6-aeb9-56ace4724864.PNG)

## Week 2:

  * Write a Java program based on ANTLR that takes a java file as an input and outputs a modified intermediate Java file (injected code).
  * Run the modified intermediate generated Java file to show which blocks of the code are visited..
    * As an example for a Java input file, we used this: 
      ![input_file_2](https://user-images.githubusercontent.com/48108210/164873195-a449428d-5e6e-4498-a946-1445864f25ef.PNG)
    * We could either generate the code in the console, or writing it to an output Java file as shown in screens
      ![run](https://user-images.githubusercontent.com/48108210/164873198-e4da35d9-db20-45ef-b4eb-1d0c31d85fe0.PNG)
      ![output](https://user-images.githubusercontent.com/48108210/164873199-8548c291-8c18-4016-9837-3e5b7d53e05f.PNG)
    * By running, the generated output Java file, we can show which blocks are visited in the console:
      ![output_run](https://user-images.githubusercontent.com/48108210/164873200-0e38a72a-b8e2-496b-b7d5-e84670d40d67.PNG)
    
## Week 3: 

  * Use the output from Week 2 to generate an HTML with highlighted red/green lines for visited/non-visited blocks. 
  * Full documentation using doxygen for the classes and functions developed only. 
  * Make sure that the pipeline does not include any manual effort. 
  * Show at least 3 Java examples that shows difficult scenarios. 
    * As an example for a Java input file, we used this: [Input.java]()  
      ![input](https://user-images.githubusercontent.com/48108210/165795142-cdb27143-d880-4e45-98eb-9cbc416377d0.PNG)
    * Then all the required files are generated automatically with a simple run of "CodeGenerator" driver code (in the main method)
    * The generated Output.java file:  
      [Output.java]()
    * The generated text file of the indices of the visited blocks: [visited_blocks.txt]()  
      ![visited_blocks](https://user-images.githubusercontent.com/48108210/165795149-d84be279-e665-4843-b1d8-797a339929c5.PNG)
    * The generated HTML file: [index.html]()  
      ![html](https://user-images.githubusercontent.com/48108210/165795133-e918f431-df9a-4e13-b4b4-8531040d7aeb.PNG)
