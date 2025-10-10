import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Exhibition Registration System for SALSA Dance Festival
 * Victoria University - Guild Office
 * 
 * This application manages participant registration with GUI and MS Access database integration
 */
public class ExhibitionRegistrationSystem extends JFrame {
    
    // Database connection components
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    
    // Database configuration
    private static final String DB_PATH = "src/exhibitionregistration/sql_setup_script.sql";
    private static final String DB_URL = "jdbc:ucanaccess://" + DB_PATH;
    
    // GUI Components
    private JTextField txtRegID;
    private JTextField txtName;
    private JComboBox<String> cmbDepartment;
    private JTextField txtPartner;
    private JTextField txtContact;
    private JTextField txtEmail;
    private JLabel lblImageDisplay;
    private JButton btnBrowseImage;
    
    // Image handling
    private String imagePath = "";
    private byte[] imageData = null;
    
    // Buttons
    private JButton btnRegister, btnSearch, btnUpdate, btnDelete, btnClear, btnExit;
    
    /**
     * Constructor - Initializes the GUI and database connection
     */
    public ExhibitionRegistrationSystem() {
        setTitle("SALSA Dance Festival - Participant Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initializeDatabase();
        initializeGUI();
        
        setVisible(true);
    }
    
    /**
     * Initialize database connection and create table if not exists
     */
    private void initializeDatabase() {
        try {
            // Load UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            
            // Establish connection
            conn = DriverManager.getConnection(DB_URL);
            
            // Create table if it doesn't exist
            createTableIfNotExists();
            
            System.out.println("Database connected successfully!");
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                "UCanAccess Driver not found!\nPlease ensure UCanAccess JAR files are in the classpath.",
                "Driver Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Database connection failed!\n" + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Create participants table if it doesn't exist
     */
    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Participants (" +
                "RegistrationID VARCHAR(20) PRIMARY KEY, " +
                "ParticipantName VARCHAR(100) NOT NULL, " +
                "Department VARCHAR(100) NOT NULL, " +
                "DancingPartner VARCHAR(100), " +
                "ContactNumber VARCHAR(20) NOT NULL, " +
                "EmailAddress VARCHAR(100) NOT NULL, " +
                "UniversityIDImage LONGBINARY)";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table verified/created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
    
    /**
     * Initialize GUI components
     */
    private void initializeGUI() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Form Panel
        JPanel formPanel = createFormPanel();
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create header panel with title and logo
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 51, 102));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("SALSA DANCE FESTIVAL");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitle = new JLabel("Victoria University - Participant Registration System");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(200, 220, 255));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(new Color(0, 51, 102));
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Create form panel with input fields
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(15, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Left panel - Input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Registration ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(createLabel("Registration ID:"), gbc);
        gbc.gridx = 1;
        txtRegID = createTextField();
        inputPanel.add(txtRegID, gbc);
        
        // Participant Name
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(createLabel("Participant Name:"), gbc);
        gbc.gridx = 1;
        txtName = createTextField();
        inputPanel.add(txtName, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(createLabel("Department:"), gbc);
        gbc.gridx = 1;
        String[] departments = {
            "Select Department",
            "Computer Science & IT",
            "Business & Management",
            "Engineering",
            "Arts & Humanities",
            "Health Sciences",
            "Law",
            "Education",
            "Natural Sciences"
        };
        cmbDepartment = new JComboBox<>(departments);
        cmbDepartment.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbDepartment.setPreferredSize(new Dimension(250, 35));
        inputPanel.add(cmbDepartment, gbc);
        
        // Dancing Partner
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(createLabel("Dancing Partner:"), gbc);
        gbc.gridx = 1;
        txtPartner = createTextField();
        inputPanel.add(txtPartner, gbc);
        
        // Contact Number
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(createLabel("Contact Number:"), gbc);
        gbc.gridx = 1;
        txtContact = createTextField();
        inputPanel.add(txtContact, gbc);
        
        // Email Address
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(createLabel("Email Address:"), gbc);
        gbc.gridx = 1;
        txtEmail = createTextField();
        inputPanel.add(txtEmail, gbc);
        
        // Right panel - Image display
        JPanel imagePanel = createImagePanel();
        
        formPanel.add(inputPanel, BorderLayout.CENTER);
        formPanel.add(imagePanel, BorderLayout.EAST);
        
        return formPanel;
    }
    
    /**
     * Create image display panel
     */
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout(10, 10));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 51, 102), 2),
            "University ID Image",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(0, 51, 102)
        ));
        imagePanel.setPreferredSize(new Dimension(250, 300));
        
        lblImageDisplay = new JLabel();
        lblImageDisplay.setPreferredSize(new Dimension(220, 220));
        lblImageDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        lblImageDisplay.setVerticalAlignment(SwingConstants.CENTER);
        lblImageDisplay.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        lblImageDisplay.setBackground(new Color(250, 250, 250));
        lblImageDisplay.setOpaque(true);
        lblImageDisplay.setText("<html><center>No Image<br>Selected</center></html>");
        lblImageDisplay.setForeground(Color.GRAY);
        
        btnBrowseImage = new JButton("Browse Image");
        btnBrowseImage.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBrowseImage.setBackground(new Color(0, 102, 204));
        btnBrowseImage.setForeground(Color.WHITE);
        btnBrowseImage.setFocusPainted(false);
        btnBrowseImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBrowseImage.addActionListener(e -> browseImage());
        
        imagePanel.add(lblImageDisplay, BorderLayout.CENTER);
        imagePanel.add(btnBrowseImage, BorderLayout.SOUTH);
        
        return imagePanel;
    }
    
    /**
     * Create button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));
        
        btnRegister = createButton("Register", new Color(34, 139, 34));
        btnSearch = createButton("Search", new Color(0, 102, 204));
        btnUpdate = createButton("Update", new Color(255, 140, 0));
        btnDelete = createButton("Delete", new Color(220, 20, 60));
        btnClear = createButton("Clear", new Color(128, 128, 128));
        btnExit = createButton("Exit", new Color(70, 70, 70));
        
        // Add action listeners
        btnRegister.addActionListener(e -> registerParticipant());
        btnSearch.addActionListener(e -> searchParticipant());
        btnUpdate.addActionListener(e -> updateParticipant());
        btnDelete.addActionListener(e -> deleteParticipant());
        btnClear.addActionListener(e -> clearFields());
        btnExit.addActionListener(e -> exitApplication());
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExit);
        
        return buttonPanel;
    }
    
    /**
     * Helper method to create styled labels
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    /**
     * Helper method to create styled text fields
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(250, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }
    
    /**
     * Helper method to create styled buttons
     */
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(110, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Browse and select image file
     */
    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select University ID Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePath = selectedFile.getAbsolutePath();
            
            try {
                // Read image file into byte array
                FileInputStream fis = new FileInputStream(selectedFile);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                imageData = baos.toByteArray();
                fis.close();
                
                // Display image in label
                displayImage(imageData);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error reading image file: " + e.getMessage(),
                    "Image Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Display image in label
     */
    private void displayImage(byte[] imgData) {
        try {
            if (imgData != null && imgData.length > 0) {
                ByteArrayInputStream bis = new ByteArrayInputStream(imgData);
                BufferedImage image = ImageIO.read(bis);
                
                // Scale image to fit label
                Image scaledImage = image.getScaledInstance(
                    lblImageDisplay.getWidth() - 10,
                    lblImageDisplay.getHeight() - 10,
                    Image.SCALE_SMOOTH);
                
                lblImageDisplay.setIcon(new ImageIcon(scaledImage));
                lblImageDisplay.setText("");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error displaying image: " + e.getMessage(),
                "Display Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate input fields
     */
    private boolean validateInput() {
        if (txtRegID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registration ID is required!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtRegID.requestFocus();
            return false;
        }
        
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Participant Name is required!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        
        if (cmbDepartment.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a Department!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            cmbDepartment.requestFocus();
            return false;
        }
        
        if (txtContact.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Contact Number is required!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtContact.requestFocus();
            return false;
        }
        
        // Validate contact number format
        if (!txtContact.getText().matches("^[0-9+\\-\\s()]+$")) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Contact Number format!\nUse numbers, +, -, (), or spaces only.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtContact.requestFocus();
            return false;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email Address is required!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        // Validate email format
        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Email Address format!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Register new participant
     */
    private void registerParticipant() {
        if (!validateInput()) {
            return;
        }
        
        String sql = "INSERT INTO Participants (RegistrationID, ParticipantName, " +
                     "Department, DancingPartner, ContactNumber, EmailAddress, " +
                     "UniversityIDImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtRegID.getText().trim());
            pst.setString(2, txtName.getText().trim());
            pst.setString(3, cmbDepartment.getSelectedItem().toString());
            pst.setString(4, txtPartner.getText().trim());
            pst.setString(5, txtContact.getText().trim());
            pst.setString(6, txtEmail.getText().trim());
            
            if (imageData != null) {
                pst.setBytes(7, imageData);
            } else {
                pst.setNull(7, Types.LONGVARBINARY);
            }
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                    "Participant registered successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                JOptionPane.showMessageDialog(this,
                    "Registration ID already exists!\nPlease use a different ID.",
                    "Duplicate Entry",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error registering participant:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            closeResources();
        }
    }
    
    /**
     * Search for participant by Registration ID
     */
    private void searchParticipant() {
        String regID = txtRegID.getText().trim();
        
        if (regID.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter Registration ID to search!",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            txtRegID.requestFocus();
            return;
        }
        
        String sql = "SELECT * FROM Participants WHERE RegistrationID = ?";
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, regID);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                txtName.setText(rs.getString("ParticipantName"));
                cmbDepartment.setSelectedItem(rs.getString("Department"));
                txtPartner.setText(rs.getString("DancingPartner"));
                txtContact.setText(rs.getString("ContactNumber"));
                txtEmail.setText(rs.getString("EmailAddress"));
                
                // Load and display image
                byte[] imgData = rs.getBytes("UniversityIDImage");
                if (imgData != null) {
                    imageData = imgData;
                    displayImage(imgData);
                } else {
                    lblImageDisplay.setIcon(null);
                    lblImageDisplay.setText("<html><center>No Image<br>Available</center></html>");
                    imageData = null;
                }
                
                JOptionPane.showMessageDialog(this,
                    "Participant found!",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No participant found with Registration ID: " + regID,
                    "Not Found",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error searching participant:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources();
        }
    }
    
    /**
     * Update existing participant record
     */
    private void updateParticipant() {
        if (!validateInput()) {
            return;
        }
        
        String sql = "UPDATE Participants SET ParticipantName=?, Department=?, " +
                     "DancingPartner=?, ContactNumber=?, EmailAddress=?, " +
                     "UniversityIDImage=? WHERE RegistrationID=?";
        
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, cmbDepartment.getSelectedItem().toString());
            pst.setString(3, txtPartner.getText().trim());
            pst.setString(4, txtContact.getText().trim());
            pst.setString(5, txtEmail.getText().trim());
            
            if (imageData != null) {
                pst.setBytes(6, imageData);
            } else {
                pst.setNull(6, Types.LONGVARBINARY);
            }
            
            pst.setString(7, txtRegID.getText().trim());
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                    "Participant record updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No participant found with Registration ID: " + txtRegID.getText(),
                    "Update Failed",
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error updating participant:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources();
        }
    }
    
    /**
     * Delete participant record
     */
    private void deleteParticipant() {
        String regID = txtRegID.getText().trim();
        
        if (regID.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter Registration ID to delete!",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            txtRegID.requestFocus();
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete participant with ID: " + regID + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Participants WHERE RegistrationID = ?";
            
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, regID);
                
                int result = pst.executeUpdate();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Participant deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No participant found with Registration ID: " + regID,
                        "Delete Failed",
                        JOptionPane.WARNING_MESSAGE);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting participant:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            } finally {
                closeResources();
            }
        }
    }
    
    /**
     * Clear all input fields
     */
    private void clearFields() {
        txtRegID.setText("");
        txtName.setText("");
        cmbDepartment.setSelectedIndex(0);
        txtPartner.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        lblImageDisplay.setIcon(null);
        lblImageDisplay.setText("<html><center>No Image<br>Selected</center></html>");
        imagePath = "";
        imageData = null;
        txtRegID.requestFocus();
    }
    
    /**
     * Close application
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
    
    /**
     * Close database resources
     */
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new ExhibitionRegistrationSystem());
    }
}