# ProductsListApp

A web application to manage a product list, allowing to add, delete, and update items

![צילום מסך 2024-08-18 214323](https://github.com/user-attachments/assets/3cd6e638-9322-45d9-b3ee-ebaf7660c46d)
## Architecture
- Database (MySQL) - Comprises of Prodcuts table, Tags table and Product_tags table
- ProductsManager (Java Spring Boot) - A REST service which allows to read and write into the database
- User Interface (Angular) - Components: products list, form. Services: database interaction handler, components communicator

## Installation

- Run docker engine
- Run "docker compose up" in "service" folder
- The app will be available in http://localhost:4200/

By Tomer Fried
tomerfried96@gmail.com
