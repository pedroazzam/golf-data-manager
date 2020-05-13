package br.com.golfinvest.data.view;

import br.com.golfinvest.data.config.ExcelAdapter;
import br.com.golfinvest.data.model.Pessoa;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class PessoalMassAdd extends JFrame {

    private JdbcTemplate jdbcTemplate;
    DefaultTableModel tableModel;
    String col[] = {"Nome", "E-Mail", "CPF", "Cod Assessor", "Cod Banco", "Agência", "Conta", "dv"};
    List<Pessoa> pessoas;
    int editingId;
    private String notInsertedMessage = "";

    private JPanel pessoalMassAddRootPanel;
    private JButton inserirButton;
    private JTable table1;

    public PessoalMassAdd(String title, JdbcTemplate jdbcTemplatePassed) {
        super();
        title = title + " - (Registro de pessoal em massa no banco de dados)";
        setTitle(title);
        setSize(1000, 480);
        jdbcTemplate = jdbcTemplatePassed;

        initComponents();
        regEvents();

    }

    public void initComponents() {
        System.out.println("Init Components PessoalMassAdd...");
        setContentPane(pessoalMassAddRootPanel);
        tableModel = new DefaultTableModel(col, 0);
        table1.setModel(tableModel);
        table1.setRowSelectionAllowed(true);
        setVisible(true);

        // This is the line that does all the magic!
        ExcelAdapter myAd = new ExcelAdapter(table1);

        // Insert 200 blank lines to table
        for (int i = 1; i < 200; i++) {
            Object[] object = {null, null, null, null, null, null, null, null};
            tableModel.addRow(object);
        }

    }


    public void regEvents() {

        inserirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertAll();
            }
        });

    }

    public void insertAll() {
        boolean inserted;
        for (int i = 0; i < table1.getRowCount(); i++) {
//            System.out.println("Verificando: " + tableModel.getValueAt(i, 0));
            if ((tableModel.getValueAt(i, 0) != null) && (!tableModel.getValueAt(i, 0).equals(""))) {
                System.out.println("Dentro do IF: " + tableModel.getValueAt(i, 0));
                for (int n = 0; n <= 7; n++) {
                    if (tableModel.getValueAt(i, n) == null) {
                        tableModel.setValueAt("", i, n);
                    }
                }

                inserted = inserirPessoal(tableModel.getValueAt(i, 0).toString(),
                        tableModel.getValueAt(i, 1).toString(),
                        tableModel.getValueAt(i, 2).toString(),
                        tableModel.getValueAt(i, 3).toString(),
                        tableModel.getValueAt(i, 4).toString(),
                        tableModel.getValueAt(i, 5).toString(),
                        tableModel.getValueAt(i, 6).toString(),
                        tableModel.getValueAt(i, 7).toString());
                if (inserted) {
                    // Delete informations at current line
                    for (int n = 0; n <= 7; n++) {
                        tableModel.setValueAt("", i, n);
                    }
                    inserted = false;
                }
            }
        }
        if (!notInsertedMessage.equals("")) {
            JOptionPane.showMessageDialog(null, "As linhas não inseridas permaneceram nesta tabela. Veja as causas abaixo:" + "\n" + notInsertedMessage, "Uma ou mais linhas não foram inseridas", JOptionPane.ERROR_MESSAGE);
            notInsertedMessage = "";
        }
    }

    public boolean inserirPessoal(String nome, String email, String cpf, String codAssessor, String codBanco, String agencia, String conta, String dv) {

        String sql = "INSERT INTO pessoal (nome, email, cpf, codigo_assessor, codigo_banco, agencia, conta, dv) VALUES( ";
        sql = sql + "'" + nome + "', ";
        sql = sql + "'" + email + "', ";
        sql = sql + "'" + cpf + "', ";
        sql = sql + "'" + codAssessor + "', ";
        sql = sql + "'" + codBanco + "', ";
        sql = sql + "'" + agencia + "', ";
        sql = sql + "'" + conta + "', ";
        sql = sql + "'" + dv + "');";
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Mensagem de erro: " + e.getCause());
//            JOptionPane.showMessageDialog(null, e.getCause());
            notInsertedMessage = notInsertedMessage + "\n" + e.getCause();
            e.printStackTrace();
            return false;
        }
    }

    public void getClipboard() {
        // Create a Clipboard object using getSystemClipboard() method
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Get data stored in the clipboard that is in the form of a string (text)
        try {
            System.out.println(c.getData(DataFlavor.stringFlavor));

        } catch (UnsupportedFlavorException unsupportedFlavorException) {
            unsupportedFlavorException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        pessoalMassAddRootPanel = new JPanel();
        pessoalMassAddRootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        pessoalMassAddRootPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(20, 20, 10, 20), -1, -1));
        panel1.add(panel2, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        label1.setText("Insira na tabela abaixo os registros que deseja inserir no banco de dados");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(10, 20, 20, 20), -1, -1));
        panel1.add(panel3, BorderLayout.SOUTH);
        inserirButton = new JButton();
        inserirButton.setText("Inserir");
        panel3.add(inserirButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(10, 20, 10, 20), -1, -1));
        panel1.add(panel4, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return pessoalMassAddRootPanel;
    }

}
