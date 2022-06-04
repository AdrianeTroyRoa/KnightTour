import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ThreadLocalRandom;

class KnightTour{
    public static final int N = 8;
    public static int[] key = new int[N*N];
    class GFG{ //In honor of the algorithm source: GeeksForGeeks
        // Move pattern on basis of the change of
        // x coordinates and y coordinates respectively
        public static final int cx[] = {1, 1, 2, 2, -1, -1, -2, -2};
        public static final int cy[] = {2, -2, 1, -1, 2, -2, 1, -1};

        // function restricts the knight to remain within
        // the 8x8 chessboard
        boolean limits(int x, int y)
        {
            return ((x >= 0 && y >= 0) &&
                    (x < N && y < N));
        }

        /* Checks whether a square is valid and
        empty or not */
        boolean isempty(int a[], int x, int y)
        {
            return (limits(x, y)) && (a[y * N + x] < 0);
        }

        /* Returns the number of empty squares
        adjacent to (x, y) */
        int getDegree(int a[], int x, int y)
        {
            int count = 0;
            for (int i = 0; i < N; ++i)
                if (isempty(a, (x + cx[i]),
                            (y + cy[i])))
                    count++;

            return count;
        }

        // Picks next point using Warnsdorff's heuristic.
        // Returns false if it is not possible to pick
        // next point.
        Cell nextMove(int a[], Cell cell)
        {
            int min_deg_idx = -1, c,
                min_deg = (N + 1), nx, ny;

            // Try all N adjacent of (*x, *y) starting
            // from a random adjacent. Find the adjacent
            // with minimum degree.
            int start = ThreadLocalRandom.current().nextInt(1000) % N;
            for (int count = 0; count < N; ++count)
            {
                int i = (start + count) % N;
                nx = cell.x + cx[i];
                ny = cell.y + cy[i];
                if ((isempty(a, nx, ny)) &&
                    (c = getDegree(a, nx, ny)) < min_deg)
                {
                    min_deg_idx = i;
                    min_deg = c;
                }
            }

            // IF we could not find a next cell
            if (min_deg_idx == -1)
                return null;

            // Store coordinates of next point
            nx = cell.x + cx[min_deg_idx];
            ny = cell.y + cy[min_deg_idx];

            // Mark next move
            a[ny * N + nx] = a[(cell.y) * N +
                            (cell.x)] + 1;

            // Update next point
            cell.x = nx;
            cell.y = ny;

            return cell;
        }

        /* displays the chessboard with all the
        legal knight's moves */
        void print(int a[])
        {
            for (int i = 0; i < N; ++i)
            {
                for (int j = 0; j < N; ++j)
                    key[j*N+i] = a[j*N+i];
                    //System.out.printf("%d\t", a[j * N + i]);
                //System.out.printf("\n");
            } }

        /* checks its neighbouring squares */
        /* If the knight ends on a square that is one
        knight's move from the beginning square,
        then tour is closed */
        boolean neighbour(int x, int y, int xx, int yy)
        {
            for (int i = 0; i < N; ++i)
                if (((x + cx[i]) == xx) &&
                    ((y + cy[i]) == yy))
                    return true;

            return false;
        }

        /* Generates the legal moves using warnsdorff's
        heuristics. Returns false if not possible */
        boolean findClosedTour(int x, int y)
        {
            // Filling up the chessboard matrix with -1's
            int a[] = new int[N * N];
            for (int i = 0; i < N * N; ++i)
                a[i] = -1;

            // initial position
            int sx = x;
            int sy = y;

            // Current points are same as initial points
            Cell cell = new Cell(sx, sy);

            a[cell.y * N + cell.x] = 1; // Mark first move.

            // Keep picking next points using
            // Warnsdorff's heuristic
            Cell ret = null;
            for (int i = 0; i < N * N - 1; ++i)
            {
                ret = nextMove(a, cell);
                if (ret == null)
                    return false;
            }

            // Check if tour is closed (Can end
            // at starting point)
            if (!neighbour(ret.x, ret.y, sx, sy))
                return false;

            print(a);
            return true;
        }
    }
    KnightTour(){
        //declaring and initializing variables for chess cell colors
        Color alpha = Color.decode("#769656");
        Color beta = Color.decode("#eeeed2");
        //declaring and initializing the knight piece icon
        ImageIcon iniicon = new ImageIcon("./horse.png");
        Image img = iniicon.getImage();
        Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        Icon icon = new ImageIcon(newimg);
        //declaring and initializing the JFrame object
        JFrame frame = new JFrame("Knight Tour");

        //declaring a JButton 2D array
        JButton[][] cells = new JButton[N][N];

        //setting GridLayout of the JFrame with respect to N
        frame.setLayout(new GridLayout(N,N));

        //initializing and implementing the JButton's idle form
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                cells[i][j] = new JButton();
                cells[i][j].setFocusable(false);
                cells[i][j].setBorder(null);
                if((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0))
                    cells[i][j].setBackground(beta);
                else
                    cells[i][j].setBackground(alpha);
                frame.add(cells[i][j]);
            }
        }

        //corresponds to the effects of clicking a JButton. this block starts
        //by making a clone for the cells jbutton 2D array
        JButton[] clone = new JButton[N*N]; 
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                clone[j*N+i] = cells[i][j];
            }
        }
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                int x = i;
                int y = j;
                clone[j*N+i].addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        final Timer timer = new Timer(500, null);
                        for(int z=0; z<N*N; z++){
                            clone[z].setText("");
                        }
                        for(int a=0; a<N; a++)
                            for(int b=0; b<N; b++){
                                if((a % 2 == 0 && b % 2 != 0) || (a % 2 != 0 && b % 2 == 0))
                                    cells[a][b].setBackground(beta);
                                else
                                    cells[a][b].setBackground(alpha);
                            }
                        //connects to the findClosedTour method for value assigning and solution
                        while(!new GFG().findClosedTour(x, y)){
                            ;
                        }

                        //delays for 500ms per move of the alleged knight piece
                        timer.addActionListener(new ActionListener() {
                            int k = 0;
                            int l;
                            int lVal;
                            int lInd;
                            public void actionPerformed(ActionEvent event){
                                for(int a=0; a<N; a++)
                                    for(int b=0; b<N; b++){
                                        //applies the same idle bg color to each cell, with fg color setting
                                        //for the coming step number
                                        cells[a][b].setForeground(Color.black);
                                        if(lVal != 0)
                                            clone[lInd].setText(String.valueOf(lVal));
                                        if((a % 2 == 0 && b % 2 != 0) || (a % 2 != 0 && b % 2 == 0)){
                                            cells[a][b].setIcon(null);
                                            cells[a][b].setBackground(beta);
                                        }
                                        else{
                                            cells[a][b].setIcon(null);
                                            cells[a][b].setBackground(alpha);
                                        }
                                    }
                                //changes color per movement of the knight piece and step number
                                for(l=0; l<N*N; l++){
                                    if(k==key[l]){
                                        clone[l].setIcon(icon);
                                        lInd = l;
                                        lVal = key[l];
                                    }
                                }

                                //stops the process when all cells are allocated once
                                if(k>(N*N)-1){timer.stop();}
                                k++;
                            }
                        });
                        timer.setRepeats(true);
                        timer.start();
                        //System.out.println(key[l*N+k]);
                    }
                });
            }
        }

        //other usual frame config
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        //calling the constructor method
        new KnightTour();
    }

}

//class utilized by the Warnsdorff's algorithm
class Cell
{
	int x;
	int y;

	public Cell(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
/*
 * DISCLAIMER:
 *
 * This Knight's Tour solution is solved using Warnsdorff's algorithm. The code to implement
 * it is from https://geeksforgeeks.org: particularly from SaeedZarinfam.
 *
 * Only the integration into a graphical user interface (GUI) through the Java Foundational Classes
 * is attributed to Adriane Troy V. Roa. This includes the overall form, features, and the like of
 * the application.
 */
