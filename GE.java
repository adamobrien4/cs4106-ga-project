public class GE {
    public static void main(String args[]) {
        System.out.println("Hello World");

        int numberOfVertices = 5;
        int[][] adjacencyMatrix = {{0,1,1,0,0},{1,0,1,0,0},{1,1,0,1,1},{0,0,1,0,1},{0,0,1,1,0}};
        int[] ordering = {2,0,3,1,4};

        GraphVisualisation gv = new GraphVisualisation(adjacencyMatrix, ordering, numberOfVertices);
    }
}