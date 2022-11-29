package net.beeapm.agent.log;

import net.beeapm.agent.common.BeeUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用于BeeAPM的log组件还没初始化前的日志打印，日志内容输出到{user.home}/logs/bee-apm.log文件里，并输出到控制台中<br/>
 * 比如BeeAPM启动时候的日志输出<br/>
 * 其它地方请使用 {@link LogManager} 获取{@link LogImpl}对象进行日志打印<br/>
 *
 * @author yuan
 * @date 2019/12/19
 */
public class BeeLog {
    private static BufferedWriter writer;
    private static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void init() {
        if (writer != null) {
            return;
        }
        synchronized (BeeLog.class) {
            if (writer != null) {
                return;
            }
            try {
                String path = System.getProperty("user.home") + File.separator + "/logs/bee-apm.log";
                System.out.println("log path=" + path);
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(String msg) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(dateFmt.format(new Date())).append("] ").append(msg);
            write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String msg) {
        init();
        try {
            msg = msg + "\n";
            System.out.print(msg);
            writer.write(msg);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        BeeUtils.close(writer);
    }

}
