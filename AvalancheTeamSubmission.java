import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Team Avalanche Submission for Etude 2
 */
public class AvalancheTeamSubmission extends Rollin {

    Random R = new Random();
    Set<Integer> outSet = new HashSet<Integer>();

    public AvalancheTeamSubmission() {
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
                return i;
            }
        }

        return checkRolledDiced(roll, dice); // Do an intelligent analysis to choose which to swap or not to swap
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
                if (outSet.contains(j)) { // Continue if already in set
                    continue;
                }
                copyDice[i] = j;
                if (isComplete(copyDice)) {
                    outSet.add(j);
                }
            }

            if (outSet.size() >= 5) { // For performance reasons break as the set has reached the maximum size that it will
                break;
            }
        }
        
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

            if (outs >= 5) { // For performance reasons break as the set has reached the maximum size that it will
                break;
            }
        }

        if (outs == 0) {
            
            return R.nextInt(6); // Do a random replacement if there is no valid rolls.
        }
        

        return mostOuts;
    }

}