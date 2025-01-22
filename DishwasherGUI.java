import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DishwasherGUI {
    private DishwasherList dishwasherList;
    private JFrame frame;
    private JTextField idField, typeField, modelField, enginePowerField, maxSpeedField, priceField, releaseDateField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat;
    private DatabaseConnection databaseConnection;

    public DishwasherGUI(DishwasherList dishwasherList) {
        this.dishwasherList = dishwasherList;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public void createAndShowGUI() {
        frame = new JFrame("Dishwasher Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Отступы между компонентами

        // Настройка полей ввода и меток
        idField = new JTextField(15);
        typeField = new JTextField(15);
        modelField = new JTextField(15);
        enginePowerField = new JTextField(15);
        maxSpeedField = new JTextField(15);
        releaseDateField = new JTextField(15);
        priceField = new JTextField(15);

        String[] labels = {"ID:", "Type:", "Model:", "Engine Power:", "Max Speed:", "Release Date (yyyy-MM-dd):", "Price:"};
        JTextField[] fields = {idField, typeField, modelField, enginePowerField, maxSpeedField, releaseDateField, priceField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            inputPanel.add(fields[i], gbc);
        }

        // Настройка кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Add Dishwasher");
        addButton.addActionListener(new AddButtonListener());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Dishwasher");
        updateButton.addActionListener(new UpdateButtonListener());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Dishwasher");
        deleteButton.addActionListener(new DeleteButtonListener());
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Type", "Model", "Engine Power", "Max Speed", "Release Date", "Price"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener(searchField));

        JButton saveButton = new JButton("Save to DB");
        saveButton.addActionListener(new SaveButtonListener());
        JButton loadButton = new JButton("Load from File");
        loadButton.addActionListener(new LoadButtonListener());

        JComboBox<String> sortComboBox = new JComboBox<>(
                new String[]{"Sort by Price", "Sort by Speed", "Sort by Engine Power"});
        sortComboBox.addActionListener(new SortComboBoxListener());

        JButton archiveButton = new JButton("Create Archive");
        archiveButton.addActionListener(new ArchiveButtonListener());

        actionPanel.add(searchField);
        actionPanel.add(searchButton);
        actionPanel.add(saveButton);
        actionPanel.add(loadButton);
        actionPanel.add(sortComboBox);
        actionPanel.add(archiveButton);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    }


    private void updateTable(List<Dishwasher> dishwashers) {
        tableModel.setRowCount(0);
        for (Dishwasher dishwasher : dishwashers) {
            tableModel.addRow(new Object[]{
                    dishwasher.getId(),
                    dishwasher.getType(),
                    dishwasher.getModel(),
                    dishwasher.getEnginePower(),
                    dishwasher.getMaxSpeed(),
                    dateFormat.format(dishwasher.getReleaseDate()),
                    dishwasher.getPrice()
            });
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(idField.getText());
                String type = typeField.getText();
                String model = modelField.getText();
                double enginePower = Double.parseDouble(enginePowerField.getText());
                double maxSpeed = Double.parseDouble(maxSpeedField.getText());
                Date releaseDate = dateFormat.parse(releaseDateField.getText());
                double price = Double.parseDouble(priceField.getText());

                Dishwasher dishwasher = new DishwasherBuilder()
                        .setId(id)
                        .setType(type)
                        .setModel(model)
                        .setEnginePower(enginePower)
                        .setMaxSpeed(maxSpeed)
                        .setReleaseDate(releaseDate)
                        .setPrice(price)
                        .build();

                dishwasherList.addDishwasher(dishwasher);
                tableModel.addRow(new Object[]{id, type, model, enginePower, maxSpeed, dateFormat.format(releaseDate), price});

                JOptionPane.showMessageDialog(frame, "Dishwasher added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                idField.setText("");
                typeField.setText("");
                modelField.setText("");
                enginePowerField.setText("");
                maxSpeedField.setText("");
                releaseDateField.setText("");
                priceField.setText("");
            } catch (NumberFormatException | ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        private final JTextField searchField;

        public SearchButtonListener(JTextField searchField) {
            this.searchField = searchField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String query = searchField.getText().toLowerCase();
            if (query.isEmpty()) {
                updateTable(dishwasherList.getDishwashers());
                return;
            }
            var filteredList = dishwasherList.getDishwashers().stream()
                    .filter(d -> d.toString().toLowerCase().contains(query))
                    .toList();
            if (filteredList.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No results found for: " + query, "Search", JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateTable(filteredList);
            }
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseConnection.getDatabaseFile()))) {
                for (Dishwasher dishwasher : dishwasherList.getDishwashers()) {
                    writer.write(dishwasher.toString());
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(frame, "Data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName().toLowerCase();

                try {
                    List<Dishwasher> dishwashers;
                    if (fileName.endsWith(".json")) {
                        dishwashers = JSONFileHandler.readFromFile(selectedFile.getPath());
                    } else if (fileName.endsWith(".xml")) {
                        dishwashers = XMLFileHandler.readFromFile(selectedFile.getPath());
                    } else if (fileName.endsWith(".txt")) {
                        dishwashers = FileHandler.readFromFile(selectedFile.getPath());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Unsupported file type. Please select a JSON or XML file.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    for (Dishwasher dishwasher : dishwashers) {
                        dishwasherList.addDishwasher(dishwasher);
                        tableModel.addRow(new Object[]{
                                dishwasher.getId(),
                                dishwasher.getType(),
                                dishwasher.getModel(),
                                dishwasher.getEnginePower(),
                                dishwasher.getMaxSpeed(),
                                dateFormat.format(dishwasher.getReleaseDate()),
                                dishwasher.getPrice()
                        });
                    }

                    JOptionPane.showMessageDialog(frame, "Data loaded successfully from: " + selectedFile.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "File selection canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    private class SortComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
            String selectedOption = (String) comboBox.getSelectedItem();
            if ("Sort by Price".equals(selectedOption)) {
                dishwasherList.sortByPrice();
            } else if ("Sort by Speed".equals(selectedOption)) {
                dishwasherList.sortBySpeed();
            } else if ("Sort by Engine Power".equals(selectedOption)) {
                dishwasherList.sortByEnginePower();
            }
            updateTable(dishwasherList.getDishwashers());
        }
    }
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a dishwasher to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            dishwasherList.removeDishwasher(id);
            tableModel.removeRow(selectedRow);

            JOptionPane.showMessageDialog(frame, "Dishwasher deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a dishwasher to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idField.getText());
                String type = typeField.getText();
                String model = modelField.getText();
                double enginePower = Double.parseDouble(enginePowerField.getText());
                double maxSpeed = Double.parseDouble(maxSpeedField.getText());
                Date releaseDate = dateFormat.parse(releaseDateField.getText());
                double price = Double.parseDouble(priceField.getText());

                Dishwasher updatedDishwasher = new DishwasherBuilder()
                        .setId(id)
                        .setType(type)
                        .setModel(model)
                        .setEnginePower(enginePower)
                        .setMaxSpeed(maxSpeed)
                        .setReleaseDate(releaseDate)
                        .setPrice(price)
                        .build();

                dishwasherList.getDishwashers().set(selectedRow, updatedDishwasher);

                tableModel.setValueAt(id, selectedRow, 0);
                tableModel.setValueAt(type, selectedRow, 1);
                tableModel.setValueAt(model, selectedRow, 2);
                tableModel.setValueAt(enginePower, selectedRow, 3);
                tableModel.setValueAt(maxSpeed, selectedRow, 4);
                tableModel.setValueAt(dateFormat.format(releaseDate), selectedRow, 5);
                tableModel.setValueAt(price, selectedRow, 6);

                JOptionPane.showMessageDialog(frame, "Dishwasher updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException | ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private class ArchiveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true); // Allow multiple file selection
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                String[] filePaths = new String[selectedFiles.length];
                for (int i = 0; i < selectedFiles.length; i++) {
                    filePaths[i] = selectedFiles[i].getAbsolutePath();
                }

                String archiveType = JOptionPane.showInputDialog(frame, "Enter archive type (zip/jar):", "zip");
                if ("zip".equalsIgnoreCase(archiveType)) {
                    try {
                        Archiver.createZipArchive("archive.zip", filePaths);
                        JOptionPane.showMessageDialog(frame, "ZIP archive created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error creating ZIP archive: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if ("jar".equalsIgnoreCase(archiveType)) {
                    try {
                        new Archiver().createJarArchive("archive.jar", filePaths);
                        JOptionPane.showMessageDialog(frame, "JAR archive created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error creating JAR archive: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid archive type. Please choose 'zip' or 'jar'.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "File selection canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}