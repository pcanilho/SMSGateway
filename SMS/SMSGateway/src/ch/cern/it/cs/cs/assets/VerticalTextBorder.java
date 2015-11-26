package ch.cern.it.cs.cs.assets;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.border.Border;

public class VerticalTextBorder implements Border {
    @Override
    public Insets getBorderInsets(final Component c) {
        return new Insets(0, 15, 0, 0);
    }

    @Override
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
        final Graphics2D g2 = (Graphics2D)g;
        final AffineTransform fontAT = new AffineTransform();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        fontAT.rotate(-Math.PI/2);
        g2.setFont(g2.getFont().deriveFont(fontAT));
        g2.drawString("Menu", 10, height);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}