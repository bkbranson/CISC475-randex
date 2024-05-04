package edu.udel.cisc675.randex;
import java.util.ArrayList;

/* Module FindProblems: finds all problems (text beginning with
   "\begin{problem}" and ending with "\end{problem}") in the given
   character array.  Constructs arrays probStarts and probStops.  Each
   has length n, the number of problems.  probStarts[i] is the index
   in chars of the first character of problem i.  probStops[i] is 1
   more than the index in chars of the last character of problem i.

   The client should instantiate this class with the chars produced
   by module Input, then call method execute, then read the fields
   probStarts and probStops. */
public class FindProblems {

    /* "\begin{problem}" as character array. */
    public final static char[] beginProblem =
	"\\begin{problem}".toCharArray();

    /* "\end{problem}" as character array. */
    public final static char[] endProblem =
	"\\end{problem}".toCharArray();

    /* The characters of the original file, from module Input (in). */
    char[] chars;

    /* The start index of each problem (out). */
    int[] probStarts;

    /* The stop index of each problem (out). */
    int[] probStops;

    /* The random number generator (in) */
    RandomGenerator rand;

    /* Permutation of problem IDs (out) */
    int[] probPerm;

    /* Constructor: sets chars field and does nothing else. */
    public FindProblems(char[] chars, RandomGenerator rand) {
	this.chars = chars;
	this.rand = rand;
    }

    /* Determines whether the sequence of characters in chars starting
       at offset off matches the chars of sought. */
    private static boolean match(char[] chars, int off, char[] sought) {
	for (int i=0; i<sought.length; i++) {
	    if (off+i >= chars.length || sought[i] != chars[off+i])
		return false;
	}
	return true;
    }

    /* Constructs probStarts and probStops. */
    public void execute() {
	ArrayList<Integer> startList = new ArrayList<>(),
	    stopList = new ArrayList<>();
	int n = chars.length;
	boolean inProblem = false; // is i currently inside a problem?
	for (int i=0; i<n; i++) {
	    if (match(chars, i, beginProblem)) {
		if (inProblem)
		    throw new RuntimeException
			("Encountered \\begin{problem} when inside a problem");
		startList.add(i);
		inProblem = true;
	    } else if (match(chars, i, endProblem)) {
		if (!inProblem)
		    throw new RuntimeException
			("Encountered \\end{problem} when outside any problem");
		inProblem = false;
		stopList.add(i + endProblem.length);
	    }
	}
	if (inProblem)
	    throw new RuntimeException
		("Missing \\endProblem for last problem");
	int nprob = startList.size();
	assert nprob == stopList.size();
	probStarts = new int[nprob];
	probStops = new int[nprob];
	for (int i=0; i<nprob; i++)
	    probStarts[i] = startList.get(i);
	for (int i=0; i<nprob; i++)
	    probStops[i] = stopList.get(i);

	int np = probStarts[i].length;
	this.probPerm = new int[np];
	for (int i=0; i<np; i++)
	    probPerm[i] = i;
	//System.out.print("rands = ");
	for (int i=np-1; i>=0; i--) {
	    int j = rand.nextInt(i+1);
	    //System.out.print(" "+j);
	    if (i!=j) {
		int t = probPerm[i];
		probPerm[i] = probPerm[j];
		probPerm[j] = t;
	    }
	}
	//System.out.println();
    }

}
