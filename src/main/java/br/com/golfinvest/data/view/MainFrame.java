package br.com.golfinvest.data.view;

import br.com.golfinvest.data.model.ActivationLogDAO;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class MainFrame extends JFrame {
    private PessoalFrame pessoal;
    private ProdutosFrame produto;
    private CapitaoFrame capitao;

    private JPanel mainFrameRootPanel;
    private JButton openDBButon;
    private JTextField dbAddressTextField;
    private JButton createDBButton;
    private JPanel dbPanel;
    private JPanel buttonsPanel;
    private JButton pessoalButton;
    private JButton produtoButton;
    private JButton capitaoButton;
    private JPanel contentPanel;
    private JButton sairButton;
    private JLabel byEmailLabel;
    private JLabel byWebLabel;
    private JPanel contactPanel;
    String aws;
    String user;
    String pass;
    private String emailAddress = "pedro@pedroazzam.me";
    private String webAddress = "https://pedroazzam.me/";


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

        setSize(1300, 900);


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

        // Setting image icon
        Image imgDB = new ImageIcon(
                this.getClass().getResource("/db.png")).getImage();
        setIconImage(imgDB);

        //Developed by
        byWebLabel.setText("<html><a href=" + webAddress + " style=color:rgb(0,0,0);>pedroazzam.me</a></html>");
        byWebLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        byWebLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(webAddress));
                } catch (URISyntaxException | IOException ex) {
                    // ...
                }
            }
        });

        byEmailLabel.setText("<html><a href=# style=color:rgb(0,0,0);>" + emailAddress + "</a></html>");
        byEmailLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        byEmailLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().mail(new URI("mailto:" + emailAddress + "?subject=Contact"));
                } catch (URISyntaxException | IOException ex) {
                    // ...
                }
            }
        });

        setExtendedState(Frame.MAXIMIZED_BOTH);
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
                int result = fc.showOpenDialog(mainFrameRootPanel);

                if (result == 0) {
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
            }
        });

        pessoalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pessoal == null || pessoal.isClosed()) {
                    pessoal = new PessoalFrame(getTitle(), jdbcTemplate);

//                    Dimension contentPaneSize = getContentPane().getSize();
//                    Dimension jInternalFrameSize = pessoal.getSize();
//                    pessoal.setLocation((contentPaneSize.width - jInternalFrameSize.width) / 3,
//                            (contentPaneSize.height - jInternalFrameSize.height) / 3);

                    Dimension contentPaneSize = contentPanel.getSize();
                    pessoal.setSize(contentPaneSize.width, (int) (contentPaneSize.height * 0.9));
                    pessoal.setLocation(0, 0);

                    contentPanel.add(pessoal);
                }
            }
        });

        produtoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (produto == null || produto.isClosed()) {
                    produto = new ProdutosFrame(getTitle(), jdbcTemplate);

                    Dimension contentPaneSize = getContentPane().getSize();
                    Dimension jInternalFrameSize = produto.getSize();
                    produto.setLocation((contentPaneSize.width - jInternalFrameSize.width) / 3,
                            (contentPaneSize.height - jInternalFrameSize.height) / 3);

                    contentPanel.add(produto);
                }
            }
        });

        capitaoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (capitao == null || capitao.isClosed()) {
                    capitao = new CapitaoFrame(getTitle(), jdbcTemplate);

                    Dimension contentPaneSize = getContentPane().getSize();
                    Dimension jInternalFrameSize = capitao.getSize();
                    capitao.setLocation((contentPaneSize.width - jInternalFrameSize.width) / 3,
                            (contentPaneSize.height - jInternalFrameSize.height) / 3);

                    contentPanel.add(capitao);
                }
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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
                    // here you can have your logic of comparison.
                    if (line.toString().substring(0, 2).equals("--")) {
                        System.out.println("Command Disabled: " + line);
                    } else {
                        System.out.println("Command Enabled: " + line);
                        jdbcTemplate.execute(line);
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
        dbPanel.setLayout(new GridLayoutManager(2, 2, new Insets(20, 20, 10, 20), -1, -1));
        dbPanel.setBackground(new Color(-6242860));
        mainFrameRootPanel.add(dbPanel, BorderLayout.NORTH);
        dbPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        openDBButon = new JButton();
        Font openDBButonFont = this.$$$getFont$$$(null, Font.BOLD, 16, openDBButon.getFont());
        if (openDBButonFont != null) openDBButon.setFont(openDBButonFont);
        openDBButon.setText("Abrir Banco de Dados");
        dbPanel.add(openDBButon, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dbAddressTextField = new JTextField();
        dbAddressTextField.setBackground(new Color(-8679521));
        Font dbAddressTextFieldFont = this.$$$getFont$$$(null, Font.BOLD, 18, dbAddressTextField.getFont());
        if (dbAddressTextFieldFont != null) dbAddressTextField.setFont(dbAddressTextFieldFont);
        dbPanel.add(dbAddressTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        createDBButton = new JButton();
        Font createDBButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, createDBButton.getFont());
        if (createDBButtonFont != null) createDBButton.setFont(createDBButtonFont);
        createDBButton.setText("Criar Banco de Dados");
        dbPanel.add(createDBButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(4, 1, new Insets(30, 50, 20, 50), -1, -1));
        buttonsPanel.setBackground(new Color(-14531487));
        mainFrameRootPanel.add(buttonsPanel, BorderLayout.WEST);
        pessoalButton = new JButton();
        Font pessoalButtonFont = this.$$$getFont$$$(null, Font.BOLD, 18, pessoalButton.getFont());
        if (pessoalButtonFont != null) pessoalButton.setFont(pessoalButtonFont);
        pessoalButton.setIcon(new ImageIcon(getClass().getResource("/people-100x100.png")));
        pessoalButton.setText("Pessoal/Assessores");
        buttonsPanel.add(pessoalButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        produtoButton = new JButton();
        Font produtoButtonFont = this.$$$getFont$$$(null, Font.BOLD, 18, produtoButton.getFont());
        if (produtoButtonFont != null) produtoButton.setFont(produtoButtonFont);
        produtoButton.setIcon(new ImageIcon(getClass().getResource("/product-100x100.png")));
        produtoButton.setText("Produto");
        buttonsPanel.add(produtoButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        capitaoButton = new JButton();
        Font capitaoButtonFont = this.$$$getFont$$$(null, Font.BOLD, 18, capitaoButton.getFont());
        if (capitaoButtonFont != null) capitaoButton.setFont(capitaoButtonFont);
        capitaoButton.setIcon(new ImageIcon(getClass().getResource("/po-100x100.png")));
        capitaoButton.setText("Capitão");
        buttonsPanel.add(capitaoButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 31), null, 0, false));
        sairButton = new JButton();
        Font sairButtonFont = this.$$$getFont$$$(null, Font.BOLD, 18, sairButton.getFont());
        if (sairButtonFont != null) sairButton.setFont(sairButtonFont);
        sairButton.setIcon(new ImageIcon(getClass().getResource("/exit-100x100.png")));
        sairButton.setText("Sair");
        buttonsPanel.add(sairButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.setBackground(new Color(-14408925));
        mainFrameRootPanel.add(contentPanel, BorderLayout.CENTER);
        contactPanel = new JPanel();
        contactPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        contactPanel.setBackground(new Color(-10461088));
        contentPanel.add(contactPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contactPanel.add(spacer1);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Courier New", Font.ITALIC, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16777216));
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(4);
        label1.setText("developed by Pedro Azzam");
        contactPanel.add(label1);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Courier New", Font.ITALIC, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-16777216));
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("web:");
        contactPanel.add(label2);
        byWebLabel = new JLabel();
        Font byWebLabelFont = this.$$$getFont$$$("Courier New", Font.ITALIC, 12, byWebLabel.getFont());
        if (byWebLabelFont != null) byWebLabel.setFont(byWebLabelFont);
        byWebLabel.setForeground(new Color(-16777216));
        byWebLabel.setHorizontalAlignment(4);
        byWebLabel.setHorizontalTextPosition(2);
        byWebLabel.setText("Label");
        contactPanel.add(byWebLabel);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Courier New", Font.ITALIC, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-16777216));
        label3.setHorizontalAlignment(4);
        label3.setHorizontalTextPosition(4);
        label3.setText("email:");
        contactPanel.add(label3);
        byEmailLabel = new JLabel();
        Font byEmailLabelFont = this.$$$getFont$$$("Courier New", Font.ITALIC, 12, byEmailLabel.getFont());
        if (byEmailLabelFont != null) byEmailLabel.setFont(byEmailLabelFont);
        byEmailLabel.setForeground(new Color(-16777216));
        byEmailLabel.setHorizontalAlignment(4);
        byEmailLabel.setHorizontalTextPosition(2);
        byEmailLabel.setText("Label");
        contactPanel.add(byEmailLabel);
        final Spacer spacer2 = new Spacer();
        contentPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
