package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private  int ScreenSize;
    private JImageDisplay ImageDisplay;
    private FractalGenerator FracGen;
    private Rectangle2D.Double Range;

    public FractalExplorer(int size)
    {
        ScreenSize = size;
        FracGen = new Mandelbrot();
        Range = new Rectangle2D.Double();
        FracGen.getInitialRange(Range);
        ImageDisplay = new JImageDisplay(ScreenSize, ScreenSize);
    }

    public void createAndShowGUI()
    {
        JFrame Frame = new JFrame("Fractal Explorer");
        ImageDisplay.setLayout(new BorderLayout());
        Frame.add(ImageDisplay, BorderLayout.CENTER);

        JButton ResetButton = new JButton("Reset Button");
        ResetEvent ResEv = new ResetEvent();
        ResetButton.addActionListener(ResEv);

        ClickZoom Click = new ClickZoom();
        ImageDisplay.addMouseListener(Click);

        Frame.add(ResetButton, BorderLayout.SOUTH);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Frame.pack ();
        Frame.setVisible (true);
        Frame.setResizable (false);
    }

    public void drawFractal()
    {
        for(int x = 0; x < ScreenSize; x++)
        {
            for (int y = 0; y < ScreenSize; y++)
            {
                int CountIterX = 0;
                int CountIterY = 0;
                double xCoord = FractalGenerator.getCoord (Range.x, Range.x + Range.width, ScreenSize, x);
                double yCoord = FractalGenerator.getCoord (Range.y, Range.y + Range.height, ScreenSize, y);
                int Iteration = FracGen.numIterations(xCoord, yCoord);
                if (Iteration == -1)
                    ImageDisplay.drawPixel(x, y, 0);
                else
                {
                    float hue = 0.1f + (float) Iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    ImageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        ImageDisplay.repaint();
    }

    private class ResetEvent implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            FracGen.getInitialRange(Range);
            drawFractal();
        }
    }

    private class ClickZoom extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            double xCoord = FracGen.getCoord(Range.x, Range.x + Range.width, ScreenSize, x);
            double yCoord = FracGen.getCoord(Range.y, Range.y + Range.height, ScreenSize, y);
            FracGen.recenterAndZoomRange(Range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
}
