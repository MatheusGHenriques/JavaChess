package GUI;

import Files.MatchSaveAndPreferences;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window.
 */
public class ChessApp extends JFrame{
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    protected MenuPanel menuPanel;
    protected GamePanel gamePanel;
    private boolean lightMode = false;
    private int time = 600;

    /**
     * Starts the main App.
     */
    public static void main(String[] args){
        ChessApp chess = new ChessApp();
    }

    /**
     * Instantiates the window and its main components.
     */
    public ChessApp(){
        setTitle("Java Chess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gamePanel = new GamePanel(this);
        menuPanel = new MenuPanel(this, gamePanel);

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        add(mainPanel);

        String boardString = MatchSaveAndPreferences.loadGame(gamePanel, gamePanel.chessApp.menuPanel);
        if(boardString != null)
            gamePanel.getChessBoard().stringToBoard(boardString);
        lightMode = !lightMode;

        switchColorMode();
        setVisible(true);
    }

    /**
     * Switches the app's color mode between light and dark mode.
     */
    public void switchColorMode(){
        Color backgroundColor = lightMode? Color.WHITE : Color.DARK_GRAY;
        Color foregroundColor = lightMode? Color.BLACK : Color.WHITE;

        styleRoundButton(menuPanel.continueButton, backgroundColor, foregroundColor, foregroundColor);
        styleRoundButton(menuPanel.newGameButton, backgroundColor, foregroundColor, foregroundColor);
        styleRoundButton(menuPanel.quitButton, backgroundColor, foregroundColor, foregroundColor);
        styleRoundButton(menuPanel.colorModeButton, backgroundColor, foregroundColor, foregroundColor);
        styleRoundButton(menuPanel.assistsButton, backgroundColor, foregroundColor, foregroundColor);
        styleRoundButton(menuPanel.contrastButton, backgroundColor, foregroundColor, foregroundColor);

        styleRoundButton(gamePanel.startButton, backgroundColor, foregroundColor, foregroundColor);
        styleRoundButton(gamePanel.restartButton, backgroundColor, foregroundColor, foregroundColor);
        styleRoundButton(gamePanel.quitButton, backgroundColor, foregroundColor, foregroundColor);

        mainPanel.setBackground(backgroundColor);
        menuPanel.setBackground(backgroundColor);
        gamePanel.setBackground(backgroundColor);

        menuPanel.titlePanel.setBackground(backgroundColor);
        menuPanel.imagePanel.setBackground(backgroundColor);
        menuPanel.blackImagePanel.setBackground(backgroundColor);
        menuPanel.whiteImagePanel.setBackground(backgroundColor);
        menuPanel.centerPanel.setBackground(backgroundColor);
        menuPanel.timePanel.setBackground(backgroundColor);
        menuPanel.row3Panel.setBackground(backgroundColor);
        menuPanel.row2Panel.setBackground(backgroundColor);
        menuPanel.row1Panel.setBackground(backgroundColor);

        gamePanel.sidePanel.setBackground(backgroundColor);
        gamePanel.rightPanel.setBackground(backgroundColor);
        gamePanel.scrollPane.setBackground(backgroundColor);
        gamePanel.boardPanel.setBackground(backgroundColor);

        menuPanel.timeLabel.setForeground(foregroundColor);
        menuPanel.titleLabel.setForeground(foregroundColor);
        menuPanel.timeField.setBackground(backgroundColor);
        menuPanel.timeField.setForeground(foregroundColor);

        gamePanel.victoryLabel.setForeground(foregroundColor);
        gamePanel.victoryLabel.setBackground(backgroundColor);
        gamePanel.infoLabel.setBackground(backgroundColor);
        gamePanel.infoLabel.setForeground(foregroundColor);
        gamePanel.imagePanel.setBackground(backgroundColor);
        gamePanel.blackImage.setBackground(backgroundColor);
        gamePanel.whiteImage.setBackground(backgroundColor);
        gamePanel.whiteTimerLabel.setForeground(foregroundColor);
        gamePanel.blackTimerLabel.setForeground(foregroundColor);

        lightMode = !lightMode;
        if(lightMode){
            menuPanel.colorModeButton.setText("Light Mode");
            menuPanel.imageCard.show(menuPanel.imagePanel, "black");
        }else{
            menuPanel.colorModeButton.setText("Dark Mode");
            menuPanel.imageCard.show(menuPanel.imagePanel, "white");

        }
        menuPanel.colorToggleButtons();
    }

    /**
     * Styles a button in a predefined manner.
     *
     * @param button          The JButton to be styled.
     * @param backgroundColor The background's color of the button.
     * @param foregroundColor The foreground's color of the button.
     * @param outlineColor    The outline's color of the button.
     */
    protected void styleRoundButton(JButton button, Color backgroundColor, Color foregroundColor, Color outlineColor){
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI(){
            @Override
            public void paint(Graphics g, JComponent c){
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();
                int arcWidth = 20;
                int arcHeight = 20;

                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);

                g2.setColor(outlineColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, width - 1, height - 1, arcWidth, arcHeight);

                super.paint(g, c);
                g2.dispose();
            }
        });
    }

    /**
     * Switches to the specified panel.
     *
     * @param cardName String that represents the panel to be shown ("Game" or "Menu").
     */
    public void showCard(String cardName){
        cardLayout.show(mainPanel, cardName);
    }

    /**
     * Checks the current color mode (light or dark).
     *
     * @return True if light mode is on, and false if dark mode is on.
     */
    public boolean isLightMode(){
        return lightMode;
    }

    /**
     * Sets the current color mode (light or dark)
     *
     * @param lightMode If true, sets the mode to light mode, and if false, sets it to dark mode.
     */
    public void setLightMode(boolean lightMode){
        this.lightMode = lightMode;
    }

    /**
     * Sets the timer for each player of the game.
     *
     * @param time Integer representing the time in seconds.
     */
    public void setTime(int time){
        this.time = time;
    }

    /**
     * Returns the time for each player of the game.
     *
     * @return An integer representing the time in seconds.
     */
    public int getTime(){
        return time;
    }
}