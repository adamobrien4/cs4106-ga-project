import java.util.*;
import java.util.Map.Entry;

class Project {
    private int numberOfVertices;
    private int[][] adjacencyMatrix = { { 0, 1, 1, 0, 0 }, { 1, 0, 1, 0, 0 }, { 1, 1, 0, 1, 1 }, { 0, 0, 1, 0, 1 },
            { 0, 0, 1, 1, 0 } };

    public void run() {
        Random rand = new Random();

        // TODO : Read adjacencyMatrix from file

        numberOfVertices = 5;
        HashMap<Integer, Double> currentPopulationFitnesses = new HashMap<>();

        // Step 2 - User Input
        int populationSize = 25;
        int generations = 25;
        int crossoverRate = 40;
        int mutationRate = 40;

        // Step 3 - Array Decleration
        HashMap<Integer, int[]> currentPopulation = new HashMap<>();
        HashMap<Integer, int[]> nextPopulation = new HashMap<>();

        for(int generation = 0; generation < generations; generation++){

            // Empty out any fitnesses from last generation
            currentPopulationFitnesses.clear();

            // Step 4 - Generate first population
            if(generation == 0){
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

                        int t = newOrdering[rand_pos_1];
                        newOrdering[rand_pos_1] = newOrdering[rand_pos_2];
                        newOrdering[rand_pos_2] = t;
                    }

                    // Add newOrdering to currentPopulation
                    currentPopulation.put(i, newOrdering);

                    // Print newOrdering to console
                    // System.out.print("Ordering " + i + ": ");
                    // System.out.println(Arrays.toString(newOrdering));
                    // System.out.println("Fitness Score : " + currentPopulationFitnesses.get(i));
                    // System.out.println("");

                }
            } else {
                nextPopulation.entrySet().forEach(entry -> {
                    currentPopulation.put(entry.getKey(), entry.getValue() );
                });

                nextPopulation.clear();
            }

            _print("CurrentPopulation for generation : " + generation);
            printOrderings(currentPopulation);

            // Generate the current populations fitness
            for(int i = 0; i < populationSize; i++) {
                currentPopulationFitnesses.put(i, fitnessFunction(currentPopulation.get(i)));
            }

            _print("CurrentPopulation Fitness for generation : " + generation);
            printOrderings(currentPopulationFitnesses, 1);

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

            _print("RankedOrderingIndexes for generation : " + generation);
            _print(Arrays.toString(rankedOrderingIndexes));

            ArrayList<Integer> s1 = new ArrayList<>();
            ArrayList<Integer> s2 = new ArrayList<>();
            ArrayList<Integer> s3 = new ArrayList<>();

            _print("s1 : " + s1.size());
            _print("s2 : " + s2.size());
            _print("s3 : " + s3.size());

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
                    s1.add((int)rankedOrderingIndexes[i]);
                    // System.out.println("Adding : " + i + " to s1");
                } else if (i > populationSize - s1s3len - 1) {
                    s3.add((int)rankedOrderingIndexes[i]);
                    // System.out.println("Adding : " + i + " to s3");
                } else {
                    s2.add((int)rankedOrderingIndexes[i]);
                    // System.out.println("Adding : " + i + " to s2");
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

            _print("ResultingOrderings for generation : " + generation);
           _print(resultingOrderings.toString());

            // Step 5 (b) (i) - CrossOver
            while (resultingOrderings.size() > 0) {

                System.out.println("Size : " + resultingOrderings.size());

                if (resultingOrderings.size() == 1) {
                    nextPopulation.put(nextPopulation.size(), currentPopulation.get(resultingOrderings.get(0)));
                    resultingOrderings.remove(0);
                    continue;
                }

                int probabilityRate = rand.nextInt(100);

                // Possibility
                if (crossoverRate >= probabilityRate && resultingOrderings.size() >= 2) {

                    _print("Size should be greater than or equal to 2 : " + resultingOrderings.size());

                    // Select two random orderings from current population
                    int p1 = rand.nextInt(resultingOrderings.size());
                    int p2 = 0;
                    while (p1 == p2) {
                        p2 = rand.nextInt(resultingOrderings.size());
                    }

                    _print("--Size : " + resultingOrderings.size());
                    _print("--p1 : " + p1);
                    _print("--p2 : " + p2);

                    // Crossover parent orderings
                    int[] ordering1 = currentPopulation.get(resultingOrderings.get(p1));
                    int[] ordering2 = currentPopulation.get(resultingOrderings.get(p2));

                    // System.out.println("ProbabilityRate : " + probabilityRate);
                    // System.out.println("CrossoverRate : " + crossoverRate);

                    int[] newOrdering1 = ordering1;
                    int[] newOrdering2 = ordering2;

                    // p1 = parent 1 index, p2 = parent 2 index
                    int cuttingPoint = rand.nextInt(numberOfVertices - 3) + 1;

                    // System.out.println("Parent 1 : " + p1);
                    // System.out.println("Parent 2 : " + p2);
                    // System.out.println("CuttingPoint : " + cuttingPoint);

                    // System.out.println("ordering1");
                    for (int i = 0; i < ordering1.length; i++) {
                        // System.out.print(ordering1[i] + " , ");
                    }
                    // System.out.println("ordering2");
                    for (int i = 0; i < ordering2.length; i++) {
                        // System.out.print(ordering2[i] + " , ");
                    }

                    // Make newOrdering1
                    int[] p1_p1 = Arrays.copyOfRange(ordering1, 0, cuttingPoint);
                    int[] p1_p2 = Arrays.copyOfRange(ordering2, cuttingPoint, numberOfVertices);

                    for (int i = 0; i < p1_p2.length; i++) {
                        newOrdering1[i] = p1_p2[i];
                    }
                    for (int i = numberOfVertices - cuttingPoint; i < numberOfVertices; i++) {
                        newOrdering1[i] = p1_p1[numberOfVertices - i - 1];
                    }

                    // Make newOrdering2
                    int[] p2_p1 = Arrays.copyOfRange(ordering2, 0, cuttingPoint);
                    int[] p2_p2 = Arrays.copyOfRange(ordering1, cuttingPoint, numberOfVertices);

                    for (int i = 0; i < p2_p2.length; i++) {
                        newOrdering2[i] = p2_p2[i];
                    }
                    for (int i = numberOfVertices - cuttingPoint; i < numberOfVertices; i++) {
                        newOrdering2[i] = p2_p1[numberOfVertices - i - 1];
                    }

                    // System.out.print("newOrdering1 : ");
                    // System.out.println(Arrays.toString(newOrdering1));

                    // System.out.print("newOrdering2 : ");
                    // System.out.println(Arrays.toString(newOrdering2));

                    // Replace missing or duplicate
                    replaceDuplicate(newOrdering1);
                    replaceDuplicate(newOrdering2);

                    // System.out.print("newOrdering1 : ");
                    // System.out.println(Arrays.toString(newOrdering1));

                    // System.out.print("newOrdering2 : ");
                    // System.out.println(Arrays.toString(newOrdering2));

                    // Add newOrderings to next population
                    nextPopulation.put(nextPopulation.size(), newOrdering1);
                    nextPopulation.put(nextPopulation.size(), newOrdering2);

                    _print("Size : " + resultingOrderings.size());
                    _print("p1 : " + p1);
                    _print("p2 : " + p2);
                    
                    if(p1 > p2) {
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

                    nextPopulation.put(nextPopulation.size(), o);
                    resultingOrderings.remove(r_index);
                }

                // Reproduction
                if ((crossoverRate == mutationRate) && mutationRate <= probabilityRate && resultingOrderings.size() >= 1) {
                    int r_index = rand.nextInt(resultingOrderings.size());

                    nextPopulation.put(nextPopulation.size(), currentPopulation.get(r_index));

                    resultingOrderings.remove(r_index);
                }
            }

            _print("NextPopulation for generation : " + generation);
            printOrderings(nextPopulation);

            if(generation == generations-1) {
                // Generate the current populations fitness
                for(int i = 0; i < populationSize; i++) {
                    _print("i : " + fitnessFunction(nextPopulation.get(i)));
                }
            }
        }

        // System.out.println("\n\nNext Population");
        for (int i = 0; i < nextPopulation.size(); i++) {
            // System.out.println(Arrays.toString(nextPopulation.get(i)));
        }

        // Print the ordering with the best score
        // GraphVisualisation gv = new GraphVisualisation(adjacencyMatrix, currentPopulation.get((int) rankedOrderingIndexes[0]), numberOfVertices);
    }

    // Calculates the length of all lines for the requested ordering (closer to 0 is
    // better)
    private double fitnessFunction(int[] ordering) {
        double chunk = (Math.PI * 2) / ((double) ordering.length);
        int radius = 100;
        int mov = 200;
        double total_distance = 0;

        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = i + 1; j < numberOfVertices; j++) {
                if (adjacencyMatrix[ordering[i]][ordering[j]] == 1) {
                    int x1 = (int) (Math.cos(i * chunk) * radius) + mov;
                    int y1 = (int) (Math.sin(i * chunk) * radius) + mov;
                    int x2 = (int) (Math.cos(j * chunk) * radius) + mov;
                    int y2 = (int) (Math.sin(j * chunk) * radius) + mov;

                    // Get length of line (x1, y1) -> (x2, y2)
                    total_distance += Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
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
        boolean[] duplicate = new boolean[numberOfVertices];
        boolean[] exists = new boolean[numberOfVertices];

        for (int i = 0; i < ordering.length; i++) {
            if (exists[ordering[i]]) {
                // Number has been seen before
                duplicate[ordering[i]] = true;
            } else {
                exists[ordering[i]] = true;
            }
        }

        for (int i = 0; i < duplicate.length; i++) {
            if (duplicate[i]) {
                breakloop: for (int j = 0; j < numberOfVertices; j++) {
                    if (!exists[j]) {
                        duplicate[i] = false;
                        exists[j] = true;

                        ordering[i] = j;
                        break breakloop;
                    }
                }
            }
        }
    }

    private void _print(String s) {
        System.out.println(s);
    }

    private void printOrderings(HashMap<Integer, int[]> arr) {
        arr.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " -> " + Arrays.toString(entry.getValue()) );
        });
    }

    private void printOrderings(HashMap<Integer, Double> arr, int a) {
        arr.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " -> " + entry.getValue() );
        });
    }
}