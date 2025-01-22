# Dishwasher's Manager 
**Dishwasher's Manager** is a desktop application developed using Java and the Swing library for managing a database of dishwashers. The application provides an intuitive graphical interface that allows you to perform key operations with data, such as adding, editing, deleting, sorting and searching.

### Main functions:
1. **Adding new records**
The user can add new dishwashers by specifying parameters such as ID, type, model, motor power, maximum speed, production date and price.

2. **Editing records**
The ability to update information about the selected dishwasher directly from the table.

3. **Delete records**
Allows you to delete selected records from the database.

4. **Search**
A search through the database is implemented. The user can enter a keyword, and the application will display only those records that contain this word.

5. **Sorting**
It is possible to sort records by price, maximum speed or engine power.

6. **Saving and loading data**
- Saving data to a text, XML or JSON file.
- Loading data from external files with the specified formats.

7. **Archiving**
It is planned to implement a data archiving function.

8. **Database integration**
Using a Singleton approach to connect to and work with the database.

### Technical details:
- **Development language**: Java
- **Graphical interface**: Swing
- **Architecture**:
- The program uses the MVC approach to separate logic, data and interface.
- Data is stored in the `DishwasherList` object, and interaction with the database is implemented through the `DatabaseConnection` class.

- **File formats**:
- Support for text, XML and JSON formats for working with files.

- **Modular structure**:
Each functional element of the application is allocated to a separate module or class, which ensures ease of support and expansion.

### Target audience:
The project will be useful for dishwasher manufacturers, distributors, home appliance stores and anyone who needs convenient management of large amounts of data on household appliances.

### Advantages:
- Ease of use due to a clear interface.
- Flexibility in working with data.
- Extensibility for adding new functions (for example, connecting to cloud databases or integrating with other systems).

**Dishwasher's Manager** is an effective solution for managing information about dishwashers, which simplifies working with large amounts of data.
