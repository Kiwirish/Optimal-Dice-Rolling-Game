import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class JamiesAttemptMod extends Rollin {

    Random R = new Random();
    Set<Integer> outSet = new HashSet<Integer>();

    public JamiesAttemptMod() {
    }

    public int handleRoll(int roll, int[] dice) {
        // Check for complete
        if (Rollin.isComplete(dice)) {
            return -1;
        }

        // Check brute force complete
        for (int i = 0; i < dice.length; i++) {
            int[] copy = dice.clone();

            copy[i] = roll;
            if (Rollin.isComplete(copy)) {
                // System.out.println("Brute forced it");
                return i;
            }
        }

        
        return checkRolledDiced(roll, dice);
    }

    /**
     * Checks the 6 dice to see what numbers can be rolled to complete it
     * @param dice
     * @return The number of possible valid roles that would complete the set
     */
    public int checkOuts(int[] dice) {
        outSet.clear();

        for (int i = 0; i < dice.length - 1; i++) {
            int[] copyDice = dice.clone();

            for (int j = 1; j <= 6; j++) {
                if (outSet.contains(j)) {
                    continue;
                }
                copyDice[i] = j;
                if (isComplete(copyDice)) {
                    outSet.add(j);
                }
            }

            if (outSet.size() >= 5) {
                break;
            }
        }
        //System.out.println(outSet);
        return outSet.size();
    }

    /**
     * 
     * @param roll
     * @param dice
     * @return The index to swap the new roll with.
     */
    public int checkRolledDiced(int roll, int[] dice) {
        int mostOuts = 6; // The return swap index
        int outs = checkOuts(dice); // The amount of number of valid roles with no change

        for (int i = 0; i < dice.length - 1; i++) {
            int[] copyDice = dice.clone();
            copyDice[i] = roll;
            if (checkOuts(copyDice) > outs) { // if there is more valid roles when swapping in the new roll then use it. update out with new highest value, 
                outs = checkOuts(copyDice);
                mostOuts = i; // Updates the swap index return val to the index that was swapped.
            }

            if (outs >= 5) {
                break;
            }
        }

        if (outs == 0) {
            // ToDo: if there is not outs but is at least one set, then use either Blake or Matt
            // return GetProbSwap(roll, dice); // Has a ~20% worst case performance improvement in terms of roles compared to Pomo, negligible average performance impact, worse worst case compute time compared to random.
            // Other wise use random ?
            return R.nextInt(6); // Random greatly improves worst case efficiency compared with non mod, also has a ~10% lower worst case roll performance compared to Pomo.
        }
        

        return mostOuts;
    }

    private int[] CopyArray(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i]; 
        }
        return b;
    }

    private int[] SetIndicesToPlainArray(int[] indices, int[] array) {
        int[] plainArray = new int[indices.length]; 

        for (int i = 0; i < indices.length; i++) {
            plainArray[i] = array[indices[i]];
        }

        return plainArray;
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
            return R.nextInt(6);
        } else {

            // ToDo: Prune the sets array so that there are no sets that though use different indexes are actually the same
            HashSet<Integer> toRemove = new HashSet<>();
            for (int i = sets.size() - 1;  i >= 0; i--) {
                if (toRemove.contains(i)) {
                    continue;
                }
                int[] compareAgainst = SetIndicesToPlainArray(sets.get(i), dice);
                for (int j = i - 1; j >= 0; j--) {
                    if (toRemove.contains(j)) {
                        continue;
                    }
                    int[] thisOne = SetIndicesToPlainArray(sets.get(j), dice);
                    if (Arrays.equals(compareAgainst, thisOne)) {
                        // Remove from the array
                        toRemove.add(j);
                    }
                }
            }

            // Remove them
            Integer[] indexesToRemove = toRemove.toArray(new Integer[0]);
            Arrays.sort(indexesToRemove);
            for (int i = indexesToRemove.length - 1; i >= 0; i--) {
                sets.remove(i);
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
        public int compareTo(JamiesAttemptMod.Scoring o) {
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
        public int compareTo(JamiesAttemptMod.IndexAndSet o) {
            if (this.GetScore() == o.GetScore()) 
                return 0; 
            else if (this.GetScore() < o.GetScore()) 
                return 1; 
            else
                return -1; 
        }
    }

}