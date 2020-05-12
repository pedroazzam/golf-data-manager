package br.com.golfinvest.data.view;

import br.com.golfinvest.data.model.Pessoa;
import br.com.golfinvest.data.model.Produto;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class ProdutosFrame extends JFrame {

    private JdbcTemplate jdbcTemplate;
    List<Produto> produtos;
    DefaultTableModel tableModel;
    String col[] = {"Nome"};
    DefaultListModel tagProdListModel;
    DefaultListModel tagN1ListModel;
    DefaultListModel tagN2ListModel;
    DefaultListModel tagN3ListModel;
    int editingId;

    private JPanel produtoRootPanel;
    private JPanel principalPanel;
    private JPanel southButtonPanel;
    private JButton insertButton;
    private JButton editButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JPanel centerPanel;
    private JSplitPane centerSplitPanel;
    private JPanel leftSplittedPanel;
    private JPanel rightSplittedPanel;
    private JTable produtosTable;
    private JPanel topLeftPanel;
    private JPanel eastLeftPanel;
    private JPanel centerLeftPanel;
    private JList tagProdList;
    private JList tagN1List;
    private JList tagN2List;
    private JList tagN3List;
    private JTextField tagProdTextField;
    private JButton tagProdAddButton;
    private JButton tagProdDeleteButton;
    private JTextField tagN1TextField;
    private JTextField tagN2TextField;
    private JTextField tagN3TextField;
    private JButton tagN1DeleteButton;
    private JButton tagN1AddButton;
    private JButton tagN2DeleteButton;
    private JButton tagN2AddButton;
    private JButton tagN3DeleteButton;
    private JButton tagN3AddButton;
    private JTextField nomeProdutoTextField;
    private JButton inserirTesteButton;

    public ProdutosFrame(String title, JdbcTemplate jdbcTemplatePassed) {
        super();
        title = title + " - (Registro de produtos no banco de dados)";
        setTitle(title);
        setSize(1000, 600);
        jdbcTemplate = jdbcTemplatePassed;

        initComponents();
        regEvents();
    }

    public void initComponents() {
        System.out.println("Init Components ProdutosFrame...");
        setContentPane(produtoRootPanel);
        tableModel = new DefaultTableModel(col, 0);
        produtosTable.setModel(tableModel);
        tagProdListModel = new DefaultListModel();
        tagN1ListModel = new DefaultListModel();
        tagN2ListModel = new DefaultListModel();
        tagN3ListModel = new DefaultListModel();
        tagProdList.setModel(tagProdListModel);
        tagN1List.setModel(tagN1ListModel);
        tagN2List.setModel(tagN2ListModel);
        tagN3List.setModel(tagN3ListModel);

        inserirTesteButton.setVisible(false); // Disable button to insert test data

        buttonsFromListsOFF();

        setVisible(true);
        preencherTabelaSelectAll();
    }

    public void regEvents() {

        inserirTesteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    InputStream is = new ClassPathResource("insert-produtos.sql").getInputStream();
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

                preencherTabelaSelectAll();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarEdicao();
            }
        });

        produtosTable.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                //System.out.println("Someone is editing me!");
                //Caso entre em modo de edição da tabela, roda um select again.
                int sR = produtosTable.getSelectedRow();
                cancelarEdicao();
                produtosTable.setRowSelectionInterval(sR, sR);
                editarProduto();
            }
        });

        nomeProdutoTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (nomeProdutoTextField.getText().isEmpty()) {
                    buttonsFromListsOFF();
                } else {
                    buttonsFromListsON();
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProduto();
            }
        });

        tagProdAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagProdTextField.getText().isEmpty()) {
                    tagProdListModel.addElement(tagProdTextField.getText());
                    tagProdTextField.setText("");
                }
            }
        });

        tagN1AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagN1TextField.getText().isEmpty()) {
                    tagN1ListModel.addElement(tagN1TextField.getText());
                    tagN1TextField.setText("");
                }
            }
        });

        tagN2AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagN2TextField.getText().isEmpty()) {
                    tagN2ListModel.addElement(tagN2TextField.getText());
                    tagN2TextField.setText("");
                }
            }
        });

        tagN3AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagN3TextField.getText().isEmpty()) {
                    tagN3ListModel.addElement(tagN3TextField.getText());
                    tagN3TextField.setText("");
                }
            }
        });

        tagProdDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagProdList.isSelectionEmpty()) {
                    tagProdList.getSelectedValuesList().forEach(iten -> tagProdListModel.removeElement(iten));
                }
            }
        });

        tagN1DeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagN1List.isSelectionEmpty()) {
                    tagN1List.getSelectedValuesList().forEach(iten -> tagN1ListModel.removeElement(iten));
                }
            }
        });

        tagN2DeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagN2List.isSelectionEmpty()) {
                    tagN2List.getSelectedValuesList().forEach(iten -> tagN2ListModel.removeElement(iten));
                }
            }
        });

        tagN3DeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tagN3List.isSelectionEmpty()) {
                    tagN3List.getSelectedValuesList().forEach(iten -> tagN3ListModel.removeElement(iten));
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarProdutos();
                cancelarEdicao();
            }
        });

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirProduto();
            }
        });

    }

    public void preencherTabelaSelectAll() {
        produtos = jdbcTemplate.query("SELECT * FROM produto", (rs, rowNum) -> new Produto(rs.getInt("id"), rs.getString("nome"), rs.getString("tag_col_produto"), rs.getString("tag_col_nivel01"), rs.getString("tag_col_nivel02"), rs.getString("tag_col_nivel03")));
        tableModel = new DefaultTableModel(col, 0);

        produtosTable.setModel(tableModel);
        produtos.forEach(produto -> {
            System.out.println(produto.getNome());
            Object[] object = {produto.getNome()};
            tableModel.addRow(object);
        });

    }

    public void cancelarEdicao() {
        limparCampos();
        editButton.setText("Editar");
        insertButton.setEnabled(true);
        buttonsFromListsOFF();
        preencherTabelaSelectAll();
    }

    public void limparCampos() {
        nomeProdutoTextField.setText("");
        tagProdTextField.setText("");
        tagN1TextField.setText("");
        tagN2TextField.setText("");
        tagN3TextField.setText("");
        tagProdListModel.clear();
        tagN1ListModel.clear();
        tagN2ListModel.clear();
        tagN3ListModel.clear();


    }

    public void editarProduto() {
        if (editButton.getText() == "Editar") {
            // iniciar a edição
            if (produtosTable.getSelectedRow() > -1) {
                String produto = tableModel.getValueAt(produtosTable.getSelectedRow(), 0).toString();
                nomeProdutoTextField.setText(produto);
                editingId = (Integer) jdbcTemplate.queryForObject("SELECT id FROM produto WHERE upper(nome) = upper('" + produto + "')", new Object[]{}, Integer.class);
                preencherListaTag(produto, tagProdListModel, "tag_col_produto");
                preencherListaTag(produto, tagN1ListModel, "tag_col_nivel01");
                preencherListaTag(produto, tagN2ListModel, "tag_col_nivel02");
                preencherListaTag(produto, tagN3ListModel, "tag_col_nivel03");

                buttonsFromListsON();

                editButton.setText("Completar edição");
                insertButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Para editar é necessário selecionar a tabela antes.", "Selecionar para editar", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            // concluir a edição
            if (nomeProdutoTextField.getText().isEmpty() || tagProdListModel.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Os campos 'Nome do produto' e 'Lista Tags Coluna Produto' são obrigatórios.", "Preencher os dados", JOptionPane.WARNING_MESSAGE);
            } else {
                String tagsProduto = getListModelToString(tagProdListModel);
                String tagsN1 = getListModelToString(tagN1ListModel);
                String tagsN2 = getListModelToString(tagN2ListModel);
                String tagsN3 = getListModelToString(tagN3ListModel);
                String sql = "UPDATE produto SET ";
                sql = sql + "nome = '" + nomeProdutoTextField.getText() + "', ";
                sql = sql + "tag_col_produto = '" + tagsProduto + "', ";
                sql = sql + "tag_col_nivel01 = '" + tagsN1 + "', ";
                sql = sql + "tag_col_nivel02 = '" + tagsN2 + "', ";
                sql = sql + "tag_col_nivel03 = '" + tagsN3 + "' ";
                sql = sql + "WHERE id = " + editingId;
                System.out.println("Executando: " + sql);

                try {
                    jdbcTemplate.execute(sql);

                    limparCampos();
                    preencherTabelaSelectAll();
                    editButton.setText("Editar");
                    insertButton.setEnabled(true);
                    buttonsFromListsOFF();
                    JOptionPane.showMessageDialog(null, "Editado com sucesso", "Editado", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getListModelToString(DefaultListModel listModel) {
        String str = "";
        for (int i = 0; i < listModel.size(); i++) {
            if (i == 0) {
                str = listModel.getElementAt(i).toString();
            } else {
                str = str + ";" + listModel.getElementAt(i).toString();
            }
        }
        return str;
    }

    public void preencherListaTag(String produto, DefaultListModel listModel, String nomeColunaTagDB) {
        //PREENCHER LISTA TAGS
//        String produto = "Renda Variavel";
        String sql = "SELECT produto_id, produto_tag FROM (" +
                "WITH RECURSIVE split(produto_id, produto_nome, produto_tag, rest) AS (" +
                "  SELECT id, nome, '', " + nomeColunaTagDB + " || ';' FROM produto WHERE id" +
                "   UNION ALL" +
                "  SELECT produto_id, produto_nome, " +
                "         substr(rest, 0, instr(rest, ';'))," +
                "         substr(rest, instr(rest, ';')+1)" +
                "    FROM split" +
                "   WHERE rest <> '')" +
                "SELECT produto_id, produto_nome, produto_tag" +
                "  FROM split " +
                " WHERE produto_tag <> ''" +
                " ) WHERE upper(produto_nome) = upper('" + produto + "');";

        List<String> itensTag;
        itensTag = jdbcTemplate.query(sql, (rs, rowNum) -> new String(rs.getString("produto_tag")));
        itensTag.forEach(tag -> listModel.addElement(tag));

        editingId = (Integer) jdbcTemplate.queryForObject("SELECT id FROM produto WHERE upper(nome) = upper('" + produto + "')", new Object[]{}, Integer.class);
        System.out.println("PRODUTO: " + produto + " ID: " + editingId);

    }

    public void buttonsFromListsON() {
        tagProdDeleteButton.setEnabled(true);
        tagProdAddButton.setEnabled(true);
        tagN1DeleteButton.setEnabled(true);
        tagN1AddButton.setEnabled(true);
        tagN2DeleteButton.setEnabled(true);
        tagN2AddButton.setEnabled(true);
        tagN3DeleteButton.setEnabled(true);
        tagN3AddButton.setEnabled(true);
    }

    public void buttonsFromListsOFF() {
        tagProdDeleteButton.setEnabled(false);
        tagProdAddButton.setEnabled(false);
        tagN1DeleteButton.setEnabled(false);
        tagN1AddButton.setEnabled(false);
        tagN2DeleteButton.setEnabled(false);
        tagN2AddButton.setEnabled(false);
        tagN3DeleteButton.setEnabled(false);
        tagN3AddButton.setEnabled(false);
    }

    public void deletarProdutos() {
        if (produtosTable.getSelectedRow() > -1) {

            int[] rList = produtosTable.getSelectedRows();
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
                    editingId = produtos.get(r).getId();

                    System.out.println("Delete iniciada para: " + produtos.get(r).getNome() + " Row: " + r);
                    System.out.println("Delete iniciada para Id: " + produtos.get(r).getId());

                    String sql = "DELETE FROM produto WHERE id = " + editingId;
                    try {
                        jdbcTemplate.execute(sql);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getCause(), "Erro ao deletar", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                        return;
                    }

                }
                limparCampos();
                preencherTabelaSelectAll();
                JOptionPane.showMessageDialog(null, "Informações deletadas com sucesso", "Deletado", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {
            JOptionPane.showMessageDialog(null, "Para deletar é necessário selecionar a tabela antes.", "Selecione antes", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void inserirProduto() {
        if (nomeProdutoTextField.getText().isEmpty() || tagProdListModel.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos 'Nome do produto' e 'Lista Tags Coluna Produto' são obrigatórios.", "Preencher os dados", JOptionPane.WARNING_MESSAGE);
        } else {
            String tagsProduto = getListModelToString(tagProdListModel);
            String tagsN1 = getListModelToString(tagN1ListModel);
            String tagsN2 = getListModelToString(tagN2ListModel);
            String tagsN3 = getListModelToString(tagN3ListModel);
            String sql = "INSERT INTO produto (nome, tag_col_produto, tag_col_nivel01, tag_col_nivel02, tag_col_nivel03) VALUES( ";
            sql = sql + "'" + nomeProdutoTextField.getText() + "', ";
            sql = sql + "'" + tagsProduto + "', ";
            sql = sql + "'" + tagsN1 + "', ";
            sql = sql + "'" + tagsN2 + "', ";
            sql = sql + "'" + tagsN3 + "');";
            try {
                jdbcTemplate.execute(sql);
                limparCampos();
                preencherTabelaSelectAll();
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
        produtoRootPanel = new JPanel();
        produtoRootPanel.setLayout(new CardLayout(0, 0));
        principalPanel = new JPanel();
        principalPanel.setLayout(new BorderLayout(0, 0));
        produtoRootPanel.add(principalPanel, "Card1");
        southButtonPanel = new JPanel();
        southButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        principalPanel.add(southButtonPanel, BorderLayout.SOUTH);
        inserirTesteButton = new JButton();
        inserirTesteButton.setText("Inserir Teste");
        southButtonPanel.add(inserirTesteButton);
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
        cancelButton.setText("Cancelar/Limpar");
        southButtonPanel.add(cancelButton);
        deleteButton = new JButton();
        Font deleteButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, deleteButton.getFont());
        if (deleteButtonFont != null) deleteButton.setFont(deleteButtonFont);
        deleteButton.setText("Deletar");
        southButtonPanel.add(deleteButton);
        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout(0, 0));
        principalPanel.add(centerPanel, BorderLayout.CENTER);
        centerSplitPanel = new JSplitPane();
        centerSplitPanel.setDividerLocation(592);
        centerSplitPanel.setDividerSize(10);
        centerSplitPanel.setEnabled(false);
        centerPanel.add(centerSplitPanel, "Card1");
        centerSplitPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        rightSplittedPanel = new JPanel();
        rightSplittedPanel.setLayout(new CardLayout(0, 0));
        centerSplitPanel.setRightComponent(rightSplittedPanel);
        rightSplittedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Produtos Registrados", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 16, rightSplittedPanel.getFont()), null));
        final JScrollPane scrollPane1 = new JScrollPane();
        rightSplittedPanel.add(scrollPane1, "Card1");
        produtosTable = new JTable();
        scrollPane1.setViewportView(produtosTable);
        leftSplittedPanel = new JPanel();
        leftSplittedPanel.setLayout(new BorderLayout(0, 0));
        centerSplitPanel.setLeftComponent(leftSplittedPanel);
        leftSplittedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Registro de Produtos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 16, leftSplittedPanel.getFont()), null));
        topLeftPanel = new JPanel();
        topLeftPanel.setLayout(new GridLayoutManager(1, 2, new Insets(20, 20, 20, 10), -1, -1));
        leftSplittedPanel.add(topLeftPanel, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Nome do Produto: *");
        topLeftPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeProdutoTextField = new JTextField();
        Font nomeProdutoTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, 18, nomeProdutoTextField.getFont());
        if (nomeProdutoTextFieldFont != null) nomeProdutoTextField.setFont(nomeProdutoTextFieldFont);
        topLeftPanel.add(nomeProdutoTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        eastLeftPanel = new JPanel();
        eastLeftPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        leftSplittedPanel.add(eastLeftPanel, BorderLayout.EAST);
        final Spacer spacer1 = new Spacer();
        eastLeftPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        eastLeftPanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        centerLeftPanel = new JPanel();
        centerLeftPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 20, 10, 10), -1, -1));
        leftSplittedPanel.add(centerLeftPanel, BorderLayout.CENTER);
        final JScrollPane scrollPane2 = new JScrollPane();
        centerLeftPanel.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(null, "Lista Tags Coluna Produto*", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, null, null));
        tagProdList = new JList();
        tagProdList.setBackground(new Color(-262193));
        scrollPane2.setViewportView(tagProdList);
        final JScrollPane scrollPane3 = new JScrollPane();
        centerLeftPanel.add(scrollPane3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane3.setBorder(BorderFactory.createTitledBorder(null, "Lista Tags Coluna Nível 2", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagN2List = new JList();
        tagN2List.setBackground(new Color(-918273));
        scrollPane3.setViewportView(tagN2List);
        tagProdAddButton = new JButton();
        tagProdAddButton.setBackground(new Color(-262193));
        tagProdAddButton.setText("Adicionar");
        centerLeftPanel.add(tagProdAddButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tagProdDeleteButton = new JButton();
        tagProdDeleteButton.setBackground(new Color(-262193));
        tagProdDeleteButton.setText("Deletar");
        centerLeftPanel.add(tagProdDeleteButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tagN2TextField = new JTextField();
        tagN2TextField.setBackground(new Color(-918273));
        centerLeftPanel.add(tagN2TextField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tagN2DeleteButton = new JButton();
        tagN2DeleteButton.setBackground(new Color(-918273));
        tagN2DeleteButton.setText("Deletar");
        centerLeftPanel.add(tagN2DeleteButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tagN2AddButton = new JButton();
        tagN2AddButton.setBackground(new Color(-918273));
        tagN2AddButton.setText("Adicionar");
        centerLeftPanel.add(tagN2AddButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tagProdTextField = new JTextField();
        tagProdTextField.setBackground(new Color(-262193));
        tagProdTextField.setText("");
        tagProdTextField.setToolTipText("Insira a Tag aqui e clique em adicionar");
        centerLeftPanel.add(tagProdTextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane4 = new JScrollPane();
        centerLeftPanel.add(scrollPane4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane4.setBorder(BorderFactory.createTitledBorder(null, "Lista Tags Coluna Nível 1", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagN1List = new JList();
        tagN1List.setBackground(new Color(-918273));
        scrollPane4.setViewportView(tagN1List);
        tagN1TextField = new JTextField();
        tagN1TextField.setBackground(new Color(-918273));
        tagN1TextField.setToolTipText("Insira a Tag aqui e clique em adicionar");
        centerLeftPanel.add(tagN1TextField, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tagN1DeleteButton = new JButton();
        tagN1DeleteButton.setBackground(new Color(-918273));
        tagN1DeleteButton.setText("Deletar");
        centerLeftPanel.add(tagN1DeleteButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tagN1AddButton = new JButton();
        tagN1AddButton.setBackground(new Color(-918273));
        tagN1AddButton.setText("Adicionar");
        centerLeftPanel.add(tagN1AddButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane5 = new JScrollPane();
        centerLeftPanel.add(scrollPane5, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane5.setBorder(BorderFactory.createTitledBorder(null, "Lista Tags Coluna Nível 3", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tagN3List = new JList();
        tagN3List.setBackground(new Color(-918273));
        scrollPane5.setViewportView(tagN3List);
        tagN3TextField = new JTextField();
        tagN3TextField.setBackground(new Color(-918273));
        tagN3TextField.setToolTipText("Insira a Tag aqui e clique em adicionar");
        centerLeftPanel.add(tagN3TextField, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tagN3DeleteButton = new JButton();
        tagN3DeleteButton.setBackground(new Color(-918273));
        tagN3DeleteButton.setText("Deletar");
        centerLeftPanel.add(tagN3DeleteButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tagN3AddButton = new JButton();
        tagN3AddButton.setBackground(new Color(-918273));
        tagN3AddButton.setText("Adicionar");
        centerLeftPanel.add(tagN3AddButton, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return produtoRootPanel;
    }
}
