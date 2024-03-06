import java.util.Random;

public class Test {

    static Random R = new Random();

    public static void main(String[] args) {
        boolean testIndividual = false;

        Rollin individual = new AvalancheM(true);
        String individualName = "ProbSel";

        if (testIndividual) {

            // New stuff
            int[] d = new int[6];
            boolean useRand = true;
            if (useRand) {
                for (int i = 0; i < d.length; i++) {
                    d[i] = R.nextInt(6) + 1;
                }
            } else {
                int[] f = { 1 , 3, 2, 1, 2, 6 };
                for (int i = 0; i < d.length; i++) {
                    d[i] = f[i];
                }
            }
            if (Rollin.isComplete(d)) {
                System.out.println("Complete in no steps");
                return;
            }


    
            System.out.println("Initial dice: " + java.util.Arrays.toString(d));
            int roll = R.nextInt(6) + 1;
            System.out.println("Roll: " + roll);

            int re = individual.handleRoll(roll, d);
            if (re == -1) {
                System.out.println("Complete in one step");
                return;
            } else {
                d[re] = roll;
            }
            
            System.out.println(individualName + " changes: index " + re);
            System.out.println("Dice: " + java.util.Arrays.toString(d));
    
            System.out.println("Running random until complete...");
            while (!Rollin.isComplete(d)) {
                roll = R.nextInt(6) + 1;
                System.out.println("Roll: " + roll);
                
                int toChange = individual.handleRoll(roll, d);
                
                System.out.println(individualName + " changes: index " + toChange);
                if (toChange >= 0 && toChange <= 5) {
                    d[toChange] = roll;
                }
                
                System.out.println("Dice: " + java.util.Arrays.toString(d));
            }

        } else {
            int numOfTrials = 1000000;

            RunTestGeneric(numOfTrials, new RandomRoller(), "rand");
            RunTestGeneric(numOfTrials, new Avalanche(), "Aval");
            RunTestGeneric(numOfTrials, new PlusOrMinusOne(), "Pomo");
            RunTestGeneric(numOfTrials, new AvalancheM(), "ProbSel");
            RunTestGeneric(numOfTrials, new JamiesAttempt(), "Jamies");
        }        
    }

    public static void RunTestGeneric(int numOfTrials, Rollin rollin, String name) {
        double totalRolls = 0;
        int alreadyComplete = 0;

        for (int j = 0; j < numOfTrials; j++) {
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }
            int roll = 0;


            if (Rollin.isComplete(d)) {
                // was already complete
                alreadyComplete++;
            } else {
                while (!Rollin.isComplete(d)) {
                    roll = R.nextInt(6) + 1;
                    int toChange = rollin.handleRoll(roll, d);
                    if (toChange >= 0 && toChange <= 5) {
                        d[toChange] = roll;
                    }
                    
                    totalRolls++;
                }
            }
        }
        System.out.println("Average rolls for " + name + " = " + totalRolls / (numOfTrials));
        System.out.println("Average rolls for " + name + " (excluding immediately complete as 0 rolls) = " + totalRolls / (numOfTrials - alreadyComplete));
        System.out.println("Immediately Complete = " + alreadyComplete);
    }


}
