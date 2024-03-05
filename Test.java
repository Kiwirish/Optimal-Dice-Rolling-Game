import java.util.Random;

public class Test {

    static Random R = new Random();

    public static void main(String[] args) {
        boolean testIndividual = false;

        Rollin individual = new AvalancheM();
        String individualName = "ProbSel";

        if (testIndividual) {

            // New stuff
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }
    
            System.out.println("Initial dice: " + java.util.Arrays.toString(d));
            int roll = R.nextInt(6) + 1;
            System.out.println("Roll: " + roll);

            int re = individual.handleRoll(roll, d);
            if (re == -1) {
                System.out.println("Initial Six were complete");
            }
            d[re] = roll;
            System.out.println(individualName + " changes: index " + re);
            System.out.println("Dice: " + java.util.Arrays.toString(d));
    
            System.out.println("Running random until complete...");
            while (!Rollin.isComplete(d)) {
                roll = R.nextInt(6) + 1;
                System.out.println("Roll: " + roll);
                // int toChange = rand.handleRoll(roll, d);
                int toChange = individual.handleRoll(roll, d);
                // System.out.println("RandomRoller changes: " + toChange);
                System.out.println(individualName + " changes: index " + toChange);
                d[toChange] = roll;
                System.out.println("Dice: " + java.util.Arrays.toString(d));
            }

        } else {
            int numOfTrials = 1000000;

            RunTestGeneric(numOfTrials, new RandomRoller(), "rand");
            RunTestGeneric(numOfTrials, new Avalanche(), "Aval");
            RunTestGeneric(numOfTrials, new PlusOrMinusOne(), "Pomo");
            RunTestGeneric(numOfTrials, new AvalancheM(), "ProbSel");
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

            int roll = R.nextInt(6) + 1;
            // intialise dice
            int re = rollin.handleRoll(roll, d);
            if (re == -1) {
                // was already complete
                alreadyComplete++;
            } else {
                d[re] = roll;
                totalRolls++;
                while (!Rollin.isComplete(d)) {
                    roll = R.nextInt(6) + 1;
                    int toChange = rollin.handleRoll(roll, d);
                    d[toChange] = roll;
                    totalRolls++;
                }
            }
        }
        System.out.println("Average rolls for " + name + " = " + totalRolls / numOfTrials);
        System.out.println("total already complete = " + alreadyComplete);
    }


}
