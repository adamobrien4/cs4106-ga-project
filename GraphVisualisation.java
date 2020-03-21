import javax.swing.JFrame;

public class GraphVisualisation extends JFrame {

    private static final String TITLE = "Graph Visualisation";
    private static final int WIDTH = 960;
    private static final int HEIGHT = 960;
    private int[][] adjacencyMatrix;
    private int numberOfVertices;
    private int[] ordering;
    private double chunk;

    public GraphVisualisation(int[][] am, int[] ord, int nv) {
        this.adjacencyMatrix = am;
        this.ordering = ord;
        this.numberOfVertices = nv;
        this.chunk = (Math.PI * 2) / ((double) numberOfVertices);

        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g) {
        int radius = 100;
        int mov = 200;

        for(int i = 0; i < numberOfVertices; i++) {
            for(int j = i + 1; j < numberOfVertices; j++) {
                if(adjacencyMatrix[ordering[i]][ordering[j]] == 1) {
                    g.drawLine(
                        (int) (Math.cos(i * chunk) * radius) + mov,
                        (int) (Math.sin(i * chunk) * radius) + mov,
                        (int) (Math.cos(j * chunk) * radius) + mov,
                        (int) (Math.sin(j * chunk) * radius) + mov
                    );
                }
            }
        }
    }

}