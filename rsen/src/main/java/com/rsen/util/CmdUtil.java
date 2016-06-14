package com.rsen.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robi on 2016-06-14 18:58.
 */
public class CmdUtil {
    private static Process process;

    /**
     * 判断程序是否在运行
     */
    public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        List<ActivityManager.RunningTaskInfo> list = getAllRunningTask(context);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equalsIgnoreCase(packageName)
                    && info.baseActivity.getPackageName().equalsIgnoreCase(packageName)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }


    /**
     * 检查APK是否安装
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查APK是否安装
     */
    public static boolean isAppInstalled(final Context context, final String packageName) {
        try {
            final PackageManager pm = context.getPackageManager();
            final PackageInfo info = pm.getPackageInfo(packageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 得到所有正在运行的任务
     */
    public static List<ActivityManager.RunningTaskInfo> getAllRunningTask(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);//得到正在运行的100个app
        return list;
    }

    /**
     * 得到所有正在运行的进程,
     */
    public static List<AppInfo> getAllRunningProcess(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses(); // 获得手机进程列表
        List<ResolveInfo> resolveInfos = loadAllInstallApps(context);//获取所有安装的APP,用于得到APP的标签.
        List<AppInfo> appInfos = new ArrayList<AppInfo>();//需要返回的数据

        for (ActivityManager.RunningAppProcessInfo info : infos) {
// 去除包含Android包名跟本包名的进程
            if (info.processName.indexOf("android") == -1
                    && info.processName.indexOf(context.getPackageName()) == -1) {
                AppInfo appInfo = new AppInfo();
                if (info.processName.lastIndexOf(":") == -1) {
                    appInfo.packageName = info.processName;
                } else {
                    appInfo.packageName = info.processName.substring(0, info.processName.lastIndexOf(":"));
                }
                appInfo.label = getPackageNameLabel(context, appInfo.packageName, resolveInfos);
                appInfo.packageName = info.processName;
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }

    /**
     * 返回所有安装的app, Intent.ACTION_MAIN
     * Intent.CATEGORY_LAUNCHER
     */
    public static List<ResolveInfo> loadAllInstallApps(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return context.getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public static List<PackageInfo> loadAllApps(Context context) {
        return context.getPackageManager().getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES);//包含卸载的程序
    }

    /**
     * 通过包名,获取标签名
     */
    public static String getPackageNameLabel(Context context, String packageName, List<ResolveInfo> resolveInfos) {
        String label = "";
        for (ResolveInfo info : resolveInfos) {
            if (info.activityInfo.packageName.equalsIgnoreCase(packageName)) {
                label = (String) info.activityInfo.loadLabel(context.getPackageManager());
            }
        }
        return label;
    }

    /**
     * 获取所有安装APP的信息
     */
    public static List<AppInfo> getAllAppInfo(Context context) {
        List<AppInfo> apps = new ArrayList<>();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo info : packages) {
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = info.applicationInfo.loadLabel(context.getPackageManager()).toString();
            tmpInfo.packageName = info.packageName;
            tmpInfo.versionName = info.versionName;
            tmpInfo.versionCode = info.versionCode;
            tmpInfo.appIcon = info.applicationInfo.loadIcon(context.getPackageManager());
            apps.add(tmpInfo);
        }
        return apps;
    }

    /**
     * 通过包名,获取标签名
     */
    public static String getPackageNameLabel(Context context, String packageName) {
        List<ResolveInfo> infos = loadAllInstallApps(context);
        String label = "";
        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equalsIgnoreCase(packageName)) {
                label = (String) info.activityInfo.loadLabel(context.getPackageManager());
            }
        }
        return label;
    }

    /**
     * 删除文件,文件夹,包含子文件
     */
    public static boolean deleteFiles(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
                return true;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFiles(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 杀进程
     *
     * @param packageName the package name
     */
    public static void killApp(String packageName) {
        initProcess();
        killProcess(packageName);
        close();
    }

    /**
     * 申请root权限
     */
    public static boolean initProcess() {
        if (process == null) {
            try {
                process = Runtime.getRuntime().exec("su");//申请root权限
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 杀进程, 调用此方法之前,请先调用initProcess
     */
    public static boolean killProcess(String packageName) {
        if (process != null) {
            OutputStream out = process.getOutputStream();//可以理解为,得到了键盘...
            String cmd = "am force-stop " + packageName + " \n";
            try {
                out.write(cmd.getBytes());//理解为:在键盘上输入 am force-stop ...
                out.flush();

                close();//
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static void close() {
        if (process != null) {
            try {
                process.getOutputStream().close();
                process = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用root权限 执行命令
     */
    public static int executeSuCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");//申请root权限
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());

// 部分手机Root之后Library path 丢失，导入library path可解决该问题
            dos.writeBytes("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            dos.writeBytes((cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();

            process.waitFor();//等待执行完毕
            int result = process.exitValue();
            process.destroy();
            return result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    /**
     * 使用root权限, 安装apk
     */
    public static boolean installApk(String filePath) {
        if (executeSuCmd("pm install -f -r " + filePath) != -1) {
            return true;
        }
        return false;
    }

    public static void startInstallApk(Context context, String filePath) {
        startInstallApk(context, new File(filePath));
    }


    /**
     * 使用root权限, 卸载安装apk
     */
    public static boolean uninstallApk(String packageName) {
        if (executeSuCmd("pm uninstall  " + packageName) != -1) {
            return true;
        }
        return false;
    }

    public static void startUninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }


    public static void startInstallApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void installApp(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW.HIDE");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    public static String installAPP(String filePath) {
        String[] args = {"pm", "install", "-r", filePath};
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write("\n".getBytes());
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    /**
     * 静默安装
     *
     * @param file
     * @return
     */
    public static boolean slientInstall(File file) {
        boolean result = false;
        Process process = null;
        OutputStream out = null;
        try {
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            dataOutputStream.writeBytes("chmod 777 " + file.getPath() + "\n");
            dataOutputStream.writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r " +
                    file.getPath());
// 提交命令
            dataOutputStream.flush();
// 关闭流操作
            dataOutputStream.close();
            out.close();
            int value = process.waitFor();

// 代表成功
            if (value == 0) {
                result = true;
            } else if (value == 1) { // 失败
                result = false;
            } else { // 未知情况
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取指定路径中,所有类型的文件
     */
    public static List<FileInfo> getAllFiles(String path, String ext, CallBack callback) {
        List<FileInfo> files = new ArrayList<>();
        File file = new File(path);
        try {
            for (File f : file.listFiles()) {
                if (f.isDirectory() && f.canRead()) {
                    callback.onCall(f.getAbsolutePath());
                    files.addAll(getAllFiles(f.getAbsolutePath(), ext, callback));
                } else if (f.isFile() && f.getName().endsWith(ext)) {
                    files.add(new FileInfo(f.getName(), f.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * 启动APP
     */
    public static void startApp(Context context, String packageName) {
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(LaunchIntent);
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(File fileFrom, File fileTo) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(fileFrom);
            output = new FileOutputStream(fileTo);
            int in = input.read();
            while (in != -1) {
                output.write(in);
                in = input.read();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 复制文件
    public static void copyFile2(File sourceFile, File targetFile)
            throws IOException {
// 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

// 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

// 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
// 刷新此缓冲的输出流
        outBuff.flush();

//关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
// 新建目标目录
        (new File(targetDir)).mkdirs();
// 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
// 源文件
                File sourceFile = file[i];
// 目标文件
                File targetFile = new
                        File(new File(targetDir).getAbsolutePath()
                        + File.separator + file[i].getName());
                copyFile2(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
// 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
// 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    public static class AppInfo implements Serializable {
        public String label = "";//标签,
        public String packageName = "";//包名
        public String processName = "";//进程名

        public String appName;
        public String versionName;
        public int versionCode;
        public Drawable appIcon;
    }

    public static class FileInfo {
        public String fileName = "";//文件名
        public String filePath = "";//文件完整路径

        public FileInfo() {
        }

        public FileInfo(String fileName, String filePath) {
            this.fileName = fileName;
            this.filePath = filePath;
        }
    }

    public abstract static class CallBack {
        public abstract void onCall(String string);
    }

}
