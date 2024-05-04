package edu.udel.cisc675.randex;
import java.util.ArrayList;

/* Module FindAnswers: searches through the character array from the file,
   and for each problem, finds the starting and ending points of each answer.
   The answer includes "\item" and ends just before the next "\item" or
   "\end{enumerate}". */
public class FindAnswers {

    // strings we will search for...
    
    public final static char[] beginEnumerate =
	"\\begin{enumerate}".toCharArray();
    
    public final static char[] item = "\\item".toCharArray();
    
    public final static char[] endEnumerate =
	"\\end{enumerate}".toCharArray();

    /* The chars array generated by Input module (in) */
    char[] chars;

    /* Start index of each problem, from module FindProblems (in) */
    int[] probStarts;

    /* Stop index of each problem, from module FindProblems (in) */
    int[] probStops;

    /* For each problem i, start index of each answer to the problem
       (out).  This is an array of length number of problems.
       answerStarts[i] is an array whose length is the number of
       answers to the i-th problem.  answerStarts[i][j] is the index
       in chars of the first character of the j-th answer to the i-th
       problem. */
    int[][] answerStarts;

    /* Like answerStarts, except it gives the stop index of each
       answer (out).  That is 1 greater than the index of the last
       character of the answer. */
    int[][] answerStops;

	/* For each i in 0..numProblems - 1, the number of answers to
     * problem i (in). */
    int[] numAnswers;

    /* Random number generator (in) */
    RandomGenerator rand;

    /* A permutation for each problem (out) */
    int[][] answerPerms;

    
    /* Constructs new FindAnswers instance from the given data.  Sets
       the fields and does nothing else. */
    public FindAnswers(char[] chars, int[] probStarts, int[] probStops, RandomGenerator rand) {
	this.chars = chars;
	this.probStarts = probStarts;
	this.probStops = probStops;
	this.rand = rand;
    }

    /* Tells you whether the sequence of chars starting at position in
       array chars matches those of c */
    private boolean match(int off, char[] c) {
	int n = c.length;
	if (off+n > chars.length)
	    return false;
	for (int i=0; i<n; i++) {
	    if (c[i] != chars[off+i])
		return false;
	}
	return true;
    }

    /* Converts an array list of Integer to an array of int. */
    private static int[] toArray(ArrayList<Integer> list) {
	int n = list.size();
	int[] result = new int[n];
	for (int i=0; i<n; i++)
	    result[i] = list.get(i);
	return result;
    }

    /* Finds the answers to the problem with ID pid, setting
       answerStarts[pid] and answerStops[pid].  */
    private void findAnswersInProblem(int pid) {
	ArrayList<Integer> startList = new ArrayList<>(),
	    stopList = new ArrayList<>();
	int i = probStarts[pid]; // starting character index for problem pid
	int stop = probStops[pid];
	for (; i < stop && !match(i, beginEnumerate); i++) ;
	if (i == stop)
	    throw new RuntimeException
		("No \\begin{enumerate} found for problem "+pid);
	for (; i < stop; i++) {
	    if (match(i, endEnumerate)) {
		if (!startList.isEmpty()) stopList.add(i);
		break;
	    }
	    if (match(i, item)) {
		if (!startList.isEmpty()) stopList.add(i);
		startList.add(i);
	    }
	}
	if (i == stop)
	    throw new RuntimeException
		("No \\end{enumerate} found for problem "+pid);
	int nanswer = startList.size();
	assert nanswer == stopList.size();
	answerStarts[pid] = toArray(startList);
	answerStops[pid] = toArray(stopList);
    }

	/* Constructs random permutation for problem pid, writing to
       answerPerms[pid][*]. */
    private void randomizeProblem(int pid) {
	int nanswer = numAnswers[pid];
	for (int i=0; i<nanswer; i++)
	    answerPerms[pid][i] = i;
	//System.out.print("rands = ");
	for (int i=nanswer-1; i>=0; i--) {
	    int j = rand.nextInt(i+1);
	    //System.out.print(" "+j);
	    if (i!=j) {
		int t = answerPerms[pid][i];
		answerPerms[pid][i] = answerPerms[pid][j];
		answerPerms[pid][j] = t;
	    }
	}
	//System.out.println();
    }

    /* Constructs answerStarts and answerStops. */
    public void execute() {
	int nprob = probStarts.length;
	answerStarts = new int[nprob][];
	answerStops = new int[nprob][];
	for (int i=0; i<nprob; i++)
	    findAnswersInProblem(i);
    }

	for (int i=0; i<numAnswer.length; i++){
		numAnswers[i] = answerStarts[i].length;
	}
	int nb = numAnswers.length;
	answerPerms = new int[nb][];
	for (int i=0; i<nb; i++) {
	    answerPerms[i] = new int[numAnswers[i]];
	    randomizeProblem(i);
	}
}