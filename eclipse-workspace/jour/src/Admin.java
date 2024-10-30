import javax.swing.*;
import conection.DBConnection;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Admin extends JPanel {

    private JTextField surnameField; 
    private JTextField nameField; 
    private JTextField patronymicField; 
    private JTextField groupField;
    private JTable table;
    private JTable groupTable;
    private DefaultTableModel tableModel; 
    private DefaultTableModel groupTableModel;

    public Admin() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(208, 215, 217));
        JLabel headerLabel = new JLabel("АДМІН");
        headerLabel.setFont(new Font("System", Font.BOLD, 36));
        headerPanel.add(headerLabel);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel curatorPanel = new JPanel();
        curatorPanel.setLayout(null);

        JLabel lastNameLabel = new JLabel("Прізвище");
        lastNameLabel.setFont(new Font("System", Font.PLAIN, 18));
        lastNameLabel.setBounds(61, 339, 100, 20);
        curatorPanel.add(lastNameLabel);

        surnameField = new JTextField();
        surnameField.setBounds(61, 376, 150, 26);
        curatorPanel.add(surnameField);

        JLabel firstNameLabel = new JLabel("Ім'я");
        firstNameLabel.setFont(new Font("System", Font.PLAIN, 18));
        firstNameLabel.setBounds(228, 339, 100, 20);
        curatorPanel.add(firstNameLabel);

        nameField = new JTextField(); 
        nameField.setBounds(228, 376, 150, 26);
        curatorPanel.add(nameField);

        JLabel patronymicLabel = new JLabel("По батькові");
        patronymicLabel.setFont(new Font("System", Font.PLAIN, 18));
        patronymicLabel.setBounds(395, 339, 100, 20);
        curatorPanel.add(patronymicLabel);

        patronymicField = new JTextField(); 
        patronymicField.setBounds(395, 376, 150, 26);
        curatorPanel.add(patronymicField);

        String[] columnNames = {"Прізвище", "Ім'я", "По батькові"};
        tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(61, 14, 696, 314);
        curatorPanel.add(tableScrollPane);

        JButton addButton = new JButton("Додати");
        addButton.setBounds(568, 375, 91, 26);
        addButton.addActionListener(e -> addCurator());
        curatorPanel.add(addButton);

        JButton deleteButton = new JButton("Видалити");
        deleteButton.setBounds(673, 375, 85, 26);
        deleteButton.addActionListener(e -> deleteCurator()); 
        curatorPanel.add(deleteButton);

        tabbedPane.addTab("Куратор", curatorPanel);

        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(null);

        String[] groupColumnNames = {"Група"};
        DefaultTableModel groupTableModel = new DefaultTableModel(groupColumnNames, 0);

        this.groupTableModel = new DefaultTableModel(groupColumnNames, 0);
        this.groupTable = new JTable(this.groupTableModel);

        JScrollPane groupTableScrollPane = new JScrollPane(this.groupTable);
        groupTableScrollPane.setBounds(37, 33, 314, 235);
        groupPanel.add(groupTableScrollPane);

        JLabel groupLabel = new JLabel("Група");
        groupLabel.setFont(new Font("System", Font.BOLD, 18));
        groupLabel.setBounds(37, 269, 100, 20);
        groupPanel.add(groupLabel);

        groupField = new JTextField(); 
        groupField.setBounds(37, 296, 162, 34);
        groupPanel.add(groupField);

        JButton addGroupButton = new JButton("Додати");
        addGroupButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		groupAdd();
        	}
        });
        addGroupButton.setBounds(37, 346, 100, 40);
        groupPanel.add(addGroupButton);

        JButton updateGroupButton = new JButton("Оновити");
        updateGroupButton.setBounds(142, 346, 100, 40);
        groupPanel.add(updateGroupButton);

        JButton deleteGroupButton = new JButton("Видалити");
        deleteGroupButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		deleteGroup();
        	}
        });
        deleteGroupButton.setBounds(260, 346, 100, 40);
        groupPanel.add(deleteGroupButton);

        JComboBox<String> teacherComboBox = new JComboBox<>();
        JComboBox<String> groupComboBox = new JComboBox<>();
        teacherComboBox.setBounds(444, 87, 150, 26);
        groupComboBox.setBounds(619, 87, 150, 26);
        groupPanel.add(teacherComboBox);
        groupPanel.add(groupComboBox);

        JLabel teacherLabel = new JLabel("Викладач");
        teacherLabel.setFont(new Font("System", Font.BOLD, 18));
        teacherLabel.setBounds(445, 60, 100, 20);
        groupPanel.add(teacherLabel);

        JLabel groupComboLabel = new JLabel("Група");
        groupComboLabel.setFont(new Font("System", Font.BOLD, 18));
        groupComboLabel.setBounds(620, 60, 100, 20);
        groupPanel.add(groupComboLabel);

        String[] groupAssignColumnNames = {"Викладач", "Група"};
        JTable groupAssignTable = new JTable(new Object[][]{}, groupAssignColumnNames);
        JScrollPane groupAssignScrollPane = new JScrollPane(groupAssignTable);
        groupAssignScrollPane.setBounds(444, 128, 322, 258);
        groupPanel.add(groupAssignScrollPane);

        JLabel assignCuratorLabel = new JLabel("Призначення куратора");
        assignCuratorLabel.setFont(new Font("System", Font.BOLD, 24));
        assignCuratorLabel.setBounds(444, 22, 300, 30);
        groupPanel.add(assignCuratorLabel);

        JButton backButton = new JButton("Назад");
        backButton.setBounds(665, 397, 103, 40);
        groupPanel.add(backButton);

        tabbedPane.addTab("Група", groupPanel);

        JPanel exportPanel = new JPanel();
        tabbedPane.addTab("Експорт студентів", exportPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        loadCurators();
        loadGroups();
    }

    public void addCurator() {
        String surname = surnameField.getText();
        String name = nameField.getText();
        String patronymic = patronymicField.getText();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO curator (surname, name, bat) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, surname);
            pst.setString(2, name);
            pst.setString(3, patronymic);
            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(null, "Куратора додано успішно");
                loadCurators(); 
            } else {
                JOptionPane.showMessageDialog(null, "Невдалося додати куратора");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Помилка бази даних: " + e.getMessage());
        }
    }

    public void deleteCurator() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String surname = table.getValueAt(selectedRow, 0).toString(); 
            String name = table.getValueAt(selectedRow, 1).toString(); 
            String patronymic = table.getValueAt(selectedRow, 2).toString(); 
            try {
                Connection con = DBConnection.getConnection();
                String sql = "DELETE FROM curator WHERE surname = ? AND name = ? AND bat = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, surname);
                pst.setString(2, name);
                pst.setString(3, patronymic);
                int rowCount = pst.executeUpdate();

                if (rowCount > 0) {
                    JOptionPane.showMessageDialog(null, "Куратора видалено успішно");
                    loadCurators(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Невдалося видалити куратора");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Помилка бази даних: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Будь ласка, виберіть куратора для видалення");
        }
    }

    public void loadCurators() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT surname, name, bat FROM curator";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                String surname = rs.getString("surname");
                String name = rs.getString("name");
                String patronymic = rs.getString("bat");
                tableModel.addRow(new Object[]{surname, name, patronymic});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Помилка завантаження кураторів: " + e.getMessage());
        }
    }

    
    public void groupAdd() {
        String nameGroup = groupField.getText();
        
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO `group` (nameGroup) VALUES (?)"; 
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, nameGroup);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(null, "Групу додано");
            } else {
                JOptionPane.showMessageDialog(null, "Невдалося додати групу");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Помилка бази даних: " + e.getMessage());
        }
    }
    
    public void deleteGroup() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String  groupName = table.getValueAt(selectedRow, 0).toString();
            try {
                Connection con = DBConnection.getConnection();
                String sql = "DELETE FROM group WHERE nameGroup = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, groupName);

                int rowCount = pst.executeUpdate();

                if (rowCount > 0) {
                    JOptionPane.showMessageDialog(null, "Видалення групи");
                    loadCurators(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Невдалося видалити групу");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Помилка бази даних: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Будь ласка, виберіть групу для видалення");
        }
    }
    
    public void loadGroups() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT nameGroup FROM `group`";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            groupTableModel.setRowCount(0); 

            while (rs.next()) {
                String groupName = rs.getString("nameGroup");
                groupTableModel.addRow(new Object[]{groupName});  
            }

            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Помилка при завантаженні груп: " + e.getMessage());
        }
    }

}
