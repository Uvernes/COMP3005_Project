Instructions for running this program

1) Using PgAdmin 4, create a database called “bookstore_database”

2) create all the tables using the DDL.txt file found in the SQL directory. 

3) Insert in the initial data for the database using the DML.txt file found in the SQL directory.

4) Create a new java project with an empty source file directory. For example. On IntelliJ, this would simply mean creating a new project.

5) Copy and paste all of the source files from the Github link above into the source file directory.

6) Setup the connection to bookstore_database. To do this, you must have a JDBC driver. Additionally, in the BookstoreApp, manually replace the user and password strings with what your username and password are (line 14).

7) Run the program using the bookstore the main method found in BookstoreApp.java 

Useful notes

To log in as a pre-existing customer, use:
username = “leo10” , password = “goat”

To log in as an owner, use:
username = “uvito5”, password = “abcde”

You may also register as a new customer and then sign into your newly created customer account.

Other note: The order and user relations in the database are called order_info and user_account, respectively. This is since Pg Admin 4 does not allow order or user as table names.
