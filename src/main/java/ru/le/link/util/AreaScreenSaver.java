package ru.le.link.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AreaScreenSaver {
    // 214 - desktop, 100 - browser
    private static final int PIXELS_ONE_SCROLL = 100;
    private static final String PATH = "src/main/resources/";
    private static final int IMAGES_COUNT = 10;
    private static int id = 1;
    private Robot robot;
    private Rectangle area;

    public AreaScreenSaver() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        JButton button = new JButton();
        button.setText("leftAndUp");
        button.addKeyListener(new KeyAdapter() {
            Point leftAndUp;
            Point right;

            @Override
            public void keyTyped(KeyEvent event) {
                if (event.getKeyChar() == ' ') {
                    if (leftAndUp == null) {
                        leftAndUp = MouseInfo.getPointerInfo().getLocation();
                        button.setText("right");
                    } else if (area == null) {
                        right = MouseInfo.getPointerInfo().getLocation();
                        area = new Rectangle(leftAndUp.x, leftAndUp.y, right.x - leftAndUp.x, PIXELS_ONE_SCROLL);
                        button.setText("start");
                    } else {
                        int width = (int) area.getWidth();
                        BufferedImage result = new BufferedImage(width,
                                IMAGES_COUNT * PIXELS_ONE_SCROLL, BufferedImage.TYPE_INT_RGB);
                        Graphics g = result.getGraphics();
                        int y = 0;
                        for (int i = 0; i < IMAGES_COUNT; i++) {
                            try {
                                BufferedImage image = robot.createScreenCapture(area);
                                g.drawImage(image, 0, y, null);
                                y += image.getHeight();
                                Thread.sleep(500);
                                robot.mouseWheel(1);
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            ImageIO.write(result, "png", new File(PATH + id++ + ".png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        JFrame frame = new JFrame();
        frame.add(button);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AreaScreenSaver();
    }
}
