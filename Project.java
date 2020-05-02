import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;

class Project {
    private int numberOfVertices;
    private int[][] adjacencyMatrix;

    private JFrame f;

    public void run() {
        Random rand = new Random();
        ArrayList<EdgeListItem> edgeListData = new ArrayList<>();

        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                // Split the values at the space
                String[] parts = data.split("\\s+");
                int[] n1 = new int[2];
                for (int n = 0; n < 2; n++) {
                    n1[n] = Integer.parseInt(parts[n]);
                    if (n1[n] > numberOfVertices) {
                        numberOfVertices = n1[n];
                    }
                }

                edgeListData.add(new EdgeListItem(n1[0], n1[1]));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            // System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Add one to number of vertices because it will be one shorter than needed.
        numberOfVertices++;

        // Create adjacency matrix
        adjacencyMatrix = new int[numberOfVertices][numberOfVertices];

        for (int i = 0; i < edgeListData.size(); i++) {
            EdgeListItem item = edgeListData.get(i);
            adjacencyMatrix[item.entries[0]][item.entries[1]] = 1;
            adjacencyMatrix[item.entries[1]][item.entries[0]] = 1;
        }

        // System.out.println("Orderings have " + numberOfVertices + "vertices.");

        System.out.println("Adjacency Matrix : ");
        for(int i = 0; i < numberOfVertices; i++){
            for(int j = 0; j < numberOfVertices; j++){
                System.out.print(adjacencyMatrix[i][j] + ", ");
            }
            System.out.println("");
        }

        f = new JFrame();

        String str_p = "";
        String str_g = "";
        String str_cr = "";
        String str_mu = "";
        String str_bo = "";

        //String str_p = "250";
        //String str_g = "100";
        //String str_cr = "40";
        //String str_mu = "40";

        boolean p_valid = false,
                g_valid = false,
                cr_valid = false,
                mu_valid = false,
                bo_valid = false;

        /*
        boolean p_valid = true,
                g_valid = true,
                cr_valid = true,
                mu_valid = true;
        */

        int bo = 0;

        while(!p_valid){
            str_p = JOptionPane.showInputDialog(f, "Population Size:");

            if(str_p.length() > 0){
                try {
                    double d = Double.parseDouble(str_p);

                    if(d > 0){
                        p_valid = true;
                        continue;
                    }
                } catch (NumberFormatException nfe) {
                    // Invalid
                }
            }
            JOptionPane.showMessageDialog(f, "Population size must be a positive number!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }

        while(!g_valid){
            str_g = JOptionPane.showInputDialog(f, "Generations Amount:");

            if(str_g.length() > 0){
                try {
                    double d = Double.parseDouble(str_g);

                    if(d > 0){
                        g_valid = true;
                        continue;
                    }
                } catch (NumberFormatException nfe) {
                    // Invalid
                }
            }
            JOptionPane.showMessageDialog(f, "Generation amount must be a number greater than 0!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }

        while(!cr_valid) {
            str_cr = JOptionPane.showInputDialog(f, "Crossover Rate:");

            if(str_cr.length() > 0){
                try {
                    double d = Double.parseDouble(str_cr);

                    if(d>=0 && d<=100){
                        cr_valid = true;
                        continue;
                    }
                } catch (NumberFormatException nfe) {
                    // Invalid
                }
            }
            JOptionPane.showMessageDialog(f, "Crossover Rate must be between 0 and 100", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }

        while(!mu_valid){
            str_mu = JOptionPane.showInputDialog(f, "Mutation Rate:");

            if(str_mu.length() > 0){
                try {
                    double d = Double.parseDouble(str_mu);

                    if(d>=0 && d<=100){
                        mu_valid = true;
                        continue;
                    }
                    continue;
                } catch (NumberFormatException nfe) {
                    // Invalid
                }
            }
            JOptionPane.showMessageDialog(f, "Mutation Rate must be between 0 and 100", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }

        while(!bo_valid){
            str_bo = JOptionPane.showInputDialog(f, "Print best ordering every n generations (0 = only print solution ordering):");

            if(str_bo.length() > 0){
                try {
                    double d = Double.parseDouble(str_bo);

                    if(d>=0){
                        bo_valid = true;
                        bo = Integer.parseInt(str_bo);
                        continue;
                    }
                    continue;
                } catch (NumberFormatException nfe) {
                    // Invalid
                }
            }
            JOptionPane.showMessageDialog(f, "n must be a number", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }

        HashMap<Integer, Double> currentPopulationFitnesses = new HashMap<>();

        // Step 2 - User Input
        int populationSize = Integer.parseInt(str_p);
        int generations = Integer.parseInt(str_g);
        int crossoverRate = Integer.parseInt(str_cr);
        int mutationRate = Integer.parseInt(str_mu);

        // Step 3 - Array Decleration
        HashMap<Integer, int[]> currentPopulation = new HashMap<>();
        HashMap<Integer, int[]> nextPopulation = new HashMap<>();

        for (int generation = 0; generation < generations; generation++) {

            // Empty out any fitnesses from last generation
            currentPopulationFitnesses.clear();
            currentPopulation.clear();

            // Step 4 - Generate first population
            if (generation == 0) {
                // Generate a new random ordering
                for (int i = 0; i < populationSize; i++) {
                    int[] newOrdering = new int[numberOfVertices];

                    // Put values into new ordering e.g. [0,1,2,3,4,...,n-1]
                    for (int j = 0; j < numberOfVertices; j++) {
                        newOrdering[j] = j;
                    }

                    // Randomly move the elements in the array
                    for (int j = 0; j < numberOfVertices * 2; j++) {
                        int rand_pos_1 = rand.nextInt(numberOfVertices);
                        int rand_pos_2 = rand.nextInt(numberOfVertices);

                        while(rand_pos_1 == rand_pos_2){
                            rand_pos_2 = rand.nextInt(numberOfVertices);
                        }

                        int t = newOrdering[rand_pos_1];
                        newOrdering[rand_pos_1] = newOrdering[rand_pos_2];
                        newOrdering[rand_pos_2] = t;
                    }

                    // Add newOrdering to currentPopulation
                    currentPopulation.put(i, newOrdering);
                }
            } else {
                nextPopulation.entrySet().forEach(entry -> {
                    currentPopulation.put(entry.getKey(), entry.getValue());
                });

                nextPopulation.clear();
            }

            // _print("CurrentPopulation for generation : " + generation);
            printOrderings(currentPopulation);

            // Generate the current populations fitness
            for (int i = 0; i < populationSize; i++) {
                currentPopulationFitnesses.put(i, fitnessFunction(currentPopulation.get(i)));
            }

            // Step 5 - Fitness Sort

            // Sort all the current orderings by fitness
            // HashMap<OrderingIndex, FitnessScore> ...
            HashMap<Integer, Double> sortedOrderings = sortByComparator(currentPopulationFitnesses, true);

            // List of currentPopulation ordering indexes sorted by FitnessScore
            // e.g. rankedOrderingIndexes[0] = the index of the lowest FitnessScore amount
            // current orderings
            // e.g. rankedOrderingIndexes[5] = the index of the 5th lowest FitnessScore
            // amount current orderings
            Object[] rankedOrderingIndexes = sortedOrderings.keySet().toArray();

            for(int i = 0; i < rankedOrderingIndexes.length; i++){
                _print(rankedOrderingIndexes[i] + " -> " + Arrays.toString(currentPopulation.get((int)rankedOrderingIndexes[i])) + " -> " + currentPopulationFitnesses.get(rankedOrderingIndexes[i]));
            }

            // Printing the last generations best ordering to the screen
            if(bo > 0 ){
                if((generation % bo) == 0){
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (Exception e) {
    
                    }
                    new GraphVisualisation(adjacencyMatrix,
                    currentPopulation.get((int) rankedOrderingIndexes[0]), numberOfVertices, "Best Ordering for Generation : " + generation + " -> " + currentPopulationFitnesses.get(rankedOrderingIndexes[0]) );
                }
            }
            
            
            ArrayList<Integer> s1 = new ArrayList<>();
            ArrayList<Integer> s2 = new ArrayList<>();
            ArrayList<Integer> s3 = new ArrayList<>();

            // This piece of code ensures that s1 and s3 are the same size
            // E.g. if lenght = 5, s1 = 2 elements, s2 = 1 element and s3 = 2 elements also
            int s1s3len;
            double splt = populationSize / 3.0;

            if (splt - Math.floor(splt) < 0.5) {
                s1s3len = (int) Math.floor(splt);
            } else {
                s1s3len = (int) Math.ceil(splt);
            }

            for (int i = 0; i < populationSize; i++) {
                if (i < s1s3len) {
                    s1.add((int) rankedOrderingIndexes[i]);
                } else if (i > populationSize - s1s3len - 1) {
                    s3.add((int) rankedOrderingIndexes[i]);
                } else {
                    s2.add((int) rankedOrderingIndexes[i]);
                }
            }

            // Remove the worst third performing orderings
            // and replace them with the best third

            // resultingOrderings contains the index's of the orderings to be used for the
            // next population
            ArrayList<Integer> resultingOrderings = new ArrayList<>();
            resultingOrderings.addAll(s1);
            resultingOrderings.addAll(s2);
            resultingOrderings.addAll(s1);

            // Step 5 (b)
            while (resultingOrderings.size() > 0) {

                int probabilityRate = rand.nextInt(100);

                // Crossover
                if (crossoverRate >= probabilityRate && resultingOrderings.size() >= 2) {

                    // _print("Crossover is being performed");

                    // _print("Size should be greater than or equal to 2 : " + resultingOrderings.size());

                    // Select two random orderings from current population
                    int p1 = rand.nextInt(resultingOrderings.size());
                    int p2 = 0;
                    while (p1 == p2) {
                        p2 = rand.nextInt(resultingOrderings.size());
                    }

                    // Crossover parent orderings
                    int[] ordering1 = currentPopulation.get(resultingOrderings.get(p1));
                    int[] ordering2 = currentPopulation.get(resultingOrderings.get(p2));

                    int[] newOrdering1 = new int[numberOfVertices];
                    int[] newOrdering2 = new int[numberOfVertices];

                    // p1 = parent 1 index, p2 = parent 2 index
                    int cuttingPoint = rand.nextInt(numberOfVertices - 3) + 1;

                    // Split parent orderings
                    int[] p1_p1 = Arrays.copyOfRange(ordering1, 0, cuttingPoint);
                    int[] p1_p2 = Arrays.copyOfRange(ordering1, cuttingPoint, numberOfVertices);

                    int[] p2_p1 = Arrays.copyOfRange(ordering2, 0, cuttingPoint);
                    int[] p2_p2 = Arrays.copyOfRange(ordering2, cuttingPoint, numberOfVertices);

                    // Make newOrdering1
                    System.arraycopy(p1_p1, 0, newOrdering1, 0, cuttingPoint);
                    System.arraycopy(p2_p2, 0, newOrdering1, cuttingPoint, p2_p2.length);

                    System.arraycopy(p2_p1, 0, newOrdering2, 0, cuttingPoint);
                    System.arraycopy(p1_p2, 0, newOrdering2, cuttingPoint, p1_p2.length);

                    // Replace missing or duplicate
                    replaceDuplicate(newOrdering1);
                    replaceDuplicate(newOrdering2);

                    // Add newOrderings to next population
                    nextPopulation.put(nextPopulation.size(), newOrdering1);
                    nextPopulation.put(nextPopulation.size(), newOrdering2);

                    // Remove the ordering with the higher index first
                    // avoid errors by removing incorrect orderings
                    if (p1 > p2) {
                        resultingOrderings.remove(p1);
                        resultingOrderings.remove(p2);
                    } else {
                        resultingOrderings.remove(p2);
                        resultingOrderings.remove(p1);
                    }
                }


                // Mutation
                if ((crossoverRate <= probabilityRate) && (probabilityRate <= (crossoverRate + mutationRate))
                        && resultingOrderings.size() >= 1) {

                    // _print("Applying mutation");

                    int r_index = rand.nextInt(resultingOrderings.size());
                    int[] o = currentPopulation.get(r_index);

                    int r1 = rand.nextInt(numberOfVertices);
                    int r2 = 1;

                    while (r1 == r2) {
                        r2 = rand.nextInt(numberOfVertices);
                    }

                    int t = o[r1];

                    o[r1] = o[r2];
                    o[r2] = t;

                    // Add new ordering to nextPopulation
                    nextPopulation.put(nextPopulation.size(), o);
                    // Remove ordering from array
                    resultingOrderings.remove(r_index);
                }

                // Reproduction
                if ( probabilityRate > (mutationRate + crossoverRate) && resultingOrderings.size() >= 1 ) {
                    int r_index = rand.nextInt(resultingOrderings.size());

                    nextPopulation.put(nextPopulation.size(), currentPopulation.get(r_index));

                    resultingOrderings.remove(r_index);
                }
            }

            // _print("NextPopulation for generation : " + generation);
            printOrderings(nextPopulation);

            // If the final generation has been reached
            if (generation == generations - 1) {
                // Generate the current populations fitness
                int lowestFitnessIndex = -1;
                double lowestFitnessScore = 999999;
                for (int i = 0; i < populationSize; i++) {
                    double fitnessValue = fitnessFunction(nextPopulation.get(i));
                    if(fitnessValue < lowestFitnessScore){
                        lowestFitnessIndex = i;
                        lowestFitnessScore = fitnessValue;
                    }
                }

                new GraphVisualisation(adjacencyMatrix,
                nextPopulation.get(lowestFitnessIndex), numberOfVertices, "Final Generation Best Ordering -> " + lowestFitnessScore);
            }
        }
    }

    // Calculates the length of all lines for the requested ordering (closer to 0 is
    // better)
    private double fitnessFunction(int[] ordering) {
        double chunk = (Math.PI * 2.0) / ((double) ordering.length);
        double total_distance = 0;

        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = i + 1; j < numberOfVertices; j++) {
                if (adjacencyMatrix[ordering[i]][ordering[j]] == 1) {
                    double x1 = Math.cos(i * chunk);
                    double y1 = Math.sin(i * chunk);
                    double x2 = Math.cos(j * chunk);
                    double y2 = Math.sin(j * chunk);

                    // Get length of line (x1, y1) -> (x2, y2)
                    double lineLength = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
                    total_distance += lineLength; 
                }
            }
        }

        return total_distance;
    }

    private static HashMap<Integer, Double> sortByComparator(HashMap<Integer, Double> unsortMap, final boolean order) {

        List<Entry<Integer, Double>> list = new LinkedList<Entry<Integer, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Integer, Double>>() {
            public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private void replaceDuplicate(int[] ordering) {
        ArrayList<Integer> dupLoc = new ArrayList<>();
        boolean[] exists = new boolean[numberOfVertices];

        for (int i = 0; i < ordering.length; i++) {
            if (exists[ordering[i]]) {
                // Number has been seen before
                dupLoc.add(i);
            } else {
                exists[ordering[i]] = true;
            }
        }

        for(int i = 0; i < exists.length; i++){
            if(!exists[i]) {
                ordering[dupLoc.get(0)] = i;
                dupLoc.remove(0);
                exists[i] = true;
            }
        }
    }

    private void _print(String s) {
        // System.out.println(s);
    }

    private void printOrderings(HashMap<Integer, int[]> arr) {
        arr.entrySet().forEach(entry -> {
            // System.out.println(entry.getKey() + " -> " + Arrays.toString(entry.getValue()));
        });
    }
}

class EdgeListItem {
    public int[] entries = new int[2];

    public EdgeListItem(int a, int b){
        entries[0] = a;
        entries[1] = b;
    }
}