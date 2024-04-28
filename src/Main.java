import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class Main {
    public static final String[] validation ={
        "FULL","HALF","HOLIDAY","MONTHLY_VIP","PERMANENT_VIP","TWO_DAYS","YEARLY_VIP"
    };
    public static void Log(String info){
        System.out.println("[INFO] "+info);
    }

    public static void LogError(String warnings){
            System.out.println("[Error] "+warnings);
    }

    public static void ShowPrompt(String prompt){
        System.out.println("[Show Prompt] "+prompt);
    }
    public static String session_storage;
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("欢迎使用迪士尼管理系统!");
        API api = new API();
        api.Connect();
        boolean logged_in = false;
        Scanner scanner = new Scanner(System.in);
        while (!logged_in){
            System.out.println("[Show Prompt] 按1注册，按2登陆");
            String next = scanner.next();
            if(next.equals("1")){
                System.out.println("[Show Prompt] 请输入用户名:");
                String uname=scanner.next();
                System.out.println("[Show Prompt] 请输入密码:");
                String pwd = scanner.next();
                System.out.println("[Show Prompt] 请确认密码:");
                String pwd_config = scanner.next();
                if(pwd_config.equals(pwd)){
                    logged_in=api.Register(uname,pwd);
                    if(logged_in){
                        System.out.println("[INFO] 注册成功！");
                        session_storage=uname;
                        break;
                    }else{
                        System.out.println("[Error] 注册失败！");
                        continue;
                    }
                }else{
                    System.out.println("[Error] 两次输入密码不一致，请重新输入！");
                    continue;
                }
            } else if (next.equals("2")) {
                System.out.println("[Show Prompt] 请输入用户名:");
                String uname=scanner.next();
                System.out.println("[Show Prompt] 请输入密码:");
                String pwd = scanner.next();
                logged_in=api.Login(uname,pwd);
                if(logged_in){
                    System.out.println("[INFO] 登陆成功！");
                    session_storage = uname;
                    break;
                }else {
                    System.out.println("[Error] 登陆失败！");
                }
            }else{
                System.out.println("[Error] 无效的命令");
            }
        }
        while (true){
            ShowPrompt("按1搜索角色，按2购票，按3查询订单，按4支付订单，按q退出");
            String command = scanner.next();
            if(command.equals("1")){
                ShowPrompt("输入角色名称，如果输入'*'则显示所有角色");
                String kw = scanner.next();
                if(kw.equals("*")){
                    api.GetAllCharacters();
                }else{
                    api.SearchCharacters(kw);
                }
            } else if (command.equals("2")) {
                api.PurchaseTicketTips();
                ShowPrompt("请输入你想购买的类型：");
                String type = scanner.next();
                if(Arrays.asList(validation).contains(type)){
                    ShowPrompt("你是否需要支付吗？(y/n,n for default)");
                    String paid = scanner.next();
                    if(paid.equals("y")){
                        Log("支付成功");
                        if(api.PurchaseTickets(session_storage,type,"PAID")){
                            Log("出票成功");
                        }else {
                            LogError("创建订单失败");
                            continue;
                        }
                    }else{
                        if(api.PurchaseTickets(session_storage,type,"UNPAID")){
                            Log("创建订单成功");
                        }else{
                            LogError("创建订单失败");
                            continue;
                        }
                    }
                }else{
                    LogError("请输入正确的票类型");
                    continue;
                }
            }else if (command.equals("3")){
                api.SearchForOrders(session_storage);
            }else if (command.equals("4")){
                api.SearchForSumMoney(session_storage);
                ShowPrompt("是否要支付所有订单(y/n, n for default)");
                if(scanner.next().equals("y")){
if(api.PayAllUnpaid(session_storage)){
    Log("已经支付所有的订单");
}else{
    LogError("因为某些原因，支付失败，或者没有可支付的订单");
}
                }else{
Log("支付取消");
                }
            }else if(command.equals("q")){
api.connection.close();
break;
            }else{
                LogError("无效的命令！");
            }
        }

    }
}