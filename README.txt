-- Critique of Design v1 -- 

My main critique of design v1 is that it contains multiple data transforming components, and according to Parsons' this is a bad design choice as it restricts contraction or extension. The two modules that do this are RandomizeAnswers and RandomizeProblems. Both of these permute a field in the FindAnswers and FindProblems classes respectively. The program does not fit Parsons' description of excessive information distribution as even though a field of one module is needed as input to another there is no way to circumvent this (the nature of the problem is sequential and the other solution is to have global variables and calculation, which would overcomplicate the randex.java file and show secrets). The randex program also does not contain components that perform more than one function as each is step is distinct and unique so there is no violation there. Trying to combine any (other than the aforementioned data transforming components, which I omit in my explanation below) results in an extremely long component that should really be broken up. Input is needed to process the file, FindProblems find and sorts the problems into a 1d array while FindAnswers does the same but for answers and into a 2d array, and Output is needed to format the output using all the data from these modules. 

-- Design v2 --

Input:

This module is going to be used to process the input file. It will read in a Latex file and store all the characters into an array of characters. The secrets are the exact implementation of how the file is processed. The purpose of this module is to confine the reading of input to only one place in the entire code and allow a single point of access to the read in data.

Uses Relation : USES: none; USED BY: FindProblems, FindAnswers, Output, and Randex.

This module is left unchanged. It contains a string field filename and a character array names chars (which is where all the characters in the file are stored and is what field will be utilized by other modules). The initilaizer sets the filename field and the method "execute" reads in the characters into the array chars. This is not a private variable so the other modules will access this by doing INPUT.chars. 


FindProblems:

This module is going to be used to find where the problems are in the array of characters. It does so by having two arrays and storing the start and end index for each problem. The secrets are the process used to determine these indices as well as the shuffling done by the Fisher-Yates algorithm on the array copy. The purpose of this module is to have a clear way to access the problems as well as their permutation.

Uses Relation : USES: Input; USED BY: FindAnswers, Output, and Randex. 

This module is a combination of the original FindProblems and RandomizeProblems. The fields used by other modules are the three integer arrays probStarts, probStops, and probPerms. There are some small local variables that are used in the processing. The method "execute" calculates the problem start and end indices and stores them in their respective arrays. It then uses the random number passed in to permute the problems, storing the result in probPerms. 

FindAnswers:

This module is used to find where the answers are in the array of characters. This differs from FindProblems since there can be several answers, making the resulting answerStarts and answerStops arrays being 2d-arrays. The secrets of this module are the process of finding the indicies for the answers as well as as the shuffling done. This module's purpose is to organize where the answers to each problem is so that it can be organized into output later. It utilizes the arrays of indices in FindProblem to know where to start looking.

Uses Relation : USES: Input, FindProblems; USED BY: Output, Randex. 

This module, like FindProblems, is a combination of FindAnswers and RandomizeAnswers. It is the same structure in that part of this module generates the 2d-arrays of indices along with the permutation of this array. The fields that will be used by other modules are the 2d int arrays answerStarts, answerStops, and answerPerms. The method "execute" calculates the indicies for the answers and populates the two respective arrays. It then uses the random number to generate answerPerms, the permutation for the answers.

Output:

This module is used to generate output from all of the previous modules. The secrets of this module are the operations needed to meaningfully print out the data based on the stored indicies. The purpose of this module to allow all the printing being done by a single module, making it easy to modify and accessible.

Uses Relation : USES: Input, FindAnswers, FindProblems; USED BY: Randex

This module's fields are the output stream, the input character array, and all 3 arrays in both FindProblems and FindAnswers. This is so that it can compile all of the info from the several other modules and consolidate into a stream of output. All of this is in the "execute" method, the only one used by other modules.

Randex:

This module is going to be used to control the flow of the program (essentially it is main). It does so by creating instances of the previously mentioned modules and calling their execute methods. The secrets in this module are just the control flow of the program as any deeper workings are in the modules themselves. 

Uses Relation : USES: Input, FindProblems, FindAnswers, Output; USED BY: none

This module's fields are every other module mentioned. This is because this is the the "main" function of the program and needs access to every module to make the program run smoothly. While this may sound against best practices, as mentioned previously this is a very sequential program so this is required to ensure we process one piece at a time. 


--Anticipation of Change--

0. Better/more robust error reporting - v1 and v2 would both need to include more messages within their modules. v1's will end up more complex due to the two extra modules while v2 would be more condensed and easier to read.

1. Ignore text in LaTeX comments (comments start with % and extend to end of line) - v1 and v2 both utilize FindProblem and FindAnswer to process the array "chars" to form indices. Ignoring latex commands would need to be worked into these modules, or potentially into the input module itself. In either case, both versions would need to make some module larger.

2. An exam may be divided into sections.  Randomize within each section but do not move a problem from one section to another. - v1 would struggle with this due to having the permutation of the problems and finding them separate. v2 would have a much better time since everything is in scope for both of these issues.
