import java.util.HashSet;
import java.util.Set;

public class JamiesAttempt extends Rollin {

    Set<Integer> outSet = new HashSet<Integer>();

    public JamiesAttempt() {
    }

    public int handleRoll(int roll, int[] dice) {
        checkOuts(roll, dice);
        return -1;
    }

    // Checks the 6 dice to see what numbers can be rolled to complete it
    public int checkOuts(int roll, int[] dice){
        outSet.clear();
        
        for(int i = 0; i<dice.length-1; i++){
            int[] copyDice = dice.clone();

            for(int j = 1; j <=6 ; j++){
                copyDice[i] = j;
                if(isComplete(copyDice)){
                    outSet.add(j);
                }
            }
        }
        System.out.println(outSet);
        return outSet.size();
    }
    
  
}