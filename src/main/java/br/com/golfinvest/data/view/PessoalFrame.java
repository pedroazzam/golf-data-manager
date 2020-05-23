package br.com.golfinvest.data.view;

import br.com.golfinvest.data.model.Pessoa;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class PessoalFrame extends JInternalFrame {

    private JdbcTemplate jdbcTemplate;
    DefaultTableModel tableModel;
    String col[] = {"Nome", "E-Mail", "CPF", "Cod Assessor", "RepPrev", "RepSeg", "Cod Banco", "Agência", "Conta", "dv", "Xerife", "RepSeg"};
    String[] xerifeItems = {""};
    SortedComboBoxModel xerifeComboBoxModel;
    List<Pessoa> pessoas;
    int editingId;
    String editingCPF;
    int sR = -1;

    private JPanel pessoalRootPanel;
    private JButton editButton;
    private JPanel westLabelPanel;
    private JPanel eastGridPanel;
    private JPanel centerTextPanel;
    private JTextField nomeTextField;
    private JTextField emailTextField;
    private JTextField cpfTextField;
    private JTable table1;
    private JPanel southButtonPanel;
    private JButton insertTestButton;
    private JTextField codAssessorTextField;
    private JTextField codBancoTextField;
    private JTextField agenciaTextField;
    private JTextField contaTextField;
    private JButton insertButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTextField dvTextField;
    private JButton inserirEmMassaButton;
    private JTextField repassePrevAssessorTextField;
    private JTextField repasseSegAssessorTextField;
    private JComboBox xerifeComboBox;
    private JTextField repassePrevXerifeTextField;
    private JButton resizeTableButton;
    private JButton refreshTableButton;

    public PessoalFrame(String title, JdbcTemplate jdbcTemplatePassed) {
        super();
        title = title + " - (Registro de pessoal no banco de dados)";
        setTitle(title);


//        setSize(1200, 480);
        jdbcTemplate = jdbcTemplatePassed;

        initComponents();
        regEvents();

    }

    public void initComponents() {
        System.out.println("Init Components PessoalFrame...");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(pessoalRootPanel);
//        setLocationRelativeTo(null);

        insertTestButton.setVisible(false); // Disable button to insert test data

        tableModel = new DefaultTableModel(col, 0);
        table1.setModel(tableModel);

        table1.setRowSelectionAllowed(true);

        xerifeComboBoxModel = new SortedComboBoxModel(xerifeItems);
        xerifeComboBox.setModel(xerifeComboBoxModel);

        setMaximizable(true); // maximize
//        setIconifiable(true); // set minimize
        setClosable(true); // set closed
        setResizable(true); // set resizable

        // java - get screen size using the Toolkit class
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("W= " + screenSize.width + " H= " + screenSize.height);
        table1.setPreferredScrollableViewportSize(new Dimension((int) (screenSize.width / 3.5), screenSize.height / 3));

        setVisible(true);
        preencherTabelaEComboBoxSelectAll();
    }


    public void regEvents() {

        insertTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    InputStream is = new ClassPathResource("insert-pessoal.sql").getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);   // add everything to StringBuilder
//                    JOptionPane.showMessageDialog(null, line);
                        System.out.println("antes " + line + " depois");
                        jdbcTemplate.execute(line);
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, ioException.getCause());
                    ioException.printStackTrace();
                }

                preencherTabelaEComboBoxSelectAll();
            }
        });


        table1.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                nomeTextField.setRequestFocusEnabled(true);
                nomeTextField.requestFocus();
                JOptionPane.showMessageDialog(null, "Para editar dados, selecione a linha na tabela e aperte o botão Editar.", "Selecionar para editar", JOptionPane.WARNING_MESSAGE);
//                cancelarEdicao();
//                preencherTabelaEComboBoxSelectAll();
//                table1.getSelectionModel().clearSelection();
//                preencherTabelaEComboBoxSelectAll();
                System.out.println("Someone is trying to edit me!");

                //este estava ativo
//                sR = table1.convertRowIndexToModel(table1.getSelectedRow());//table1.getSelectedRow();
//
//                System.out.println("TESTE DE INDEX: " + table1.convertRowIndexToModel(table1.getSelectedRow()));
//
//                pessoas.forEach(p -> {
//                    if (p.getCpf() == tableModel.getValueAt(sR, 2)) {
//                        editingId = p.getId();
//                        editingCPF = p.getCpf();
//                        System.out.println("Foi selecionado na tabela o ID: " + editingId + " E cpf: " + editingCPF);
//                    }
//                });
//                editarPessoal();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarEdicao();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarPessoal();
            }
        });

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirPessoal();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarPessoal();
            }
        });

        inserirEmMassaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PessoalMassAdd pessoalMassAdd = new PessoalMassAdd(getTitle(), jdbcTemplate);
            }
        });

        refreshTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                cancelarEdicao();
                preencherTabelaEComboBoxSelectAll();
                JOptionPane.showMessageDialog(null, "Tabela atualizada com sucesso!", "Refresh Table", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        resizeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // java - get screen size using the Toolkit class
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                if (resizeTableButton.getText() == "Aumentar") {
                    System.out.println("W= " + screenSize.width + " H= " + screenSize.height);
                    table1.setPreferredScrollableViewportSize(new Dimension((int) (screenSize.width / 1.5), screenSize.height / 3));
                    table1.setFillsViewportHeight(true);
//                    table1.setCellSelectionEnabled(true);
                    resizeTableButton.setText("Encolher");
                } else {
                    System.out.println("W= " + screenSize.width + " H= " + screenSize.height);
                    table1.setPreferredScrollableViewportSize(new Dimension((int) (screenSize.width / 3.5), screenSize.height / 3));
                    table1.setFillsViewportHeight(true);
//                    table1.setCellSelectionEnabled(true);
                    resizeTableButton.setText("Aumentar");
                }
            }
        });

    }

    public void preencherTabelaEComboBoxSelectAll() {

        pessoas = jdbcTemplate.query("SELECT * FROM pessoal", (rs, rowNum) -> new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("cpf"), rs.getString("codigo_assessor"), rs.getBigDecimal("repasse_prev_assessor"), rs.getBigDecimal("repasse_seg_assessor"), rs.getInt("codigo_banco"), rs.getString("agencia"), rs.getString("conta"), rs.getString("dv"), rs.getString("codigo_xerife"), rs.getBigDecimal("repasse_prev_xerife")));

        tableModel = new DefaultTableModel(col, 0);
        table1.setModel(tableModel);

        xerifeComboBoxModel.removeAllElements();
        xerifeComboBoxModel.addElement("");
        pessoas.forEach(pessoa -> {
            System.out.println(pessoa.getNome());
            Object[] object = {pessoa.getNome(), pessoa.getEmail(), pessoa.getCpf(), pessoa.getCodigoAssessor(), pessoa.getRepassePrevAssessor(), pessoa.getRepasseSegAssessor(), pessoa.getCodigoBanco(), pessoa.getAgencia(), pessoa.getConta(), pessoa.getDv(), pessoa.getCodigoXerife(), pessoa.getRepassePrevXerife()};
            tableModel.addRow(object);

            xerifeComboBoxModel.addElement(pessoa.getCodigoAssessor());
        });
        xerifeComboBox.setSelectedIndex(0);
        resizeColumnWidth(table1);
    }

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 15, width);
            }
            if (width > 300)
                width = 300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public void limparCampos() {
        nomeTextField.setText("");
        emailTextField.setText("");
        cpfTextField.setText("");
        codAssessorTextField.setText("");
        repassePrevAssessorTextField.setText("");
        repasseSegAssessorTextField.setText("");
        codBancoTextField.setText("");
        agenciaTextField.setText("");
        contaTextField.setText("");
        dvTextField.setText("");
        xerifeComboBox.setSelectedIndex(0);
        repassePrevXerifeTextField.setText("");
        sR = -1;
    }

    public void editarPessoal() {
        if (editButton.getText() == "Editar") {
            // iniciar a edição
            if (table1.getSelectedRow() > -1) {
                if (sR == -1) {
                    sR = table1.convertRowIndexToModel(table1.getSelectedRow());
                }

                int r = sR;//table1.getSelectedRow();
                System.out.println("r= " + r + " e cpf= " + tableModel.getValueAt(r, 2));

//                pessoas.forEach(p -> {
//                    if (p.getCpf() == tableModel.getValueAt(r, 2)) {
//                        editingId = p.getId();
//                        System.out.println("O ID que está sendo editado é: " + editingId);
//                    }
//
//                });

                editingId = pessoas.get(r).getId();

                System.out.println("Edição iniciada para: " + pessoas.get(r).getNome());
                System.out.println("Edição iniciada para Id: " + pessoas.get(r).getId());

                nomeTextField.setText(tableModel.getValueAt(r, 0).toString());
                emailTextField.setText(tableModel.getValueAt(r, 1).toString());
                cpfTextField.setText(tableModel.getValueAt(r, 2).toString());
                codAssessorTextField.setText(tableModel.getValueAt(r, 3).toString());
                repassePrevAssessorTextField.setText(tableModel.getValueAt(r, 4).toString());
                repasseSegAssessorTextField.setText(tableModel.getValueAt(r, 5).toString());
                codBancoTextField.setText(tableModel.getValueAt(r, 6).toString());
                agenciaTextField.setText(tableModel.getValueAt(r, 7).toString());
                contaTextField.setText(tableModel.getValueAt(r, 8).toString());
                dvTextField.setText(tableModel.getValueAt(r, 9).toString());
                xerifeComboBox.setSelectedItem(tableModel.getValueAt(r, 10).toString());
                repassePrevXerifeTextField.setText(tableModel.getValueAt(r, 11).toString());
                editButton.setText("Completar edição");
                insertButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Para editar é necessário selecionar a tabela antes.", "Selecionar para editar", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            // antes de prosseguir, verificar se os campos BigDecimal possuem valores que não números

            // concluir a edição
            String sql = "UPDATE pessoal SET ";
            sql = sql + "nome = '" + nomeTextField.getText() + "', ";
            sql = sql + "email = '" + emailTextField.getText() + "', ";
            sql = sql + "cpf = '" + cpfTextField.getText() + "', ";
            sql = sql + "codigo_assessor = '" + codAssessorTextField.getText() + "', ";
            sql = sql + "repasse_prev_assessor = '" + ((repassePrevAssessorTextField.getText() == null) || (repassePrevAssessorTextField.getText().isEmpty()) ? "0" : repassePrevAssessorTextField.getText()) + "', ";
            sql = sql + "repasse_seg_assessor = '" + ((repasseSegAssessorTextField.getText() == null) || (repasseSegAssessorTextField.getText().isEmpty()) ? "0" : repasseSegAssessorTextField.getText()) + "', ";
            sql = sql + "codigo_banco = '" + codBancoTextField.getText() + "', ";
            sql = sql + "agencia = '" + agenciaTextField.getText() + "', ";
            sql = sql + "conta = '" + contaTextField.getText() + "', ";
            sql = sql + "dv = '" + dvTextField.getText() + "', ";
            sql = sql + "codigo_xerife = '" + String.valueOf(xerifeComboBox.getSelectedItem()) + "', ";
            sql = sql + "repasse_prev_xerife = '" + ((repassePrevXerifeTextField.getText() == null) || (repassePrevXerifeTextField.getText().isEmpty()) ? "0" : repassePrevXerifeTextField.getText()) + "' ";
            sql = sql + "WHERE id = " + editingId;
            System.out.println("Executando: " + sql);

            try {
                jdbcTemplate.execute(sql);

                limparCampos();
                preencherTabelaEComboBoxSelectAll();
                editButton.setText("Editar");
                insertButton.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Editado com sucesso", "Editado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                System.out.println("Mensagem de erro: " + e.getCause());
                JOptionPane.showMessageDialog(null, e.getCause(), "Erro de edição", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        }
    }

    public void cancelarEdicao() {
        limparCampos();
        editButton.setText("Editar");
        insertButton.setEnabled(true);
//        preencherTabelaEComboBoxSelectAll();
    }

    public void deletarPessoal() {
        if (table1.getSelectedRow() > -1) {
            table1.convertRowIndexToModel(table1.getSelectedRow());
            int[] rList = table1.getSelectedRows();
            String message;

            if (rList.length > 1) {
                message = "Tem certeza que deseja deletar todas as " + rList.length + " linhas selecionadas?";
            } else {
                message = "Tem certeza que deseja deletar a linha selecionada?";
            }

            int reply = JOptionPane.showConfirmDialog(null, message, "Deletando informações", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {

                for (int i = 0; i < rList.length; i++) {
                    //Converting rList from view to model
                    int r = table1.convertRowIndexToModel(rList[i]);

                    System.out.println("DELETANDO... " + r);
                    editingId = pessoas.get(r).getId();

                    System.out.println("Delete iniciada para: " + pessoas.get(r).getNome() + " Row: " + r);
                    System.out.println("Delete iniciada para Id: " + pessoas.get(r).getId());

                    String sql = "DELETE FROM pessoal WHERE id = " + editingId;
                    try {
                        jdbcTemplate.execute(sql);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getCause(), "Erro ao deletar", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                        return;
                    }

                }
//                limparCampos();
                cancelarEdicao();
                preencherTabelaEComboBoxSelectAll();
                JOptionPane.showMessageDialog(null, "Informações deletadas com sucesso", "Deletado", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {
            JOptionPane.showMessageDialog(null, "Para deletar é necessário selecionar a tabela antes.", "Selecione antes", JOptionPane.WARNING_MESSAGE);
        }

    }

    public void inserirPessoal() {
        if (nomeTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || cpfTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome, E-Mail e CPF são dados obrigatórios.", "Preencher os dados", JOptionPane.WARNING_MESSAGE);
        } else {
            String sql = "INSERT INTO pessoal (nome, email, cpf, codigo_assessor, repasse_prev_assessor, repasse_seg_assessor, codigo_banco, agencia, conta, dv, codigo_xerife, repasse_prev_xerife) VALUES( ";
            sql = sql + "'" + nomeTextField.getText() + "', ";
            sql = sql + "'" + emailTextField.getText() + "', ";
            sql = sql + "'" + cpfTextField.getText() + "', ";
            sql = sql + "'" + codAssessorTextField.getText() + "', ";
            sql = sql + "'" + ((repassePrevAssessorTextField.getText() == null) || (repassePrevAssessorTextField.getText().isEmpty()) ? "0" : repassePrevAssessorTextField.getText()) + "', ";
            sql = sql + "'" + ((repasseSegAssessorTextField.getText() == null) || (repasseSegAssessorTextField.getText().isEmpty()) ? "0" : repasseSegAssessorTextField.getText()) + "', ";
            sql = sql + "'" + codBancoTextField.getText() + "', ";
            sql = sql + "'" + agenciaTextField.getText() + "', ";
            sql = sql + "'" + contaTextField.getText() + "', ";
            sql = sql + "'" + dvTextField.getText() + "', ";
            sql = sql + "'" + String.valueOf(xerifeComboBox.getSelectedItem()) + "', ";
            sql = sql + "'" + ((repassePrevXerifeTextField.getText() == null) || (repassePrevXerifeTextField.getText().isEmpty()) ? "0" : repassePrevXerifeTextField.getText()) + "');";
            try {
                jdbcTemplate.execute(sql);
                limparCampos();
                preencherTabelaEComboBoxSelectAll();
            } catch (Exception e) {
                System.out.println("Mensagem de erro: " + e.getCause());
                JOptionPane.showMessageDialog(null, e.getCause());
                e.printStackTrace();
            }
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
        pessoalRootPanel = new JPanel();
        pessoalRootPanel.setLayout(new BorderLayout(0, 0));
        westLabelPanel = new JPanel();
        westLabelPanel.setLayout(new GridLayoutManager(8, 1, new Insets(30, 10, 50, 5), -1, -1));
        westLabelPanel.setBackground(new Color(-8679521));
        westLabelPanel.setToolTipText("");
        pessoalRootPanel.add(westLabelPanel, BorderLayout.WEST);
        westLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, null, null));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        label1.setText("Nome:");
        westLabelPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-1));
        label2.setText("E-Mail:");
        westLabelPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-1));
        label3.setText("CPF:");
        westLabelPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setForeground(new Color(-1));
        label4.setText("Cod Assessor:");
        westLabelPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setForeground(new Color(-1));
        label5.setText("Cod. Banco:");
        westLabelPanel.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, -1, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setForeground(new Color(-1));
        label6.setText("Agência:");
        westLabelPanel.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, -1, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setForeground(new Color(-1));
        label7.setText("Conta:");
        westLabelPanel.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, Font.BOLD, -1, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setForeground(new Color(-1));
        label8.setText("Xerife:");
        westLabelPanel.add(label8, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eastGridPanel = new JPanel();
        eastGridPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 10), -1, -1));
        eastGridPanel.setBackground(new Color(-8679521));
        pessoalRootPanel.add(eastGridPanel, BorderLayout.EAST);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBackground(new Color(-8679521));
        eastGridPanel.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        table1.setAutoCreateColumnsFromModel(true);
        table1.setAutoCreateRowSorter(true);
        table1.setAutoResizeMode(0);
        table1.setColumnSelectionAllowed(false);
        table1.setInheritsPopupMenu(false);
        table1.setMinimumSize(new Dimension(-1, -1));
        table1.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table1.setSelectionForeground(new Color(-14408925));
        table1.putClientProperty("Table.isFileList", Boolean.FALSE);
        scrollPane1.setViewportView(table1);
        resizeTableButton = new JButton();
        Font resizeTableButtonFont = this.$$$getFont$$$(null, -1, 9, resizeTableButton.getFont());
        if (resizeTableButtonFont != null) resizeTableButton.setFont(resizeTableButtonFont);
        resizeTableButton.setText("Aumentar");
        eastGridPanel.add(resizeTableButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshTableButton = new JButton();
        Font refreshTableButtonFont = this.$$$getFont$$$(null, -1, 9, refreshTableButton.getFont());
        if (refreshTableButtonFont != null) refreshTableButton.setFont(refreshTableButtonFont);
        refreshTableButton.setText("Refresh table");
        eastGridPanel.add(refreshTableButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        centerTextPanel = new JPanel();
        centerTextPanel.setLayout(new GridLayoutManager(8, 5, new Insets(30, 0, 50, 20), -1, -1));
        centerTextPanel.setBackground(new Color(-8679521));
        pessoalRootPanel.add(centerTextPanel, BorderLayout.CENTER);
        centerTextPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        nomeTextField = new JTextField();
        Font nomeTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, nomeTextField.getFont());
        if (nomeTextFieldFont != null) nomeTextField.setFont(nomeTextFieldFont);
        centerTextPanel.add(nomeTextField, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        emailTextField = new JTextField();
        Font emailTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, emailTextField.getFont());
        if (emailTextFieldFont != null) emailTextField.setFont(emailTextFieldFont);
        centerTextPanel.add(emailTextField, new GridConstraints(1, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cpfTextField = new JTextField();
        Font cpfTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, cpfTextField.getFont());
        if (cpfTextFieldFont != null) cpfTextField.setFont(cpfTextFieldFont);
        centerTextPanel.add(cpfTextField, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        codBancoTextField = new JTextField();
        Font codBancoTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, codBancoTextField.getFont());
        if (codBancoTextFieldFont != null) codBancoTextField.setFont(codBancoTextFieldFont);
        centerTextPanel.add(codBancoTextField, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        agenciaTextField = new JTextField();
        Font agenciaTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, agenciaTextField.getFont());
        if (agenciaTextFieldFont != null) agenciaTextField.setFont(agenciaTextFieldFont);
        centerTextPanel.add(agenciaTextField, new GridConstraints(5, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        contaTextField = new JTextField();
        Font contaTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, contaTextField.getFont());
        if (contaTextFieldFont != null) contaTextField.setFont(contaTextFieldFont);
        centerTextPanel.add(contaTextField, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dvTextField = new JTextField();
        Font dvTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, dvTextField.getFont());
        if (dvTextFieldFont != null) dvTextField.setFont(dvTextFieldFont);
        centerTextPanel.add(dvTextField, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.BOLD, -1, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setForeground(new Color(-1));
        label9.setText("DV:");
        centerTextPanel.add(label9, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, -1, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setForeground(new Color(-1));
        label10.setText("Rep. Prev.(%):");
        centerTextPanel.add(label10, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        codAssessorTextField = new JTextField();
        Font codAssessorTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, codAssessorTextField.getFont());
        if (codAssessorTextFieldFont != null) codAssessorTextField.setFont(codAssessorTextFieldFont);
        centerTextPanel.add(codAssessorTextField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        repassePrevAssessorTextField = new JTextField();
        Font repassePrevAssessorTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, repassePrevAssessorTextField.getFont());
        if (repassePrevAssessorTextFieldFont != null)
            repassePrevAssessorTextField.setFont(repassePrevAssessorTextFieldFont);
        centerTextPanel.add(repassePrevAssessorTextField, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, -1, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setForeground(new Color(-1));
        label11.setText("Rep. Seg.(%):");
        centerTextPanel.add(label11, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        repasseSegAssessorTextField = new JTextField();
        Font repasseSegAssessorTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, repasseSegAssessorTextField.getFont());
        if (repasseSegAssessorTextFieldFont != null)
            repasseSegAssessorTextField.setFont(repasseSegAssessorTextFieldFont);
        centerTextPanel.add(repasseSegAssessorTextField, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        xerifeComboBox = new JComboBox();
        centerTextPanel.add(xerifeComboBox, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, Font.BOLD, -1, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setForeground(new Color(-1));
        label12.setText("Rep. Prev.(%):");
        centerTextPanel.add(label12, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        repassePrevXerifeTextField = new JTextField();
        Font repassePrevXerifeTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, repassePrevXerifeTextField.getFont());
        if (repassePrevXerifeTextFieldFont != null) repassePrevXerifeTextField.setFont(repassePrevXerifeTextFieldFont);
        centerTextPanel.add(repassePrevXerifeTextField, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        southButtonPanel = new JPanel();
        southButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        southButtonPanel.setBackground(new Color(-10461088));
        pessoalRootPanel.add(southButtonPanel, BorderLayout.SOUTH);
        inserirEmMassaButton = new JButton();
        Font inserirEmMassaButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, inserirEmMassaButton.getFont());
        if (inserirEmMassaButtonFont != null) inserirEmMassaButton.setFont(inserirEmMassaButtonFont);
        inserirEmMassaButton.setText("Inserir em Massa");
        southButtonPanel.add(inserirEmMassaButton);
        insertTestButton = new JButton();
        Font insertTestButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, insertTestButton.getFont());
        if (insertTestButtonFont != null) insertTestButton.setFont(insertTestButtonFont);
        insertTestButton.setText("Inserir Teste");
        southButtonPanel.add(insertTestButton);
        insertButton = new JButton();
        Font insertButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, insertButton.getFont());
        if (insertButtonFont != null) insertButton.setFont(insertButtonFont);
        insertButton.setText("Inserir");
        southButtonPanel.add(insertButton);
        editButton = new JButton();
        Font editButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, editButton.getFont());
        if (editButtonFont != null) editButton.setFont(editButtonFont);
        editButton.setText("Editar");
        southButtonPanel.add(editButton);
        cancelButton = new JButton();
        Font cancelButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, cancelButton.getFont());
        if (cancelButtonFont != null) cancelButton.setFont(cancelButtonFont);
        cancelButton.setText("Cancelar / Limpar campos");
        southButtonPanel.add(cancelButton);
        deleteButton = new JButton();
        Font deleteButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, deleteButton.getFont());
        if (deleteButtonFont != null) deleteButton.setFont(deleteButtonFont);
        deleteButton.setText("Deletar");
        southButtonPanel.add(deleteButton);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return pessoalRootPanel;
    }


    // Customize the code for sorting of items in the JComboBox
    private class SortedComboBoxModel extends DefaultComboBoxModel {
        public SortedComboBoxModel() {
            super();
        }

        public SortedComboBoxModel(Object[] items) {
            Arrays.sort(items);
            int size = items.length;
            for (int i = 0; i < size; i++) {
                super.addElement(items[i]);
            }
            setSelectedItem(items[0]);
        }

        public SortedComboBoxModel(Vector items) {
            Collections.sort(items);
            int size = items.size();
            for (int i = 0; i < size; i++) {
                super.addElement(items.elementAt(i));
            }
            setSelectedItem(items.elementAt(0));
        }

        public void addElement(Object element) {
            insertElementAt(element, 0);
        }

        public void insertElementAt(Object element, int index) {
            int size = getSize();
            for (index = 0; index < size; index++) {
                Comparable c = (Comparable) getElementAt(index);
                if (c.compareTo(element) > 0) {
                    break;
                }
            }
            super.insertElementAt(element, index);
        }
    }


}