package tetris.dataAccessLayer;

import tetris.model.HighScore;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

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
            return highScore.scoreList().get(rowIndex).user();
        }else if (columnIndex == 1){
            return highScore.scoreList().get(rowIndex).score();
        }else if (columnIndex == 2) {
            return highScore.scoreList().get(rowIndex).level();
        }else{
            return highScore.scoreList().get(rowIndex).lines();
        }
    }
}
