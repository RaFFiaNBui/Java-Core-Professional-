import java.sql.*;

public class DBClass {

    private static Connection conn;
    private static Statement stmt;

    public static void main(String[] args)  {

        try {
            connection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            createDB();
            cleanAndFill();
            priceOfProduct("Product 555");
            priceOfProduct("Product 5555");
            changePrice("Product 555", 333);
            priceOfProduct("Product 555");
            fromPriceToPrice(330, 335);
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //создание подключения к базе данных
    private static void connection () throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
        stmt = conn.createStatement();
        System.out.println("База данных подключена!");
    }

    //отключение от базы данных
    private static void disconnect () throws SQLException {
        conn.close();
    }

    //создание таблицы товаров
    private static void createDB () throws SQLException {
        stmt.execute("CREATE TABLE IF not exists 'products' (" +
                "'good_id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'good_name' TEXT, " +
                "'good_price' INTEGER);");

        stmt.execute("CREATE TABLE IF not exists 'products' (" +
                "'good_id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'good_name' TEXT, " +
                "'good_price' INTEGER);");
        System.out.println("Таблица создана!");
    }

    //очищаем таблицу и заполняем ее  1000 товаров
    private static void cleanAndFill () throws SQLException {

        stmt.executeUpdate("DELETE FROM products");
        System.out.println("Таблица очищенна!");

        conn.setAutoCommit(false);
        for (int i = 1; i <1001; i++) {
            stmt.addBatch("INSERT INTO products (good_name, good_price) VALUES ('Product "+i+"', '"+i+"')");
        }
        stmt.executeBatch();
        conn.setAutoCommit(true);
        System.out.println("Таблица заполнена!");
    }

    //создание запроса цены по названию
    private static void priceOfProduct (String str) throws SQLException {

        ResultSet rs = stmt.executeQuery("SELECT good_price FROM products WHERE good_name = '"+str+"';");
        if(rs.next()) {
            System.out.println("Товар " + str + " стоит " + rs.getInt(1));
        } else {
            System.out.println("Товара " + str + " нет!");
        }
    }

    //изменение цены товара
    private static void changePrice (String name, Integer price) throws SQLException {

        stmt.executeUpdate("UPDATE products SET good_price = '"+price+"' WHERE good_name='"+name+"';");
        System.out.println("Цена товара " + name + " изменена!");
    }

    //выводим товары из ценового диапозона
    private static void fromPriceToPrice (int priceFrom, int priceTo) throws SQLException {

        ResultSet rs = stmt.executeQuery("SELECT good_name, good_price FROM products WHERE good_price BETWEEN '"+priceFrom+"' AND '"+priceTo+"';");

        while (rs.next()) {
            System.out.println(rs.getString("good_name") + " " + rs.getInt("good_price"));
        }
    }
}
