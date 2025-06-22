import javax.swing.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.StringReader;
import java.util.List;

public class Main{
    public static void main(String[] args) throws java.io.IOException {
        JFrame frame = new JFrame("Soapbug");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Image icon = ImageIO.read(Main.class.getResource("/icon.png"));
        frame.setIconImage(icon);
        frame.setResizable(false);
        
        JPanel panel = new JPanel();
        
        String games = Files.readString(Paths.get("downloadable.txt"));

        JTextArea textArea = new JTextArea(16, 39);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        textArea.setFont(new java.awt.Font("Courier", 0, 12));
        JScrollPane sp = new JScrollPane(textArea);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        String dialogue = "INSERT GAME NAME HERE (e.g. c.exe)\n^^^^^^^^^^^^^^^^^^^^^\n\nAvailable Games:\n"+games;
        textArea.setText(dialogue+"\n\npress ctrl+enter");
        
        panel.setBackground(Color.BLACK);
        panel.add(sp);
        frame.add(panel);
        frame.setVisible(true);
        
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    String firstLine = "";
                    String line = "";
                    try {
                        BufferedReader reader = new BufferedReader(new StringReader(textArea.getText()));
                        firstLine = reader.readLine();
                        reader.close();
                    } catch (Exception g) {
                        g.printStackTrace();
                    }
                    String searchString = firstLine;
                    try (BufferedReader reader = new BufferedReader(new FileReader("urls.txt"))) {
                        
                        while ((line = reader.readLine()) != null) {
                            if (line.contains(searchString)) {
                                System.out.println("Found line: " + line);
                                break; // Remove this 'break' if you want to find all matching lines
                            }
                        }
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                    try (BufferedInputStream in = new BufferedInputStream(new URL(line).openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(firstLine)) {

                    byte[] dataBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }

                    System.out.println("Download complete!");

                    } catch (IOException h) {
                        System.out.println("Download failed: " + h.getMessage());
                    }
                }
            }
        });
    }
}
