package br.com.golfinvest.data.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private JPanel mainFrameRootPanel;
    private JButton openDBButon;
    private JTextField dbAddressTextField;
    private JButton createDBButton;
    private JPanel dbPanel;
    private JPanel buttonsPanel;
    private JLabel buttonsPanelLabel;
    private JButton pessoalButton;
    private JButton produtoButton;
    private JButton capitaoButton;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MainFrame(String title) {
        super();
        setTitle(title);
    }

    public void initComponents() {

        setSize(640, 480);


        System.out.println("Init Components MainFrame...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainFrameRootPanel);

        dbAddressTextField.setEditable(false);
        Font font = new Font("Serif", Font.BOLD, 15);
        dbAddressTextField.setFont(font);

//        buttonsPanel.setVisible(false);
        setLocationRelativeTo(null);
        regEvents();
        setVisible(true);

    }


    public void regEvents() {

        openDBButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                fc.setDialogTitle("Abrir o arquivo de Banco de Dados...");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Data Base File", "db");
                fc.setFileFilter(filter);
                fc.showOpenDialog(mainFrameRootPanel);
                String file = fc.getSelectedFile().getAbsolutePath();
                try {
                    openDataBase(file);
                    JOptionPane.showMessageDialog(null, "Banco de dados aberto com sucesso!", "Banco de dados", JOptionPane.INFORMATION_MESSAGE);
                    buttonsPanel.setVisible(true);
                } catch (Exception ex) {
                    buttonsPanel.setVisible(false);
                    JOptionPane.showMessageDialog(null, ex.getCause(), "Problemas em abrir o banco de dados", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        createDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create data base
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                fc.setDialogTitle("Pasta do Banco de Dados a ser criado...");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.showOpenDialog(mainFrameRootPanel);
                String file = fc.getSelectedFile().getPath();
                file = file + File.separator + "golf-data-manager.db";
                try {
                    openDataBase(file);
                    JOptionPane.showMessageDialog(null, "Banco de dados criado com sucesso!", "Banco de dados", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    buttonsPanel.setVisible(false);
                    JOptionPane.showMessageDialog(null, ex.getCause(), "Problemas em criar o banco de dados", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

                try {
                    createTables();
                    JOptionPane.showMessageDialog(null, "Tabelas e Triggers construídas com sucesso!", "Banco de dados - Schema", JOptionPane.INFORMATION_MESSAGE);
                    buttonsPanel.setVisible(true);
                } catch (FileNotFoundException ex) {
                    buttonsPanel.setVisible(false);
                    JOptionPane.showMessageDialog(null, ex.getCause(), "Banco de dados - Schema", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        pessoalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pessoal pessoal = new Pessoal(getTitle(), jdbcTemplate);
            }
        });

        produtoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Produtos produto = new Produtos(getTitle(), jdbcTemplate);
            }
        });

    }

    public void createTables() throws FileNotFoundException {
        if (!dbAddressTextField.getText().isEmpty()) {
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "init-db.sql");
            System.out.println(file.getPath());
            String ic;
            Scanner sc = new Scanner(file);//file to be scanned
            while (sc.hasNextLine()) {
                ic = sc.nextLine();
                System.out.println("antes " + ic + " depois");
                jdbcTemplate.execute(ic);
            }
            sc.close(); //closes the scanner
        } else {
            JOptionPane.showMessageDialog(null, "Para criar as tabelas, primeiro precisa criar ou abrir um arquivo de banco de dados.");
        }
    }

    public void openDataBase(String dbAddress) {
        dbAddressTextField.setText(null);
        String driverClassName = "org.sqlite.JDBC";
        String jdbcUrl = "jdbc:sqlite:" + dbAddress;
        // using DataSourceBuilder:
        DataSource dataSource = DataSourceBuilder.create().driverClassName(driverClassName).url(jdbcUrl).build();
        // and make the jdbcTemplate
        jdbcTemplate.setDataSource(dataSource);
        dbAddressTextField.setText(dbAddress);
//        buttonsPanel.setVisible(true);
    }


}
