import java.awt.*;
import javax.swing.JFrame;

public class GraphVisualisation extends JFrame {

    private static final int WIDTH = 960;
    private static final int HEIGHT = 960;
    private int[][] adjacencyMatrix;
    private int numberOfVertices;
    private int[] ordering;
    private double chunk;

    public GraphVisualisation(int[][] am, int[] ord, int nv, String title) {
        this.adjacencyMatrix = am;
        this.ordering = ord;
        this.numberOfVertices = nv;
        this.chunk = (Math.PI * 2) / ((double) numberOfVertices);

        setTitle(title);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g) {
        int radius = 100;
        int mov = 200;
        int nodeNameAddition = 20;

        for(int i = 0; i < numberOfVertices; i++) {
            for(int j = i + 1; j < numberOfVertices; j++) {
                if(adjacencyMatrix[ordering[i]][ordering[j]] == 1) {
                    int x1 = (int) (Math.cos(i * chunk) * radius) + mov;
                    int y1 = (int) (Math.sin(i * chunk) * radius) + mov;
                    int x2 = (int) (Math.cos(j * chunk) * radius) + mov;
                    int y2 = (int) (Math.sin(j * chunk) * radius) + mov;
                    g.setColor(Color.lightGray);
                    g.drawLine(
                        x1,
                        y1,
                        x2,
                        y2
                    );

                    g.setColor(Color.blue);
                    g.drawArc(x1, y1, 5, 5, 0, 360);
                    g.drawArc(x2, y2, 5, 5, 0, 360);

                    g.setColor(Color.blue);
                    g.drawString(String.valueOf(ordering[i]), x1, y1 + nodeNameAddition);
                    g.drawString(String.valueOf(ordering[j]), x2, y2 + nodeNameAddition);
                }
            }
        }
    }

}