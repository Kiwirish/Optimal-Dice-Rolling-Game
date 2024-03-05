import java.util.Arrays;

public class FirstAttemptBlake extends Rollin {
    
    public FirstAttemptBlake() { 
        super();
    }

    // my approach:
    // check if complete 
    // sort die 
    // determine if the roll directly contributes to a set or a sequence 
    // if it does, return the replacement index to substistute that roll into.
    // if the roll is not directly contributing to forming a set or sequence, 
    // then find your currently least useful die and return its index to substistute that roll into.
    // Therefore, either the directly contributing index is returned to be replaced w the roll 
    // or the least useful die on your way to forming a set or sequence out of your current die is 
    // returned to be replaced by the roll. 

    @Override
    public int handleRoll(int roll, int[] dice){

        if(Rollin.isComplete(dice)){
            return -1;
        }

        int[] sortedDice = Arrays.copyOf(dice, dice.length);
        Arrays.sort(sortedDice);

        int replacementDice = findSetFormingDice(roll, sortedDice);
        if(replacementDice != -1){
            return replacementDice;
        }

        int newReplacementDice = findLeastUsefulDice(roll, sortedDice);
        return newReplacementDice;
    }

    // method to decide which die to replace when the new roll conpletes a set. R
    // replaces die that're not contributing to potential sets
    // another method to assess whether a new roll can contribute to forming or finishing a sequence.
    private int findSetFormingDice(int roll, int[] sortedDice){

        // check for direct contribution to 3 of same kind 
        int[] counts = new int[7]; // counts for each die value 

        for (int die : sortedDice){
            counts[die]++;
        }
        //if roll completes 3 of a kind (as there being 2 of a kind means roll completes the 3)
        if(counts[roll] == 2){
            return findIndexOf(sortedDice, roll);
        }

        // if roll contributes to a sequence then find least useful dice index 
        if (isPartOfSeq(roll, sortedDice)){
            return findLeastUsefulDice(roll, sortedDice);
        }
        //no setforming contribution found 
        return -1; 
    
    }
    // method to identify which of current dice is least beneficial to keep 
    // dice not part of any potential sequence or set is considered least useful 
    // returns least useful dice index to be replaced 
    private int findLeastUsefulDice(int roll, int[] sortedDice){
    
        for (int i = 0; i < sortedDice.length; i++) {
            // die not contributing to a sequence found as leastuseful  
            if (!isPartOfSeq(sortedDice[i],sortedDice)) {
                return i; 
            }
        }

        // if all current die are useful, then fallback on replacing the die that is 
        // furthest numerically from the new roll as that will be least likely to help in forming some kind of set 
        // calculate the absolute distance between each die's value and the roll's value 
        // The die with the maximum distance considered the least useful 
        // Returns the index of the die furthest from the new die's roll 
        int maxDistance = 0; 
        int index = -1; 
        for(int i = 0 ; i < sortedDice.length; i++){
            int distance = Math.abs(sortedDice[i] - roll);
            if(distance > maxDistance){
                maxDistance = distance;
                index = i;
            }
        }
        return index;
    }
    // checks if given roll is part of or can contribute to sequence in current die
    // returns true if roll value directly fits into a sequence 
    // assiisted w copilot 
    private boolean isPartOfSeq(int roll, int[] sortedDice){

        boolean foundStartOrEnd = false, foundMiddle = false;
        for (int i = 0; i < sortedDice.length; i++) {
            
            // Check for start or end of a sequence
            if (i < sortedDice.length - 1 && (roll == sortedDice[i] - 1 || roll == sortedDice[i + 1] + 1)) {
                foundStartOrEnd = true;
            }
            // Check for filling a gap in the middle of a sequence
            if (i > 0 && i < sortedDice.length - 1 && sortedDice[i - 1] + 2 == sortedDice[i + 1] && roll == sortedDice[i - 1] + 1) {
                foundMiddle = true;
            }
        }
        // Value is part of a sequence if it can start, extend, or fill a gap
        return foundStartOrEnd || foundMiddle;


    }
    // convenience method copilot 
    private int findIndexOf(int[] sortedDice, int value) {
        // Find index of value in the sorted array
        for (int i = 0; i < sortedDice.length; i++) {
            if (sortedDice[i] == value) {
                return i;
            }
        }
        return -1; 
    }




}
