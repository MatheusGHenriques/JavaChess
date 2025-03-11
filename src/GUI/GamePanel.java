package GUI;

import Files.MatchSaveAndPreferences;
import Logic.Board;
import Logic.ChessPiece;
import Logic.VectorPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The game panel and its components.
 */
public class GamePanel extends JPanel{
    protected ChessApp chessApp;
    protected JTable chessTable;
    protected Board chessBoard;
    protected JPanel boardPanel;
    protected JScrollPane scrollPane;
    protected JPanel imagePanel;
    protected CardLayout imageLayout;
    protected HighlightCellRenderer cellRenderer;
    protected JPanel rightPanel;

    protected JButton startButton;
    protected JButton restartButton;
    protected JButton quitButton;
    protected JPanel sidePanel;
    protected JLabel whiteTimerLabel;
    protected JLabel blackTimerLabel;
    protected ImagePanel whiteImage;
    protected ImagePanel blackImage;
    protected JLabel victoryLabel;
    protected JLabel infoLabel;

    private int whiteTimeRemaining;
    private int blackTimeRemaining;
    private Timer whiteTimer;
    private Timer blackTimer;
    private boolean isWhiteTurn = true;

    /**
     * Instantiates the GamePanel and its components.
     *
     * @param chessApp Reference of the main App.
     */
    public GamePanel(ChessApp chessApp){
        this.chessApp = chessApp;
        setLayout(null);

        chessBoard = new Board(true);
        chessTable = createChessBoard();

        scrollPane = new JScrollPane(chessTable);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);

        boardPanel = new JPanel(null);
        boardPanel.add(scrollPane);
        add(boardPanel);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setAlignmentX(CENTER_ALIGNMENT);

        whiteTimerLabel = new JLabel("White: 00:00");
        blackTimerLabel = new JLabel("Black: 00:00");
        whiteTimerLabel.setAlignmentX(CENTER_ALIGNMENT);
        blackTimerLabel.setAlignmentX(CENTER_ALIGNMENT);

        infoLabel = new JLabel("");
        infoLabel.setAlignmentX(CENTER_ALIGNMENT);
        victoryLabel = new JLabel("");
        victoryLabel.setAlignmentX(CENTER_ALIGNMENT);

        startButton = new JButton("Start");
        restartButton = new JButton("Restart");
        quitButton = new JButton("Exit to Menu");
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        restartButton.setAlignmentX(CENTER_ALIGNMENT);
        quitButton.setAlignmentX(CENTER_ALIGNMENT);

        imageLayout = new CardLayout();
        imagePanel = new JPanel(imageLayout);

        whiteImage = new ImagePanel("Images/wp.png");
        whiteImage.setAlignmentX(CENTER_ALIGNMENT);
        blackImage = new ImagePanel("Images/bp.png");
        blackImage.setAlignmentX(CENTER_ALIGNMENT);

        imagePanel.add(whiteImage, "white");
        imagePanel.add(blackImage, "black");

        rightPanel = new JPanel(null);
        rightPanel.add(sidePanel);
        add(rightPanel);

        initializeTimers();
        buttonsListeners();
        resizeSidePanel();
        updateBoard();
        lockTable();
    }

    /**
     * Creates the buttons' action listeners.
     */
    private void buttonsListeners(){
        restartButton.addActionListener(actionEvent -> restartGame());
        startButton.addActionListener(actionEvent -> resumeGame());
        quitButton.addActionListener(actionEvent -> {
            chessApp.showCard("Menu");
            pauseGame();
            MatchSaveAndPreferences.saveGame(chessBoard, GamePanel.this);
            chessApp.menuPanel.enableContinueButton();
        });
    }

    /**
     * Initialize the players' timers.
     */
    private void initializeTimers(){
        whiteTimer = new Timer(1000, actionEvent -> {
            if(whiteTimeRemaining > 0){
                whiteTimeRemaining--;
                updateTimerLabels();
            }else{
                gameEnds();
                infoLabel.setText("White's time has ended!");
            }
        });

        blackTimer = new Timer(1000, actionEvent -> {
            if(blackTimeRemaining > 0){
                blackTimeRemaining--;
                updateTimerLabels();
            }else{
                gameEnds();
                infoLabel.setText("Black's time has ended!");

            }
        });
    }

    /**
     * Updates dynamically the element's sizes.
     */
    @Override
    public void doLayout(){
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int boardSize = Math.min(panelWidth, panelHeight);

        boardPanel.setBounds(0, 0, boardSize, boardSize);
        scrollPane.setBounds(0, 0, boardSize, boardSize);

        int cellSize = (boardSize - 10) / 8;
        if(cellSize < 1)
            cellSize = 1;
        chessTable.setRowHeight(cellSize);
        for(int i = 0; i < chessTable.getColumnCount(); i++)
            chessTable.getColumnModel().getColumn(i).setPreferredWidth(cellSize);

        int rightWidth = panelWidth - boardSize;
        if(rightWidth < 0) rightWidth = 0;
        rightPanel.setBounds(boardSize, 0, rightWidth, panelHeight);

        sidePanel.setBounds(0, 0, rightWidth, panelHeight);
        sidePanel.setMaximumSize(new Dimension(rightWidth - 10, panelHeight));
        sidePanel.setPreferredSize(new Dimension(rightWidth - 10, panelHeight));

        resizeSidePanel();

        super.doLayout();
    }

    /**
     * Creates the visual chess board using a JTable.
     *
     * @return The created JTable.
     */
    private JTable createChessBoard(){
        JTable table = new JTable(8, 8){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table.setTableHeader(null);

        table.setRowHeight(50);
        for(int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(50);

        cellRenderer = new HighlightCellRenderer();
        table.setDefaultRenderer(Object.class, cellRenderer);

        return table;
    }

    /**
     * Dynamically resizes the side panel and its elements.
     */
    private void resizeSidePanel(){
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int fontSize = Math.max(12, panelHeight / 25);

        int imageSize = panelHeight / 4;

        int buttonWidth = (int) (panelWidth / 4.5);
        int buttonHeight = panelHeight / 25;

        whiteTimerLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        blackTimerLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        infoLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        victoryLabel.setFont(new Font("Arial", Font.BOLD, fontSize));

        whiteImage.setMaximumSize(new Dimension(imageSize, imageSize));
        blackImage.setMaximumSize(new Dimension(imageSize, imageSize));
        imagePanel.setMaximumSize(new Dimension(imageSize, imageSize));

        updateButtonSizeAndFont(startButton, buttonWidth, buttonHeight, fontSize);
        updateButtonSizeAndFont(restartButton, buttonWidth, buttonHeight, fontSize);
        updateButtonSizeAndFont(quitButton, buttonWidth, buttonHeight, fontSize);

        sidePanel.removeAll();
        sidePanel.add(Box.createVerticalStrut(panelHeight / 20));
        sidePanel.add(blackTimerLabel);
        sidePanel.add(Box.createVerticalStrut(panelHeight / 10));
        sidePanel.add(infoLabel);
        sidePanel.add(Box.createVerticalStrut(panelHeight / 30));
        sidePanel.add(victoryLabel);
        sidePanel.add(Box.createVerticalStrut(panelHeight / 30));
        sidePanel.add(imagePanel);
        sidePanel.add(Box.createVerticalStrut(panelHeight / 30));
        sidePanel.add(startButton);
        sidePanel.add(Box.createVerticalStrut(panelHeight / 30));
        sidePanel.add(restartButton);
        sidePanel.add(Box.createVerticalStrut(panelHeight / 30));
        sidePanel.add(quitButton);
        sidePanel.add(Box.createVerticalStrut(panelHeight / 5));
        sidePanel.add(whiteTimerLabel);

        sidePanel.revalidate();
        sidePanel.repaint();
    }

    /**
     * Updates the size and font of a JButton.
     *
     * @param button   The JButton to be updated.
     * @param width    The JButton's new width.
     * @param height   The JButton's new height.
     * @param fontSize The JButton's new font size.
     */
    private void updateButtonSizeAndFont(JButton button, int width, int height, int fontSize){
        if(button == null) return;
        button.setMaximumSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, (int) (fontSize / 1.5)));
    }

    /**
     * Restarts the game.
     */
    public void restartGame(){
        chessBoard = new Board(true);
        whiteTimeRemaining = chessApp.getTime();
        blackTimeRemaining = chessApp.getTime();
        isWhiteTurn = false;
        switchTurn();
        infoLabel.setText("");
        victoryLabel.setText("");
        pauseGame();
    }

    /**
     * Updates the visual chess board according to the logic board.
     */
    private void updateBoard(){
        for(int k = 0; k < 8; k++){
            for(int i = 0; i < 8; i++){
                String pos = VectorPosition.convertVectorToStringPosition(new int[]{k, i});
                ChessPiece piece = chessBoard.getPiece(pos);
                if(piece != null)
                    chessTable.setValueAt(piece.getImage(), k, i);
                else
                    chessTable.setValueAt(null, k, i);
            }
        }
    }

    /**
     * Pauses the game.
     */
    public void pauseGame(){
        lockTable();
        cellRenderer.clearAllHighlights();
        whiteTimer.stop();
        blackTimer.stop();
        updateTimerLabels();
        updateBoard();
        startButton.setEnabled(true);
    }

    /**
     * Resumes the game.
     */
    public void resumeGame(){
        startButton.setEnabled(false);
        unlockTable();
        updateTimerLabels();
        updateBoard();
        if(isWhiteTurn)
            whiteTimer.start();
        else blackTimer.start();
    }

    /**
     * Ends the game.
     */
    public void gameEnds(){
        lockTable();
        whiteTimer.stop();
        blackTimer.stop();
        if(isWhiteTurn){
            imageLayout.show(imagePanel, "black");
            victoryLabel.setText("Black's Victory!");
        }else{
            imageLayout.show(imagePanel, "white");
            victoryLabel.setText("White's Victory!");
        }
    }

    /**
     * Switches player's turn, verifying if there was a check or checkmate.
     */
    public void switchTurn(){
        isWhiteTurn = !isWhiteTurn;
        if(chessBoard.checkMate(isWhiteTurn)){
            gameEnds();
            infoLabel.setText("Check Mate!");
            return;
        }
        if(isWhiteTurn){
            blackTimer.stop();
            whiteTimer.start();
            imageLayout.show(imagePanel, "white");
        }else{
            whiteTimer.stop();
            blackTimer.start();
            imageLayout.show(imagePanel, "black");
        }
        if(chessBoard.getMyKing("white").isInCheck(chessBoard) != null || chessBoard.getMyKing("black").isInCheck(chessBoard) != null)
            infoLabel.setText("Check!");
        else infoLabel.setText("");
    }

    /**
     * Updates the timers' labels.
     */
    private void updateTimerLabels(){
        whiteTimerLabel.setText("White: " + formatTime(whiteTimeRemaining));
        blackTimerLabel.setText("Black: " + formatTime(blackTimeRemaining));
    }

    /**
     * Formats the time for the timers.
     *
     * @param seconds Integer representing the time in seconds.
     * @return String with the formatted time to be displayed.
     */
    private String formatTime(int seconds){
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    /**
     * Locks the table, preventing user interaction.
     */
    public void lockTable(){
        chessTable.setEnabled(false);

        for(MouseListener listener : chessTable.getMouseListeners())
            chessTable.removeMouseListener(listener);
    }

    /**
     * Unlocks the table, allowing user interaction. Also adds mouse interactions to pieces.
     */
    public void unlockTable(){
        chessTable.setEnabled(true);

        chessTable.addMouseListener(new MouseAdapter(){
            private int selectedRow = -1, selectedCol = -1;
            private boolean isPieceSelected = false;
            private int[] lastMoveFrom = null;
            private int[] lastMoveTo = null;
            private final ArrayList<int[]> validMoves = new ArrayList<>();

            private long lastClickTime = 0;

            @Override
            public void mouseClicked(MouseEvent e){
                long clickTime = System.currentTimeMillis();
                long DOUBLE_CLICK_THRESHOLD = 100;
                if(clickTime - lastClickTime < DOUBLE_CLICK_THRESHOLD)
                    return;

                lastClickTime = clickTime;

                int r = chessTable.rowAtPoint(e.getPoint());
                int c = chessTable.columnAtPoint(e.getPoint());

                if(isPieceSelected){
                    int[] from = {selectedRow, selectedCol};
                    int[] to = {r, c};

                    if(isValidMove(to)){
                        chessBoard.movePiece(from, to, GamePanel.this);
                        lastMoveFrom = from;
                        lastMoveTo = to;
                        updateBoard();
                        switchTurn();
                    }

                    isPieceSelected = false;
                    validMoves.clear();
                    cellRenderer.setSelectedCell(-1, -1);
                    cellRenderer.setValidMoves(validMoves);
                    cellRenderer.setLastMove(lastMoveFrom, lastMoveTo);
                }else{
                    ChessPiece piece = chessBoard.getPiece(new int[]{r, c});
                    if(piece != null && ((isWhiteTurn && piece.getPlayer().getColor().equals("white")) || (!isWhiteTurn && piece.getPlayer().getColor().equals("black")))){
                        cellRenderer.setSelectedCell(r, c);
                        isPieceSelected = true;
                        selectedRow = r;
                        selectedCol = c;
                        validMoves.clear();

                        ArrayList<String> validatedMoves = piece.getPossibleMoves(chessBoard, true);

                        if(validatedMoves != null)
                            for(String move : validatedMoves){
                                int[] moveVector = VectorPosition.convertStringToVector(move);
                                validMoves.add(moveVector);
                            }

                        cellRenderer.setValidMoves(validMoves);
                    }else{
                        isPieceSelected = false;
                        validMoves.clear();
                        cellRenderer.setSelectedCell(-1, -1);
                        cellRenderer.setValidMoves(validMoves);
                    }
                }
                chessTable.repaint();
            }

            private boolean isValidMove(int[] position){
                for(int[] move : validMoves)
                    if(move[0] == position[0] && move[1] == position[1])
                        return true;
                return false;
            }
        });
    }

    /**
     * Opens up a JDialog above the table, prompting the user to choose to which piece its pawn shall be promoted.
     *
     * @return String that shows which piece the user chose.
     */
    public String showImageSelectionOverlay(){
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Promoted! Select the new piece:", true);
        dialog.setLayout(new GridLayout(2, 2));
        dialog.setSize(400, 200);

        final String[] selectedPiece = {null};

        ImageIcon q, b, r, n;
        if(isWhiteTurn){
            q = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wq.png")));
            b = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wb.png")));
            r = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wr.png")));
            n = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wn.png")));
        }else{
            q = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/bq.png")));
            b = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/bb.png")));
            r = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/br.png")));
            n = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/bn.png")));
        }

        JButton btnQ = createImageButton(q, "q", selectedPiece, dialog);
        JButton btnB = createImageButton(b, "b", selectedPiece, dialog);
        JButton btnR = createImageButton(r, "r", selectedPiece, dialog);
        JButton btnN = createImageButton(n, "n", selectedPiece, dialog);

        dialog.add(btnQ);
        dialog.add(btnB);
        dialog.add(btnR);
        dialog.add(btnN);

        Point tableLocation = chessTable.getLocationOnScreen();
        dialog.setLocation(tableLocation.x + getWidth() / 8, tableLocation.y + getHeight() / 3);

        dialog.setVisible(true);

        return selectedPiece[0];
    }

    /**
     * Creates a JButton with a piece's image for the promotion selection panel.
     *
     * @param icon          The ImageIcon to be shown in the button.
     * @param pieceCode     The code that a piece should return when chosen.
     * @param selectedPiece The String vector that represents the chosen piece.
     * @param dialog        The JDialog where the button will be placed.
     * @return The JButton created.
     */
    private JButton createImageButton(ImageIcon icon, String pieceCode, String[] selectedPiece, JDialog dialog){
        JButton button = new JButton(icon);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.setBackground(restartButton.getBackground());

        button.addActionListener(actionEvent -> {
            selectedPiece[0] = pieceCode;
            dialog.dispose();
        });

        return button;
    }

    /**
     * Gets white's remaining time.
     *
     * @return An integer with white's remaining time in seconds.
     */
    public int getWhiteTimeRemaining(){
        return whiteTimeRemaining;
    }

    /**
     * Gets black's remaining time.
     *
     * @return An integer with black's remaining time in seconds.
     */
    public int getBlackTimeRemaining(){
        return blackTimeRemaining;
    }

    /**
     * Sets white's remaining time.
     *
     * @param whiteTimeRemaining An integer with white's remaining time in seconds.
     */
    public void setWhiteTimeRemaining(int whiteTimeRemaining){
        this.whiteTimeRemaining = whiteTimeRemaining;
    }

    /**
     * Sets black's remaining time.
     *
     * @param blackTimeRemaining An integer with black's remaining time in seconds.
     */
    public void setBlackTimeRemaining(int blackTimeRemaining){
        this.blackTimeRemaining = blackTimeRemaining;
    }

    /**
     * Sets the players' turn.
     *
     * @param whiteTurn If true, it'll be white's turn; if false, it'll be black's turn.
     */
    public void setWhiteTurn(boolean whiteTurn){
        isWhiteTurn = whiteTurn;
    }

    /**
     * Gets the players' turn.
     *
     * @return True if it's white's turn, and false if it's black's turn.
     */
    public boolean isWhiteTurn(){
        return isWhiteTurn;
    }

    /**
     * Gets the reference of the logic board.
     *
     * @return The reference of the logic board.
     */
    public Board getChessBoard(){
        return chessBoard;
    }

    /**
     * Sets visual assists mode.
     *
     * @param visualAssists If true, visual assists will be on; if false, it'll be off.
     */
    public void setVisualAssists(boolean visualAssists){
        cellRenderer.setVisualAssistsMode(visualAssists);
    }

    /**
     * Sets color contrast mode.
     *
     * @param colorContrast If true, color contrast will be on; if false, it'll be off.
     */
    public void setColorContrast(boolean colorContrast){
        cellRenderer.setContrastMode(colorContrast);
    }

    /**
     * Gets the current visual assists mode.
     *
     * @return True if the visual assists mode is on, and false if it's off.
     */
    public boolean isVisualAssists(){
        return cellRenderer.isVisualAssistsMode();
    }

    /**
     * Gets the current color contrast mode.
     *
     * @return True if the color contrast mode is on, and false if it's off.
     */
    public boolean isColorContrast(){
        return cellRenderer.isContrastMode();
    }

    /**
     * Get the main App's reference.
     *
     * @return The main App's reference.
     */
    public ChessApp getChessApp(){
        return chessApp;
    }
}