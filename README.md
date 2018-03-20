LambdaCalculusInterpreter
=========================

A little lambda calculus interpreter; just a bit of a project to have something entirely my own work online.  

When run, it pops up a window with three sections:
- The field at the bottom is the input field, and the only one that can be 
edited
- The area in the top left is for displaying the history of inputs and resultant
computations
- The area in the top right is for displaying the names of user-defined
variables  

Lambda expressions can be typed into the input field, using the standard form of
[lambda]param.expression
To type the lambda character, use the semicolon key.  
All applications must be parenthesized (parentheses are necessary to the
parsing algorithm)  
Variable names must be alphanumeric  
The enter key enters the lambda expression to be interpreted.  
To store the result in a user-defined variable, precede your expression with an id and the equal sign, resulting in the form  
  id = expression  
ids must also be alphanumeric and can be reassigned  

For help in-program, type "help"  
To exit, type "exit"  

The program now also has an editor, which can be opened by typing "editor"
into the input field.  
The editor provides an area to type, which also changes the semicolon into a 
lambda. To run the editor's contents, hit "Run All" in the Run menu, or 
press F9.
Each line acts as an individual statement, and the lines are executed in order.
The editor also allows saving and loading of contents. While no filetype is 
properly associated with the editor, I have taken to using the extension
".lamc".  

To run from jar file, use the command line arguments  
"java -Xss64m -Dfile.encoding=UTF-8 -jar LambdaInterp.jar"
