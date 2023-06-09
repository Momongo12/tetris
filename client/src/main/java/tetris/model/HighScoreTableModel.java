package tetris.model;


import javax.swing.table.AbstractTableModel;

/**
 * @author denMoskvin
 * @version 1.0
 */
public class HighScoreTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Player", "Score", "Level", "Lines"};
    private HighScore highScore;
    public HighScoreTableModel(HighScore highScore) {
        this.highScore = highScore;
    }

    public void updateHighScoreData(HighScore highScore) { this.highScore = highScore; }

    @Override
    public int getRowCount() {
        return highScore.scoreList().size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int index){
        return COLUMN_NAMES[index];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0){
            return highScore.scoreList().get(rowIndex).getUsername();
        }else if (columnIndex == 1){
            return highScore.scoreList().get(rowIndex).getScore();
        }else if (columnIndex == 2) {
            return highScore.scoreList().get(rowIndex).getLevel();
        }else{
            return highScore.scoreList().get(rowIndex).getLines();
        }
    }
}
