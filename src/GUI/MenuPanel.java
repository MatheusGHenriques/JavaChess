package GUI;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;

/**
 * The menu panel and its components.
 */
public class MenuPanel extends JPanel{
    protected JTextField timeField;
    protected JButton continueButton;
    protected JButton newGameButton;
    protected JButton quitButton;
    protected JButton colorModeButton;
    protected JButton contrastButton;
    protected JButton assistsButton;
    protected JLabel titleLabel;
    protected JPanel titlePanel;

    protected JPanel timePanel;
    protected JLabel timeLabel;
    protected JPanel imagePanel;
    protected JPanel centerPanel;
    protected ImagePanel whiteImagePanel;
    protected ImagePanel blackImagePanel;
    protected CardLayout imageCard;
    protected JPanel row3Panel;
    protected JPanel row2Panel;
    protected JPanel row1Panel;

    private boolean contrastMode;
    private boolean assistsMode;

    protected ChessApp chessApp;
    protected GamePanel gamePanel;

    /**
     * Instantiates the MenuPanel and its components.
     *
     * @param chessApp  Reference of the main App.
     * @param gamePanel Reference of the GamePanel.
     */
    public MenuPanel(ChessApp chessApp, GamePanel gamePanel){
        this.chessApp = chessApp;
        this.gamePanel = gamePanel;

        setLayout(new BorderLayout());

        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Java Chess", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        titlePanel.add(Box.createVerticalStrut(50));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(20));

        add(titlePanel, BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        row1Panel = new JPanel();
        row2Panel = new JPanel();
        row3Panel = new JPanel();
        row1Panel.setLayout(new GridBagLayout());
        row2Panel.setLayout(new GridBagLayout());
        row3Panel.setLayout(new GridBagLayout());

        row1Panel.setAlignmentX(CENTER_ALIGNMENT);
        row2Panel.setAlignmentX(CENTER_ALIGNMENT);
        row3Panel.setAlignmentX(CENTER_ALIGNMENT);

        continueButton = new JButton(" Continue  ");
        newGameButton = new JButton("New Game");
        quitButton = new JButton("Save & Quit");
        colorModeButton = new JButton(" Dark Mode ");
        assistsButton = new JButton("Visual Assists");
        contrastButton = new JButton("High Contrast");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);

        gbc.weightx = 1;
        gbc.gridx = 0;
        row1Panel.add(newGameButton, gbc);
        row2Panel.add(assistsButton, gbc);
        row3Panel.add(colorModeButton, gbc);

        gbc.weightx = 0;
        gbc.gridx = 1;
        row1Panel.add(Box.createHorizontalStrut(0), gbc);
        row2Panel.add(Box.createHorizontalStrut(0), gbc);
        row3Panel.add(Box.createHorizontalStrut(0), gbc);

        gbc.weightx = 1;
        gbc.gridx = 2;
        row1Panel.add(continueButton, gbc);
        row2Panel.add(contrastButton, gbc);
        row3Panel.add(quitButton, gbc);

        timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
        timeLabel = new JLabel("Time for each player (in minutes): ");
        timeField = new JTextField("      10", 2);
        timeField.setBorder(new StrokeBorder(new BasicStroke()));

        timePanel.add(timeLabel);
        timePanel.add(timeField);
        timePanel.setMaximumSize(new Dimension(300, 20));

        blackImagePanel = new ImagePanel("Images/bk.png");
        whiteImagePanel = new ImagePanel("Images/wk.png");
        imageCard = new CardLayout();
        imagePanel = new JPanel(imageCard);
        imagePanel.add(blackImagePanel, "black");
        imagePanel.add(whiteImagePanel, "white");
        imagePanel.setAlignmentX(CENTER_ALIGNMENT);
        imagePanel.setMaximumSize(new Dimension(200, 200));

        centerPanel.add(imagePanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(timePanel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(row1Panel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(row2Panel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(row3Panel);

        add(centerPanel, BorderLayout.CENTER);

        buttonsListeners();
    }

    /**
     * Creates the buttons' action listeners.
     */
    private void buttonsListeners(){
        newGameButton.addActionListener(actionEvent -> {
            chessApp.showCard("Game");
            chessApp.setTime((int) ((Double.parseDouble(timeField.getText())) * 60));
            gamePanel.restartGame();
        });

        continueButton.addActionListener(actionEvent -> {
            chessApp.showCard("Game");
            gamePanel.setWhiteTurn(!gamePanel.isWhiteTurn());
            gamePanel.switchTurn();
            gamePanel.pauseGame();
            if(gamePanel.chessBoard.checkMate(gamePanel.isWhiteTurn()) || gamePanel.chessBoard.checkMate(!gamePanel.isWhiteTurn()))
                gamePanel.startButton.setEnabled(false);
        });

        quitButton.addActionListener(actionEvent -> System.exit(0));

        colorModeButton.addActionListener(actionEvent -> chessApp.switchColorMode());

        assistsButton.addActionListener(actionEvent -> toggleAssists());

        contrastButton.addActionListener(actionEvent -> toggleContrast());
    }

    /**
     * Dynamically resizes the MenuPanel and its components.
     */
    @Override
    public void doLayout(){
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int fontSize = Math.max(12, panelHeight / 40);

        int titleFontSize = fontSize + 20;

        int imageSize = panelHeight / 4;

        int buttonWidth = (int) (panelWidth / 4.5);
        int buttonHeight = panelHeight / 30;

        int textFieldWidth = panelWidth / 7;
        int textFieldHeight = panelHeight / 20;

        timeLabel.setFont(new Font("Arial", Font.PLAIN, fontSize));
        titleLabel.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        timeField.setFont(new Font("Arial", Font.BOLD, fontSize));
        timePanel.setMaximumSize(new Dimension((fontSize * 20) + 10, textFieldHeight));
        timeField.setMaximumSize(new Dimension(fontSize, textFieldHeight));
        timeLabel.setMaximumSize(new Dimension(fontSize * 16, textFieldHeight));

        row1Panel.setMaximumSize(new Dimension(buttonWidth * 2, buttonHeight * 2));
        row2Panel.setMaximumSize(new Dimension(buttonWidth * 2, buttonHeight * 2));
        row3Panel.setMaximumSize(new Dimension(buttonWidth * 2, buttonHeight * 2));

        timeField.setMaximumSize(new Dimension(textFieldWidth, textFieldHeight));

        imagePanel.setMaximumSize(new Dimension(imageSize, imageSize));

        updateButtonSizeAndFont(continueButton, buttonWidth, buttonHeight, fontSize);
        updateButtonSizeAndFont(newGameButton, buttonWidth, buttonHeight, fontSize);
        updateButtonSizeAndFont(colorModeButton, buttonWidth, buttonHeight, fontSize);
        updateButtonSizeAndFont(quitButton, buttonWidth, buttonHeight, fontSize);
        updateButtonSizeAndFont(contrastButton, buttonWidth, buttonHeight, fontSize);
        updateButtonSizeAndFont(assistsButton, buttonWidth, buttonHeight, fontSize);

        centerPanel.removeAll();

        centerPanel.add(imagePanel);
        centerPanel.add(Box.createVerticalStrut(panelHeight / 40));
        centerPanel.add(timePanel);
        centerPanel.add(Box.createVerticalStrut(panelHeight / 30));
        centerPanel.add(row1Panel);
        centerPanel.add(Box.createVerticalStrut(panelHeight / 30));
        centerPanel.add(row2Panel);
        centerPanel.add(Box.createVerticalStrut(panelHeight / 30));
        centerPanel.add(row3Panel);

        centerPanel.revalidate();
        centerPanel.repaint();
        super.doLayout();
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
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
    }

    /**
     * Enables the continue button.
     */
    public void enableContinueButton(){
        continueButton.setEnabled(true);
    }

    /**
     * Disables the continue button.
     */
    public void disableContinueButton(){
        continueButton.setEnabled(false);
    }

    /**
     * Switches the visual assists' mode.
     */
    private void toggleAssists(){
        assistsMode = !gamePanel.isVisualAssists();
        gamePanel.setVisualAssists(assistsMode);
        colorToggleButtons();
    }

    /**
     * Switches the contrast mode.
     */
    private void toggleContrast(){
        contrastMode = !gamePanel.isColorContrast();
        gamePanel.setColorContrast(contrastMode);
        colorToggleButtons();
    }

    /**
     * Updates the contrast and visual assists buttons' colors.
     */
    protected void colorToggleButtons(){
        chessApp.styleRoundButton(contrastButton, chessApp.isLightMode()? Color.DARK_GRAY : Color.WHITE, chessApp.isLightMode()? Color.WHITE : Color.DARK_GRAY, gamePanel.isColorContrast()? Color.GREEN : Color.RED);
        chessApp.styleRoundButton(assistsButton, chessApp.isLightMode()? Color.DARK_GRAY : Color.WHITE, chessApp.isLightMode()? Color.WHITE : Color.DARK_GRAY, gamePanel.isVisualAssists()? Color.GREEN : Color.RED);
    }
}