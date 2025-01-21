import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DishwasherGUI {
    private DishwasherList dishwasherList;
    private JFrame frame;
    private JTextField idField, typeField, modelField, enginePowerField, maxSpeedField, priceField, releaseDateField;
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
        frame.setSize(500, 400);

        JPanel inputPanel = new JPanel(new GridLayout(8, 2));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Type:"));
        typeField = new JTextField();
        inputPanel.add(typeField);

        inputPanel.add(new JLabel("Model:"));
        modelField = new JTextField();
        inputPanel.add(modelField);

        inputPanel.add(new JLabel("Engine Power:"));
        enginePowerField = new JTextField();
        inputPanel.add(enginePowerField);

        inputPanel.add(new JLabel("Max Speed:"));
        maxSpeedField = new JTextField();
        inputPanel.add(maxSpeedField);

        inputPanel.add(new JLabel("Release Date (yyyy-MM-dd):"));
        releaseDateField = new JTextField();
        inputPanel.add(releaseDateField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        JButton addButton = new JButton("Add Dishwasher");
        addButton.addActionListener(new AddButtonListener());
        inputPanel.add(addButton);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Type", "Model", "Engine Power", "Max Speed", "Release Date", "Price"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JPanel actionPanel = new JPanel();
        JButton saveButton = new JButton("Save to File");
        saveButton.addActionListener(new SaveButtonListener());
        JButton loadButton = new JButton("Load from File");
        loadButton.addActionListener(new LoadButtonListener());
        JButton sortButton = new JButton("Sort by Price");
        sortButton.addActionListener(new SortButtonListener());
        JButton archiveButton = new JButton("Create Archive");
        archiveButton.addActionListener(new ArchiveButtonListener());

        actionPanel.add(saveButton);
        actionPanel.add(loadButton);
        actionPanel.add(sortButton);
        actionPanel.add(archiveButton);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.NORTH);

        frame.setVisible(true);
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
            try {
                dishwasherList.getDishwashers().clear();
                tableModel.setRowCount(0);

                File file = DatabaseConnection.getInstance().getDatabaseFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length != 7) {
                            throw new IllegalArgumentException("Invalid data format: " + line);
                        }

                        int id = Integer.parseInt(parts[0].trim());
                        String type = parts[1].trim();
                        String model = parts[2].trim();
                        double enginePower = Double.parseDouble(parts[3].trim());
                        double maxSpeed = Double.parseDouble(parts[4].trim());
                        Date releaseDate = dateFormat.parse(parts[5].trim());
                        double price = Double.parseDouble(parts[6].trim());

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
                    }
                }

                JOptionPane.showMessageDialog(frame, "Data loaded from file.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (ParseException | IOException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private class SortButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dishwasherList.sortByPrice();
            tableModel.setRowCount(0);
            for (Dishwasher dishwasher : dishwasherList.getDishwashers()) {
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
            JOptionPane.showMessageDialog(frame, "Data sorted by price.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class ArchiveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, "Archiving not implemented yet.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
