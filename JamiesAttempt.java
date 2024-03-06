import java.util.HashSet;
import java.util.Set;

public class JamiesAttempt extends Rollin {

    Set<Integer> outSet = new HashSet<Integer>();

    public JamiesAttempt() {
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

    // Checks the 6 dice to see what numbers can be rolled to complete it
    public int checkOuts(int[] dice) {
        outSet.clear();

        for (int i = 0; i < dice.length - 1; i++) {
            int[] copyDice = dice.clone();

            for (int j = 1; j <= 6; j++) {
                copyDice[i] = j;
                if (isComplete(copyDice)) {
                    outSet.add(j);
                }
            }
        }
        //System.out.println(outSet);
        return outSet.size();
    }

    public int checkRolledDiced(int roll, int[] dice) {
        int mostOuts = 6;
        int outs = checkOuts(dice);

        for (int i = 0; i < dice.length - 1; i++) {
            int[] copyDice = dice.clone();
            copyDice[i] = roll;
            if (checkOuts(copyDice) > outs) {
                outs = checkOuts(copyDice);
                mostOuts = i;
            }
        }

        return mostOuts;
    }

}