package com.cebeastern.attendease.ui;

import com.cebeastern.attendease.model.*;
import com.cebeastern.attendease.service.AttendanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendEaseApp extends JFrame {
    private final AttendanceService attendanceService = new AttendanceService();
    private final StudentSearch studentSearch = new StudentSearch();
    private final Administrator administrator = new Administrator("A-001", "AttendEase Admin", "admin", "admin123");
    private final ClassRecord classRecord = new ClassRecord("C-101", "IT101", "Computer Fundamentals", "BSIT-1", "Mon/Wed 8:00-9:00");
    private final CardLayout cardLayout = new CardLayout();
    private final DefaultTableModel studentTableModel = tableModel("Student No.", "Name", "Course", "Year Level", "Attendance");
    private final DefaultTableModel attendanceTableModel = tableModel("Date", "Student", "Class", "Status", "Remarks");
    private final DefaultTableModel personalAttendanceTableModel = tableModel("Date", "Class", "Status", "Remarks");
    private final JLabel studentCountValue = new JLabel("0");
    private final JLabel attendanceCountValue = new JLabel("0");
    private final JLabel presentCountValue = new JLabel("0");
    private final JLabel studentWelcome = new JLabel();
    private final JLabel studentProfile = new JLabel();
    private Student activeStudent;

    private static final Color INK = new Color(32, 43, 45);
    private static final Color MUTED = new Color(107, 120, 119);
    private static final Color GREEN = new Color(28, 111, 88);
    private static final Color PALE = new Color(244, 247, 243);
    private static final Color BORDER = new Color(222, 230, 224);

    public AttendEaseApp() {
        setTitle("AttendEase | Student Attendance Management");
        setSize(1120, 760);
        setMinimumSize(new Dimension(900, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loadDemoData();
        setLayout(cardLayout);
        add(buildLoginPage(), "login");
        add(buildAdministratorDashboard(), "admin");
        add(buildStudentDashboard(), "student");
        cardLayout.show(getContentPane(), "login");
    }

    private JPanel buildLoginPage() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(PALE);
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(34, 38, 34, 38)));

        GridBagConstraints layout = new GridBagConstraints();
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 1;
        layout.insets = new Insets(0, 0, 8, 0);
        JLabel brand = new JLabel("AttendEase");
        brand.setFont(new Font("SansSerif", Font.BOLD, 30));
        brand.setForeground(GREEN);
        loginPanel.add(brand, layout);

        layout.gridy++;
        JLabel subtitle = new JLabel("Student Attendance Management");
        subtitle.setForeground(MUTED);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 22, 0));
        loginPanel.add(subtitle, layout);

        JComboBox<String> roleChoice = new JComboBox<>(new String[]{"Administrator", "Student"});
        JTextField usernameField = new JTextField(22);
        JPasswordField passwordField = new JPasswordField(22);
        JButton signUpButton = new JButton("Sign Up as a student");
        signUpButton.setVisible(false);
        addLoginRow(loginPanel, layout, "Sign in as", roleChoice);
        addLoginRow(loginPanel, layout, "Username", usernameField);
        addLoginRow(loginPanel, layout, "Password", passwordField);

        layout.gridy++;
        layout.insets = new Insets(10, 0, 0, 0);
        JButton signInButton = primaryButton("Sign in");
        loginPanel.add(signInButton, layout);
        JLabel credentials = new JLabel("Demo: admin / admin123  |  renier / 1234");
        credentials.setForeground(MUTED);
        credentials.setFont(new Font("SansSerif", Font.PLAIN, 12));
        layout.gridy++;
        layout.insets = new Insets(14, 0, 0, 0);
        loginPanel.add(credentials, layout);
        layout.gridy++;
        layout.insets = new Insets(8, 0, 0, 0);
        loginPanel.add(signUpButton, layout);

        signInButton.addActionListener(event -> authenticate(
                (String) roleChoice.getSelectedItem(), usernameField.getText(), passwordField.getPassword()));
        roleChoice.addActionListener(event -> signUpButton.setVisible("Student".equals(roleChoice.getSelectedItem())));
        signUpButton.addActionListener(event -> showStudentSignUpDialog(usernameField));
        passwordField.addActionListener(event -> signInButton.doClick());
        page.add(loginPanel);
        return page;
    }

    private void addLoginRow(JPanel panel, GridBagConstraints layout, String label, JComponent field) {
        layout.gridy++;
        layout.gridwidth = 1;
        layout.weightx = 0;
        layout.insets = new Insets(0, 0, 6, 12);
        JLabel caption = new JLabel(label);
        caption.setForeground(INK);
        panel.add(caption, layout);
        layout.gridx = 1;
        layout.weightx = 1;
        layout.insets = new Insets(0, 0, 14, 0);
        panel.add(field, layout);
        layout.gridx = 0;
        layout.gridwidth = 2;
    }

    private void authenticate(String role, String username, char[] enteredPassword) {
        String password = new String(enteredPassword);
        if ("Administrator".equals(role) && administrator.login(username.trim(), password)) {
            refreshAdministratorDashboard();
            cardLayout.show(getContentPane(), "admin");
            return;
        }
        if ("Student".equals(role)) {
            for (Student student : studentSearch.getStudents()) {
                if (student.login(username.trim(), password)) {
                    activeStudent = student;
                    refreshStudentDashboard();
                    cardLayout.show(getContentPane(), "student");
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "The username or password is incorrect.", "Sign-in failed", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel buildAdministratorDashboard() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(PALE);
        page.add(buildHeader("Administrator Dashboard", "Manage students and daily attendance"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("Overview", buildOverviewTab());
        tabs.addTab("Students", buildStudentsTab());
        tabs.addTab("Attendance", buildAttendanceTab());
        tabs.addTab("Reports", buildReportsTab());
        page.add(tabs, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildHeader(String titleText, String subtitleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(18, 26, 18, 24)));
        JPanel titles = new JPanel(new GridLayout(0, 1, 0, 3));
        titles.setOpaque(false);
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(INK);
        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setForeground(MUTED);
        titles.add(title);
        titles.add(subtitle);
        header.add(titles, BorderLayout.WEST);
        JButton logout = new JButton("Sign out");
        logout.addActionListener(event -> signOut());
        header.add(logout, BorderLayout.EAST);
        return header;
    }

    private JPanel buildOverviewTab() {
        JPanel page = contentPanel();
        JPanel metrics = new JPanel(new GridLayout(1, 3, 14, 0));
        metrics.setOpaque(false);
        metrics.add(metricCard("REGISTERED STUDENTS", studentCountValue));
        metrics.add(metricCard("ATTENDANCE RECORDS", attendanceCountValue));
        metrics.add(metricCard("PRESENT TODAY", presentCountValue));
        page.add(metrics, BorderLayout.NORTH);

        JPanel recent = sectionPanel("Recent attendance");
        JTable table = createTable(attendanceTableModel);
        recent.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(recent, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildStudentsTab() {
        JPanel page = contentPanel();
        JPanel actions = new JPanel(new BorderLayout(12, 0));
        actions.setOpaque(false);
        JTextField search = new JTextField();
        search.putClientProperty("JTextField.placeholderText", "Search by name, number, or course");
        JButton addStudent = primaryButton("Register student");
        actions.add(search, BorderLayout.CENTER);
        actions.add(addStudent, BorderLayout.EAST);
        page.add(actions, BorderLayout.NORTH);
        JPanel tableSection = sectionPanel("Student directory");
        JTable table = createTable(studentTableModel);
        tableSection.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(tableSection, BorderLayout.CENTER);

        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                refreshStudentTable(search.getText());
            }
            public void insertUpdate(javax.swing.event.DocumentEvent event) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent event) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent event) { update(); }
        });
        addStudent.addActionListener(event -> showRegisterStudentDialog());
        return page;
    }

    private JPanel buildAttendanceTab() {
        JPanel page = contentPanel();
        JPanel recordPanel = sectionPanel("Record attendance");
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints layout = new GridBagConstraints();
        layout.gridx = 0;
        layout.gridy = 0;
        layout.weightx = 1;
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.insets = new Insets(5, 6, 8, 6);
        JComboBox<Student> studentChoice = new JComboBox<>();
        studentChoice.setName("Student");
        studentChoice.setRenderer((list, student, index, selected, focused) -> {
            JLabel label = new JLabel(student == null ? "" : student.getName() + "  |  " + student.getStudentNumber());
            label.setOpaque(true);
            label.setBackground(selected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(selected ? list.getSelectionForeground() : list.getForeground());
            label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            return label;
        });
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JComboBox<String> statusChoice = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        JTextField remarksField = new JTextField();
        addFormRow(form, layout, "Student", studentChoice);
        addFormRow(form, layout, "Date (YYYY-MM-DD)", dateField);
        addFormRow(form, layout, "Status", statusChoice);
        addFormRow(form, layout, "Remarks", remarksField);
        JButton recordButton = primaryButton("Save attendance");
        layout.gridy++;
        form.add(recordButton, layout);
        recordPanel.add(form, BorderLayout.NORTH);
        page.add(recordPanel, BorderLayout.WEST);

        JPanel records = sectionPanel("Attendance log");
        records.add(new JScrollPane(createTable(attendanceTableModel)), BorderLayout.CENTER);
        page.add(records, BorderLayout.CENTER);

        recordButton.addActionListener(event -> {
            Student selectedStudent = (Student) studentChoice.getSelectedItem();
            if (selectedStudent == null) {
                JOptionPane.showMessageDialog(this, "Register a student before recording attendance.");
                return;
            }
            try {
                LocalDate.parse(dateField.getText().trim());
            } catch (java.time.DateTimeException exception) {
                JOptionPane.showMessageDialog(this, "Enter the date as YYYY-MM-DD.");
                return;
            }
            attendanceService.recordAttendance(selectedStudent, classRecord, dateField.getText().trim(),
                    (String) statusChoice.getSelectedItem(), remarksField.getText().trim());
            remarksField.setText("");
            refreshAdministratorDashboard();
            JOptionPane.showMessageDialog(this, "Attendance saved.");
        });
        page.putClientProperty("studentChoice", studentChoice);
        return page;
    }

    private JPanel buildReportsTab() {
        JPanel page = contentPanel();
        JPanel report = sectionPanel("Attendance summary by student");
        JTextArea reportText = new JTextArea();
        reportText.setEditable(false);
        reportText.setFont(new Font("Monospaced", Font.PLAIN, 13));
        report.add(new JScrollPane(reportText), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh report");
        refresh.addActionListener(event -> reportText.setText(buildReportText()));
        report.add(refresh, BorderLayout.SOUTH);
        page.add(report, BorderLayout.CENTER);
        reportText.setText(buildReportText());
        return page;
    }

    private JPanel buildStudentDashboard() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(PALE);
        page.add(buildHeader("Student Dashboard", "Your attendance at a glance"), BorderLayout.NORTH);
        JPanel content = contentPanel();
        JPanel identity = sectionPanel("My profile");
        JPanel identityContent = new JPanel(new GridLayout(0, 1, 0, 8));
        identityContent.setOpaque(false);
        studentWelcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        studentWelcome.setForeground(INK);
        studentProfile.setForeground(MUTED);
        identityContent.add(studentWelcome);
        identityContent.add(studentProfile);
        identity.add(identityContent, BorderLayout.CENTER);
        content.add(identity, BorderLayout.NORTH);
        JPanel history = sectionPanel("Attendance history");
        history.add(new JScrollPane(createTable(personalAttendanceTableModel)), BorderLayout.CENTER);
        content.add(history, BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private void addFormRow(JPanel form, GridBagConstraints layout, String label, JComponent component) {
        layout.gridy++;
        form.add(new JLabel(label), layout);
        layout.gridy++;
        form.add(component, layout);
    }

    private JPanel contentPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(PALE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        return panel;
    }

    private JPanel sectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 15));
        heading.setForeground(INK);
        panel.add(heading, BorderLayout.NORTH);
        return panel;
    }

    private JPanel metricCard(String labelText, JLabel value) {
        JPanel card = new JPanel(new GridLayout(0, 1, 0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(MUTED);
        value.setFont(new Font("SansSerif", Font.BOLD, 28));
        value.setForeground(GREEN);
        card.add(label);
        card.add(value);
        return card;
    }

    private JButton primaryButton(String label) {
        JButton button = new JButton(label);
        button.setBackground(GREEN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return button;
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(222, 239, 230));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(247, 249, 247));
        return table;
    }

    private static DefaultTableModel tableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void showRegisterStudentDialog() {
        JTextField name = new JTextField();
        JTextField studentNumber = new JTextField();
        JTextField course = new JTextField();
        JTextField year = new JTextField();
        JTextField contact = new JTextField();
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Full name")); form.add(name);
        form.add(new JLabel("Student number")); form.add(studentNumber);
        form.add(new JLabel("Course")); form.add(course);
        form.add(new JLabel("Year level")); form.add(year);
        form.add(new JLabel("Contact")); form.add(contact);
        int result = JOptionPane.showConfirmDialog(this, form, "Register student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        if (name.getText().isBlank() || studentNumber.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Name and student number are required.");
            return;
        }
        if (findStudentByNumber(studentNumber.getText().trim()) != null) {
            JOptionPane.showMessageDialog(this, "That student number is already registered.");
            return;
        }
        String number = studentNumber.getText().trim();
        Student student = new Student("S-" + System.currentTimeMillis(), name.getText().trim(),
                number.toLowerCase(), "1234", number, course.getText().trim(), year.getText().trim(), contact.getText().trim());
        studentSearch.addStudent(student);
        refreshAdministratorDashboard();
        JOptionPane.showMessageDialog(this, "Student registered. Sign-in username: " + number.toLowerCase() + " | temporary password: 1234");
    }

    private void showStudentSignUpDialog(JTextField loginUsernameField) {
        JTextField name = new JTextField();
        JTextField studentNumber = new JTextField();
        JTextField course = new JTextField();
        JTextField year = new JTextField();
        JTextField contact = new JTextField();
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JPasswordField confirmPassword = new JPasswordField();
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Full name")); form.add(name);
        form.add(new JLabel("Student number")); form.add(studentNumber);
        form.add(new JLabel("Course")); form.add(course);
        form.add(new JLabel("Year level")); form.add(year);
        form.add(new JLabel("Contact")); form.add(contact);
        form.add(new JLabel("Username")); form.add(username);
        form.add(new JLabel("Password")); form.add(password);
        form.add(new JLabel("Confirm password")); form.add(confirmPassword);

        int result = JOptionPane.showConfirmDialog(this, form, "Student Sign Up",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String number = studentNumber.getText().trim();
        String accountName = username.getText().trim();
        String secret = new String(password.getPassword());
        if (name.getText().isBlank() || number.isBlank() || accountName.isBlank() || secret.isBlank()) {
            JOptionPane.showMessageDialog(this, "Name, student number, username, and password are required.");
            return;
        }
        if (!secret.equals(new String(confirmPassword.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }
        for (Student student : studentSearch.getStudents()) {
            if (student.getStudentNumber().equalsIgnoreCase(number)) {
                JOptionPane.showMessageDialog(this, "That student number is already registered.");
                return;
            }
            if (student.getUsername().equalsIgnoreCase(accountName)) {
                JOptionPane.showMessageDialog(this, "That username is already in use.");
                return;
            }
        }

        Student student = new Student("S-" + System.currentTimeMillis(), name.getText().trim(), accountName,
                secret, number, course.getText().trim(), year.getText().trim(), contact.getText().trim());
        studentSearch.addStudent(student);
        loginUsernameField.setText(accountName);
        JOptionPane.showMessageDialog(this, "Student account created. You can now sign in.");
    }

    private void refreshAdministratorDashboard() {
        refreshStudentTable("");
        refreshAttendanceTable(attendanceTableModel, null);
        studentCountValue.setText(String.valueOf(studentSearch.getStudents().size()));
        attendanceCountValue.setText(String.valueOf(attendanceService.getAllAttendance().size()));
        long presentToday = attendanceService.getAllAttendance().stream()
                .filter(record -> LocalDate.now().toString().equals(record.getAttendanceDate()))
                .filter(record -> "Present".equalsIgnoreCase(record.getStatus())).count();
        presentCountValue.setText(String.valueOf(presentToday));
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JPanel panel) {
                updateStudentChoice(panel);
            }
        }
    }

    private void updateStudentChoice(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComboBox<?> rawChoice && rawChoice.getItemCount() >= 0
                    && "Student".equals(rawChoice.getName())) {
                @SuppressWarnings("unchecked") JComboBox<Student> choice = (JComboBox<Student>) rawChoice;
                choice.removeAllItems();
                for (Student student : studentSearch.getStudents()) choice.addItem(student);
            }
            if (component instanceof Container child) updateStudentChoice(child);
        }
    }

    private void refreshStudentTable(String filter) {
        studentTableModel.setRowCount(0);
        String keyword = filter.toLowerCase().trim();
        for (Student student : studentSearch.getStudents()) {
            String searchable = (student.getName() + " " + student.getStudentNumber() + " " + student.getCourse()).toLowerCase();
            if (searchable.contains(keyword)) {
                studentTableModel.addRow(new Object[]{student.getStudentNumber(), student.getName(), student.getCourse(),
                        student.getYearLevel(), student.viewAttendanceSummary()});
            }
        }
    }

    private void refreshAttendanceTable(DefaultTableModel model, Student onlyStudent) {
        model.setRowCount(0);
        List<Attendance> records = new ArrayList<>(attendanceService.getAllAttendance());
        for (int index = records.size() - 1; index >= 0; index--) {
            Attendance attendance = records.get(index);
            Student student = findStudentByNumber(attendance.getStudentId());
            if (student == null || (onlyStudent != null && !onlyStudent.equals(student))) continue;
            model.addRow(new Object[]{attendance.getAttendanceDate(), student.getName(), classRecord.getSubjectCode(),
                    attendance.getStatus(), attendance.getRemarks()});
        }
    }

    private void refreshStudentDashboard() {
        studentWelcome.setText(activeStudent.getName());
        studentProfile.setText(activeStudent.getStudentNumber() + "  |  " + activeStudent.getCourse() + "  |  " + activeStudent.getYearLevel());
        personalAttendanceTableModel.setRowCount(0);
        List<Attendance> records = activeStudent.viewAttendance();
        for (int index = records.size() - 1; index >= 0; index--) {
            Attendance attendance = records.get(index);
            personalAttendanceTableModel.addRow(new Object[]{attendance.getAttendanceDate(), classRecord.getSubjectName(),
                    attendance.getStatus(), attendance.getRemarks()});
        }
    }

    private String buildReportText() {
        StringBuilder report = new StringBuilder("STUDENT                              PRESENT   ABSENT   LATE   RATE\n");
        report.append("------------------------------------------------------------------\n");
        for (Student student : studentSearch.getStudents()) {
            var summary = attendanceService.getAttendanceSummary(student.viewAttendance());
            double rate = attendanceService.calculateAttendancePercentage(student.viewAttendance());
            report.append(String.format("%-36s %4d      %4d    %4d   %5.1f%%\n", student.getName(),
                    summary.get("Present"), summary.get("Absent"), summary.get("Late"), rate));
        }
        if (studentSearch.getStudents().isEmpty()) report.append("No students registered.");
        return report.toString();
    }

    private void signOut() {
        activeStudent = null;
        cardLayout.show(getContentPane(), "login");
    }

    private void loadDemoData() {
        Student demoStudent1 = new Student("S-001", "Renier Languido", "renier", "1234",
                "2026-1001", "BSIT", "3rd Year", "09123456789");
        Student demoStudent2 = new Student("S-002", "Mark Melencion", "mark", "1234",
                "2026-1002", "BSIT", "3rd Year", "09123456780");
        studentSearch.addStudent(demoStudent1);
        studentSearch.addStudent(demoStudent2);
        LocalDate today = LocalDate.now();
        attendanceService.recordAttendance(demoStudent1, classRecord, today.minusDays(2).toString(), "Present", "On time");
        attendanceService.recordAttendance(demoStudent1, classRecord, today.minusDays(1).toString(), "Late", "Traffic");
        attendanceService.recordAttendance(demoStudent2, classRecord, today.minusDays(2).toString(), "Absent", "No excuse");
    }

    private Student findStudentByNumber(String studentNumber) {
        for (Student student : studentSearch.getStudents()) {
            if (student.getStudentNumber().equalsIgnoreCase(studentNumber)) return student;
        }
        return null;
    }

}
