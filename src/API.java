
import java.sql.*;
import com.mysql.jdbc.Driver;

public class API {
    Connection connection;
    public void Connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/disney","root","123456");
        System.out.println("[INFO] Connected!");
    }

    public boolean Register(String UID,String PWD)  {
        try {
        Statement statement = connection.createStatement();
        int re =statement.executeUpdate("INSERT INTO users(uid,password) VALUES(\""+UID+"\",\""+PWD+"\")");
        statement.close();
        return (re>0);}catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean Login(String UID,String PWD){
        try {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT password FROM users WHERE uid=" + "\"" + UID + "\"");
        while (resultSet.next()){
            String pwd = resultSet.getString("password");
            if(pwd.equals(PWD)){
                statement.close();
                resultSet.close();
                return true;}
        }
            statement.close();
            resultSet.close();
        return false;}catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void SearchCharacters(String name)  {
        try {
        Statement statement=connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name,world_id from characters where name="+"\""+name+"\"");
        while (resultSet.next()){
            System.out.println("I am "+name+" from "+resultSet.getString("world_id"));
        }
        statement.close();
        resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return;
    }

    public void GetAllCharacters() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name,world_id from characters");
            while (resultSet.next()) {
                System.out.println("I am " + resultSet.getString("name") + " from " + resultSet.getString("world_id"));
            }
            statement.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }}

    public void PurchaseTicketTips(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name,price from ticket_type");
            System.out.println("这是一下几种票的类型：");
            while (resultSet.next()){
                System.out.println(resultSet.getString("name")+" "+"￥"+resultSet.getString("price"));
            }
            statement.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean PurchaseTickets(String user,String type,String stat){
        try{
Statement statement = connection.createStatement();
int resultSet = statement.executeUpdate("INSERT INTO tickets values (\""+user+"\",\""+type+"\",\""+stat+"\")");
if(resultSet>0){
    statement.close();
    return  true;
}
statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void SearchForOrders(String user){
        try {
            System.out.println("已经出票的订单:");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT type from tickets where status=\"PAID\" and owner=\""+user+"\"");
            while (resultSet.next()){
                System.out.println(resultSet.getString("type"));
            }
            statement.close();
            resultSet.close();
            System.out.println("没有出票的订单:");
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery("SELECT type from tickets where status=\"UNPAID\" and owner=\""+user+"\"");
            while (resultSet1.next()){
                System.out.println(resultSet1.getString("type"));
            }
            statement1.close();
            resultSet1.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SearchForSumMoney(String User){
        try {
        Statement statement1 = connection.createStatement();
        ResultSet resultSet1 = statement1.executeQuery("SELECT type from tickets where status=\"UNPAID\" and owner=\""+User+"\"");
        int money_sum = 0;
        while (resultSet1.next()){
            String type = resultSet1.getString("type");
            Statement statement2 = connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery("SELECT price from ticket_type where name="+"\""+type+"\"");
            while (resultSet2.next()){
                System.out.println(type+" "+resultSet2.getString("price"));
                money_sum+=Integer.parseInt(resultSet2.getString("price"));
            }
        }
        System.out.println("一共要支付"+money_sum+"￥");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean PayAllUnpaid(String user){
        try {
            Statement statement1 = connection.createStatement();
            int res = statement1.executeUpdate("UPDATE tickets set status=\"PAID\" where status=\"UNPAID\" AND owner=\""+user+"\"");
            statement1.close();
            return res>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
