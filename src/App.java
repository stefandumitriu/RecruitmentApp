import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

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
    private DefaultListModel<String> userListModel;
    private DefaultListModel<String> companyListModel;
    private DefaultListModel<String> applicationListModel;

    App() throws ResumeIncompleteException, InvalidDatesException, IOException {
        super("Application");
        Test.Parser();
        this.setMinimumSize(new Dimension(400, 400));
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerPagePanel.setVisible(false);
        profilePagePanel.setVisible(false);
        this.pack();
        userListModel = new DefaultListModel<>();
        companyListModel = new DefaultListModel<>();
        applicationListModel = new DefaultListModel<>();
        popCompanyList(new ArrayList<>(Application.getInstance().companies));
        popUserList(new ArrayList<>(Application.getInstance().users));
        popApplicationList(Application.getInstance().getCompany("Google").manager.applications);
        userList.setModel(userListModel);
        companyList.setModel(companyListModel);
        applicationList.setModel(applicationListModel);
        adminPageButton.setSelected(true);
        adminPageButton.addActionListener(e -> {
            adminPagePanel.setVisible(true);
            managerPagePanel.setVisible(false);
            profilePagePanel.setVisible(false);
        });
        managerPageButton.addActionListener(e -> {
            adminPagePanel.setVisible(false);
            managerPagePanel.setVisible(true);
            profilePagePanel.setVisible(false);
        });
        profilePageButton.addActionListener(e -> {
            adminPagePanel.setVisible(false);
            managerPagePanel.setVisible(false);
            profilePagePanel.setVisible(true);
        });
        appAcceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request<Job, Consumer> selRequest = null;
                for (Request<Job, Consumer> r : Application.getInstance().getCompany("Google").manager.applications) {
                    String genString = r.getValue1().resume.userInfo.getName() + " " + r.getValue1().resume.userInfo.getSurname() + ": " +
                            String.format("%.2f", r.getScore()) + "@" + r.getKey().jobName;
                    if (genString.equals(applicationList.getSelectedValue().toString())) {
                        selRequest = r;
                        break;
                    }
                }
                Application.getInstance().getCompany("Google").manager.applications.remove(selRequest);
                ArrayList<Request<Job, Consumer>> newAppArray = new ArrayList<>();
                for (Request<Job, Consumer> r : Application.getInstance().getCompany("Google").manager.applications) {
                    if (!r.getValue1().equals(selRequest.getValue1()) && !r.getKey().equals(selRequest.getKey())) {
                        newAppArray.add(r);
                    }
                }
                Application.getInstance().getCompany("Google").manager.applications = newAppArray;
                popApplicationList(new ArrayList<>(Application.getInstance().getCompany("Google").manager.applications));
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
        contentPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
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
        managerPagePanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(managerPagePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        applicationLabel = new JLabel();
        applicationLabel.setText("Applications");
        managerPagePanel.add(applicationLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appDeclineButton = new JButton();
        appDeclineButton.setText("Decline");
        managerPagePanel.add(appDeclineButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appAcceptButton = new JButton();
        appAcceptButton.setText("Accept");
        managerPagePanel.add(appAcceptButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        applicationList = new JList();
        managerPagePanel.add(applicationList, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 100), null, 0, false));
        profilePagePanel = new JPanel();
        profilePagePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(profilePagePanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
