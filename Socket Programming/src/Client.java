import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Client {
    static Socket socket;
    static  ArrayList<MyFile> AllFile = new ArrayList<>();
    static ArrayList<MyFile> DownFile = new ArrayList<>();



    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final File[] fileToSend = new File[1];
        final String[] s3 = new String[1];
        final int[]b = new int[1];
        int[] size = new int[1];
        size[0] = 0;


        JFrame jFrame = new JFrame("Client's Code");
        jFrame.setSize(850,450);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));


        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().setBackground(Color.CYAN);

        JLabel jlTitle = new JLabel("Client's File Sender");
        jlTitle.setFont(new Font("Arial",Font.BOLD,25));
        jlTitle.setBorder(new EmptyBorder(20,0,0,0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(10,0,10,0));
        jPanel.setBackground(Color.pink);

        JLabel jIplable = new JLabel("Enter IP");
        jIplable.setFont(new Font("Arial",Font.BOLD,20));
        jIplable.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jPort = new JLabel("Enter Port");
        jPort.setFont(new Font("Arial",Font.BOLD,20));
        jPort.setAlignmentX(Component.LEFT_ALIGNMENT);


        JTextField jIp = new JTextField("localhost");
        jIp.setPreferredSize(new Dimension(100,50));
        jIp.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField jPo = new JTextField("1234");
        jPo.setPreferredSize(new Dimension(100,50));

        JButton jConnect = new JButton("Connect");
        jConnect.setPreferredSize(new Dimension(150,50));
        jConnect.setFont(new Font("Arial",Font.BOLD,18));
        jConnect.setAlignmentX(Component.RIGHT_ALIGNMENT);



        jPanel.add(jIplable);
        jPanel.add(jIp);
        jPanel.add(jPort);
        jPanel.add(jPo);
        jPanel.add(jConnect);


        JLabel jlFileName = new JLabel("Choose a file to send");
        jlFileName.setFont(new Font("Arial",Font.BOLD,20));
        jlFileName.setBorder(new EmptyBorder(50,0,0,0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlFileName.setBackground(Color.MAGENTA);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(10,0,0,0));
        jpButton.setBackground(Color.DARK_GRAY);



        JButton jbSendFile = new JButton("Send File");
        jbSendFile.setPreferredSize(new Dimension(150,75));
        jbSendFile.setFont(new Font("Arial",Font.BOLD,20));

        JButton jbChooseFile = new JButton("Choose File");
        jbChooseFile.setPreferredSize(new Dimension(150,75));
        jbChooseFile.setFont(new Font("Arial",Font.BOLD,20));





        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);


        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(new EmptyBorder(10,0,0,0));
        jPanel2.setBackground(Color.magenta);

        JButton jDownload = new JButton("Downlad from server");
        jDownload.setPreferredSize(new Dimension(250,75));
        jDownload.setFont(new Font("Arial",Font.BOLD,15));

        jPanel2.add(jDownload);

        jConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String s1 = jIp.getText();
                String s2 = jPo.getText();
                int  a = Integer.parseInt(s2);
                s3[0] = s1;
                b[0] = a;
                if(b[0] == 1234){
                    JOptionPane.showMessageDialog(jFrame,"Connected with server");
                }else{
                    JOptionPane.showMessageDialog(jFrame,"Server is not connected");
                }

            }
        });

        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a file chooser to open the dialog to choose a file.
                JFileChooser jFileChooser = new JFileChooser();
                // Set the title of the dialog.
                jFileChooser.setDialogTitle("Choose a File");
                // Show the dialog and if a file is chosen from the file chooser execute the following statements.
                if (jFileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file.
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    // Change the text of the java swing label to have the file name.
                    jlFileName.setText("The file you want to send is: " + fileToSend[0].getName());
                }
            }
        });



        socket = new Socket("localhost", 1234);
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        AllFile = (ArrayList<MyFile>) ois.readObject();
        System.out.println(AllFile.size());


        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                // If a file has not yet been selected then display this message.
                if (fileToSend[0] == null) {
                    jlFileName.setText("Please choose a file to send first!");
                    // If a file has been selected then do the following.
                } else {
                    try {
                        // Create an input stream into the file you want to send.
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        // Create a socket connection to connect with the server.

                        // Create an output stream to write to write to the server over the socket connection.
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        // Get the name of the file you want to send and store it in filename.
                        String fileName = fileToSend[0].getName();
                        // Convert the name of the file into an array of bytes to be sent to the server.
                        byte[] fileNameBytes = fileName.getBytes();
                        // Create a byte array the size of the file so don't send too little or too much data to the server.
                        byte[] fileBytes = new byte[(int)fileToSend[0].length()];
                        // Put the contents of the file into the array of bytes to be sent so these bytes can be sent to the server.
                        fileInputStream.read(fileBytes);
                        // Send the length of the name of the file so server knows when to stop reading.
                        dataOutputStream.writeInt(fileNameBytes.length);
                        // Send the file name.
                        dataOutputStream.write(fileNameBytes);
                        // Send the length of the byte array so the server knows when to stop reading.
                        dataOutputStream.writeInt(fileBytes.length);
                        // Send the actual file.
                        dataOutputStream.write(fileBytes);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        jDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame jFrame1 = new JFrame("Server");

                jFrame1.setSize(400, 400);

                jFrame1.setLayout(new BoxLayout(jFrame1.getContentPane(), BoxLayout.Y_AXIS));

                //jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel jPanel = new JPanel();

                jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

                JScrollPane jScrollPane = new JScrollPane(jPanel);

                jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                JLabel jlTitle = new JLabel("File Lists");

                jlTitle.setFont(new Font("Arial", Font.BOLD, 25));

                jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

                jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                jFrame1.add(jlTitle);
                jFrame1.add(jScrollPane);

                jFrame1.setVisible(true);


                int fileid = 0;
                File dic = new File("Server's File/");
                File[] diclist = dic.listFiles();
                int i=0;
                DownFile.clear();

                for (File file : diclist) {
                    i++;
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                        String FileName = file.getName();
                        byte[] fileContentBytes = new byte[(int) file.length()];
                        if ((int) file.length() > 0) {
                            fileInputStream.read(fileContentBytes);
                        }

                        MyFile newFile = new MyFile(fileid, FileName, fileContentBytes, getFileExtension(FileName));
                        newFile.setData(fileContentBytes);


                            DownFile.add(newFile);
                            fileid++;

                        System.out.println(newFile.getId() + " " + newFile.getName() + " " + newFile.getData().length + " " + newFile.getFileExtension());
                    } catch (FileNotFoundException er) {
                        er.printStackTrace();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }

                size[0] = i;

               /*
                File dic = new File("download/");
                File[] diclist = dic.listFiles();

                for(File files : diclist){
                    int i=0;
                    try {
                        FileInputStream fileInputStream1 = new FileInputStream(files.getAbsolutePath());
                        String name =files.getName();
                        System.out.println(name);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                    JPanel jpFileRow = new JPanel();
                    jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));

                    JLabel jlFileName = new JLabel(files.getName());
                    jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                    jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
                    if (getFileExtension(files.getName()).equalsIgnoreCase("txt")) {

                        jpFileRow.setName((String.valueOf(i)));
                        jpFileRow.addMouseListener(getMyMouseListener());

                        jpFileRow.add(jlFileName);
                        jPanel.add(jpFileRow);
                        jFrame.validate();
                    } else {

                        jpFileRow.setName((String.valueOf(i)));

                        jpFileRow.addMouseListener(getMyMouseListener());

                        jpFileRow.add(jlFileName);
                        jPanel.add(jpFileRow);

                        jFrame.validate();
                    }
                    i++;

                }

              */

                for (MyFile file : DownFile) {

                    JPanel jpFileRow = new JPanel();
                    jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));

                    JLabel jlFileName = new JLabel(file.name);
                    jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                    jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
                    if (getFileExtension(file.name).equalsIgnoreCase("txt")) {

                        jpFileRow.setName((String.valueOf(file.id)));
                        jpFileRow.addMouseListener(getMyMouseListener());

                        jpFileRow.add(jlFileName);
                        jPanel.add(jpFileRow);
                        jFrame1.validate();
                    } else {

                        jpFileRow.setName((String.valueOf(file.id)));

                        jpFileRow.addMouseListener(getMyMouseListener());

                        jpFileRow.add(jlFileName);
                        jPanel.add(jpFileRow);

                        jFrame1.validate();
                    }
                }

            }
        });

        // Add everything to the frame and make it visible.
        jFrame.add(jlTitle);
        jFrame.add(jPanel);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.add(jPanel2);
        jFrame.setVisible(true);


    }

    public static String getFileExtension(String fileName) {
        // Get the file type by using the last occurence of . (for example aboutMe.txt returns txt).
        // Will have issues with files like myFile.tar.gz.
        int i = fileName.lastIndexOf('.');
        // If there is an extension.
        if (i > 0) {
            // Set the extension to the extension of the filename.
            return fileName.substring(i + 1);
        } else {
            return "No extension found.";
        }
    }


    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the source of the click which is the JPanel.
                JPanel jPanel = (JPanel) e.getSource();
                // Get the ID of the file.
                int fileId = Integer.parseInt(jPanel.getName());
                // Loop through the file storage and see which file is the selected one.
                for (MyFile myFile : DownFile) {

                    if (myFile.getId() == fileId) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
                        jfPreview.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {

        // Frame to hold everything.
        JFrame jFrame = new JFrame("WittCode's File Downloader");
        // Set the size of the frame.
        jFrame.setSize(400, 400);

        // Panel to hold everything.
        JPanel jPanel = new JPanel();
        // Make the layout a box layout with child elements stacked on top of each other.
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        // Title above panel.
        JLabel jlTitle = new JLabel("WittCode's File Downloader");
        // Center the label title horizontally.
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Change the font family, size, and style.
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        // Add spacing on the top and bottom of the element.
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        // Label to prompt the user if they are sure they want to download the file.
        JLabel jlPrompt = new JLabel("Are you sure you want to download " + fileName + "?");
        // Change the font style, size, and family of the label.
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        // Add spacing on the top and bottom of the label.
        jlPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));
        // Center the label horizontally.
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the yes for accepting the download.
        JButton jbYes = new JButton("Yes");
        jbYes.setPreferredSize(new Dimension(150, 75));
        // Set the font for the button.
        jbYes.setFont(new Font("Arial", Font.BOLD, 20));

        // No button for rejecting the download.
        JButton jbNo = new JButton("No");
        // Change the size of the button must be preferred because if not the layout will ignore it.
        jbNo.setPreferredSize(new Dimension(150, 75));
        // Set the font for the button.
        jbNo.setFont(new Font("Arial", Font.BOLD, 20));

        // Label to hold the content of the file whether it be text of images.
        JLabel jlFileContent = new JLabel();
        // Align the label horizontally.
        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel to hold the yes and no buttons and make the next to each other left and right.
        JPanel jpButtons = new JPanel();
        // Add spacing around the panel.
        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
        // Add the yes and no buttons.
        jpButtons.add(jbYes);
        jpButtons.add(jbNo);

        // If the file is a text file then display the text.
        if (fileExtension.equalsIgnoreCase("txt")) {
            // Wrap it with <html> so that new lines are made.
            jlFileContent.setText("<html>" + new String(fileData) + "</html>");
            // If the file is not a text file then make it an image.
        } else {
            jlFileContent.setIcon(new ImageIcon(fileData));
        }

        // Yes so download file.
        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the file with its name.
                File fileToDownload = new File("Client's Download/"+fileName);
                try {
                    // Create a stream to write data to the file.
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                    // Write the actual file data to the file.
                    fileOutputStream.write(fileData);
                    // Close the stream.
                    fileOutputStream.close();
                    // Get rid of the jFrame. after the user clicked yes.
                    jFrame.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });


        // No so close window.
        jbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // User clicked no so don't download the file but close the jframe.
                jFrame.dispose();
            }
        });

        // Add everything to the panel before adding to the frame.
        jPanel.add(jlTitle);
        jPanel.add(jlPrompt);
        jPanel.add(jlFileContent);
        jPanel.add(jpButtons);

        // Add panel to the frame.
        jFrame.add(jPanel);

        // Return the jFrame so it can be passed the right data and then shown.
        return jFrame;

    }
}