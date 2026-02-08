package me.jdfunk.app;

import me.jdfunk.api.log.Logger;
import me.jdfunk.app.config.Config;

import javax.swing.*;

public class Window {
    private JPanel windowPanel;
    private Scene scene;
    private static JFrame frame;
    private static final Logger logger = Main.getLogger();

    public Window() {
        try {
            createAndShowUI();
        } catch (Exception e) {
            logger.error("Failed to start the window", e);
        }
    }

    private void createAndShowUI() throws Exception {
        logger.trace("createAndShowUI(), starting UI");
        frame = new JFrame(Config.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JMenuBar menuBar = createMenuBar();
        //frame.setJMenuBar(menuBar);

        this.windowPanel = new JPanel();
        this.windowPanel.setLayout(new BoxLayout(this.windowPanel, BoxLayout.X_AXIS));
        logger.trace("Creating scene");
        this.scene = new Scene();
        logger.info("Scene created");

        windowPanel.add(scene);
        frame.add(windowPanel);


        this.addKeyBindings();

        logger.trace("Starting scene thread");
        this.scene.start();
        logger.info("Scene thread started");

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);

        //WindowManager.registerFrame(frame, scene);
        logger.info("UI loaded");
    }

    /*
    private JMenuBar createMenuBar() {
        System.out.println("Loading menu bar...");
        GameConfig.initialize();

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            try {
                scene.restartGame();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            scene.stop();
            System.exit(0);
        });
        fileMenu.add(newGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);


        JMenu editMenu = new JMenu("Edit");
        JMenuItem preferencesItem = new JMenuItem("Preferences");
        preferencesItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Preferences coming soon!", "Preferences", JOptionPane.INFORMATION_MESSAGE);
        });
        editMenu.add(preferencesItem);


        JMenu viewMenu = new JMenu("View");
        JMenu pieceSetMenu = new JMenu("Piece Set");
        ButtonGroup pieceSetGroup = new ButtonGroup();

        System.out.println("Loading piece sets for menu bar...");
        ResourceManager.getAllPieceSets().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .forEach(pieceSet -> {
                    String displayName = formatName(pieceSet.getName());

                    //ImageIcon icon = new ImageIcon(ImageLoader.loadImage(String.valueOf(Icon.getPath(pieceSet))));


                    BufferedImage img = ImageLoader.loadImage(String.valueOf(Icon.getPath(pieceSet)));
                    System.out.println("Loaded icon from: " + Icon.getPath(pieceSet) + " (" + img.getWidth() + "x" + img.getHeight() + ")");
                    ImageIcon icon = new ImageIcon(img);


                    JRadioButtonMenuItem item = new JRadioButtonMenuItem(displayName);
                    item.setIcon(icon);
                    item.setSelected(pieceSet.equals(GameConfig.getCurrentPieceSet()));
                    item.addActionListener(e -> {
                        GameConfig.setCurrentPieceSet(pieceSet);
                        scene.getGame().reload();
                    });

                    pieceSetGroup.add(item);
                    pieceSetMenu.add(item);
                });
        System.out.println("Loaded piece sets for menu bar.");

        JMenu boardThemeMenu = new JMenu("Board Theme");
        ButtonGroup boardThemeGroup = new ButtonGroup();

        System.out.println("Loading board themes for menu bar...");
        ResourceManager.getAllThemes().stream()
                .sorted((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()))
                .forEach(theme -> {
                    String displayName = formatName(theme.getName());

                    BufferedImage img = ImageLoader.loadImage(String.valueOf(Icon.getPath(theme)));
                    ImageIcon icon = new ImageIcon(img);

                    JRadioButtonMenuItem item = new JRadioButtonMenuItem(displayName);
                    item.setIcon(icon);
                    item.setSelected(theme.equals(GameConfig.getCurrentTheme()));
                    item.addActionListener(e -> {
                        GameConfig.setCurrentTheme(theme);
                        scene.getGame().reload();
                    });

                    boardThemeGroup.add(item);
                    boardThemeMenu.add(item);
                });
        System.out.println("Loaded board themes for menu bar.");


        viewMenu.add(pieceSetMenu);
        viewMenu.add(boardThemeMenu);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        System.out.println("Menu bar loaded.");
        return menuBar;
    }

 */

    private void addKeyBindings() {
        /*
        bindKey(scene, KeyEvent.VK_A, "A",
                () -> player.LEFT = true,
                () -> player.LEFT = false
        );
         */
    }


}
