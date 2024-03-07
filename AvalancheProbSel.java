import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

public class AvalancheProbSel extends Rollin {

    Random R = new Random();
    private boolean debug = false;

    public AvalancheProbSel() {

    }

    public AvalancheProbSel(boolean debug) {
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
                if (debug) {
                    System.out.println("ProbSel force complete");
                }
                return i;
            }
        }


        // decide which one to change if not instant complete
        return GetProbSwap(roll, dice);
    }

    private int[] CopyArray(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i]; 
        }
        return b;
    }

    private int GetProbSwap(int roll, int[] dice) {
        ArrayList<int[]> sets = new ArrayList<>(); // a list of sets, where the set contains the indices to numbers in dice

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
            if (debug) {
                System.out.println("ProbSel no set use random (" + sets.size() + ")");
            }
            return R.nextInt(6);
        } else {

            if (debug) {
                System.out.println("ProbSel use smart sel");
            }
        }

        Scoring[] scores = new Scoring[sets.size()];

        // Compute a relative set score based of the remaining numbers
        for (int i = 0; i < sets.size(); i++) {
            int[] altSet = new int[3]; // The numbers not in the set
            int[] realIndices = new int[3];
            int j = 0;
            for (int k = 0; k < dice.length; k++) { // Build the alt set
                if (!InArray(k, sets.get(i))) {
                    altSet[j] = dice[k];
                    realIndices[j] = k;
                    j++;
                }
            }

            // Do check

            Scoring score = ScoreFunction(roll, altSet, dice);
            int relativeIndex = score.GetSwapIndex();
            int actualIndex = FindIndexOfValue(relativeIndex, realIndices); // Find real index
            score.SetSwapIndex(actualIndex);

            scores[i] = score;
        }

        Arrays.sort(scores);

        if (debug) {
            System.out.println("ProbSel highest score: " + scores[0].GetScore() + ", Lowest: " + scores[scores.length - 1].GetScore() + ", Total: " + scores.length);
        }

        return scores[0].GetSwapIndex();
    }

    private Scoring ScoreFunction(int roll, int[] set, int[] dice) {
        int[] indices = { 0, 1, 2 };

        // ArrayList<IndexAndSet> possibleNumbers = new ArrayList<>();
        IndexAndSet[] possibleNumbers = new IndexAndSet[4];

        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                possibleNumbers[i] = new IndexAndSet(-1);
            } else {
                possibleNumbers[i] = new IndexAndSet(i);
            }
            

            int[] setCopy = CopyArray(set);
            int[] diceCopy = CopyArray(dice);

            if (i == 3) {
                // Do no swap check
            } else {
                // swap the value at the index pointed to by the set
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
                        possibleNumbers[i].GetHastSet().add(k);
                    }
                }
            }
        }

        // Find highest scores
        Arrays.sort(possibleNumbers);

        
        if (debug) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < possibleNumbers.length; i++) {
                
                sb.append(possibleNumbers[i].toString());
                if (i < possibleNumbers.length - 1) {
                    sb.append(", ");
                }
                
            }
            sb.append("]");
            
            System.out.println("ProbSel Scoring - PN: " + sb.toString());
        }

        return new Scoring(possibleNumbers[0].GetSwapIndex(), possibleNumbers[0].GetScore());
    }

    private boolean InArray(int num, int[] array) {
        for (int n : array) {
            if (n == num) {
                return true;
            }
        }

        return false;
    }

    private int FindIndexOfValue(int value, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        } 

        return -1;
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

        public void SetSwapIndex(int swapIndex) {
            this.swapIndex = swapIndex;
        }

        public int GetSwapIndex() {
            return swapIndex;
        }

        public float GetScore() {
            return score;
        }

        @Override
        public int compareTo(AvalancheProbSel.Scoring o) {
            if (this.GetScore() == o.GetScore()) 
                return 0; 
            else if (this.GetScore() < o.GetScore()) 
                return 1; 
            else
                return -1; 
        }

    }

    public class IndexAndSet implements Comparable<IndexAndSet> {
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
            return (float)set.size() / 6;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            Integer[] nums = set.toArray(new Integer[0]);
            for (int i = 0; i < nums.length; i++) {
                
                sb.append(nums[i].toString());
                if (i < nums.length - 1) {
                    sb.append(", ");
                }
                
            }
            sb.append("]");
            return set.toString();
        }

        @Override
        public int compareTo(AvalancheProbSel.IndexAndSet o) {
            if (this.GetScore() == o.GetScore()) 
                return 0; 
            else if (this.GetScore() < o.GetScore()) 
                return 1; 
            else
                return -1; 
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
