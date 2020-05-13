package br.com.golfinvest.data.view;

import br.com.golfinvest.data.model.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.math.BigDecimal;
import java.util.List;

public class CapitaoFrame extends JInternalFrame {

    private JdbcTemplate jdbcTemplate;
    DefaultTableModel capitaesTableModel;
    String col[] = {"Capitão", "Produto", "Comissão"};
    List<Capitao> capitaoList;
    DefaultListModel produtoListModel;
    DefaultComboBoxModel capitaoComboBoxModel;


    private JPanel capitaoRootPanel;
    private JPanel fillFieldsPanel;
    private JPanel tableCapitaesPanel;
    private JComboBox capitaoComboBox;
    private JList produtoList;
    private JTextField comissaoTextField;
    private JButton insertButton;
    private JTable capitaesTable;
    private JButton deleteButton;
    private JButton limparCamposButton;

    public CapitaoFrame(String title, JdbcTemplate jdbcTemplatePassed) {
        super();
        title = title + " - (Registro de capitão de produto)";
        setTitle(title);
        setSize(600, 600);
        jdbcTemplate = jdbcTemplatePassed;

        initComponents();
        regEvents();


    }

    public void initComponents() {
        System.out.println("Init Components CapitaoFrame...");
        setContentPane(capitaoRootPanel);
        produtoListModel = new DefaultListModel();
        produtoList.setModel(produtoListModel);

        capitaoComboBoxModel = new DefaultComboBoxModel();
        capitaoComboBox.setModel(capitaoComboBoxModel);

        iniciarTabelaCapitaes();
        fillProdutoList();
        fillCapitaoComboBox();

        setMaximizable(true); // maximize
//        setIconifiable(true); // set minimize
        setClosable(true); // set closed
//        setResizable(true); // set resizable

        setVisible(true);
    }

    public void regEvents() {

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirCapitao();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarCapitao();
            }
        });

        limparCamposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        capitaesTable.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                //System.out.println("Someone is editing me!");
                iniciarTabelaCapitaes();
            }
        });

    }

    public void inserirCapitao() {
        if (capitaoComboBox.getSelectedIndex() < 0 || produtoList.isSelectionEmpty() || comissaoTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos Capitão, Produto e Comissão são obrigatórios.", "Preencher os dados", JOptionPane.WARNING_MESSAGE);
        } else {
            String capitao = capitaoComboBox.getSelectedItem().toString();
            String produto = produtoList.getSelectedValue().toString();

//            String comissaoString = comissaoTextField.getText().replaceAll(",", ".");
//            BigDecimal comissao = new BigDecimal(comissaoString);
//            System.out.println("Capitao: " + capitao + "Produto: " + produto + "Comissao: " + comissao);

//            throw new BaseException(Errors.INVALID_NUMBER_FORMAT.getDescription(comissaoTextField.getText()));

            //Get pessoal id
            String sql = "SELECT id FROM pessoal WHERE nome=?";
            int id_pessoal = (Integer) jdbcTemplate.queryForObject(
                    sql, new Object[]{capitao}, Integer.class);
            //Get produto id
            sql = "SELECT id FROM produto WHERE nome=?";
            int id_produto = (Integer) jdbcTemplate.queryForObject(
                    sql, new Object[]{produto}, Integer.class);
            //Add Capitao
            try {
                String comissaoString = comissaoTextField.getText().replaceAll(",", ".");
                BigDecimal comissao = new BigDecimal(comissaoString);
                System.out.println("Capitao: " + capitao + "Produto: " + produto + "Comissao: " + comissao);

                sql = "INSERT INTO produto_capitao (id_produto, id_pessoal, comissao) VALUES(" + id_produto + "," + id_pessoal + "," + comissao + ")";
                jdbcTemplate.execute(sql);
                limparCampos();
                iniciarTabelaCapitaes();
            } catch (Exception e) {
                System.out.println("Mensagem de erro: " + e.getCause());
                JOptionPane.showMessageDialog(null, e.getCause(), "Erro inserindo no BD", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    public void deletarCapitao() {
        if (capitaesTable.getSelectedRow() > -1) {

            int[] rList = capitaesTable.getSelectedRows();
            String message;

            if (rList.length > 1) {
                message = "Tem certeza que deseja deletar todas as " + rList.length + " linhas selecionadas?";
            } else {
                message = "Tem certeza que deseja deletar a linha selecionada?";
            }

            int reply = JOptionPane.showConfirmDialog(null, message, "Deletando informações", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {

                for (int i = 0; i < rList.length; i++) {
                    System.out.println("DELETANDO... " + rList[i]);

                    int r = rList[i]; //table1.getSelectedRow();
                    int editingId = capitaoList.get(r).getId();

                    System.out.println("Delete iniciada para: " + capitaoList.get(r).getNomeProduto() + " Row: " + r);
                    System.out.println("Delete iniciada para Id: " + capitaoList.get(r).getId());

                    String sql = "DELETE FROM produto_capitao WHERE id = " + editingId;
                    try {
                        jdbcTemplate.execute(sql);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getCause(), "Erro ao deletar", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                        return;
                    }

                }
                limparCampos();
                iniciarTabelaCapitaes();
                JOptionPane.showMessageDialog(null, "Informações deletadas com sucesso", "Deletado", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {
            JOptionPane.showMessageDialog(null, "Para deletar é necessário selecionar a tabela antes.", "Selecione antes", JOptionPane.WARNING_MESSAGE);
        }

    }

    public void iniciarTabelaCapitaes() {
        String sql = "SELECT a.id, a.id_produto, b.nome 'nome_produto', a.id_pessoal, c.nome 'nome_pessoal', a.comissao FROM produto_capitao a JOIN produto b ON a.id_produto=b.id JOIN pessoal c ON a.id_pessoal=c.id;";
        capitaoList = jdbcTemplate.query(sql, (rs, rowNum) -> new Capitao(rs.getInt("id"), rs.getInt("id_produto"), rs.getString("nome_produto"), rs.getInt("id_pessoal"), rs.getString("nome_pessoal"), rs.getBigDecimal("comissao")));

        capitaesTableModel = new DefaultTableModel(col, 0);
        capitaesTable.setModel(capitaesTableModel);

        capitaoList.forEach(capitao -> {
            System.out.println(capitao.getNomePessoal());
            Object[] object = {capitao.getNomePessoal(), capitao.getNomeProduto(), capitao.getComissao()};
            capitaesTableModel.addRow(object);
        });

    }

    public void limparCampos() {
        capitaoComboBox.setSelectedItem(null);
        fillProdutoList();
        comissaoTextField.setText("");
        iniciarTabelaCapitaes();

    }

    public void fillCapitaoComboBox() {
        String sql = "SELECT * from PESSOAL;";
        List<Pessoa> pessoas = jdbcTemplate.query(sql, (rs, rowNum) -> new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("cpf"), rs.getString("codigo_assessor"), rs.getInt("codigo_banco"), rs.getString("agencia"), rs.getString("conta"), rs.getString("dv")));
        pessoas.forEach(pessoa -> capitaoComboBoxModel.addElement(pessoa.getNome()));
        capitaoComboBox.setSelectedItem(null);
    }

    public void fillProdutoList() {
        produtoListModel.clear();
        String sql = "SELECT nome FROM produto;";
        List<String> itensTag;
        itensTag = jdbcTemplate.query(sql, (rs, rowNum) -> new String(rs.getString("nome")));
        itensTag.forEach(tag -> produtoListModel.addElement(tag));

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
        capitaoRootPanel = new JPanel();
        capitaoRootPanel.setLayout(new FormLayout("fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        fillFieldsPanel = new JPanel();
        fillFieldsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(20, 20, 10, 20), -1, -1));
        CellConstraints cc = new CellConstraints();
        capitaoRootPanel.add(fillFieldsPanel, cc.xy(1, 1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 8, new Insets(10, 10, 10, 10), -1, -1));
        fillFieldsPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Capitão:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        capitaoComboBox = new JComboBox();
        panel1.add(capitaoComboBox, new GridConstraints(0, 1, 1, 7, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Produto:");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(2, 1, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(40, -1), null, 0, false));
        produtoList = new JList();
        scrollPane1.setViewportView(produtoList);
        final JSeparator separator1 = new JSeparator();
        panel1.add(separator1, new GridConstraints(1, 0, 1, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Comissão");
        panel1.add(label3, new GridConstraints(2, 3, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comissaoTextField = new JTextField();
        panel1.add(comissaoTextField, new GridConstraints(2, 4, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        insertButton = new JButton();
        insertButton.setText("Inserir");
        panel1.add(insertButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, 40), new Dimension(80, 40), new Dimension(80, 40), 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        limparCamposButton = new JButton();
        limparCamposButton.setHorizontalTextPosition(11);
        limparCamposButton.setText("Limpar");
        panel1.add(limparCamposButton, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, 40), new Dimension(80, 40), new Dimension(80, 40), 0, false));
        tableCapitaesPanel = new JPanel();
        tableCapitaesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 20, 20, 20), -1, -1));
        capitaoRootPanel.add(tableCapitaesPanel, cc.xy(1, 3));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(10, 10, 10, 10), -1, -1));
        tableCapitaesPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Capitães", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        capitaesTable = new JTable();
        scrollPane2.setViewportView(capitaesTable);
        deleteButton = new JButton();
        deleteButton.setText("Deletar");
        panel2.add(deleteButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(90, 50), new Dimension(90, 50), new Dimension(90, 50), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return capitaoRootPanel;
    }
}
