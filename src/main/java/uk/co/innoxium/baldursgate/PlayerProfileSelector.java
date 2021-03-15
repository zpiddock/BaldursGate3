/*
 * Created by JFormDesigner on Fri Feb 26 20:15:59 GMT 2021
 */

package uk.co.innoxium.baldursgate;

import com.formdev.flatlaf.FlatIconColors;
import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;


/**
 * @author Zach Piddock
 */
public class PlayerProfileSelector extends JFrame {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JScrollPane listScrollPane;
    private JList<File> profileList;
    private JPanel infoPanel;
    private JLabel label1;
    private JLabel label2;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private final File playerProfileFolder;

    private void okClicked(ActionEvent e) {

        // We set the player profile of for the user.
        BG3Settings.playerProfile = profileList.getSelectedValue().getAbsolutePath();
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        listScrollPane = new JScrollPane();
        profileList = new JList<>();
        infoPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        okButton = new JButton();

        //======== this ========
        setTitle("Baldur's Gate 3 Profile Selection");
        setAlwaysOnTop(true);
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "insets 0,hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[grow,fill]"));

        //======== dialogPane ========
        {
            dialogPane.setLayout(new MigLayout(
                "insets panel,hidemode 3,gap 5 5",
                // columns
                "[grow,fill]",
                // rows
                "[fill]" +
                "[grow,fill]" +
                "[fill]" +
                "[]"));

            //======== listScrollPane ========
            {

                //---- profileList ----
                profileList.setBorder(null);
                profileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listScrollPane.setViewportView(profileList);
            }
            dialogPane.add(listScrollPane, "cell 0 1");

            //======== infoPanel ========
            {
                infoPanel.setLayout(new MigLayout(
                    "fillx,insets panel,hidemode 3",
                    // columns
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setText("Select Your Player Profile.");
                label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 8f));
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                infoPanel.add(label1, "cell 0 0,dock center");

                //---- label2 ----
                label2.setText("All Mods will be installed to this profile.");
                label2.setHorizontalAlignment(SwingConstants.CENTER);
                infoPanel.add(label2, "cell 0 1");
            }
            dialogPane.add(infoPanel, "cell 0 0");

            //---- okButton ----
            okButton.setText("OK");
            okButton.addActionListener(e -> okClicked(e));
            dialogPane.add(okButton, "cell 0 3,alignx center,growx 0");
        }
        contentPane.add(dialogPane, "cell 0 0");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        postCreate();
    }

    private void postCreate() {

        profileList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {

            JLabel ret = new JLabel(value.getName());
            ret.setOpaque(true);
            ret.setBackground(list.getBackground());
            ret.setForeground(list.getForeground());
            if(isSelected) {

                ret.setBackground(Color.decode(String.valueOf(FlatIconColors.OBJECTS_GREY.rgb)).darker());
            }

            return ret;
        });

        profileList.setListData(playerProfileFolder.listFiles(File::isDirectory));
    }

    public PlayerProfileSelector(File playerProfileFolder) {

        this.playerProfileFolder = playerProfileFolder;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setIconImage(Resources.CANDOR_LOGO.getImage());
        this.addWindowListener(new Adapter(this));
        initComponents();
    }
}

class Adapter extends WindowAdapter {

    private final JFrame frame;

    Adapter(JFrame frame) {

        this.frame = frame;
    }

    @Override
    public void windowIconified(WindowEvent we) {

        frame.setState(JFrame.NORMAL);
    }
}
