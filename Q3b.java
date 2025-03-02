/*
    A Game of Tetris
    Functionality:
    Queue: Use a queue to store the sequence of falling blocks.
    Stack: Use a stack to represent the current state of the game board.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import javax.swing.*;

 class GameBoard {
    private int width, height;
    private Color[][] grid;
    private Stack<Block> stack;
    private int rowsCleared = 0;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Color[height][width];
        this.stack = new Stack<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = Color.BLACK;
            }
        }
    }

    public boolean canMove(Block block, int dx, int dy) {
        int[][] shape = block.getShape();
        int x = block.getX() + dx;
        int y = block.getY() + dy;

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= width || newY >= height || (newY >= 0 && grid[newY][newX] != Color.BLACK)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void placeBlock(Block block) {
        int[][] shape = block.getShape();
        int x = block.getX();
        int y = block.getY();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    grid[y + i][x + j] = block.getColor();
                }
            }
        }
        stack.push(block);
        checkCompletedRows();
    }

    public int getRowsCleared() {
        return rowsCleared;
    }

    // Add this method to reset the rows cleared counter
    public void resetRowsCleared() {
        rowsCleared = 0;
    }

    private void checkCompletedRows() {
        for (int i = 0; i < height; i++) {
            boolean completed = true;
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == Color.BLACK) {
                    completed = false;
                    break;
                }
            }
            if (completed) {
                removeRow(i);
                rowsCleared++;
                i--; // Check the same row again after shifting down
            }
        }
    }

    private void removeRow(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = grid[i - 1][j];
            }
        }
        for (int j = 0; j < width; j++) {
            grid[0][j] = Color.BLACK;
        }
    }

    public Color[][] getGrid() {
        return grid;
    }
}

class Block {
    private int[][] shape;
    private Color color;
    private int x, y;

    public Block(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 0;
        this.y = 0;
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveDown() {
        y++;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void rotate() {
        int[][] rotatedShape = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                rotatedShape[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = rotatedShape;
    }
}

class TetrisGame {
    private GameBoard gameBoard;
    private Queue<Block> blockQueue;
    private Block currentBlock;
    private boolean gameOver;
    private Random random;

    public TetrisGame(int width, int height) {
        gameBoard = new GameBoard(width, height);
        blockQueue = new LinkedList<>();
        random = new Random();
        gameOver = false;
        generateNewBlock();
        currentBlock = blockQueue.poll();
    }

    private void generateNewBlock() {
        int[][][] shapes = {
                { { 1, 1, 1, 1 } }, // I
                { { 1, 1 }, { 1, 1 } }, // O
                { { 1, 1, 1 }, { 0, 1, 0 } }, // T
                { { 1, 1, 0 }, { 0, 1, 1 } }, // S
                { { 0, 1, 1 }, { 1, 1, 0 } }, // Z
                { { 1, 0, 0 }, { 1, 1, 1 } }, // L
                { { 0, 0, 1 }, { 1, 1, 1 } } // J
        };
        Color[] colors = { Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.RED, Color.ORANGE, Color.BLUE };
        int index = random.nextInt(shapes.length);
        blockQueue.add(new Block(shapes[index], colors[index]));
    }

    public void moveLeft() {
        if (gameBoard.canMove(currentBlock, -1, 0)) {
            currentBlock.moveLeft();
        }
    }

    public void moveRight() {
        if (gameBoard.canMove(currentBlock, 1, 0)) {
            currentBlock.moveRight();
        }
    }

    public void rotate() {
        Block rotatedBlock = new Block(currentBlock.getShape(), currentBlock.getColor());
        rotatedBlock.setX(currentBlock.getX());
        rotatedBlock.setY(currentBlock.getY());
        rotatedBlock.rotate();
        if (gameBoard.canMove(rotatedBlock, 0, 0)) {
            currentBlock.rotate();
        }
    }

    public void moveDown() {
        if (gameBoard.canMove(currentBlock, 0, 1)) {
            currentBlock.moveDown();
        } else {
            gameBoard.placeBlock(currentBlock);
            if (isBlockAtTop()) { // Check if the block has reached the top
                gameOver = true;
            } else {
                generateNewBlock();
                currentBlock = blockQueue.poll();
            }
        }
    }

    private boolean isBlockAtTop() {
        int[][] shape = currentBlock.getShape();
        int y = currentBlock.getY();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0 && (y + i) <= 0) {
                    return true; // Block has reached the top
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public Block getNextBlock() {
        return blockQueue.peek();
    }
}

public class Q3b extends JPanel implements ActionListener {
    private static final int CELL_SIZE = 30;
    private TetrisGame tetrisGame;
    private Timer timer;
    private JLabel scoreLabel, levelLabel, timeLabel;
    private int score = 0;
    private int level = 1;
    private int time = 0; // Time in seconds

    public Q3b(TetrisGame tetrisGame) {
        this.tetrisGame = tetrisGame;
        setPreferredSize(new Dimension(tetrisGame.getGameBoard().getGrid()[0].length * CELL_SIZE,
                tetrisGame.getGameBoard().getGrid().length * CELL_SIZE));
        timer = new Timer(500, this);
        timer.start();

        // Create a panel for the game info (score, level, time)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.BLACK);

        // Score Label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(scoreLabel);

        // Level Label
        levelLabel = new JLabel("Level: 1");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(levelLabel);

        // Time Label
        timeLabel = new JLabel("Time: 0s");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(timeLabel);

        // Add the info panel to the right of the game board
        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.EAST);

        // Start a separate timer to update the time every second
        Timer timeTimer = new Timer(1000, e -> {
            time++;
            timeLabel.setText("Time: " + time + "s");
        });
        timeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color[][] grid = tetrisGame.getGameBoard().getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                g.setColor(grid[i][j]);
                g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        Block currentBlock = tetrisGame.getCurrentBlock();
        int[][] shape = currentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    g.setColor(currentBlock.getColor());
                    g.fillRect((currentBlock.getX() + j) * CELL_SIZE, (currentBlock.getY() + i) * CELL_SIZE, CELL_SIZE,
                            CELL_SIZE);
                    g.setColor(Color.GRAY);
                    g.drawRect((currentBlock.getX() + j) * CELL_SIZE, (currentBlock.getY() + i) * CELL_SIZE, CELL_SIZE,
                            CELL_SIZE);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!tetrisGame.isGameOver()) {
            tetrisGame.moveDown();
            updateScore(); // Update score when rows are cleared
            updateLevel(); // Update level based on score
            repaint();
        } else {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
        }
    }

    private void updateScore() {
        // Example: Increase score by 100 for each row cleared
        int rowsCleared = tetrisGame.getGameBoard().getRowsCleared();
        if (rowsCleared > 0) {
            score += rowsCleared * 100;
            scoreLabel.setText("Score: " + score);
            tetrisGame.getGameBoard().resetRowsCleared(); // Reset the counter
        }
    }

    private void updateLevel() {
        // Example: Increase level every 500 points
        int newLevel = score / 500 + 1;
        if (newLevel > level) {
            level = newLevel;
            levelLabel.setText("Level: " + level);
            // Increase game speed as level increases
            timer.setDelay(500 / level);
        }
    }

    public static void main(String[] args) {
        TetrisGame tetrisGame = new TetrisGame(10, 20);
        JFrame frame = new JFrame("Tetris");
        Q3b gui = new Q3b(tetrisGame);
        frame.add(gui);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Add key bindings for left, right, and rotate
        InputMap inputMap = gui.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gui.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetrisGame.moveLeft();
                gui.repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetrisGame.moveRight();
                gui.repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("UP"), "rotate");
        actionMap.put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetrisGame.rotate();
                gui.repaint();
            }
        });
    }
}
