import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        /*
        //Default creation:

        Package package1 = new Package("1", 100, 100, 90);
        Package package2 = new Package("2", 200, 200, 80);
        Package package3 = new Package("3", 300, 300, 70);
        Package package4 = new Package("4", 400, 400, 60);
        Package package5 = new Package("5", 500, 500, 50);
        Package package6 = new Package("6", 600, 600, 40);
        Package package7 = new Package("7", 700, 700, 30);
        Package package8 = new Package("8", 800, 800, 20);
        Package package9 = new Package("9", 900, 900, 10);

        ArrayList<Package> packages = new ArrayList<>();

        packages.add(package1);
        packages.add(package2);
        packages.add(package3);
        packages.add(package4);
        packages.add(package5);
        packages.add(package6);
        packages.add(package7);
        packages.add(package8);
        packages.add(package9);
        */

        //todo: If you want to test the algorithm with the known, calculatable values; you can comment the below code;
        //todo: and uncomment the above one. The result sequence should be: 1 > 2 > 3 > 4 > 5 > 6 > 7 > 8 > 9 .

        //Random creation:
        ArrayList<Package> packages = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            Random rnd = ThreadLocalRandom.current();
            Package newPackage = new Package(Integer.toString(i + 1), rnd.nextInt(1000), rnd.nextInt(1000), rnd.nextInt(100));

            System.out.println("ID: " + newPackage.ID);
            System.out.println("x:  " + newPackage.x);
            System.out.println("y:  " + newPackage.y);
            System.out.println("W:  " + newPackage.weight);
            System.out.println();

            packages.add(newPackage);
            System.out.println("Package has been created.");
            System.out.println();
        }

        //Setting the desired number of chromosomes and generations.
        int numOfChr = setNumOfChr();
        int numOfGen = setNumOfGen();

        //Taking the sequence for creating routes:
        String sequence = "";
        for (int i = 0; i < packages.size(); i++) {
            sequence += (i + 1);
        }

        //Calculating total weight for speed adjustments:
        int totalWeight = 0;
        for (int i = 0; i < packages.size(); i++) {
            totalWeight += packages.get(i).weight;
        }

        //Creation of first generation and its fitness values, optimal route and time.
        ArrayList<String[]> oldGen = setFirstGen(numOfChr, sequence);
        int[] fitValues = fitControl(packages, oldGen, totalWeight);
        String[] optimalRoute = oldGen.get(findMin(fitValues));
        int optimalTime = fitValues[findMin(fitValues)];

        //Adjustments of while loop:
        int genNumber = 1;
        System.out.println("Generation: " + genNumber);
        printRoute(oldGen, fitValues);
        boolean flag = true;

        //Genetic algorithm evaluation:
        while (flag) {
            genNumber++;

            ArrayList<String[]> newGen = elitism(oldGen, fitValues);

            crossover(newGen, sequence, numOfChr);

            mutation(newGen, sequence);

            fitValues = fitControl(packages, newGen, totalWeight);

            oldGen = newGen;

            System.out.println("Generation: " + genNumber);

            printRoute(oldGen, fitValues);

            optimalRoute = optimalRoute(oldGen, fitValues, optimalTime, optimalRoute);
            optimalTime = fitness(packages, optimalRoute, totalWeight);

            flag = exitControl(genNumber, numOfGen);
        }

        finalPrinter(optimalRoute, packages, totalWeight);
    }

    //METHOD "setFirstGen": Generates the very first generation by shuffling.
    public static ArrayList<String[]> setFirstGen(int numOfChr, String sequence) {
        ArrayList<String[]> firstGen = new ArrayList<>();

        for (int i = 0; i < numOfChr; i++) {
            firstGen.add(shuffle(sequence));
        }

        return firstGen;
    }

    //METHOD "shuffle": Shuffles the characters in a given string.
    public static String[] shuffle(String sequence) {
        String[] chars = sequence.split("");

        Random rnd = ThreadLocalRandom.current();
        for (int i = chars.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = chars[index];
            chars[index] = chars[i];
            chars[i] = a;
        }

        return chars;
    }

    //METHOD "elitism": Controls a generation's chromosomes and takes the best ones to the new generation:
    public static ArrayList<String[]> elitism(ArrayList<String[]> oldGen, int[] fitValues) {
        ArrayList<String[]> newGen = new ArrayList<>();

        for (int i = 0; i < fitValues.length / 5; i++) {
            int minIndex = findMin(fitValues);
            newGen.add(oldGen.get(minIndex));
            fitValues[minIndex] = 0;
        }

        return newGen;
    }

    //METHOD "crossover": Makes crossover between chromosomes that selected by elitism.
    public static void crossover(ArrayList<String[]> newGen, String sequence, int numOfChr) {
        int size = newGen.size();

        for (int i = 0; i < (numOfChr / 5) * 4; i++) {
            Random rnd = ThreadLocalRandom.current();
            String[] chr1 = newGen.get(rnd.nextInt(size));
            String[] chr2 = newGen.get(rnd.nextInt(size));

            String[] newChr = new String[sequence.length()];
            String charPool = sequence;

            int n = rnd.nextInt(sequence.length());
            for (int j = 0; j < n; j++) {
                newChr[j] = chr1[j];
                charPool = charPool.replaceFirst(chr1[j], "*");
            }

            for (String a : chr2) {
                if (charPool.contains(a)) {
                    newChr[n] = a;
                    n++;
                    charPool = charPool.replaceFirst(a, "*");
                }
            }

            newGen.add(newChr);
        }
    }

    //METHOD "mutation": Get chromosomes in a generation and apply mutation on them by changing genes locations.
    public static void mutation(ArrayList<String[]> newGen, String sequence) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = 0; i < newGen.size(); i++) {
            int gene1 = rnd.nextInt(sequence.length());
            int gene2 = rnd.nextInt(sequence.length());

            String temp = newGen.get(i)[gene1];
            newGen.get(i)[gene1] = newGen.get(i)[gene2];
            newGen.get(i)[gene2] = temp;
        }
    }

    //METHOD "findMin": Finds minimum value in a list and returns its index value.
    public static int findMin(int[] values) {
        int minValue = Integer.MAX_VALUE;
        int minIndex = Integer.MAX_VALUE;

        for (int j = 0; j < values.length; j++) {
            if (values[j] < minValue) {
                minValue = values[j];
                minIndex = j;
            }
        }

        return minIndex;
    }

    //METHOD "fitControl": Makes fitness control on a generation.
    public static int[] fitControl(ArrayList<Package> packages, ArrayList<String[]> newGen, int totalWeight) {
        int[] fitValues = new int[newGen.size()];

        for (int i = 0; i < newGen.size(); i++) {
            fitValues[i] = fitness(packages, newGen.get(i), totalWeight);
        }

        return fitValues;
    }

    //METHOD "fitness": Controls a chromosome's genes and get a fitness value as time.
    public static int fitness(ArrayList<Package> packages, String[] route, int totalWeight) {
        float totalTime = 0;

        //Point 1:
        int x1 = 0;
        int y1 = 0;

        for (int i = 0; i < packages.size(); i++) {

            Package package2 = findPackage(packages, route[i]); //Point 2
            int x2 = package2.x;
            int y2 = package2.y;

            double distance = calculateDistance(x1, y1, x2, y2);
            double speed = avaliableSpeed(totalWeight);

            totalTime += distance / speed * 60;
            totalWeight -= package2.weight;

            //Point 1 - Update:
            x1 = x2;
            y1 = y2;
        }

        return (int) totalTime;
    }

    //METHOD "avaliableSpeed": Calculates the avaliable speed of the vehicle according to current weight total.
    public static double avaliableSpeed(int totalWeight) {
        if (totalWeight < 100) {
            return 100;
        } else if (totalWeight >= 100 & totalWeight < 200) {
            return 85;
        } else if (totalWeight >= 200 & totalWeight < 300) {
            return 70;
        } else if (totalWeight >= 300 & totalWeight < 400) {
            return 55;
        } else if (totalWeight > 400) {
            return 40;
        }
        return 0;
    }

    //METHOD "findPackage": Returns the properties of a package of given package ID.
    public static Package findPackage(ArrayList<Package> packages, String packageID) {
        for (Package a : packages) {
            if (a.ID.equals(packageID)) {
                return a;
            }
        }

        return null;
    }

    //METHOD "calculateDistance": Calculates the distance between two points.
    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) * 0.1;
    }

    //METHOD "optimalRoute": Runs at every generation and holds the optimal route in its memory.
    public static String[] optimalRoute(ArrayList<String[]> oldGen, int[] fitValues, int optimalTime, String[] bestRoute) {
        int minIndex = findMin(fitValues);
        String[] route = oldGen.get(minIndex);
        int time = fitValues[minIndex];

        if (time < optimalTime) {
            bestRoute = Arrays.copyOf(route, route.length);
            optimalTime = time;
        }

        return bestRoute;
    }

    //METHOD "exitControl": If model reached the desired generation number, exit from the model.
    public static boolean exitControl(int genNumber, int numOfGen) {
        if (genNumber == numOfGen) {
            return false;
        }
        return true;
    }

    //METHOD "setNumOfChr": Sets number of chromosomes for the model.
    public static int setNumOfChr() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Set number of chromosomes: ");

        return reader.nextInt();
    }

    //METHOD "setNumOfGen": Sets number of generations for the model.
    public static int setNumOfGen() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Set number of generations: ");

        return reader.nextInt();
    }

    //METHOD "getModel": Prints details of a given route.
    public static void printRoute(ArrayList<String[]> oldGen, int[] fitValues) {
        int minIndex = findMin(fitValues);
        String[] route = oldGen.get(minIndex);

        System.out.print("OPTIMAL ROUTE:  ");

        for (int i = 0; i < route.length; i++) {
            System.out.print(route[i]);
            if (i != route.length - 1) {
                System.out.print("  >  ");
            }
        }

        System.out.println();
        System.out.print("ESTIMATED TIME:  ");
        System.out.println(fitValues[minIndex]);
        System.out.println();
    }

    //METHOD "finalPrinter": Prints the details of the final optimal route.
    public static void finalPrinter(String[] route, ArrayList<Package> packages, int totalWeight) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃ BEST ROUTE FOUND ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━┛");

        float totalTime = 0;

        //Point 1:
        int x1 = 0;
        int y1 = 0;

        int[] speeds = new int[packages.size()];
        int[] times = new int[packages.size()];

        for (int i = 0; i < packages.size(); i++) {
            Package package2 = findPackage(packages, route[i]); //Point 2
            int x2 = package2.x;
            int y2 = package2.y;

            double distance = calculateDistance(x1, y1, x2, y2);
            double speed = avaliableSpeed(totalWeight);

            speeds[i] = (int) speed;
            times[i] = (int) (distance / speed * 60);

            totalWeight -= package2.weight;
            totalTime += distance / speed * 60;

            //Point 1 - Update:
            x1 = x2;
            y1 = y2;
        }

        System.out.println();
        System.out.println("TOTAL ESTIMATED TIME: " + (int) totalTime + " mins");
        System.out.println();

        System.out.print("0 ");

        for (int i = 0; i < packages.size(); i++) {
            System.out.print("━━━━━━━━━━━");
            System.out.print(" " + route[i] + " ");
        }
        System.out.println();

        System.out.print("  ");
        for (int i = 0; i < packages.size(); i++) {
            System.out.printf(" %3d km/h  ", speeds[i]);
            System.out.print("   ");
        }
        System.out.println();

        System.out.print("  ");
        for (int i = 0; i < packages.size(); i++) {
            System.out.printf("  %02d mins  ", times[i]);
            System.out.print("   ");
        }
        System.out.println();
    }


    //CLASS "Package": Structure of the packages.
    public static class Package {
        public String ID;
        public int x;
        public int y;
        public int weight;

        public Package(String ID, int x, int y, int weight) {
            this.ID = ID;
            this.x = x;
            this.y = y;
            this.weight = weight;
        }
    }
}