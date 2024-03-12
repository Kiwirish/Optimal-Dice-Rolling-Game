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
            return PlusOrMinusOneFunc(roll, dice);
            // return R.nextInt(6); // Random greatly improves worst case efficiency compared with non mod, also has a ~10% lower worst case roll performance compared to Pomo.
        }
        

        return mostOuts;
    }

    private int PlusOrMinusOneFunc(int roll, int[] dice) {
        // the number rolled can't win in any position

        // want a method for if there is a set then only consider
        // replacing the other indicies in the dice array

        // for (int j = 0; j < dice.length; j++) {
            int[] copyTwo = CopyArray(dice);
            int[] dontTouch = new int[3];
            int numPossibleSet = 0;
            ArrayList<Integer[]> sets = new ArrayList<>();
            // if any 3 long combo of dice is a set then random for the remain option
            for (int[][] si : setIndices) {
                if (isSet(si[0], dice)) {
                    dontTouch = CopyArray(si[0]);
                    Integer[] see = Arrays.stream(si[0]).boxed().toArray(Integer[]::new);
                    sets.add(see);
                    // System.out.print("Don't Touch ");
                    // System.out.println(Arrays.toString(dontTouch));
                    numPossibleSet++;
                }
                if (isSet(si[1], dice)) {
                    dontTouch = CopyArray(si[1]);
                    Integer[] see = Arrays.stream(si[1]).boxed().toArray(Integer[]::new);
                    sets.add(see);
                    // System.out.print("Don't Touch ");
                    // System.out.println(Arrays.toString(dontTouch));
                    numPossibleSet++;
                }
            }
            // System.out.println("num of possible sets " + numPossibleSet);
    
            if (numPossibleSet == 0) {
                // then think about which number to swap out will lead to the most possile sets
                // for now just gonna set this to random
                return R.nextInt(6);
                // this may not be a bad option
    
            } else if (numPossibleSet > 1) {
                // this is the case where the set that allows for
                // now have sets to work with
    
                // get all the indeies sotred in set and if anything is missing
                // then random in next number there for a start
                // pick the one that has the if there is an indeie not invloved in any of the
                // sets swap that
                // have arraylist<integer[]> sets to work with
                int[] timesOccured = new int[6];
    
                for (int setNum = 0; setNum < sets.size(); setNum++) {
                    Integer[] items = sets.get(setNum);
                    for (int t = 0; t < 6; t++) {
                        for (int setItem = 0; setItem < 3; setItem++) {
                            if (items[setItem] == t) {
                                timesOccured[t]++;
                            }
                        }
                    }
                }
                // now we have the frequency of numbers
                // find the min
                int min = timesOccured[0];
                int indexOfMin = 0;
    
                for (int g = 0; g < timesOccured.length; g++) {
                    if (timesOccured[g] < min) {
                        min = timesOccured[g];
                        indexOfMin = g;
                    }
                }
                // System.out.print("timesOccured ");
                // System.out.println(Arrays.toString(timesOccured));
                // want to return the index of the min
                return indexOfMin;
    
                // return R.nextInt(6);
    
            } else {
                // find the other 3 idnecies and swap on of them
                // for only one option
                int[] swapOptions = new int[3];
                // does not do what I want
    
                int count = 0;
                for (int a = 0; a < 6; a++) {
    
                    boolean found = false;
    
                    // Check if current number is in dontTouch array
                    for (int b = 0; b < 3; b++) {
                        if (dontTouch[b] == a) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        swapOptions[count++] = a;
                        if (count == 3) {
                            break;
                        }
                    }
                }
                // System.out.print("One possible set, swap otions are ");
                // System.out.println(Arrays.toString(swapOptions));
                int posReturn = R.nextInt(6);
                ArrayList<Integer> arr = new ArrayList<>();
                for (int c = 0; c < 3; c++) {
                    arr.add(swapOptions[c]);
                }
                while (true) {
                    if (!arr.contains(posReturn)) {
                        posReturn = R.nextInt(6);
                    } else {
                        return posReturn;
                    }
                }
    
            }
            // }
    
            // might want to tiebreak by using central numbers
            // if all have a equal chance ie 223366 then use one that occurs
            // tend to pick towards the middle ie 3 and 4 before 2 and 5
            // decide which one to change if not instant complete
    
            // we return the index in dice we witch to replace with our last roll
    
        }
    
        private int[] CopyArray(int[] a) {
            int[] b = new int[a.length];
            for (int i = 0; i < a.length; i++) {
                b[i] = a[i];
            }
            return b;
        }

}