import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.Scanner;

public class QueryUtilityFunctions {

    // Works
    public static boolean attribute_in_relation(Connection connection, String table, String attribute_type,
                                                String attribute_name, String attribute_value)
            throws SQLException {
        return attributes_in_table(connection, table, new String[]{attribute_type}, new String[]{attribute_name},
                new String[]{attribute_value});
    }

    // Works
    // Check if a set of attributes are all found in some row of a relation
    public static boolean attributes_in_table(Connection connection, String table, String[] attribute_types, String[] attribute_names,
                                              String[] attribute_values) throws SQLException {
        String query = "select ";
        for (int i = 0; i < attribute_names.length; i++) {
            query += attribute_names[i] + ",";
        }
        query = query.substring(0, query.length() - 1);
        query += " from " + table + " where ";

        for (int i = 0; i < attribute_values.length; i++) {
            query += attribute_names[i] + "=" + "? and ";
        }
        query = query.substring(0, query.length() - 4);

        PreparedStatement statement = connection.prepareStatement(query);
        for (int i =0 ; i<attribute_values.length; i++) {
            prepareStatementHelper(statement, i + 1, attribute_types[i], attribute_values[i]);
        }
        ResultSet rset = statement.executeQuery();
        return rset.next();
    }

    // Works
    public static void insert_into_table(Connection connection, String table, String[] attribute_types,
                                         String[] values) throws SQLException {
        if (values.length == 0)
            return;
        String query = "insert into " + table + " values (";
        for (int i =0 ; i<values.length; i++)
            query += "?, ";
        query = query.substring(0, query.length() - 2) + ")";
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i =0 ; i<values.length; i++)
            prepareStatementHelper(statement, i+1, attribute_types[i], values[i]);
        statement.executeUpdate();
    }

    private static void prepareStatementHelper(PreparedStatement statement, int col, String attribute_type,
                                               String value) throws SQLException {
        if (attribute_type.toLowerCase().equals("string"))
            statement.setString(col, value);
        else if (attribute_type.toLowerCase().equals("int"))
            statement.setInt(col, Integer.parseInt(value));
        else
            statement.setFloat(col , Float.parseFloat(value));
    }

    public static ResultSet get_table(Connection connection, String table) throws SQLException {
        String query = "select * from " + table;
        return connection.prepareStatement(query).executeQuery();
    }

    public static String[] address_registration(Connection connection, String addressType) throws SQLException {
        Scanner scan = new Scanner(System.in);

        System.out.println(addressType + " Information");
        System.out.println("-------------------");
        System.out.print("Street address: ");
        String street_address = scan.nextLine().toLowerCase();
        System.out.print("Postal code: ");
        String postal_code = scan.nextLine().toLowerCase();

        // Check if billing postal code already in database. If not, ask more questions and insert into area table
        if (!postal_code_in_database(connection, postal_code)) {
            System.out.print("City: ");
            String city = scan.nextLine().toLowerCase();
            System.out.print("Province/state: ");
            String province = scan.nextLine().toLowerCase();
            System.out.print("Country: ");
            String country = scan.nextLine().toLowerCase();
            QueryUtilityFunctions.insert_into_table
                    (connection, "area", new String[]{"string", "string", "string", "string"},
                            new String[]{postal_code, city, province, country});
        }
        // Insert into address table, unless tuple already exists
        if (!QueryUtilityFunctions.attributes_in_table(
                connection, "address", new String[]{"string", "string"},
                new String[]{"postal_code", "street_address"}, new String[]{postal_code, street_address})) {

            QueryUtilityFunctions.insert_into_table(connection, "address",
                    new String[]{"string", "string"}, new String[]{postal_code, street_address});
        }

        return new String[]{postal_code, street_address};
    }

    public static boolean username_taken(Connection connection, String username) throws SQLException {
        return QueryUtilityFunctions.attribute_in_relation(connection,"user_account", "string",
                "username", username);
    }

    public static boolean postal_code_in_database(Connection connection, String postal_code) throws SQLException {
        return QueryUtilityFunctions.attribute_in_relation(connection,"area", "String",
                "postal_code", postal_code);
    }

}
