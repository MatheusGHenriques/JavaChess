package GUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * Custom JTable's cells renderer, renders visual assists and shows which piece is selected.
 */
public class HighlightCellRenderer extends JLabel implements TableCellRenderer{
    private int selectedRow = -1;
    private int selectedColumn = -1;
    private int[] lastMoveFrom = null;
    private int[] lastMoveTo = null;
    private ArrayList<int[]> validMoves = new ArrayList<>();
    private boolean contrastMode = false;
    private boolean visualAssistsMode = true;

    /**
     * Instantiates the custom cell renderer.
     */
    protected HighlightCellRenderer(){
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /**
     * Selects a cell.
     *
     * @param row Integer representing the row of the selected cell.
     * @param col Integer representing the column of the selected cell.
     */
    public void setSelectedCell(int row, int col){
        selectedRow = row;
        selectedColumn = col;
    }

    /**
     * Sets the last move made, so it can be rendered in red.
     *
     * @param from Integer vector with the previous position of the piece.
     * @param to   Integer vector with the new position of the piece.
     */
    public void setLastMove(int[] from, int[] to){
        lastMoveFrom = from;
        lastMoveTo = to;
    }

    /**
     * Sete the allowed moves of a piece, so they can be rendered in green.
     * Define os movimentos permitidos de uma peca, renderizados de verde.
     *
     * @param moves An ArrayList of integer vectors with the valid moves of a piece.
     */
    public void setValidMoves(ArrayList<int[]> moves){
        this.validMoves = moves;
    }

    /**
     * Sets the default visual aspects of the JTable.
     *
     * @param table      Reference of the JTable that represents the board visually.
     * @param value      Value of the cell.
     * @param isSelected If the cell is selected or not.
     * @param hasFocus   If the cell is focused or not.
     * @param row        Integer representing the row of the cell.
     * @param column     Integer representing the column of the cell.
     * @return The custom cell renderer.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        if((row + column) % 2 == 0)
            setBackground(Color.WHITE);
        else if(contrastMode)
            setBackground(Color.decode("#50749c"));
        else setBackground(Color.DARK_GRAY);

        putClientProperty("row", row);
        putClientProperty("col", column);

        if(value instanceof ImageIcon icon){
            Image img = icon.getImage().getScaledInstance(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row), Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(img));
            setText("");
        }else{
            setIcon(null);
            setText(value != null? value.toString() : "");
        }
        return this;
    }

    /**
     * Paints a colored overlay above a specific cell.
     *
     * @param g Graphical component of the renderer.
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        int row = (int) getClientProperty("row");
        int col = (int) getClientProperty("col");

        Graphics2D g2d = (Graphics2D) g.create();

        if(lastMoveFrom != null && lastMoveTo != null && visualAssistsMode){
            if((lastMoveFrom[0] == row && lastMoveFrom[1] == col) || (lastMoveTo[0] == row && lastMoveTo[1] == col)){
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2d.setColor(Color.RED);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        if(row == selectedRow && col == selectedColumn){
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        if(visualAssistsMode)
            for(int[] move : validMoves)
                if(move[0] == row && move[1] == col){
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

        g2d.dispose();
    }

    /**
     * Clear all overlays in the JTable.
     */
    public void clearAllHighlights(){
        this.selectedRow = -1;
        this.selectedColumn = -1;
        this.lastMoveFrom = null;
        this.lastMoveTo = null;
        this.validMoves.clear();
    }

    /**
     * Gets whether the visual assists mode is on or off.
     *
     * @return True if visual assists mode is on, and false if it's off.
     */
    public boolean isVisualAssistsMode(){
        return visualAssistsMode;
    }

    /**
     * Sets the visual assists mode to on or off.
     *
     * @param visualAssistsMode If true, visual assists mode will be on; if false, visual assists mode will be off.
     */
    public void setVisualAssistsMode(boolean visualAssistsMode){
        this.visualAssistsMode = visualAssistsMode;
    }

    /**
     * Gets whether the contrast mode is on or off.
     *
     * @return True if contrast mode is on, and false if it's off.
     */
    public boolean isContrastMode(){
        return contrastMode;
    }

    /**
     * Sets the contrast mode to on or off.
     *
     * @param contrastMode If true, contrast mode will be on; if false, contrast mode will be off.
     */
    public void setContrastMode(boolean contrastMode){
        this.contrastMode = contrastMode;
    }
}