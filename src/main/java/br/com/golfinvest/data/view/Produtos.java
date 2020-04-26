package br.com.golfinvest.data.view;

import br.com.golfinvest.data.model.Pessoa;
import br.com.golfinvest.data.model.Produto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Produtos extends JFrame {

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

    public Produtos(String title, JdbcTemplate jdbcTemplatePassed) {
        super();
        title = title + " - (Registro de produtos no banco de dados)";
        setTitle(title);
        setSize(1000, 600);
        jdbcTemplate = jdbcTemplatePassed;

        initComponents();
        regEvents();
    }

    public void initComponents() {
        System.out.println("Init Components Produtos...");
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

        buttonsFromListsOFF();

        setVisible(true);
        preencherTabelaSelectAll();
    }

    public void regEvents() {

        inserirTesteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = null;
                try {
                    file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "insert-produtos.sql");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                System.out.println(file.getPath());
                String ic;
                Scanner sc = null;//file to be scanned
                try {
                    sc = new Scanner(file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                //returns true if there is another line to read
                while (sc.hasNextLine()) {
                    ic = sc.nextLine();
                    System.out.println("antes " + ic + " depois");      //returns the line that was skipped
                    jdbcTemplate.execute(ic);
                }
                sc.close(); //closes the scanner

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
                //Caso entre em modo de edição da tabela, rora um select again.
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

}
