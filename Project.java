import java.util.*;
import java.util.Map.Entry;

class Project {
    private int numberOfVertices;
    private int[][] adjacencyMatrix = { { 0, 1, 1, 0, 0 }, { 1, 0, 1, 0, 0 }, { 1, 1, 0, 1, 1 }, { 0, 0, 1, 0, 1 },
            { 0, 0, 1, 1, 0 } };

    public void run() {
        System.out.println("Hello World");

        Random rand = new Random();

        numberOfVertices = 5;
        int[] ordering = { 2, 0, 3, 1, 4 };
        HashMap<Integer, Double> currentPopulationFitnesses = new HashMap<>();

        // Step 2 - User Input
        int populationSize = 3;
        int generations = 5;
        int crossoverRate = 25;
        int mutationRate = 5;

        // Step 3 - Array Decleration
        int[][] currentPopulation = new int[populationSize][numberOfVertices];
        int[][] nextPopulation = new int[populationSize][numberOfVertices];

        // Step 4 - Generate first population
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
            currentPopulation[i] = newOrdering;
            currentPopulationFitnesses.put(i, fitnessFunction(newOrdering));

            // Print newOrdering to console
            System.out.print("Ordering " + i + ": ");
            System.out.println(Arrays.toString(newOrdering));
            System.out.println("Fitness Score : " + currentPopulationFitnesses.get(i));
            System.out.println("");

        }

        // Step 5 - Fitness Sort

        // Sort all the current orderings by fitness
        // HashMap<OrderingIndex, FitnessScore> ...
        HashMap<Integer, Double> sortedOrderings = sortByComparator(currentPopulationFitnesses, true);

        // List of currentPopulation ordering indexes sorted by FitnessScore
        // e.g. rankedOrderingIndexes[0] = the index of the lowest FitnessScore amount current orderings
        // e.g. rankedOrderingIndexes[5] = the index of the 5th lowest FitnessScore amount current orderings
        Object[] rankedOrderingIndexes = sortedOrderings.keySet().toArray();

        ArrayList<Integer> s1 = new ArrayList<>();
        ArrayList<Integer> s2 = new ArrayList<>();
        ArrayList<Integer> s3 = new ArrayList<>();

        // This piece of code ensures that s1 and s3 are the same size
        // E.g. if lenght = 5, s1 = 2 elements, s2 = 1 element and s3 = 2 elements also
        int s1s3len;
        double splt = numberOfVertices/3.0;

        if( splt - Math.floor(splt) < 0.5 ) {
            s1s3len = (int)Math.floor(splt);
        } else {
            s1s3len = (int)Math.ceil(splt);
        }
        
        for(int i = 0; i < numberOfVertices; i++) {
            if( i < s1s3len ) {
                s1.add(i);
            } else if( i > numberOfVertices - s1s3len - 1) {
                s3.add(i);
            } else {
                s2.add(i);
            }
        }

        // Remove the worst third performing orderings
        // and replace them with the best third
        ArrayList<Integer> resultingOrderings = new ArrayList<>();
        resultingOrderings.addAll(s1);
        resultingOrderings.addAll(s2);
        resultingOrderings.addAll(s1);

        // Step 5 (b) (i) - CrossOver

        // Print the ordering with the best score
        GraphVisualisation gv = new GraphVisualisation(adjacencyMatrix, currentPopulation[(int)rankedOrderingIndexes[0]], numberOfVertices);
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
}