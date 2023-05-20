package tetris.util;

import javax.swing.*;
import java.awt.*;

public class ViewUtil {
    public static JPanel createPanelOffset(int width, int height){
        JPanel panelOffset = new JPanel();
        panelOffset.setPreferredSize(new Dimension(width, height));
        panelOffset.setOpaque(false);

        return panelOffset;
    }
}
