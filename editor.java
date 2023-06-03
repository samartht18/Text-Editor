import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.JFileChooser;
import javax.swing.text.StyledEditorKit.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class editor {

    private JFrame frame__;
    private JTextPane editor__;
    private UndoManager undoMgr__;
    private File file__;

    private static final String MAIN_TITLE = "Text Editor ";
    private static final String DEFAULT_FONT_FAMILY = "SansSerif";
    private static final int DEFAULT_FONT_SIZE = 50;
    private static final String ELEM = AbstractDocument.ElementNameAttribute;
    private static final String COMP = StyleConstants.ComponentElementName;

    editor(){
        frame__ = new JFrame(MAIN_TITLE);

        editor__ = new JTextPane();
        JScrollPane editorScrollPane = new JScrollPane(editor__);

        editor__.setDocument(new DefaultStyledDocument());

        undoMgr__ = new UndoManager();
        getEditorDocument().addUndoableEditListener(new UndoEditListener());

        EditButtonActionListener editButtonActionListener = new EditButtonActionListener();


        JButton boldButton = new JButton(new BoldAction());
        boldButton.setHideActionText(true);
        boldButton.setText("Bold");
        boldButton.addActionListener(editButtonActionListener);

        JButton italicButton = new JButton(new ItalicAction());
        italicButton.setHideActionText(true);
        italicButton.setText("Italic");
        italicButton.addActionListener(editButtonActionListener);

        JButton underlineButton = new JButton(new UnderlineAction());
        underlineButton.setHideActionText(true);
        underlineButton.setText("Underline");
        underlineButton.addActionListener(editButtonActionListener);

        JButton colorButton = new JButton("Set Color");
        colorButton.addActionListener(new ColorActionListener());

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new UndoActionListener("UNDO"));
        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(new UndoActionListener("REDO"));

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));

        panel1.add(boldButton);
        panel1.add(italicButton);
        panel1.add(underlineButton);

        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));

        panel1.add(colorButton);

        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(new JSeparator(SwingConstants.VERTICAL));

        panel1.add(undoButton);
        panel1.add(redoButton);

        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
        toolBarPanel.add(panel1);

        frame__.add(toolBarPanel, BorderLayout.NORTH);
        frame__.add(editorScrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        JMenuItem newItem	= new JMenuItem("New");
        newItem.addActionListener(new NewFileListener());

        JMenuItem openItem	= new JMenuItem("Open");
        openItem.addActionListener(new OpenFileListener());

        JMenuItem saveItem	= new JMenuItem("Save");
        saveItem.addActionListener(new SaveFileListener());

        JMenuItem printItem	= new JMenuItem("Print");
        printItem.addActionListener(new PrintFileListener());

        JMenuItem exitItem	= new JMenuItem("Exit");
        exitItem.addActionListener(new ExitFileListener());

        JMenuItem cutItem	= new JMenuItem("Cut");
        cutItem.addActionListener(new CutFileListener());

        JMenuItem copyItem	= new JMenuItem("Copy");
        copyItem.addActionListener(new CopyFileListener());

        JMenuItem pasteItem	= new JMenuItem("Paste");
        pasteItem.addActionListener(new PasteFileListener());

        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        frame__.setJMenuBar(menuBar);


        frame__.setSize(900, 500);
        frame__.setLocation(150, 80);
        frame__.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame__.setVisible(true);

        editor__.requestFocusInWindow();
    }


    private class NewFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            editor__.setText("");
            editor__.requestFocusInWindow();
        }
    }

    private class OpenFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            file__ = chooseFile();

            if (file__ == null) {

                return;
            }

            readFile(file__);
            setFrameTitleWithExtn(file__.getName());
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showOpenDialog(frame__) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            }
            else {
                return null;
            }
        }

        private void readFile(File file) {

            StyledDocument doc = null;

            try (InputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                doc = (DefaultStyledDocument) ois.readObject();
            }
            catch (FileNotFoundException ex) {

                JOptionPane.showMessageDialog(frame__, "Input file was not found!");
                return;
            }
            catch (ClassNotFoundException | IOException ex) {

                throw new RuntimeException(ex);
            }

            editor__.setDocument(doc);
            doc.addUndoableEditListener(new UndoEditListener());

        }
    }

    private class SaveFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (file__ == null) {

                file__ = chooseFile();

                if (file__ == null) {

                    return;
                }
            }

            DefaultStyledDocument doc = (DefaultStyledDocument) getEditorDocument();

            try (OutputStream fos = new FileOutputStream(file__);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(doc);
            }
            catch (IOException ex) {

                throw new RuntimeException(ex);
            }

            setFrameTitleWithExtn(file__.getName());
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showSaveDialog(frame__) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            }
            else {
                return null;
            }
        }
    }

    private class PrintFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            {
                try {
                    // print the file
                    editor__.print();
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(frame__, evt.getMessage());
                }
            }
            editor__.requestFocusInWindow();
        }
    }

    private class ExitFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            {
                frame__.setVisible(false);
            }
            editor__.requestFocusInWindow();
        }
    }


    private class CutFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            editor__.cut();
            editor__.requestFocusInWindow();
        }
    }

    private class CopyFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            editor__.copy();
            editor__.requestFocusInWindow();
        }
    }

    private class PasteFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            editor__.paste();
            editor__.requestFocusInWindow();
        }
    }


    private void setFrameTitleWithExtn(String titleExtn) {

        frame__.setTitle(MAIN_TITLE + titleExtn);
    }

    private StyledDocument getEditorDocument() {

        StyledDocument doc = (DefaultStyledDocument) editor__.getDocument();
        return doc;
    }

    private class EditButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            editor__.requestFocusInWindow();
        }
    }


    private class ColorActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            Color newColor = JColorChooser.showDialog(frame__, "Choose a color",
                    Color.BLACK);


            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, newColor);
            editor__.setCharacterAttributes(attr, false);
            editor__.requestFocusInWindow();
        }
    }

    private class UndoEditListener implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {

            undoMgr__.addEdit(e.getEdit()); // remember the edit
        }
    }

    private class UndoActionListener implements ActionListener {

        private String type;

        public UndoActionListener(String type) {

            this.type = type;
        }


        public void actionPerformed(ActionEvent e) {

            switch (type) {

                case "UNDO":
                    if (! undoMgr__.canUndo()) {

                        editor__.requestFocusInWindow();
                        return; // no edits to undo
                    }

                    undoMgr__.undo();
                    break;

                case "REDO":
                    if (! undoMgr__.canRedo()) {

                        editor__.requestFocusInWindow();
                        return; // no edits to redo
                    }

                    undoMgr__.redo();
            }

            editor__.requestFocusInWindow();
        }
    }



    public static void main(String  args []) {

        editor e = new editor();
    }

}

