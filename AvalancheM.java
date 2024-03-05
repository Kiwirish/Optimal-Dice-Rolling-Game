import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

public class AvalancheM extends Rollin {

    Random R = new Random();
    private boolean debug = false;

    public AvalancheM() {

    }

    public AvalancheM(boolean debug) {
        this.debug = debug;
    }

    @Override
    public int handleRoll(int roll, int[] dice) {
        // Check for complete
        if (Rollin.isComplete(dice)) {
            return -1;
        }

        // Check brute force complete
        for (int i = 0; i < dice.length; i++) {
            int[] copy = CopyArray(dice);

            copy[i] = roll;
            if (Rollin.isComplete(copy)) {
                // System.out.println("ProbSel force complete");
                return i;
            }
        }


        // decide which one to change if not instant complete
        return GetProbSwap(roll, dice);


        // return R.nextInt(6);
    }

    private int[] CopyArray(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i]; 
        }
        return b;
    }

    private int GetProbSwap(int roll, int[] dice) {
        ArrayList<int[]> sets = new ArrayList<>();

        // Get current sets
        for (int[][] si : setIndices) {
            if (isSet(si[0], dice)) {
                sets.add(si[0]);
            }

            if (isSet(si[1], dice)) {
                sets.add(si[1]);
            }
        }

        if (sets.size() < 1) { // if no set then random
            // System.out.println("ProbSel no set use random (" + sets.size() + ")");
            return R.nextInt(6);
        } else {
            // System.out.println("ProbSel use smart sel");
        }

        Scoring[] scores = new Scoring[sets.size()];

        // Compute a relative set score based of the remaining numbers
        for (int i = 0; i < sets.size(); i++) {
            int[] altSet = new int[3]; // The numbers not in the set
            int j = 0;
            for (int k = 0; k < dice.length; k++) { // Build the alt set
                if (!InArray(k, sets.get(i))) {
                    altSet[j] = dice[k];
                    j++;
                }
            }

            // Do check

            scores[i] = ScoreFunction(roll, altSet, dice);
        }

        Arrays.sort(scores);

        return scores[0].GetSwapIndex();
    }

    private Scoring ScoreFunction(int roll, int[] set, int[] dice) {
        int[] indices = { 0, 1, 2 };

        ArrayList<IndexAndSet> possibleNumbers = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            possibleNumbers.add(new IndexAndSet(i));

            int[] setCopy = CopyArray(set);

            if (i == 3) {
                // Do no swap check
            } else {
                setCopy[i] = roll; // Swap number
            }

            // now do check
            for (int j = 0; j < setCopy.length; j++) {
                // What are all the alternative numbers this index could be to still make a set
                // For each index try all possible numbers with the isSet command to check if it makes a set, if it does include it in the possibleNumbers HashSet
                for (int k = 1; k <= 6; k++) {
                    int[] setCopyCopy = CopyArray(setCopy);
                    setCopyCopy[j] = k;
                    if (isSet(indices, setCopyCopy)) {
                        possibleNumbers.get(i).GetHastSet().add(k);
                    }
                }
            }
        }

        // Find highest scores
        possibleNumbers.sort(new IndexAndSetComparator());

        possibleNumbers.toString();

        return new Scoring(possibleNumbers.get(0).GetSwapIndex(), possibleNumbers.get(0).GetScore());
    }

    private boolean InArray(int num, int[] array) {
        for (int n : array) {
            if (n == num) {
                return true;
            }
        }

        return false;
    }

    /**
     * InnerAvalancheM
     */
    public class Scoring implements Comparable<Scoring> {
        private int swapIndex;
        private float score;
        
        public Scoring(int swapIndex, float score) {
            this.swapIndex = swapIndex;
            this.score = score;
        }

        public int GetSwapIndex() {
            return swapIndex;
        }

        public float GetScore() {
            return score;
        }

        @Override
        public int compareTo(AvalancheM.Scoring o) {
            if (this.GetScore() == o.GetScore()) 
                return 0; 
            else if (this.GetScore() < o.GetScore()) 
                return 1; 
            else
                return -1; 
        }

    }

    public class IndexAndSet  {
        HashSet<Integer> set;
        int indexSwap;

        public IndexAndSet(int indexSwap) {
            set = new HashSet<Integer>();

            this.indexSwap = indexSwap;
        }

        public int GetSwapIndex() {
            return indexSwap;
        }

        public HashSet<Integer> GetHastSet() {
            return set;
        }

        public float GetScore() {
            return set.size() / 6;
        }
    }

    public class IndexAndSetComparator implements Comparator<IndexAndSet> { 
  
        // override the compare() method 
        public int compare(IndexAndSet s1, IndexAndSet s2) 
        { 
            if (s1.GetHastSet().size() == s2.GetHastSet().size()) 
                return 0; 
            else if (s1.GetHastSet().size() < s2.GetHastSet().size()) 
                return 1; 
            else
                return -1; 
        } 
    } 
    
}
