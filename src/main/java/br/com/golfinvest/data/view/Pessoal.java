package br.com.golfinvest.data.view;

import br.com.golfinvest.data.model.Pessoa;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Pessoal extends JFrame {

    private JdbcTemplate jdbcTemplate;
    DefaultTableModel tableModel;
    String col[] = {"Nome", "E-Mail", "CPF", "Cod Assessor", "Cod Banco", "Agência", "Conta"};
    List<Pessoa> pessoas;
    int editingId;

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

    public Pessoal(String title, JdbcTemplate jdbcTemplatePassed) {
        super();
        title = title + " - (Registro de pessoal no banco de dados)";
        setTitle(title);
        setSize(1000, 480);
        jdbcTemplate = jdbcTemplatePassed;

        initComponents();
        regEvents();
    }

    public void initComponents() {
        System.out.println("Init Components MainFrame...");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(pessoalRootPanel);
//        setLocationRelativeTo(null);

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
//        Point newLocation = new Point(middle.x - (getWidth() / 2),
//                middle.y - (getHeight() / 2));
//        setLocation(newLocation);


        tableModel = new DefaultTableModel(col, 0);
        table1.setModel(tableModel);

        table1.setRowSelectionAllowed(true);

        setVisible(true);
        preencherTabelaSelectAll();
    }

    public void regEvents() {

        insertTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = null;
                try {
                    file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "insert-pessoal.sql");
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


        table1.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                //System.out.println("Someone is editing me!");
                //Caso entre em modo de edição da tabela, rora um select again.
                int sR = table1.getSelectedRow();
                cancelarEdicao();
                table1.setRowSelectionInterval(sR, sR);
                editarPessoal();
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

    }


    public void preencherTabelaSelectAll() {

        pessoas = jdbcTemplate.query("SELECT * FROM pessoal", (rs, rowNum) -> new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("cpf"), rs.getString("codigo_assessor"), rs.getInt("codigo_banco"), rs.getString("agencia"), rs.getString("conta")));

        tableModel = new DefaultTableModel(col, 0);
        table1.setModel(tableModel);

        pessoas.forEach(pessoa -> {
            System.out.println(pessoa.getNome());
            Object[] object = {pessoa.getNome(), pessoa.getEmail(), pessoa.getCpf(), pessoa.getCodigoAssessor(), pessoa.getCodigoBanco(), pessoa.getAgencia(), pessoa.getConta()};
            tableModel.addRow(object);
        });
    }

    public void limparCampos() {
        nomeTextField.setText("");
        emailTextField.setText("");
        cpfTextField.setText("");
        codAssessorTextField.setText("");
        codBancoTextField.setText("");
        agenciaTextField.setText("");
        contaTextField.setText("");
    }

    public void editarPessoal() {
        if (editButton.getText() == "Editar") {
            // iniciar a edição
            if (table1.getSelectedRow() > -1) {
                int r = table1.getSelectedRow();
                editingId = pessoas.get(r).getId();

                System.out.println("Edição iniciada para: " + pessoas.get(r).getNome());
                System.out.println("Edição iniciada para Id: " + pessoas.get(r).getId());

                nomeTextField.setText(tableModel.getValueAt(r, 0).toString());
                emailTextField.setText(tableModel.getValueAt(r, 1).toString());
                cpfTextField.setText(tableModel.getValueAt(r, 2).toString());
                codAssessorTextField.setText(tableModel.getValueAt(r, 3).toString());
                codBancoTextField.setText(tableModel.getValueAt(r, 4).toString());
                agenciaTextField.setText(tableModel.getValueAt(r, 5).toString());
                contaTextField.setText(tableModel.getValueAt(r, 6).toString());
                editButton.setText("Completar edição");
                insertButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Para editar é necessário selecionar a tabela antes.", "Selecionar para editar", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            // concluir a edição
            String sql = "UPDATE pessoal SET ";
            sql = sql + "nome = '" + nomeTextField.getText() + "', ";
            sql = sql + "email = '" + emailTextField.getText() + "', ";
            sql = sql + "cpf = '" + cpfTextField.getText() + "', ";
            sql = sql + "codigo_assessor = '" + codAssessorTextField.getText() + "', ";
            sql = sql + "codigo_banco = '" + codBancoTextField.getText() + "', ";
            sql = sql + "agencia = '" + agenciaTextField.getText() + "', ";
            sql = sql + "conta = '" + contaTextField.getText() + "' ";
            sql = sql + "WHERE id = " + editingId;
            System.out.println("Executando: " + sql);

            try {
                jdbcTemplate.execute(sql);

                limparCampos();
                preencherTabelaSelectAll();
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
        preencherTabelaSelectAll();
    }

    public void deletarPessoal() {
        if (table1.getSelectedRow() > -1) {

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
                    System.out.println("DELETANDO... " + rList[i]);

                    int r = rList[i]; //table1.getSelectedRow();
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
                limparCampos();
                preencherTabelaSelectAll();
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
            String sql = "INSERT INTO pessoal (nome, email, cpf, codigo_assessor, codigo_banco, agencia, conta) VALUES( ";
            sql = sql + "'" + nomeTextField.getText() + "', ";
            sql = sql + "'" + emailTextField.getText() + "', ";
            sql = sql + "'" + cpfTextField.getText() + "', ";
            sql = sql + "'" + codAssessorTextField.getText() + "', ";
            sql = sql + "'" + codBancoTextField.getText() + "', ";
            sql = sql + "'" + agenciaTextField.getText() + "', ";
            sql = sql + "'" + contaTextField.getText() + "');";
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
