package me.alessioimprota;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JPanel  {

    static long i;
    static long j;
    static long W;
    static long B;
    static long totW = -3600000;
    static long totB = -3600000;
    static boolean started = false;
    static boolean paused = false;
    static boolean infoTab = false;
    static boolean settingsTab = false;
    static boolean stopwatchMode = false;

    static DateFormat totalTimeFormat = new SimpleDateFormat("HH:mm:ss");
    static DateFormat timeFormat = new SimpleDateFormat("mm:ss");

    static JFrame frame = new JFrame();

    static Font font_about = new Font("Poppins", Font.PLAIN, 15);
    static Font font_labels = new Font("Poppins", Font.PLAIN, 16);
    static Font font_fields = new Font("Poppins", Font.PLAIN, 18);
    static Font font_buttons = new Font("Poppins", Font.BOLD, 18);
    static Font font_time = new Font("Mono", Font.PLAIN, 60);
    static Font font_status = new Font("Impact", Font.PLAIN, 60);

    static Color nord_white = new Color(236, 239, 244);
    static Color nord_pressed = new Color(94, 129, 172);
    static Color nord_light_gray = new Color(76, 86, 106);
    static Color nord_gray = new Color(59, 66, 82);
    static Color nord_black = new Color(46, 52, 64);

    static Color white = new Color(255, 255, 255);
    static Color pressed = new Color(129, 129, 129);
    static Color light_gray = new Color(86, 86, 86);
    static Color gray = new Color(66, 66, 66);
    static Color black = new Color(52, 52, 52);

    static String version = "3.1.3";
    static String year= "2023";

    static JLabel about = new JLabel("<html><center>pTimer&#8482 version " + version + "<br>\t&#169 " + year + " Alessio Improta<br>All Rights Reserved.</center></html>");
    static JLabel overall = new JLabel();
    static JLabel status = new JLabel("");
    static JLabel time = new JLabel("");
    static JLabel wtl = new JLabel("Work Time (minutes)");
    static JLabel btl = new JLabel("Break Time (minutes)");
    static JLabel[] labels = {about, overall, status, time, wtl, btl};

    static JTextField wt_field = new JTextField("50");
    static JTextField bt_field = new JTextField("10");
    static JTextField[] fields = {wt_field, bt_field};

    static CustomCheckBox onTopCB = new CustomCheckBox("Always on Top",true);
    static CustomCheckBox themeCB = new CustomCheckBox("Nord Theme", true);
    static CustomCheckBox quitCB = new CustomCheckBox("Use Q to quit", true);
    static CustomCheckBox soundCB = new CustomCheckBox("Enable sound", true);
    static CustomCheckBox minimizeCB = new CustomCheckBox("Use M to minimize", true);
    static CustomCheckBox[] checkboxes = {onTopCB, themeCB, quitCB, soundCB, minimizeCB};

    static CustomButton start = new CustomButton("Start");
    static CustomButton pause = new CustomButton("Pause");
    static CustomButton info = new CustomButton("...");
    static CustomButton reset = new CustomButton("Reset");
    static CustomButton settings = new CustomButton("Settings");
    static CustomButton quit = new CustomButton("Quit");
    static CustomButton switchMode = new CustomButton("Stopwatch Mode");
    static CustomButton[] buttons = {start, pause, info, reset, settings, quit, switchMode};

    static ScheduledExecutorService executorService;

    public static void main(String[] args) throws IOException {

        Runnable runnable = () -> {
            FrameDragListener frameDragListener = new FrameDragListener(frame);
            frame.addMouseListener(frameDragListener);
            frame.addMouseMotionListener(frameDragListener);
        };
        SwingUtilities.invokeLater(runnable);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setTitle("pTimer");
        BufferedImage icon = ImageIO.read(Objects.requireNonNull(Main.class.getResource("icon.png")));
        frame.setIconImage(icon);
        frame.setLayout(null);
        frame.setSize(350, 208);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setAlwaysOnTop (true);
        frame.getContentPane().setBackground(nord_black);
        frame.addKeyListener(new MyKeyListener());

        for (JLabel label : labels) {
            label.setForeground(nord_white);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.addKeyListener(new MyKeyListener());
            label.setFont(font_about);
        }
        about.setBounds(0, 0, 350, 100);
        overall.setBounds(0, 100, 350, 60);
        status.setBounds(0, 0, 350, 100);
        status.setFont(font_status);
        time.setBounds(0, 78, 350, 90);
        time.setFont(font_time);
        wtl.setBounds(50, 35, 200, 30);
        wtl.setFont(font_labels);
        btl.setBounds(50, 95, 200, 30);
        btl.setFont(font_labels);

        for (JTextField field : fields) {
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setForeground(nord_white);
            field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, nord_light_gray));
            field.setOpaque(false);
            field.setCaretColor(nord_white);
            field.setSelectionColor(nord_white);
            field.setFont(font_fields);
            field.addKeyListener(new MyKeyListener());
        }
        wt_field.setBounds(250, 35, 40, 30);
        bt_field.setBounds(250, 95, 40, 30);

        for (CustomButton button : buttons) {
            button.setForeground(nord_white);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.addKeyListener(new MyKeyListener());
            button.setFont(font_buttons);
            button.setMargin(new Insets(0,0,0,0));
        }
        start.setBounds(123,165,104, 35);
        pause.setBounds(180,165,104, 35);
        reset.setBounds(123,165,104, 35);
        info.setBounds(238,165,104, 35);
        settings.setBounds(8,165,104, 35);
        quit.setBounds(8,165,104, 35);
        switchMode.setBounds(181,115,161,35);
        switchMode.setFont(font_about);

        for (CustomCheckBox checkbox : checkboxes) {
            checkbox.setForeground(nord_white);
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(false);
            checkbox.addKeyListener(new MyKeyListener());
            checkbox.setFont(font_about);
        }
        onTopCB.setBounds(8,15,161,35);
        minimizeCB.setBounds(8,65,161,35);
        quitCB.setBounds(8,115,161,35);
        soundCB.setBounds(181,15,161,35);
        themeCB.setBounds(181,65,161,35);

        frame.add(wtl);
        frame.add(btl);
        frame.add(wt_field);
        frame.add(bt_field);
        frame.add(start);
        frame.add(info);
        frame.add(quit);

        frame.validate();
        frame.repaint();
        frame.setVisible(true);
        start.requestFocus();

        onTopCB.addActionListener(arg0 -> frame.setAlwaysOnTop (onTopCB.isSelected()));

        quit.addActionListener(arg0 -> System.exit(0));

        switchMode.addActionListener(arg0 -> {
            if(stopwatchMode) disableStopwatch();
            else enableStopwatch();
        });

        themeCB.addActionListener(arg0 -> {
            if(themeCB.isSelected()) enableNordTheme();
            else disableNordTheme();
            frame.validate();
            frame.repaint();
        });

        settings.addActionListener(arg0 -> {
            if(settingsTab) {
                settingsTab = false;

                settings.setText("Settings");
                reset.setText("Reset");
                settings.setBounds(8,165,104, 35);

                frame.add(overall);
                frame.add(about);
                frame.add(info);

                for (CustomCheckBox checkbox : checkboxes) frame.remove(checkbox);
                frame.remove(quit);
                frame.remove(switchMode);

            } else {
                settingsTab = true;

                settings.setText("Back");
                reset.setText("Restore");
                settings.setBounds(238,165,104, 35);

                frame.remove(overall);
                frame.remove(about);
                frame.remove(info);

                for (CustomCheckBox checkbox : checkboxes) frame.add(checkbox);
                frame.add(quit);
                frame.add(switchMode);
            }
            frame.validate();
            frame.repaint();
        });

        info.addActionListener(arg0 -> {
            if(infoTab) {
                infoTab = false;
                info.setText("...");

                if (started || stopwatchMode&&paused) {
                    info.setBounds(307, 165, 35, 35);

                    frame.add(status);
                    frame.add(time);
                    frame.add(start);
                    frame.add(pause);

                } else if (stopwatchMode) {
                    info.setBounds(238, 165, 104, 35);

                    frame.add(status);
                    frame.add(time);
                    frame.add(start);
                    frame.add(quit);

                } else {
                    info.setBounds(238, 165, 104, 35);

                    frame.add(wtl);
                    frame.add(btl);
                    frame.add(wt_field);
                    frame.add(bt_field);
                    frame.add(start);
                    frame.add(quit);
                }

                frame.remove(overall);
                frame.remove(about);
                frame.remove(reset);
                frame.remove(settings);

            } else {
                infoTab = true;
                info.setBounds(238,165,104, 35);
                info.setText("Back");

                overall.setText("<html><center>Overall Work Time: "+ totalTimeFormat.format(totW) + "<br>Overall Break Time: " + totalTimeFormat.format(totB) + "</center></html>");

                frame.remove(wtl);
                frame.remove(btl);
                frame.remove(wt_field);
                frame.remove(bt_field);
                frame.remove(start);
                frame.remove(pause);
                frame.remove(time);
                frame.remove(status);
                frame.remove(quit);

                frame.add(overall);
                frame.add(about);
                frame.add(reset);
                frame.add(settings);
            }
            frame.validate();
            frame.repaint();
        });

        start.addActionListener(arg0 -> {
            if (!started) {
                start.setText("Reset");
                start.setBounds(66,165,104, 35);
                info.setBounds(307,165,35, 35);

                frame.remove(quit);
                frame.add(pause);

                started = true;

                if (stopwatchMode) {
                    executorService = Executors.newSingleThreadScheduledExecutor();
                    executorService.scheduleAtFixedRate(StopWatch::new, 0, 1, TimeUnit.MILLISECONDS);

                } else {
                    frame.remove(wtl);
                    frame.remove(btl);
                    frame.remove(wt_field);
                    frame.remove(bt_field);

                    frame.add(status);
                    frame.add(time);

                    try {
                        W = timeFormat.parse(wt_field.getText() + ":00").getTime();
                        B = timeFormat.parse(bt_field.getText() + ":00").getTime();
                        i = W + 3600000;
                        j = B + 3600000;
                    } catch (ParseException e) {
                        status.setText("Error");
                        time.setText("");
                    }

                    if(!status.getText().equals("Error")) {
                        executorService = Executors.newSingleThreadScheduledExecutor();
                        executorService.scheduleAtFixedRate(pTimer::new, 0, 1, TimeUnit.MILLISECONDS);
                    }
                }

            } else {
                start.setText("Start");
                start.setBounds(123,165,104, 35);
                info.setBounds(238,165,104, 35);
                pause.setText("Pause");


                paused = false;
                started = false;

                if (stopwatchMode) {
                    time.setText("00:00:00");
                    i=-3600000;
                    executorService.shutdownNow();

                } else {
                    time.setText("");
                    frame.add(wtl);
                    frame.add(btl);
                    frame.add(wt_field);
                    frame.add(bt_field);

                    frame.remove(status);
                    frame.remove(time);

                    if(!status.getText().equals("Error")) {
                        executorService.shutdownNow();
                    } else {
                        status.setText("");
                    }
                }
                frame.add(quit);
                frame.remove(pause);
            }
            frame.validate();
            frame.repaint();
        });

        pause.addActionListener(arg0 -> {
            if (paused) {
                pause.setText("Pause");
                paused = false;
                executorService = Executors.newSingleThreadScheduledExecutor();
                if(!stopwatchMode) executorService.scheduleAtFixedRate(pTimer::new, 1, 1, TimeUnit.MILLISECONDS);
                else executorService.scheduleAtFixedRate(StopWatch::new, 1, 1, TimeUnit.MILLISECONDS);
            } else if(!status.getText().equals("Error")){
                pause.setText("Resume");
                paused = true;
                executorService.shutdownNow();
            }
        });

        reset.addActionListener(arg0 -> {
            if(!settingsTab) {
                totW = -3600000;
                totB = -3600000;
                if (!started || paused) overall.setText("<html><center>Overall Work Time: " + totalTimeFormat.format(totW) + "<br>Overall Break Time: " + totalTimeFormat.format(totB) + "</center></html>");
            } else {
                frame.setAlwaysOnTop (true);
                enableNordTheme();
                if(stopwatchMode) disableStopwatch();
                for (CustomCheckBox checkbox : checkboxes) checkbox.setSelected(true);
                frame.validate();
                frame.repaint();
            }
        });
    }

    public static void enableStopwatch() {
        stopwatchMode = true;
        switchMode.setText("Timer Mode");
        if(started) executorService.shutdownNow();
        started = false;
        paused = false;
        status.setText("Stopwatch");
        time.setText("00:00:00");
        i=-3600000;
        start.setText("Start");
        pause.setText("Pause");
        start.setBounds(123,165,104, 35);
    }

    public static void disableStopwatch() {
        stopwatchMode = false;
        switchMode.setText("Stopwatch Mode");
        if (started) executorService.shutdownNow();
        started = false;
        paused = false;
        status.setText("");
        time.setText("");
        start.setText("Start");
        pause.setText("Pause");
        start.setBounds(123,165,104, 35);
    }

    public static void enableNordTheme() {
        frame.getContentPane().setBackground(nord_black);
        for (JTextField field : fields) {
            field.setForeground(nord_white);
            field.setCaretColor(nord_white);
            field.setSelectionColor(nord_white);
            field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, nord_light_gray));
        }
        for (JLabel label : labels) label.setForeground(nord_white);
        for (CustomButton button : buttons) button.setForeground(nord_white);
        for (CustomCheckBox checkbox : checkboxes) checkbox.setForeground(nord_white);
    }

    public static void disableNordTheme() {
        frame.getContentPane().setBackground(black);
        for (JTextField field : fields) {
            field.setForeground(white);
            field.setCaretColor(white);
            field.setSelectionColor(white);
            field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, light_gray));
        }
        for (JLabel label : labels) label.setForeground(white);
        for (CustomButton button : buttons) button.setForeground(white);
        for (CustomCheckBox checkbox : checkboxes) checkbox.setForeground(white);
    }

    public static class pTimer {

        public pTimer() {

            if(infoTab) overall.setText("<html><center>Overall Work Time: "+ totalTimeFormat.format(totW) + "<br>Overall Break Time: " + totalTimeFormat.format(totB) + "</center></html>");

            if (i > 0) {
                if (!status.getText().equals("Work")) status.setText("Work");
                time.setText(timeFormat.format(i));
                i = i - 1;
                totW = totW + 1;
            } else if (j > 0) {
                if (!status.getText().equals("Break")){
                    status.setText("Break");
                    if (soundCB.isSelected()) java.awt.Toolkit.getDefaultToolkit().beep();
                }
                time.setText(timeFormat.format(j));
                j = j - 1;
                totB = totB + 1;
            } else if(W!=-3600000 || B!=-3600000){
                if (soundCB.isSelected()) java.awt.Toolkit.getDefaultToolkit().beep();
                i = W + 3600000;
                j = B + 3600000;
            } else {
                status.setText("Error");
                time.setText("");
                executorService.shutdownNow();
            }
        }
    }

    public static class StopWatch {

        public StopWatch() {
            i=i+1;
            time.setText(totalTimeFormat.format(i));
        }
    }

    public static class CustomCheckBox extends JCheckBox {

        public CustomCheckBox(String text, boolean selected) {
            super(text, selected);
            super.setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if(themeCB.isSelected()) {
                if (getModel().isPressed()) g.setColor(nord_pressed);
                else if (getModel().isRollover()) g.setColor(nord_light_gray);
                else g.setColor(nord_gray);
            } else {
                if (getModel().isPressed()) g.setColor(pressed);
                else if (getModel().isRollover()) g.setColor(light_gray);
                else g.setColor(gray);
            }
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    public static class CustomButton extends JButton {

        public CustomButton(String text) {
            super(text);
            super.setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if(themeCB.isSelected()) {
                if (getModel().isPressed()) g.setColor(nord_pressed);
                else if (getModel().isRollover()) g.setColor(nord_light_gray);
                else g.setColor(nord_gray);
            } else {
                if (getModel().isPressed()) g.setColor(pressed);
                else if (getModel().isRollover()) g.setColor(light_gray);
                else g.setColor(gray);
            }
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    public static class MyKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {

            if (quitCB.isSelected() && e.getKeyCode() == 81) System.exit(0);

            if (minimizeCB.isSelected() && e.getKeyCode() == 77) frame.setState(Frame.ICONIFIED);

            if (e.getKeyCode() == 39) {
                if(status.getText().equals("Work")) {
                    i = i + 60000;
                    time.setText(timeFormat.format(i));
                } else if(status.getText().equals("Break")) {
                    j = j + 60000;
                    time.setText(timeFormat.format(j));
                }
            }

            if (e.getKeyCode() == 37) {
                if(status.getText().equals("Work")) {
                    if(i>60000) i = i - 60000;
                    else i = 0;
                    time.setText(timeFormat.format(i));
                } else if(status.getText().equals("Break")) {
                    if(j>60000) j = j - 60000;
                    else j = 0;
                    time.setText(timeFormat.format(j));
                }
            }
        }
    }

    public static class FrameDragListener extends MouseAdapter {

        private final JFrame frame;
        private Point mouseDownCompCoordinates = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoordinates = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoordinates = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoordinates = e.getLocationOnScreen();
            frame.setLocation(currCoordinates.x - mouseDownCompCoordinates.x, currCoordinates.y - mouseDownCompCoordinates.y);
        }
    }
}