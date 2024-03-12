import java.util.HashSet;
import java.util.Set;

public class HopefulImprovement extends Rollin{
    
    // this version refines decision-making of which die to swap when 
    // no replacement die directly adds to num of outs by selecting 
    // a die based on its potential contribution to future rolls. 
    // 

    // instead of defaulting to index 6 outside of valid range, we can choose a die 
    // to replace if it is:
    // Isolated in value from others 
    // or 
    // has the least freq among curr dice



    Set<Integer> outSet = new HashSet<Integer>();

    public HopefulImprovement() {
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
        //int mostOuts = 6;
        int bestIndex = -1;
        int outs = checkOuts(dice);
        int bestOuts = outs;

        for (int i = 0; i < dice.length - 1; i++) {
            int[] copyDice = dice.clone();
            copyDice[i] = roll;

            int newOuts = checkOuts(copyDice);
            if (newOuts > bestOuts) {
                bestOuts = newOuts; 
                bestIndex = i;
            }
        }
        //no replacement increases outs, fallback to least common values or isolated values 
        // to replace 
        if (bestIndex == -1){
            bestIndex = FallbackIndex(roll, dice);
        }

        return bestIndex;
    }

    private int FallbackIndex(int roll, int[] dice){

        int furthestIndex = 0; 
        int maxDistance = 0; 
        for(int i = 0 ; i < dice.length ; i ++){
            int distance = Math.abs(dice[i] - roll);
            if(distance>maxDistance){
                maxDistance = distance;
                furthestIndex = i;
            }
        }
        return furthestIndex;
    }

}
