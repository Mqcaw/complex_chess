package me.jdfunk.app;

import me.jdfunk.app.config.Config;
import me.jdfunk.app.render.GameRenderer;
import me.jdfunk.app.render.Java2DRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Scene extends JPanel implements Runnable{

    private GameRenderer renderer;

    private Thread gameThread;
    private boolean isRunning = false;

    //private int size = Config.getBoardSizeInPixels();
    private int size = Config.BOARD_SIZE_IN_PIXELS;

    public Scene() throws Exception {
        //let swing use double buffering
        this.setDoubleBuffered(true);
        // we are using a game loop to repaint, so we don't want swing randomly doing it for us
        this.setIgnoreRepaint(true);

        initSwingMouseEvents();
    }

    public void start() {
        if (gameThread == null) {
            isRunning = true;
            gameThread = new Thread(this, "game-loop");
            gameThread.start();
        }
    }

    public void stop() {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restartGame() throws Exception {

    }

    @Override
    public void run() {
        while (isRunning) {
            update();
            repaint();
            try { Thread.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (renderer == null) renderer = new Java2DRenderer(g2d);
        if (!renderer.isInitialized()) renderer.initialize();

        if (renderer instanceof Java2DRenderer java2d) java2d.setG2d(g2d);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }

    public void update() {
    }

    private void initSwingMouseEvents() {
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        };

        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }
}
