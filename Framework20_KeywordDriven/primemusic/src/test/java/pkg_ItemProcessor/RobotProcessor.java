package pkg_ItemProcessor;
import org.apache.log4j.Logger;
import pkg_global.GlobalObjects;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class RobotProcessor extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(RobotProcessor.class);

    public static void ProcessRobotItem(String sTcIndex, String sTcAction, String sTcParticular){
        myLog.info("Log: TCProcessor : " + sTcIndex + " " + sTcAction + " " + sTcParticular);

        switch (sTcAction){
            case "send_keys_robot":
                try{
                    Thread.sleep(2000);
                    utilRobot                       = new Robot();
                    utilRobot.keyPress(KeyEvent.VK_BACK_SPACE);
                    Thread.sleep(2000);
                    StringSelection stringSelection = new StringSelection(sTcParticular.replace("text=",""));
                    Clipboard clipboard             = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    utilRobot.keyPress(KeyEvent.VK_CONTROL);
                    utilRobot.keyPress(KeyEvent.VK_V);
                    utilRobot.keyRelease(KeyEvent.VK_V);
                    utilRobot.keyRelease(KeyEvent.VK_CONTROL);
                    Thread.sleep(2000);
                }catch (Exception robotEx){}
                break;
            case "key_press":
                // currently only Enter key is supported
                try{
                    Thread.sleep(2000);
                    utilRobot.keyPress(KeyEvent.VK_ENTER);
                    Thread.sleep(4000);
                }catch (Exception robotEx){}
                break;
//            case "click_nth_element":
//                break;
            default:
                break;
        }
        hmGlobalData.put(sTcIndex, "pass");
    }

}
