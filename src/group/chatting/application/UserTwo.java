package group.chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.text.*;

public class UserTwo implements ActionListener, Runnable {

    JTextPane text;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    JScrollPane scrollPane;

    BufferedReader reader;
    BufferedWriter writer;
    String name = "Stefan";

    UserTwo() {

        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

//        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/"));
//        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
//        ImageIcon i3 = new ImageIcon(i2);
//        JLabel back = new JLabel(i3);
//        back.setBounds(5, 20, 25, 25);
//        p1.add(back);
//
//        back.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent ae) {
//                System.exit(0);
//            }
//        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.jpg"));
        Image i5 = i4.getImage().getScaledInstance(60,60, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(8, 5, 60, 60);
        p1.add(profile);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel morevert = new JLabel(i15);
        morevert.setBounds(420, 20, 10, 25);
        p1.add(morevert);

        JLabel name = new JLabel("STEFAN");
        name.setBounds(86, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Damon, Stefan, Klaus");
        int maxChars = 25;
        status.setBounds(86, 35, 160, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        a1 = new JPanel();
//        a1.setLayout(new BorderLayout());
        a1.setBackground(Color.WHITE);
//        a1.add(vertical, BorderLayout.PAGE_START);

        scrollPane = new JScrollPane(a1);
        scrollPane.setBounds(5, 75, 440, 570);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        f.add(scrollPane);

        text = new JTextPane();
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        text.setMargin(new Insets(8, 8, 8, 8));   // adds breathing room so text isn't jammed at the edge

        JScrollPane textScroll = new JScrollPane(text);
        textScroll.setBounds(5, 655, 270, 40);
        textScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        textScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        f.add(textScroll);

        JButton emoji = new JButton();
        ImageIcon emojiIcon = new ImageIcon(ClassLoader.getSystemResource("icons/emoji.png"));
        Image scaledEmoji = emojiIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        emoji.setIcon(new ImageIcon(scaledEmoji));
        emoji.setBounds(280, 655, 40, 40);
        emoji.setBackground(Color.WHITE);
        emoji.setFocusPainted(false);
        f.add(emoji);
        emoji.addActionListener(e -> showEmojiPicker(emoji));

        JButton send = new JButton("Send");
        send.setBounds(325, 655, 118, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

// NOW add the Enter-key binding, since `send` exists at this point
        text.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
        text.getActionMap().put("sendMessage", new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                send.doClick();
            }
        });

        f.setSize(450, 700);
        f.setLocation(490, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);

        f.setVisible(true);

        try {
            Socket socket = new Socket("localhost", 2003);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = "<html><p>" + name + "</p><p>" + extractMessageText() + "</p></html>";

            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            right.setBackground(Color.WHITE);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            try {
                writer.write(out);
                writer.write("\r\n");
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String replaceEmojiTags(String text) {
        Pattern pattern = Pattern.compile("\\[emoji:(.*?)\\]");
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String filename = matcher.group(1);
            java.net.URL url = ClassLoader.getSystemResource("icons/emojis/" + filename);
            String imgTag = (url != null)
                    ? "<img src='" + url.toExternalForm() + "' width='20' height='20'>"
                    : "";
            matcher.appendReplacement(sb, Matcher.quoteReplacement(imgTag));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String processed = replaceEmojiTags(out);

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + processed + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(0, 15, 0, 50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);

        return panel;
    }

    public void run() {
        try {
            String msg = "";
            while(true) {
                msg = reader.readLine();
                if (msg.contains(name)) {
                    continue;
                }

                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.setBackground(Color.WHITE);
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);

                a1.add(vertical, BorderLayout.PAGE_START);

                vertical.revalidate();
                vertical.repaint();
                scrollToBottom();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserTwo one = new UserTwo();
        Thread t1 = new Thread(one);
        t1.start();
    }

    private void showEmojiPicker(Component invoker) {
        JPopupMenu popup = new JPopupMenu();
        JPanel grid = new JPanel(new GridLayout(3, 4, 4, 4));

        String[] emojiFiles = {
                "happy.png", "laugh.png", "heart.png", "rich.png",
                "shh.png", "redheart.png", "cool.png", "coolglasses.png",
                "explosion.png", "cry.png", "v.png", "angry.png", "bat.png", "clown.png", "coffin.png", "sad.png", "vampire.png", "wow.png"
        };

        for (String file : emojiFiles) {
            JButton btn = new JButton();
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/emojis/" + file));
            System.out.println(ClassLoader.getSystemResource("icons/emojis/" + file));
            Image scaled = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);

            btn.addActionListener(ev -> {
                insertEmojiIcon(file);
                text.requestFocusInWindow();
            });

            grid.add(btn);
        }

        popup.add(grid);
        popup.show(invoker, 0, -160); // shows just above the emoji button
    }

    private void insertEmojiIcon(String file) {
        try {
            java.net.URL url = ClassLoader.getSystemResource("icons/emojis/" + file);
            if (url == null) return;

            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon finalIcon = new ImageIcon(scaled);

            StyledDocument doc = text.getStyledDocument();
            Style style = text.addStyle("emojiStyle", null);
            StyleConstants.setIcon(style, finalIcon);

            // Store the filename as an attribute so we can extract it later when sending
            style.addAttribute("emojiFile", file);

            doc.insertString(doc.getLength(), " ", style);
            text.setCaretPosition(doc.getLength());
            MutableAttributeSet plainAttrs = new SimpleAttributeSet();
            text.setCharacterAttributes(plainAttrs, true);
            ((StyledEditorKit) text.getEditorKit()).getInputAttributes().removeAttributes(
                    ((StyledEditorKit) text.getEditorKit()).getInputAttributes()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractMessageText() {
        StyledDocument doc = text.getStyledDocument();
        StringBuilder sb = new StringBuilder();

        try {
            for (int i = 0; i < doc.getLength(); i++) {
                Element el = doc.getCharacterElement(i);
                AttributeSet attrs = el.getAttributes();
                Object emojiFile = attrs.getAttribute("emojiFile");

                if (emojiFile != null) {
                    sb.append("[emoji:").append(emojiFile).append("]");
                } else {
                    sb.append(doc.getText(i, 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vBar = scrollPane.getVerticalScrollBar();
            vBar.setValue(vBar.getMaximum());
        });

    }
}