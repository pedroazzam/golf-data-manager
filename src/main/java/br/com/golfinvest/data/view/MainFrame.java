package br.com.golfinvest.data.view;

import br.com.golfinvest.data.config.SpringContext;
import br.com.golfinvest.data.model.ActivationLogDAO;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
    String aws;
    String user;
    String pass;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MainFrame(String title, String aws, String user, String pass) {
        super();
        setTitle(title);
        this.aws = aws;
        this.user = user;
        this.pass = pass;

    }

    public void initComponents() {

        setSize(640, 480);


        System.out.println("Init Components MainFrame...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainFrameRootPanel);

        dbAddressTextField.setEditable(false);
        Font font = new Font("Serif", Font.BOLD, 15);
        dbAddressTextField.setFont(font);

        openDBButon.setEnabled(false);
        createDBButton.setEnabled(false);

        buttonsPanel.setVisible(false);
        setLocationRelativeTo(null);
        regEvents();
        setVisible(true);

        credentialValidation();

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

        capitaoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Capitao capitao = new Capitao(getTitle(), jdbcTemplate);
            }
        });


    }

    public void credentialValidation() {
        // Credential validation ------------------------------
        String title = getTitle();
        setTitle(title + " [...aguarde...]");
        System.out.println("Validating credential...");
        ActivationLogDAO ald = new ActivationLogDAO(aws, user, pass);
        boolean valid = true;
//        boolean valid = ald.validateCredential("golf");
//        ald.logRegister(valid);

        if (valid) {
            openDBButon.setEnabled(true);
            createDBButton.setEnabled(true);
            setTitle(title);
            System.out.println("Validation successful...");
        } else {
            setTitle(title + " [...não validado...] - tente novamente");
            System.out.println("Validation denied...");
        }
        // Credential validation ------------------------------
    }


    public void createTables() throws FileNotFoundException {
        if (!dbAddressTextField.getText().isEmpty()) {


//            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "init-db.sql");
//            System.out.println(file.getPath());
//            JOptionPane.showMessageDialog(null, file.getPath());
//
//            String ic;
//            Scanner sc = new Scanner(file);//file to be scanned
//            while (sc.hasNextLine()) {
//                ic = sc.nextLine();
//                System.out.println("antes " + ic + " depois");
//                jdbcTemplate.execute(ic);
//            }
//            sc.close(); //closes the scanner


            try {
                InputStream is = new ClassPathResource("init-db.sql").getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);   // add everything to StringBuilder
//                    JOptionPane.showMessageDialog(null, line);
                    System.out.println("antes " + line + " depois");
                    jdbcTemplate.execute(line);
                    // here you can have your logic of comparison.
                    if (line.toString().equals(".")) {
                        // do something
                    }
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, ioException.getCause());
                ioException.printStackTrace();
            }
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
        mainFrameRootPanel = new JPanel();
        mainFrameRootPanel.setLayout(new BorderLayout(0, 0));
        dbPanel = new JPanel();
        dbPanel.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
        mainFrameRootPanel.add(dbPanel, BorderLayout.NORTH);
        dbPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        openDBButon = new JButton();
        Font openDBButonFont = this.$$$getFont$$$(null, Font.BOLD, -1, openDBButon.getFont());
        if (openDBButonFont != null) openDBButon.setFont(openDBButonFont);
        openDBButon.setText("Abrir Banco de Dados");
        dbPanel.add(openDBButon, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dbAddressTextField = new JTextField();
        dbAddressTextField.setBackground(new Color(-3879731));
        Font dbAddressTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, -1, dbAddressTextField.getFont());
        if (dbAddressTextFieldFont != null) dbAddressTextField.setFont(dbAddressTextFieldFont);
        dbPanel.add(dbAddressTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        createDBButton = new JButton();
        Font createDBButtonFont = this.$$$getFont$$$(null, Font.BOLD, -1, createDBButton.getFont());
        if (createDBButtonFont != null) createDBButton.setFont(createDBButtonFont);
        createDBButton.setText("Criar Banco de Dados");
        dbPanel.add(createDBButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 50, 50, 50), -1, -1));
        mainFrameRootPanel.add(buttonsPanel, BorderLayout.CENTER);
        buttonsPanelLabel = new JLabel();
        Font buttonsPanelLabelFont = this.$$$getFont$$$(null, Font.BOLD, 18, buttonsPanelLabel.getFont());
        if (buttonsPanelLabelFont != null) buttonsPanelLabel.setFont(buttonsPanelLabelFont);
        buttonsPanelLabel.setHorizontalAlignment(0);
        buttonsPanelLabel.setText("Escolha a informação a ser preenchida no banco de dados");
        buttonsPanel.add(buttonsPanelLabel, new GridConstraints(0, 0, 6, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pessoalButton = new JButton();
        Font pessoalButtonFont = this.$$$getFont$$$(null, Font.BOLD, 18, pessoalButton.getFont());
        if (pessoalButtonFont != null) pessoalButton.setFont(pessoalButtonFont);
        pessoalButton.setText("Pessoal/Assessores");
        buttonsPanel.add(pessoalButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        produtoButton = new JButton();
        Font produtoButtonFont = this.$$$getFont$$$(null, Font.BOLD, 18, produtoButton.getFont());
        if (produtoButtonFont != null) produtoButton.setFont(produtoButtonFont);
        produtoButton.setText("Produto");
        buttonsPanel.add(produtoButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        capitaoButton = new JButton();
        Font capitaoButtonFont = this.$$$getFont$$$(null, Font.BOLD, 18, capitaoButton.getFont());
        if (capitaoButtonFont != null) capitaoButton.setFont(capitaoButtonFont);
        capitaoButton.setText("Capitão");
        buttonsPanel.add(capitaoButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return mainFrameRootPanel;
    }
}
