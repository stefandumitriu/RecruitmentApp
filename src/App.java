import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class App extends JFrame {
    private JButton adminPageButton;
    private JButton managerPageButton;
    private JButton profilePageButton;
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JList userList;
    private JList companyList;
    private JPanel contentPanel;
    private JPanel adminPagePanel;
    private JPanel managerPagePanel;
    private JPanel profilePagePanel;
    private JLabel companyListLabel;
    private JLabel userListLabel;
    private JLabel applicationLabel;
    private JList applicationList;
    private JButton appAcceptButton;
    private JButton appDeclineButton;
    private JPanel companyInfoPanel;
    private JLabel companyNameLabel;
    private JList departmentList;
    private JList departmentEmployeesList;
    private JLabel departmentsLabel;
    private JLabel departmentEmployeesLabel;
    private JButton getBudgetButton;
    private JTextField budgetTextField;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea informationArea;
    private JList educationList;
    private JList experienceList;
    private JLabel experienceLabel;
    private JLabel educationLabel;
    private JLabel informationLabel;
    private JComboBox<String> companyBox;
    private DefaultListModel<String> userListModel;
    private DefaultListModel<String> companyListModel;
    private DefaultListModel<String> applicationListModel;
    private DefaultListModel<String> departmentListModel;
    private DefaultListModel<String> departmentEmployeesListModel;
    private DefaultListModel<String> educationListModel;
    private DefaultListModel<String> experienceListModel;

    App() throws ResumeIncompleteException, InvalidDatesException, IOException {
        super("Application");
        Test.Parser();
        String[] companies = new String[Application.getInstance().getCompanies().size()];
        for (int i = 0; i < Application.getInstance().getCompanies().size(); i++) {
            companyBox.addItem(Application.getInstance().getCompanies().get(i).name);
        }
        companyBox.setSelectedIndex(0);
        this.setMinimumSize(new Dimension(800, 800));
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerPagePanel.setVisible(false);
        profilePagePanel.setVisible(false);
        companyInfoPanel.setVisible(false);
        this.pack();
        userListModel = new DefaultListModel<>();
        companyListModel = new DefaultListModel<>();
        applicationListModel = new DefaultListModel<>();
        departmentListModel = new DefaultListModel<>();
        departmentEmployeesListModel = new DefaultListModel<>();
        experienceListModel = new DefaultListModel<>();
        educationListModel = new DefaultListModel<>();
        popCompanyList(new ArrayList<>(Application.getInstance().companies));
        popUserList(new ArrayList<>(Application.getInstance().users));
        popApplicationList(Application.getInstance().getCompany((String) companyBox.getSelectedItem()).manager.applications);
        userList.setModel(userListModel);
        companyList.setModel(companyListModel);
        applicationList.setModel(applicationListModel);
        departmentList.setModel(departmentListModel);
        departmentEmployeesList.setModel(departmentEmployeesListModel);
        educationList.setModel(educationListModel);
        experienceList.setModel(experienceListModel);
        informationArea.setLineWrap(true);
        informationArea.setWrapStyleWord(true);
        adminPageButton.setSelected(true);
        adminPageButton.addActionListener(e -> {
            adminPagePanel.setVisible(true);
            managerPagePanel.setVisible(false);
            profilePagePanel.setVisible(false);
            companyInfoPanel.setVisible(false);
            departmentEmployeesListModel.clear();
            informationArea.setText("");
            experienceListModel.clear();
            educationListModel.clear();
        });
        managerPageButton.addActionListener(e -> {
            adminPagePanel.setVisible(false);
            managerPagePanel.setVisible(true);
            profilePagePanel.setVisible(false);
            companyInfoPanel.setVisible(false);
            departmentEmployeesListModel.clear();
            informationArea.setText("");
            experienceListModel.clear();
            educationListModel.clear();
        });
        profilePageButton.addActionListener(e -> {
            adminPagePanel.setVisible(false);
            managerPagePanel.setVisible(false);
            profilePagePanel.setVisible(true);
            companyInfoPanel.setVisible(false);
            departmentEmployeesListModel.clear();
            informationArea.setText("");
            experienceListModel.clear();
            educationListModel.clear();
        });
        appAcceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request<Job, Consumer> selRequest = null;
                for (Request<Job, Consumer> r : Application.getInstance().getCompany((String) companyBox.getSelectedItem()).manager.applications) {
                    String genString = r.getValue1().resume.userInfo.getName() + " " + r.getValue1().resume.userInfo.getSurname() + ": " +
                            String.format("%.2f", r.getScore()) + "@" + r.getKey().jobName;
                    if (genString.equals(applicationList.getSelectedValue().toString())) {
                        selRequest = r;
                        break;
                    }
                }
                Application.getInstance().getCompany((String) companyBox.getSelectedItem()).manager.applications.remove(selRequest);
                for (Company c : Application.getInstance().getCompanies()) {
                    ArrayList<Request<Job, Consumer>> newAppArray = new ArrayList<>();
                    for (Request<Job, Consumer> r : c.manager.applications) {
                        if (!r.getValue1().equals(selRequest.getValue1()) && !r.getKey().equals(selRequest.getKey())) {
                            newAppArray.add(r);
                        }
                    }
                    c.manager.applications = newAppArray;
                }
                popApplicationList(Application.getInstance().getCompany((String) companyBox.getSelectedItem()).manager.applications);
                if (Application.getInstance().users.contains(selRequest.getValue1())) {
                    try {
                        Application.getInstance().getCompany(selRequest.getKey().company).add(((User) selRequest.getValue1()).convert(),
                                Application.getInstance().getCompany(selRequest.getKey().company).departments.get(0));
                    } catch (ResumeIncompleteException resumeIncompleteException) {
                        resumeIncompleteException.printStackTrace();
                    }
                    Application.getInstance().users.remove(selRequest.getValue1());
                    popUserList(new ArrayList<>(Application.getInstance().users));
                    int idx = applicationList.getSelectedIndex();
                    if (idx != -1) {
                        applicationListModel.remove(idx);
                    }
                }
            }
        });
        companyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    adminPagePanel.setVisible(false);
                    managerPagePanel.setVisible(false);
                    profilePagePanel.setVisible(false);
                    companyInfoPanel.setVisible(true);
                    companyNameLabel.setText(companyListModel.get(index));
                    Company c = Application.getInstance().getCompany(companyListModel.get(index));
                    popDepartmentList(c.departments);
                }
            }
        });
        departmentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    Company c = Application.getInstance().getCompany(companyNameLabel.getText());
                    popEmployeesList(c.departments.get(index).employees);
                }
            }
        });
        getBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Company c = Application.getInstance().getCompany(companyNameLabel.getText());
                int index = departmentList.getSelectedIndex();
                budgetTextField.setText(String.format("%s has a budget of: %.2f", c.departments.get(index).getClass().getName(),
                        c.departments.get(index).getTotalSalaryBudget()));
            }
        });
        departmentList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                budgetTextField.setText("");
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] userName = searchField.getText().split(" ");
                searchField.setText("");
                Consumer c = Consumer.searchByName(userName[1]);
                if (c != null) {
                    String s = "";
                    s += "Name: " + c.resume.userInfo.getName() + " " + c.resume.userInfo.getSurname() + "\n" +
                            "Email: " + c.resume.userInfo.getEmail() + "\n" +
                            "Phone: " + c.resume.userInfo.getPhone() + "\n";
                    s += "Languages: [";
                    for (Map.Entry<String, String> lang : c.resume.userInfo.getLanguages().entrySet()) {
                        s += " " + lang.getKey() + "-" + lang.getValue() + " ";
                    }
                    s += "]\n";
                    informationArea.setText(s);
                    popEducationList(c.resume.education);
                    popExperienceList(c.resume.experience);
                } else {
                    informationArea.setText("");
                    educationListModel.clear();
                    experienceListModel.clear();
                }
            }
        });
        companyBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popApplicationList(Application.getInstance().getCompany((String) companyBox.getSelectedItem()).manager.applications);
            }
        });
    }

    public void popUserList(ArrayList<User> users) {
        userListModel.clear();
        for (User u : users) {
            userListModel.addElement(u.resume.userInfo.getName() + " " + u.resume.userInfo.getSurname());
        }
    }

    public void popCompanyList(ArrayList<Company> companyArrayList) {
        for (Company c : companyArrayList) {
            companyListModel.addElement(c.name);
        }
    }

    public void popApplicationList(ArrayList<Request<Job, Consumer>> applicationList) {
        applicationListModel.clear();
        for (Request<Job, Consumer> r : applicationList) {
            applicationListModel.addElement(r.getValue1().resume.userInfo.getName() + " " + r.getValue1().resume.userInfo.getSurname() + ": " +
                    String.format("%.2f", r.getScore()) + "@" + r.getKey().jobName);
        }
    }

    public void popDepartmentList(ArrayList<Department> departments) {
        departmentListModel.clear();
        for (Department d : departments) {
            departmentListModel.addElement(d.getClass().getName());
        }
    }

    public void popEmployeesList(ArrayList<Employee> employees) {
        departmentEmployeesListModel.clear();
        for (Employee e : employees) {
            departmentEmployeesListModel.addElement(e.resume.userInfo.getName() + " " + e.resume.userInfo.getSurname());
        }
    }

    public void popEducationList(ArrayList<Education> education) {
        educationListModel.clear();
        for (Education e : education) {
            educationListModel.addElement(e.institution + ": " + e.startYear.getYear() + " > " + e.gradYear.getYear() + "; GPA: " + e.gradGPA + "(" + e.level + ")");
        }
    }

    public void popExperienceList(ArrayList<Experience> experience) {
        experienceListModel.clear();
        for (Experience e : experience) {
            experienceListModel.addElement(e.position + "@" + e.company + ": " + e.startYear.getYear() + " > " + e.endYear.getYear());
        }
    }

    public static void main(String[] args) throws ResumeIncompleteException, InvalidDatesException, IOException {
        App app = new App();
        app.setVisible(true);
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(contentPanel, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(24, 90), null, 0, false));
        adminPagePanel = new JPanel();
        adminPagePanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(adminPagePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        companyListLabel = new JLabel();
        companyListLabel.setText("Companies");
        adminPagePanel.add(companyListLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userListLabel = new JLabel();
        userListLabel.setText("Users");
        adminPagePanel.add(userListLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        companyList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        companyList.setModel(defaultListModel1);
        adminPagePanel.add(companyList, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        userList = new JList();
        adminPagePanel.add(userList, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        managerPagePanel = new JPanel();
        managerPagePanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(managerPagePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        applicationLabel = new JLabel();
        applicationLabel.setText("Applications");
        managerPagePanel.add(applicationLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appDeclineButton = new JButton();
        appDeclineButton.setText("Decline");
        managerPagePanel.add(appDeclineButton, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appAcceptButton = new JButton();
        appAcceptButton.setText("Accept");
        managerPagePanel.add(appAcceptButton, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        applicationList = new JList();
        managerPagePanel.add(applicationList, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 100), null, 0, false));
        companyBox = new JComboBox();
        managerPagePanel.add(companyBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        profilePagePanel = new JPanel();
        profilePagePanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(profilePagePanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchField = new JTextField();
        profilePagePanel.add(searchField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        profilePagePanel.add(searchButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        informationArea = new JTextArea();
        profilePagePanel.add(informationArea, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        educationList = new JList();
        profilePagePanel.add(educationList, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        experienceList = new JList();
        profilePagePanel.add(experienceList, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        informationLabel = new JLabel();
        informationLabel.setText("Information");
        profilePagePanel.add(informationLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        educationLabel = new JLabel();
        educationLabel.setText("Education");
        profilePagePanel.add(educationLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        experienceLabel = new JLabel();
        experienceLabel.setText("Experience");
        profilePagePanel.add(experienceLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        companyInfoPanel = new JPanel();
        companyInfoPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(companyInfoPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        companyNameLabel = new JLabel();
        companyNameLabel.setText("Label");
        companyInfoPanel.add(companyNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        departmentList = new JList();
        companyInfoPanel.add(departmentList, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        departmentEmployeesList = new JList();
        companyInfoPanel.add(departmentEmployeesList, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        departmentsLabel = new JLabel();
        departmentsLabel.setText("Departments");
        companyInfoPanel.add(departmentsLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        departmentEmployeesLabel = new JLabel();
        departmentEmployeesLabel.setText("Employees");
        companyInfoPanel.add(departmentEmployeesLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        getBudgetButton = new JButton();
        getBudgetButton.setText("Get Total Budget");
        companyInfoPanel.add(getBudgetButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        budgetTextField = new JTextField();
        companyInfoPanel.add(budgetTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(menuPanel, new GridConstraints(0, 0, 2, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(24, 47), null, 0, false));
        adminPageButton = new JButton();
        adminPageButton.setText("Admin Page");
        menuPanel.add(adminPageButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(111, 60), null, 0, false));
        managerPageButton = new JButton();
        managerPageButton.setText("Manager Page");
        menuPanel.add(managerPageButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(123, 60), null, 0, false));
        profilePageButton = new JButton();
        profilePageButton.setText("Profile Page");
        menuPanel.add(profilePageButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(111, 60), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
