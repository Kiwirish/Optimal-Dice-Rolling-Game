import java.util.Random;

public class Test {

    static Random R = new Random();

    public static void main(String[] args) {
        boolean testIndividual = false;

        Rollin individual = new JamiesAttempt();
        // Rollin individual = new AvalancheM(true);
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
                int[] f = { 1 , 1, 4, 1, 2, 6 };
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
            // roll = 6;
            System.out.println("Roll: " + roll);
            
            int re = individual.handleRoll(roll, d);
            if (re >= 0 && re <= 5) {
                d[re] = roll;
            } else {
                if (Rollin.isComplete(d)) {
                    System.out.println("Complete in one step");
                    return;
                }
            }
            // if (re == -1) {
            //     System.out.println("Complete in one step");
            //     return;
            // } else {
            //     d[re] = roll;
            // }
            
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


        long totalTime = 0;
        long startTime = 0;
        long longestTime = 0;

        for (int j = 0; j < numOfTrials; j++) {
            startTime = System.nanoTime();
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

            long iterationTime = System.nanoTime() - startTime;
            if (iterationTime > longestTime) {
                longestTime = iterationTime;
            }
            totalTime += iterationTime;
        }

        // long totalTime = System.nanoTime() - startTime;
        // double timeMS = (double)totalTime / 1000000.0;

        System.out.println("--------");
        System.out.println("Average time (ms) for " + name + " = " + (((double)totalTime / (double)numOfTrials) / 1000000) + ". Total time (ms) = " + (totalTime / 1000000));
        System.out.println("Max time (ms) for " + name + " = " + ((double)longestTime / 1000000.0));

        System.out.println("Average rolls for " + name + " = " + totalRolls / (numOfTrials));
        System.out.println("Average rolls for " + name + " (excluding immediately complete as 0 rolls) = " + totalRolls / (numOfTrials - alreadyComplete));
        System.out.println("Immediately Complete = " + alreadyComplete);
        System.out.println("--------");
    }


}
