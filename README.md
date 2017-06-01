# RJcente
[![a](https://img.shields.io/badge/最新版本-点击查询-orange.svg)](http://jcenter.bintray.com/com/rsen/rsen/)
[![b](https://img.shields.io/badge/最后更新-2016--1--1-brightgreen.svg)](http://jcenter.bintray.com/com/rsen/rsen/)
[![c](https://img.shields.io/badge/最新版本-1.1.0-yellow.svg)](http://jcenter.bintray.com/com/rsen/rsen/)
[![d](https://img.shields.io/badge/com.rsen:rsen:-1.1.0-blue.svg)](http://jcenter.bintray.com/com/rsen/rsen/)

RSen常用公共类,发布到Jcenter

## 使用方法
```groovy
//2015-12-15 17:20:54 
compile 'com.rsen:rsen:0.0.2'

//2015-12-16 16:52:12
compile 'com.rsen:rsen:0.1.0'

//2016-1-1 12:52:12
compile 'com.rsen:rsen:1.1.0'
```


## 使用介绍
暂无

## 项目推荐
### [常用工具类](https://github.com/h4de5ing/AndroidCommon)
- 每一个Android开发者在日常开发中都会积累一些自己的代码片段
- 目的：
       * 1.将常用功能模块做成工具类
       * 2.封装Android系统api,简化api的使用
       * 3.收集一些高效的正确的代码片段避免下次踩坑
       * 4.尽量少依赖第三方
- 能力一般,水平有限,难免有Bug,如果有任何问题,请<a href="https://github.com/h4de5ing/AndroidCommon/issues">反馈</a>
- 如果你有更好的代码,请提交<a href="https://github.com/h4de5ing/AndroidCommon/pulls">Pull request</a>
 
* 感谢各位的star,你们的支持是我继续的动力,好了,本着多一点真诚,少一点套路的原则,完善一下文档
* 为了让更多人用上这个库,现在推出Eclipse <a href="https://github.com/h4de5ing/AndroidCommon/blob/master/com.code19.library-0.07.jar">lib下载</a>

```
调用方法如此简单：
调用结果 = 类名.方法名( 参数 )
AppUtils.getAppName(MainActivity.this,com.code19.androidcommon);
```
   
## library Module中的类：

- AppUtils.java 应用工具类
    * getAppName 获取应用名称
    * getAppIcon 获取应用图标
    * getAppDate 获取应用更新日期
    * getAppSize 获取应用大小
    * getAppApk 获取应用apk文件
    * getAppVersionName 获取应用版本名称
    * getAppVersionCode 获取应用版本号
    * getAppInstaller 获取应用的安装市场
    * getAppPackageName 获取应用包名
    * hasPermission 是否有权限
    * isInstalled 应用是否安装
    * installApk 安装应用
    * uninstallApk 卸载应用
    * isSystemApp 是否是系统应用
    * isServiceRunning 服务是否在运行
    * stopRunningService 停止服务
    * getNumCores 获取Cpu内核数
    * killProcesses 结束进程
    * runScript 运行脚本
    * getRootPermission 获得root权限

- BitmapUtils.java Bitmap工具类
   * decodeFile 解析文件为bitmap
   * getImageThumbnail 获取图片缩略图
        
- CacheUtils.java 缓存工具类
    * setCache 设置缓存
    * getCache 获取缓存

- CipherUtils.java 密码工具类
    * encode(String input) 获取字符串md5值
    * encode(InputStream in) 获取输入流的md5值
    * base64Encode Base64加密
    * base64Decode Base64解密
    * XorEncode 异或加密
    * XorDecode 异或解密

- CoordinateTransformUtil.java GPS坐标转换工具
    * 百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换的工具
    * bd09towgs84 百度坐标系(BD-09)转WGS坐标(百度坐标纬度,百度坐标经度),WGS84坐标数组
    * wgs84tobd09 WGS坐标转百度坐标系(BD-09)(WGS84坐标系的经度,WGS84坐标系的纬度),百度坐标数组
    * gcj02tobd09 火星坐标系(GCJ-02)转百度坐标系(BD-09)(火星坐标经度,火星坐标纬度),百度坐标数组
    * bd09togcj02 百度坐标系(BD-09)转火星坐标系(GCJ-02)(百度坐标纬度,百度坐标经度),火星坐标数组
    * wgs84togcj02 WGS84转GCJ02(火星坐标系)(WGS84坐标系的经度,WGS84坐标系的纬度),火星坐标数组
    * gcj02towgs84 GCJ02(火星坐标系)转GPS84(火星坐标系的经度,火星坐标系纬度),WGS84坐标数组
    * transformlat 纬度转换
    * transformlng 经度转换
    * out_of_china 判断是否在国内，不在国内不做偏移

- DateUtil.java 日期工具类
    * formatDataTime 格式化日期时间
    * formatDate 格式化日期
    * formatTime 格式化时间
    * formatDateCustom 自定义格式的格式化日期时间
    * string2Date 将时间字符串转换成Date
    * getTime 获取系统时间
    * subtractDate 计算两个时间差
    * getDateAfter 得到几天后的时间
    * getWeekOfMonth 获取当前时间为本月的第几周
    * getDayOfWeek 获取当前时间为本周的第几天

- DensityUtil.java 屏幕工具类
    * dip2px dp转像素
    * px2dip 像素转dp
    * px2sp 像素转sp 
    * sp2px sp转像素
    * getScreenW 获取屏幕宽度
    * getScreenH 获取屏幕高度
    * getScreenRealSize 获取屏幕的真实高度
    * getStatusBarH 获取状态栏高度
    * getNavigationBarrH 获取导航栏高度
    
- DeviceUtils.java 设备信息工具
    * getAndroidID 获取AndroidID
    * getIMEI 获取设备IMEI码
    * getIMSI 获取设备IMSI码
    * getWifiMacAddr 获取MAC地址
    * getIP 获取网络IP地址(优先获取wifi地址)
    * getWifiIP 获取WIFI连接下的ip地址
    * getGPRSIP 获取GPRS连接下的ip地址
    * getSerial 获取设备序列号
    * getSIMSerial 获取SIM序列号
    * getPhoneNumber 获取手机号码(未获取成功)
    * getMNC 获取网络运营商 46000,46002,46007 中国移动,46001 中国联通,46003 中国电信
    * getCarrier 获取网络运营商：中国电信,中国移动,中国联通
    * getModel 获取硬件型号
    * getBuildBrand 获取编译厂商
    * getBuildHost 获取编译服务器主机
    * getBuildTags 获取描述Build的标签
    * getBuildTime 获取系统编译时间
    * getBuildUser 获取系统编译作者
    * getBuildVersionRelease 获取编译系统版本(5.1)
    * getBuildVersionCodename 获取开发代号
    * getBuildVersionIncremental 获取源码控制版本号  
    * getBuildVersionSDK 获取编译的SDK
    * getBuildID 获取修订版本列表(LMY47D)
    * getSupportedABIS CPU指令集
    * getManufacturer 获取硬件制造厂商
    * getBootloader 获取系统启动程序版本号
    * getScreenDisplayID 
    * getDisplayVersion 获取系统版本号
    * getLanguage 获取语言
    * getCountry 获取国家
    * getOSVersion 获取系统版本:5.1.1
    * getGSFID 获取GSF序列号
    * getBluetoothMAC 获取蓝牙地址
    * getPsuedoUniqueID Android设备物理唯一标识符
    * getFingerprint 构建标识,包括brand,name,device,version.release,id,version.incremental,type,tags这些信息
    * getHardware 获取硬件信息
    * getProduct 获取产品信息
    * getDevice 获取设备信息
    * getBoard 获取主板信息
    * getRadioVersion 获取基带版本(无线电固件版本 Api14以上)
    * getUA 获取的浏览器指纹(User-Agent)
    * getDensity 获取得屏幕密度
    * getGoogleAccounts 获取google账号


- FileUtils.java  文件工具类
    * closeIO 关闭IO流
    * isFileExist 文件是否存在
    * writeFile 将字符串写入到文件
    * readFile 从文件中读取字符串
    * copyFileFast 快速复制
    * shareFile 分享文件
    * zip zip压缩
    * unzip zip解压
    * formatFileSize 格式化文件大小
    * Stream2File 将输入流写入到文件
    * createFolder 创建文件夹
    * createFolder 创建文件夹(支持覆盖已存在的同名文件夹)
    * getFolderName 获取文件夹名称
    * deleteFile 删除目录下的文件
    * openImage 打开图片
    * openVideo 打开视频
    * openURL 打开URL

- ImageUtils.java 图片工具类
    * calculateInSampleSize 计算图片的压缩比率
    * getPictureDegree 获取图片的角度
    * rotaingImageView 旋转图片
    * decodeScaleImage 加载图片并压缩
    * getRoundedCornerBitmap 获取圆角图片
    //* decodeUriAsBitmap 解析URL流为图片
    * bitmap2File bitmap存为文件
    * compressImage 质量压缩
    * compressFixBitmap 固定大小压缩 
    
- JsonUtils.java Json工具类(需要依赖Gson 2.0以上)
    * toJson 对象转json
    * fromJson json转对象
    * mapToJson Map转为JSONObject
    * collection2Json 集合转换为JSONArray
    * object2Json Object对象转换为JSONArray
    * string2JSONObject json字符串生成JSONObject对象
    
- L.java 日志工具
    * init 初始化日志开关和TAG(默认日志为开,TAG为"ghost")
    * v VERBOSE 
    * d DEBUG
    * i INFO
    * w WARN
    * e ERROR
    * a ASSERT
    * json 输出json
    * xml 输出xml
    
    
- NetUtils.java 网络工具
    * getNetworkType 获取网络类型
    * getNetworkTypeName 获取网络名称
    * isConnected 检查网络状态
    * isNetworkAvailable 网络可用性
    * isWiFi 是否wifi
    * openNetSetting 打开网络设置界面
    * setWifiEnabled 设置wifi状态
    * getWifiScanResults 获取wifi列表
    * getScanResultsByBSSID 过滤扫描结果
    * getWifiConnectionInfo 获取wifi连接信息
    
- SPUtils.java SharedPreferences工具
    * setSP 存储SharedPreferences值
    * getSp 获取SharedPreferences值
    * cleanAllSP 清除所有的SP值

- StringUtils.java 字符串工具
    * getChsAscii 汉字转成ASCII码
    * convert 单字解析
    * getSelling 词组解析
    * parseEmpty 将null转化为""
    * isEmpty 是否是空字符串
    * chineseLength 中文长度
    * strLength 字符串长度
    * subStringLength 获取指定长度的字符所在位置
    * isChinese 是否是中文
    * isContainChinese 是否包含中文
    * strFormat2 不足2位前面补0
    * convert2Int 类型安全转换
    * decimalFormat 指定小数输出

- SystemUtils.java 系统工具
    * sendSMS 调用系统发送短信
    * forwardToDial 跳转到拨号
    * callPhone 直接呼叫号码
    * sendMail 发邮件
    * hideKeyBoard 隐藏系统键盘
    * isBackground 判断当前应用程序是否后台运行
    * isSleeping 判断手机是否处理睡眠
    * installApk 安装apk
    * isRooted 是否root
    * isRunningOnEmulator 当前设备是否是模拟器
    * getAppVersionName 获取当前应用程序的版本名称
    * getAppVersionCode 获取当前应用程序的版本号
    * goHome 返回Home
    * getSign 获取应用签名
    * hexdigest 32位签名
    * getDeviceUsableMemory 获取设备可用空间
    * gc 清理后台进程和服务
    * createDeskShortCut 创建桌面快捷方式
    * createShortcut 创建快捷方式
    * shareText 分享文本
    * shareFile 分享文件(此方法是调用FileUtils.shareFile中的方式)
    * getShareTargets 获取可接受分享的应用
    * getCurrentLanguage 获取当前系统的语言 
    * getLanguage 获取当前系统的语言
    * isGpsEnabled GPS是否打开
    * showSoftInputMethod 显示软键盘
    * closeSoftInputMethod 关闭软键盘
    * showSoftInput 显示软键盘
    * closeSoftInput 关闭软键盘

- VerificationUtils.java 验证工具类
    * matcherRealName 判断姓名格式  
    ```
    真实姓名可以是汉字,也可以是字母,但是不能两者都有,也不能包含任何符号和数字
    1.如果是英文名,可以允许英文名字中出现空格
    2.英文名的空格可以是多个,但是不能连续出现多个
    3.汉字不能出现空格
    ```
    * matcherPhoneNum 判断手机号格式  (匹配11数字,并且13-19开头)
    * matcherAccount 判断账号格式 (4-20位字符)
    * matcherPassword 判断密码格式 (6-12位字母或数字)
    * matcherPassword2 判断密码格式 (6-12位字母或数字,必须同时包含字母和数字)
    * matcherEmail 判断邮箱格式
    * matcherIP 判断IP地址
    * matcherUrl 判断URL (http,https,ftp)
    * matcherVehicleNumber 判断中国民用车辆号牌
    * matcherIdentityCard 判断身份证号码格式
    * isNumeric 是否数值型
    * testRegex 是否匹配正则
    * checkPostcode 匹配中国邮政编码
    
- ViewUtils.java View工具
    * removeSelfFromParent
    * requestLayoutParent
    * isTouchInView
    * bigImage
    * setTVUnderLine 给TextView设置下划线
    * showPopupWindow
    * dismissPopup
    * captureView 截图
    * createViewBitmap 截图
    * convertViewToBitmap 截图
    * getActivityBitmap 获取Activity的截图
    * getStatusBarHeight 获取状态栏高度
    * getToolbarHeight 获取工具栏高度
    * getNavigationBarHeight 获取导航栏高度
    * measureView 测量view
    * getViewWidth 获取view的宽度
    * getViewHeight 获取view的高度

    
    
```shell 

    /**
     * 身份证校验
     * <p>
     * 根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定,公民身份号码是特征组合码,由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码,八位数字出生日期码,三位数字顺序码和一位数字校验码。
     * 地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
     * 出生日期码表示编码对象出生的年、月、日,其中年份用四位数字表示,年、月、日之间不用分隔符。
     * 顺序码表示同一地址码所标识的区域范围内,对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性,偶数分给女性。
     * 校验码是根据前面十七位数字码,按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。
     * 出生日期计算方法。
     * 15位的身份证编码首先把出生年扩展为4位,简单的就是增加一个19或18,这样就包含了所有1800-1999年出生的人;
     * 2000年后出生的肯定都是18位的了没有这个烦恼,至于1800年前出生的,那啥那时应该还没身份证号这个东东,⊙﹏⊙b汗...
     * 下面是正则表达式:
     * 出生日期1800-2099  /(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])/
     * 身份证正则表达式 /^[1-9]\d{5}((1[89]|20)\d{2})(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dx]$/i
     * 15位校验规则 6位地址编码+6位出生日期+3位顺序号
     * 18位校验规则 6位地址编码+8位出生日期+3位顺序号+1位校验位
     * 校验位规则     公式:∑(ai×Wi)(mod 11)……………………………………(1)
     * 公式(1)中：
     * i----表示号码字符从由至左包括校验码在内的位置序号;
     * ai----表示第i位置上的号码字符值；
     * Wi----示第i位置上的加权因子,其数值依据公式Wi=2^(n-1）(mod 11)计算得出。
     * i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1
     * Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 1
     * </P>
     *
     * @author Yoojia.Chen (yoojia.chen@gmail.com)
     * @version version 2015-05-21
     * @since 2.0
     */
```

----------
### [《Android应用开发》](https://github.com/Jhuster/Android)
Code List
----------

- Builder 给出了使用ant编译Android工程的通用shell脚本，相关博文：[Android开发实践：用脚本编译Android工程](http://ticktick.blog.51cto.com/823160/1365947)

- GroupList 封装并演示了如何使用Android的ExpandableListView控件，相关博文：[Android开发实践：多级列表的封装与应用](http://ticktick.blog.51cto.com/823160/1289642)

- PopDialog 封装并演示了如何使用Android的PopupWindow控件，相关博文：[Android开发实践：用PopupWindow实现自定义Dailog](http://ticktick.blog.51cto.com/823160/1332083)

- JniCallback 演示了如何从Native线程回调Java的函数，相关博文：[Android开发实践：JNI层线程回调Java函数示例](http://ticktick.blog.51cto.com/823160/1358558)

- JniBuffer 演示了各种从Java端到Native层的Buffer传递方法，相关博文：[Android开发实践：Java层与Jni层的数组传递](http://ticktick.blog.51cto.com/823160/1360240)

- LoopThread 演示了如何实现一个自定义的带Loop消息循环的线程，相关博文：[Android开发实践：自定义带消息循环（Looper）的工作线程](http://ticktick.blog.51cto.com/823160/1565272)

- VideoPlayer 演示了Android隐式Intent的用法，相关博文：[Android开发实践：实战演练隐式Intent的用法](http://ticktick.blog.51cto.com/823160/1621957)

- VideoServer 演示了如何利用NanoHttpd搭建一个Android视频服务器，相关博文：[基于NanoHttpd的Android视频服务器开发](http://ticktick.blog.51cto.com/823160/1705767)

- CustomView 演示了如何自定义View和ViewGroup，并给出了一个柱状图动画自定义View的实现，相关博文：

- （1）[Android开发实践：为什么要继承onMeasure()](http://ticktick.blog.51cto.com/823160/1540134)

- （2）[Android开发实践：自定义ViewGroup的onLayout()分析](http://ticktick.blog.51cto.com/823160/1542200)

- （3）[Android开发实践：自定义带动画的View](http://ticktick.blog.51cto.com/823160/1545863)

- WIFI 封装了Andriod WIFI扫描和连接的相关API，相关博文：

- （1）[Android开发实践：WIFI扫描功能的封装](http://ticktick.blog.51cto.com/823160/1394948)

- （2）[Android开发实践：WIFI连接功能的封装](http://ticktick.blog.51cto.com/823160/1410080)

- Socket封装了Android中的Socket相关API

- （1） Broadcaster.java 封装了UDP广播包的收发操作，相关博文：[Android Socket 发送广播包的那些坑](http://ticktick.blog.51cto.com/823160/1707858)

- MediaDemo 给出了Android平台下多媒体相关API的demo代码，相关博文：[Android中如何提取和生成mp4文件](http://ticktick.blog.51cto.com/823160/1710743)

- Utils 给出了一系列的工具类代码

- （1）SignatureGen.java 一个可以生成Java函数签名字符串的工具类，相关博文: [Android开发实践：JNI函数签名生成器](http://ticktick.blog.51cto.com/823160/1590209)

- （2）BitmapHelper.java 一个封装了Bitmap操作的工具类，包括图片的打开、保存、剪裁、旋转等操作，相关博文: [Android开发实践：自己动手编写图片剪裁应用（3）](http://ticktick.blog.51cto.com/823160/1604074)

- （3）ImageConvertor.java 封装了Android中各种图像格式转换，包括：NV21，YUY2、RGB565、ARGB8888、PNG、JPEG、Bitmap之间的转换操作。

Thanks

---
### [Android 开源项目分类汇总](https://github.com/Trinea/android-open-project)
**目前包括：**
>[Android 开源项目第一篇——个性化控件(View)篇](https://github.com/Trinea/android-open-project#%E7%AC%AC%E4%B8%80%E9%83%A8%E5%88%86-%E4%B8%AA%E6%80%A7%E5%8C%96%E6%8E%A7%E4%BB%B6view)  
*&nbsp;&nbsp;包括[ListView](https://github.com/Trinea/android-open-project#%E4%B8%80listview)、[ActionBar](https://github.com/Trinea/android-open-project#%E4%BA%8Cactionbar)、[Menu](https://github.com/Trinea/android-open-project#%E4%B8%89menu)、[ViewPager](https://github.com/Trinea/android-open-project#%E5%9B%9Bviewpager-gallery)、[Gallery](https://github.com/Trinea/android-open-project#%E5%9B%9Bviewpager-gallery)、[GridView](https://github.com/Trinea/android-open-project#%E4%BA%94gridview)、[ImageView](https://github.com/Trinea/android-open-project#%E5%85%ADimageview)、[ProgressBar](https://github.com/Trinea/android-open-project#%E4%B8%83progressbar)、[TextView](https://github.com/Trinea/android-open-project#%E5%85%ABtextview)、[ScrollView](https://github.com/Trinea/android-open-project#%E4%B9%9Dscrollview)、[TimeView](https://github.com/Trinea/android-open-project#%E5%8D%81timeview)、[TipView](https://github.com/Trinea/android-open-project#%E5%8D%81%E4%B8%80tipview)、[FlipView](https://github.com/Trinea/android-open-project#%E5%8D%81%E4%BA%8Cflipview)、[ColorPickView](https://github.com/Trinea/android-open-project#%E5%8D%81%E4%B8%89colorpickview)、[GraphView](https://github.com/Trinea/android-open-project#%E5%8D%81%E5%9B%9Bgraphview)、[UI Style](https://github.com/Trinea/android-open-project#%E5%8D%81%E4%BA%94ui-style)、[其他](https://github.com/Trinea/android-open-project#十六其他)*  
[Android 开源项目第二篇——工具库篇](https://github.com/Trinea/android-open-project#%E7%AC%AC%E4%BA%8C%E9%83%A8%E5%88%86-%E5%B7%A5%E5%85%B7%E5%BA%93)  
*&nbsp;&nbsp;包括[依赖注入](https://github.com/Trinea/android-open-project#一依赖注入-di)、[图片缓存](https://github.com/Trinea/android-open-project#二图片缓存)、[网络请求](https://github.com/Trinea/android-open-project#三网络请求)、[数据库 ORM 工具包](https://github.com/Trinea/android-open-project#四数据库-orm-工具包)、[Android 公共库](https://github.com/Trinea/android-open-project#五android-公共库)、[高版本向低版本兼容库](https://github.com/Trinea/android-open-project#六android-高版本向低版本兼容)、[多媒体](https://github.com/Trinea/android-open-project#七多媒体相关)、[事件总线](https://github.com/Trinea/android-open-project#八事件总线订阅者模式)、[传感器](https://github.com/Trinea/android-open-project#九传感器)、[安全](https://github.com/Trinea/android-open-project#十安全)、[插件化](https://github.com/Trinea/android-open-project#十一插件化)、[文件](https://github.com/Trinea/android-open-project#十二文件)、[其他](https://github.com/Trinea/android-open-project#十三其他)*  
[Android 开源项目第三篇——优秀项目篇](https://github.com/Trinea/android-open-project#%E7%AC%AC%E4%B8%89%E9%83%A8%E5%88%86-%E4%BC%98%E7%A7%80%E9%A1%B9%E7%9B%AE)  
*&nbsp;&nbsp;比较有意思的完整的 Android 项目*  
[Android 开源项目第四篇——开发及测试工具篇](https://github.com/Trinea/android-open-project#%E7%AC%AC%E5%9B%9B%E9%83%A8%E5%88%86-%E5%BC%80%E5%8F%91%E5%B7%A5%E5%85%B7%E5%8F%8A%E6%B5%8B%E8%AF%95%E5%B7%A5%E5%85%B7)  
*&nbsp;&nbsp;包括[开发效率工具](https://github.com/Trinea/android-open-project#一开发效率工具)、[开发自测相关](https://github.com/Trinea/android-open-project#二开发自测相关)、[测试工具](https://github.com/Trinea/android-open-project#三测试工具)、[开发及编译环境](https://github.com/Trinea/android-open-project#四开发及编译环境)、[其他](https://github.com/Trinea/android-open-project#五其他)*  
[Android 开源项目第五篇——优秀个人和团体篇](https://github.com/Trinea/android-open-project#%E7%AC%AC%E4%BA%94%E9%83%A8%E5%88%86)  
*&nbsp;&nbsp;乐于分享并且有一些很不错的开源项目的[个人](https://github.com/Trinea/android-open-project#一个人)和[组织](https://github.com/Trinea/android-open-project#二组织)，包括 JakeWharton、Chris Banes、Koushik Dutta 等大牛* 

---
### [awesome-android, android libs from github](https://github.com/snowdream/awesome-android)
### Table of contents
* [Framework](#Framework)
* [EventBus](#EventBus)
* [Orm](#Orm)
* [Image Loading](#Image_Loading)
* [Animations](#Animations)
* [Network](#Network)
* [Widget](#Widget)
  * [Material](#Material)
  * [UI](#UI)
  * [TextView/EditText](#TextView/EditText)
  * [ImageView](#ImageView)
  * [Button](#Button)
  * [CheckBox](#CheckBox)
  * [Progressbar/Progress View](#Progressbar/Progress_View)
  * [Menu](#Menu)
  * [Dialog](#Dialog)
  * [ListView/ScrollView](#ListView/ScrollView)
  * [GridView](#GridView)
  * [RatingView](#RatingView)
  * [Recyclerview](#Recyclerview)
  * [SearchView](#SearchView)
  * [ViewPager](#ViewPager)
  * [ActionBar](#ActionBar)
  * [Snackbar ](#Snackbar )
  * [Fragment](#Fragment)
  * [Activity](#Activity)
  * [Tabs](#Tabs)
  * [Toast](#Toast)
  * [LockView](#LockView)
  * [SeekBar](#SeekBar)
  * [Time View](#Time_View)
  * [Layout](#Layout)
  * [Toolbar](#Toolbar)
  * [VideoView](#VideoView)
  * [Mapview](#Mapview)
  * [Choreographer](#Choreographer)
  * [Other](#Other)
* [Plugin](#Plugin)
  * [Gradle](#Gradle)
  * [Maven](#Maven)
  * [SBT](#SBT)
  * [Intellij IDEA / Android Studio](#Intellij_IDEA_/_Android_Studio)
  * [Other](#Other-Plugin)
* [Intent](#Intent)
* [Injector](#Injector)
* [Template](#Template)
* [Adapter](#Adapter)
* [Validation](#Validation)
* [Gesture](#Gesture)
* [Game Engine](#Game_Engine)
* [Bluetooth](#Bluetooth)
* [SocialNetworks](#SocialNetworks)
* [XMPP](#XMPP)
* [Code Generation](#Code_Generation)
* [Media](#Media)
* [Embedded](#Embedded)
* [Utility](#Utility)
* [Storage](#Storage)
* [SDK](#SDK)
* [Test](#Test)
* [Chart](#Chart)
* [Icons](#Icons)
* [Colors](#Colors)
* [Font](#Font)
* [OpenGL](#OpenGL)
* [Debug](#Debug)
* [Demo](#Demo)
* [App](#App)
* [Security](#Security)
* [Tools](#Tools)
* [QRCode](#QRCode)
* [Decompiler](#Decompiler)
* [Android Wear](#Android_Wear)
* [Other](#Other1)

---
### [Android-Next 公共组件库](https://github.com/angcyo/Android-Next)
这个库是我在日常开发过程中积累下来的一些可复用组件，大部分都在我的工作项目和个人项目中有使用。

最新版本: [![Maven Central](http://img.shields.io/badge/2015.08.04-com.mcxiaoke.next:1.2.0-brightgreen.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.mcxiaoke.next%22)

**Gradle集成**

```groovy
    // core 核心库, 格式:jar和aar
    compile 'com.mcxiaoke.next:core:1.2.+'
    // task 异步任务库，格式:jar和aar
    compile 'com.mcxiaoke.next:task:1.2.+'
    // http HTTP组件, 格式:jar和aar
    compile 'com.mcxiaoke.next:http:1.2.+'
    // 异步网络和文件IO组件，替代Volley
    compile 'com.mcxiaoke.next:ioasync:1.2.+'
    // 函数操作组件
    compile 'com.mcxiaoke.next:functions:1.2.+'
    // ui UI组件, 格式:aar
    compile 'com.mcxiaoke.next:ui:1.2.+'
    // recycler EndlessRecyclerView, 格式:aar
    compile 'com.mcxiaoke.next:recycler:1.2.+'
    // extra-abc 依赖support-v7 AppCompat 格式:aar
    compile 'com.mcxiaoke.next:extras-abc:1.2.+'
    
```

**使用指南（2015.08.24更新）**

**使用前请阅读对应模块的文档和示例，如果有不清楚的地方，可以看源码，或者向我提问。**

---
### [开源框架封装](https://github.com/huangwm1984/AndroidBase)
自己整理的项目中常用到开发框架和相关Test例子、开发中遇到的问题总结，持续更新中...

1.BaseApplication、BaseActivity、BaseFragment等基类、activity堆栈式管理类以及全局网络通知

2.网络请求框架：OKhttp

3.数据库框架：Ormlite

4.图片异步加载类库：Glide

5.快速绑定控件：Butterknife

6.文件缓存：Disklrucache、SpCache(Sharepreference保存)

7.内存泄漏监测：Leakcanary

8.事件总线：AndroidEventbus

9.Json解析工具：FastJson

10.收集的各种便捷开发的工具类

11.ListView、GridView、RecyclerView快速开发适配器

12.Activity解耦CommonBlock

13.Android 6.0 Permission封装

14.集成BaseBanner控件

15.集成hongyangAndroid的多分辨率终极适配方案AndroidAutoLayout

最后感谢github上大神的开源框架和开源精神 (●'◡'●)ﾉ♥

---
### [AndroidLife](https://github.com/CaMnter/AndroidLife)
### 1. ImageScaleTypesActivity
Introduce the ImageView scale properties      
介绍ImageView scale属性  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/48424095)  
**Screenshot:**    
![ImageScaleTypesActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/1.%20ImageScaleTypesActivity.png)  
   
 ---
   
### 2.AsyncTaskActivity
Analysis of AsyncTask, and provide an AsyncTask template  
分析AsyncTask，并提供一个AsyncTask的模板  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/48525013)  
**Screenshot:**    
![ImageScaleTypesActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/2.AsyncTaskActivity.png)  

---

### 3.TextInputLayoutActivity
Show the TextInputLayout of support design  
展示 support design 的 TextInputLayout  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/48808005)   
**Screenshot:**    
![TextInputLayoutActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/3.TextInputLayoutActivity.png)  	   

---

### 4.RefreshUIActivity
Introduced the Android UI refresh four ways  
介绍 Android UI 刷新的四种方式  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/48629073)   
**Screenshot:**    
![RefreshUIActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/4.RefreshUIActivity.png)  

---

### 5.LaunchModeActivity
Show the singleTask launchmode effect  
展示 singleTask launchmode 效果  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/48682525)   
**Screenshot:**    
![LaunchModeActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/5.LaunchModeActivity.png)  

---

### 6.NavigationViewActivity
Show the NavigationView of support design  
展示 support design 的 NavigationView  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49106163)    
**Screenshot:**    
![NavigationViewActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/6.NavigationViewActivity.png)  

---

### 7.DeviceUtilActivity
Introduce the DeviceUtil  
介绍 DeviceUtil  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49126033)   
**Screenshot:**    
![DeviceUtilActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/7.DeviceUtilActivity.png)  	  

---

### 8.FloatingActionButtonActivity
Show the FloatingActionButton of support design  
展示 support design 的 FloatingActionButton  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49133395)    
**Screenshot:**     
![FloatingActionButtonActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/8.FloatingActionButtonActivity.png)  

---

### 9.SnackbarActivity
Show the Snackbar of support design  
展示 support design 的 Snackbar  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49161233)   
**Screenshot:**    
![SnackbarActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/9.SnackbarActivity.png)  

---

### 10.DateUtilActivity
Introduce the DateUtil  
介绍 DateUtil  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49181669)   
**Screenshot:**      
![DateUtilActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/10.DateUtilActivity.png)   

---

### 11.EasySlidingTabsActivity
Introduce the [EasySlidingTabs](https://github.com/CaMnter/EasySlidingTabs)  
介绍 [EasySlidingTabs](https://github.com/CaMnter/EasySlidingTabs)      
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49229189)   
**Screenshot:**     
![11.EasySlidingTabsActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/11.EasySlidingTabsActivity.png)  

---

### 12.AutoAdjustSizeEditTextActivity
Show the AutoAdjustSizeEditText  
展示 AutoAdjustSizeEditText  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49666243)   
**Screenshot:**      
![AutoAdjustSizeEditTextActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/12.AutoAdjustSizeEditTextActivity.png)  

---

### 13.AutoAdjustSizeTextViewActivity
Show the AutoAdjustSizeTextView  
展示 AutoAdjustSizeTextView  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49666243)   
**Screenshot:**      
![AutoAdjustSizeTextViewActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/13.AutoAdjustSizeTextViewActivity.png)  

---

### 14.DownloadImageToGalleryActivity
Small functions: download the pictures, and save the Android album.This involves a util - [ImageUtil](https://github.com/CaMnter/AndroidLife/blob/master/app/src/main/java/com/camnter/newlife/utils/ImageUtil.java)  
小功能：下载图片，并且保存到Android相册里。这里涉及到一个util -  [ImageUtil](https://github.com/CaMnter/AndroidLife/blob/master/app/src/main/java/com/camnter/newlife/utils/ImageUtil.java)  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49285919)   
**Screenshot:**    
![DownloadImageToGalleryActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/14.DownloadImageToGalleryActivity.png)  

---


### 15.EasyRecyclerViewActivity
Introduce the [EasyRecyclerView](https://github.com/CaMnter/EasyRecyclerView)  
介绍 [EasyRecyclerView](https://github.com/CaMnter/EasyRecyclerView)  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49341563)   
**Screenshot:**    
![EasyRecyclerViewActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/15.EasyRecyclerViewActivity.png)   


---

### 16.NormalTabLayoutActivity & SetIconTabLayoutActivity & ImageSpanTabLayoutActivity & CustomViewTabLayoutActivity
Show the TabLayout of support design and four kinds of style design  
展示 support design 中的 TabLayout ，及其四种设计样式  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49404537)    
**Screenshot:**    
![TabLayoutActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/16.TabLayoutActivity.png)    

---

###  17.CoordinatorLayoutActivity

Introduce the CoordinatorLayout of support design   
Here only including: `app:layout_scrollFlags` and `app:layout_behavior`  
介绍 support design 中的 CoordinatorLayout  
这里仅仅包括：`app:layout_scrollFlags` 和 `app:layout_behavior`      
**Screenshot:**    
![CoordinatorLayoutActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/17.CoordinatorLayoutActivity.png)    


---

### 18.SensorManagerActivity
Show the Android sensors  
展示Android中的传感器  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49498567)   
**Screenshot:**    
![SensorManagerActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/18.SensorManagerActivity.png)    


---

### 19.MvpActivity
Introduce the MVP design patterns  
介绍MVP设计模式  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49620017)

---

### 20.SQLiteActivity
A demo of SQLite  
一个关于SQLite的demo  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49641519)    
**Screenshot:**    
![SQLiteActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/20.SQLiteActivity.png)    


---

### 21.Robotlegs4AndroidActivity
Introduce an Android MVC framework - [Robotlegs4Android](https://github.com/CaMnter/Robotlegs4Android)  
介绍一个 Android MVC 框架 - [Robotlegs4Android](https://github.com/CaMnter/Robotlegs4Android)  
[Github](https://github.com/CaMnter/Robotlegs4Android)
 [Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49788259)     
 **Screenshot:**    
![Robotlegs4AndroidActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/21.Robotlegs4AndroidActivity.png)    
 
 ---
 
### 22.CustomContentProviderActivity
A about custom contentprovicer demo  
一个关于自定义contentprovicer的demo  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49849663)    
 **Screenshot:**    
![CustomContentProviderActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/22.CustomContentProviderActivity.png)    

---

### 23.DownloadServiceActivity & AIDLActivity
A demo of Android Service,introduces the remote Service (AIDL) and local Service  
一个关于Android Service的demo，介绍了远程Service（AIDL）和本地Service  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49910627)    
 **Screenshot:**    
![DownloadServiceActivityAndAIDLActivity.png](https://github.com/CaMnter/AndroidLife/raw/master/readme/23.DownloadServiceActivityAndAIDLActivity.png)    

---

### 24.ReflectionUtilActivity
Introduce the ReflectionUtil  
介绍ReflectionUtil  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49962997)    
 **Screenshot:**    
![ReflectionUtilActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/24.ReflectionUtilActivity.png)    


---

### 25.StaticReceiverActivity & DynamicReceiverActivity & DownloadReceiverActivity
Broadcastreceiver demo  
Broadcastreceiver的demo  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/49995443)     
 **Screenshot:**    
![StaticReceiverActivityAndDynamicReceiverActivityAndDownloadReceiverActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/25.StaticReceiverActivityAndDynamicReceiverActivityAndDownloadReceiverActivity.png)    

 
 
 ---


### 26.ResourcesUtilActivity
Introduce the ResourcesUtil  
介绍ResourcesUtil  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/50056747)    
 **Screenshot:**    
![ResourcesUtilActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/26.ResourcesUtilActivity.png)    


---

### 27.LocationManagerActivity
Introduce the LocationManager  
介绍LocationManager  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/50097333)  

---

### 28.RxSyncActivity & RxAsyncActivity & RxMapActivity
Introduce the [RxAndroid](https://github.com/ReactiveX/RxAndroid)  
介绍RxAndroid    
 **Screenshot:**    
![RxSyncActivityAndRxAsyncActivityAndRxMapActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/28.RxSyncActivityAndRxAsyncActivityAndRxMapActivity.png) 

---

### 29.DialogActivity
Provide two sets of custom Dialog template.  
The first kind, prompt Dialog, have disappeared.  
Second, the menu Dialog for user interaction.  
提供两套自定义Dialog模板。  
第一种，提示Dialog，有消失时间。  
第二种，菜单Dialog，用于用户交互。  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/50283987)     
 **Screenshot:**    
![DialogActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/29.DialogActivity.png) 

---

### 30.PopupWindowActivity
A scalable, easy-to-use PopupWindow.  
一个可扩展的、好用的PopupWindow。  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/50380802)    
 **Screenshot:**    
![PopupWindowActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/30.PopupWindowActivity.png) 

---

### 31.TagTextViewActivity
Rich text textview with a clickable label  
有可点击的标签的富文本textview  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/50386502)    
 **Screenshot:**    
![TagTextViewActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/31.TagTextViewActivity.png)   

---


### 32.EasyFlowLayout  

Use and rewrite it again the [FlowLayout](https://github.com/hongyangAndroid/FlowLayout)    
使用和重写了一遍 [FlowLayout](https://github.com/hongyangAndroid/FlowLayout)    
 **Screenshot:**    
![EasyFlowLayout](https://github.com/CaMnter/AndroidLife/raw/master/readme/32.EasyFlowLayout.png)    

---

### 33.SpanActivity  
Show the effect of various Span  
展示各种Span的效果  
[Detailed introduction](http://blog.csdn.net/qq_16430735/article/details/50427978)    
 **Screenshot:**    
![SpanActivity](https://github.com/CaMnter/AndroidLife/raw/master/readme/33.SpanActivity.png)   

## 牛人推荐
**国内:[https://github.com/android-cn/android-dev-cn](https://github.com/android-cn/android-dev-cn)**

昵称 | GitHub | 博客 | 介绍
:------------- | :------------- | :------------- | :------------- 
罗升阳 | | [Luoshengyang@csdn](http://blog.csdn.net/Luoshengyang) | Android 源码分析
邓凡平 | | [innost@csdn](http://blog.csdn.net/innost) | 阿拉神农
魏祝林 | | [android_tutor@csdn](http://blog.csdn.net/android_tutor) | 
Trinea | [trinea ](https://github.com/trinea) | [trinea.cn](http://www.trinea.cn/) | 性能优化 开源项目 
halzhang | [halzhang ](https://github.com/halzhang) | [halzhang@cnblogs](http://www.cnblogs.com/halzhang) | StartNews作者 
wyouflf | [wyouflf ](https://github.com/wyouflf) | [wyouflf@oschina](http://my.oschina.net/u/1171837) | xUtils作者
张兴业 | | [xyz_lmn@csdn](http://blog.csdn.net/xyz_lmn) | 
代码家 | [daimajia ](https://github.com/daimajia) | [daimajia.com](http://blog.daimajia.com/) |
stormzhang | [stormzhang ](https://github.com/stormzhang) | [stormzhang](http://stormzhang.github.io/) | 9Gag作者 AndroidDesign Love开源
郭霖 | | [guolin_blog@csdn](http://blog.csdn.net/guolin_blog) |
hanyonglu | | [hanyonglu@cnblogs](http://www.cnblogs.com/hanyonglu) | Android动画与推送 
闷瓜蛋子 | | [fookwood.com](http://www.fookwood.com)  | 云OS开发 
傲慢的上校 | | [lilu_leo@csdn](http://blog.csdn.net/lilu_leo) |  
youxiachai | [youxiachai ](https://github.com/youxiachai) | | 
dodola | [dodola ](https://github.com/dodola) | | 
Issacw0ng | [Issacw0ng ](https://github.com/Issacw0ng) | [imid.me](http://imid.me) | 
mcxiaoke | [mcxiaoke ](https://github.com/mcxiaoke) | | 
soarcn | [soarcn ](https://github.com/soarcn) | | 
谦虚的天下 | | [qianxudetianxia@cnblogs](http://www.cnblogs.com/qianxudetianxia) | 
李华明Himi | | [xiaominghimi@csdn](http://blog.csdn.net/xiaominghimi) | 
yangfuhai | [yangfuhai ](https://github.com/yangfuhai) | | afinal 作者 
张国威 | | [hellogv@csdn](http://blog.csdn.net/hellogv) |  
程序媛念茜 | | [yiyaaixuexi@csdn](http://blog.csdn.net/yiyaaixuexi) |  
wangjinyu501 | | [wangjinyu501@csdn](http://blog.csdn.net/wangjinyu501) |  
ASCE1885 | | [asce1885@csdn](http://blog.csdn.net/asce1885) | 
qinjuning | | [qinjuning@csdn](http://blog.csdn.net/qinjuning) |   
秋风的博客 | | [tangcheng_ok@csdn](http://blog.csdn.net/tangcheng_ok) | 
任玉刚 | [singwhatiwanna ](https://github.com/singwhatiwanna) | [singwhatiwanna@csdn](http://blog.csdn.net/singwhatiwanna) | 
农民伯伯 | [over140 ](https://github.com/over140) | [over140](http://over140.cnblogs.com) | [开源播放器](https://github.com/over140/OPlayer) Android 中文 api 
李宏伟 | [lihw ](https://github.com/lihw) | [paper3d.net](http://www.paper3d.net) | [Paper3D](https://github.com/lihw/FutureInterface) 
代震军 | [daizhenjun ](https://github.com/daizhenjun) | [daizhj@cnblogs](http://www.cnblogs.com/daizhj) | [ImageFilter库](https://github.com/daizhenjun/ImageFilterForAndroid)
sunzn | | [sunzn@cnblogs](http://www.cnblogs.com/sunzn) | Android 基础开发知识
pedant | [pedant ](https://github.com/pedant) | [书呆子精神院](http://pedant.cn/) | [SweetAlertDialog](https://github.com/pedant/sweet-alert-dialog)、安全与逆向
androidyue | [androidyue](https://github.com/androidyue) | [技术小黑屋](http://droidyue.com/) | Android，Java研究
Hongyang | [hongyangAndroid](https://github.com/hongyangAndroid)| [Hongyang](http://blog.csdn.net/lmj623565791)| Android
---
**国外:[https://github.com/android-cn/android-dev-com](https://github.com/android-cn/android-dev-com)**

Avatar  | Github | Blog | Description
-------------: | :------------- | :------------- | :------------- 
![Google Android](https://avatars3.githubusercontent.com/u/1342004?s=80 "Google Android") | https://github.com/google | http://android-developers.blogspot.com/ |  Google Android Developers Blog
![JakeWharton](https://avatars0.githubusercontent.com/u/66577?s=80 "JakeWharton") | https://github.com/JakeWharton | http://jakewharton.com/ |  ActionBarSherlock, Android-ViewPagerIndicator, Nine Old Androids, butterknife
![Square](https://avatars0.githubusercontent.com/u/82592?s=80 "Square") | https://github.com/square   | http://square.github.io/ | okhttp, fest-android, android-times-square, picasso, dagger, spoon
![Chris Banes](https://avatars3.githubusercontent.com/u/227486?s=80 "Chris Banes")  | https://github.com/chrisbanes | http://chris.banes.me/ | ActionBar-PullToRefresh, PhotoView, Android-BitmapCache, Android-PullToRefresh
![Jeremy Feinstein](https://avatars0.githubusercontent.com/u/1269143?s=80 "Jeremy Feinstein") | https://github.com/jfeinstein10 | http://jeremyfeinstein.com/ | SlidingMenu, JazzyViewPager
![Sergey Tarasevich](https://avatars3.githubusercontent.com/u/1223348?s=80 "Sergey Tarasevich") | https://github.com/nostra13 | http://nostra13android.blogspot.com/ | Android-Universal-Image-Loader
![Koushik Dutta](https://avatars3.githubusercontent.com/u/73924?s=80 "Koushik Dutta") | https://github.com/koush   | http://koush.com/  | Superuser, AndroidAsync, UrlImageViewHelper  
![Simon Vig](https://avatars2.githubusercontent.com/u/549365?s=80 "Simon Vig") | https://github.com/SimonVT |  http://simonvt.net/ | android-menudrawer, MessageBar 
![Cyril Mottier](https://avatars1.githubusercontent.com/u/92794?s=80 "Cyril Mottier") | https://github.com/cyrilmottier |  http://cyrilmottier.com/ | GreenDroid, Polaris
![Emil Sjolander](https://avatars2.githubusercontent.com/u/1525924?s=80 "Emil Sjolander") | https://github.com/emilsjolander |  http://emilsjolander.se/ | StickyListHeaders, sprinkles, android-FlipView
![James Smith](https://avatars1.githubusercontent.com/u/104009?s=80 "James Smith") | https://github.com/loopj | http://loopj.com | android-async-http
![Manuel Peinado](https://avatars2.githubusercontent.com/u/2700015?s=80 "Manuel Peinado") |  https://github.com/ManuelPeinado  |   | FadingActionBar, GlassActionBar, RefreshActionItem, QuickReturnHeader
![greenrobot](https://avatars2.githubusercontent.com/u/242242?s=80 "greenrobot") | https://github.com/greenrobot | http://greenrobot.de/  | greenDAO, EventBus
![Jeff Gilfelt](https://avatars0.githubusercontent.com/u/175697?s=80 "Jeff Gilfelt") |  https://github.com/jgilfelt  |  http://jeffgilfelt.com  |  android-mapviewballoons, android-viewbadger, android-actionbarstylegenerator, android-sqlite-asset-helper
![Roman Nurik](https://avatars0.githubusercontent.com/u/100155?s=80 "Roman Nurik") | https://github.com/romannurik | http://roman.nurik.net/ | muzei, Android-SwipeToDismiss
![Flavien Laurent](https://avatars1.githubusercontent.com/u/4429434?s=80 "Flavien Laurent") | https://github.com/flavienlaurent | http://www.flavienlaurent.com | NotBoringActionBar, datetimepicker, discrollview
![Gabriele Mariotti](https://avatars0.githubusercontent.com/u/2583078?s=80 "Gabriele Mariotti") | https://github.com/gabrielemariotti | http://gmariotti.blogspot.it | cardslib, colorpickercollection
![sephiroth74](https://avatars0.githubusercontent.com/u/823858?s=80 "sephiroth74") | https://github.com/sephiroth74 |  http://www.sephiroth.it/ | ImageViewZoom, HorizontalVariableListView, AndroidWheel, purePDF
![Romain Guy](https://avatars0.githubusercontent.com/u/869684?s=80 "Romain Guy") | https://github.com/romainguy |  http://www.curious-creature.org   |  ViewServer
![Kevin Sawicki](https://avatars1.githubusercontent.com/u/671378?s=80 "Kevin Sawicki") | https://github.com/kevinsawicki | https://twitter.com/kevinsawicki | http-request
![Christopher Jenkins](https://avatars0.githubusercontent.com/u/1167793?s=80 "Christopher Jenkins") | https://github.com/chrisjenx | http://about.me/chris.jenkins | Calligraphy, ParallaxScrollView
![Javier Pardo](https://avatars0.githubusercontent.com/u/1172221?s=80 "Javier Pardo") |  https://github.com/jpardogo | http://jpardogo.com | ListBuddies, FlabbyListView, GoogleProgressBar, FadingActionBar
![Chet Haase](https://lh4.googleusercontent.com/-alRF2kfXilM/AAAAAAAAAAI/AAAAAAAAH4U/1yMUbANZ_YY/s80-c/photo.jpg "Chet Haase")  |    |  http://graphics-geek.blogspot.com/ | Android framework UI team
![Matthias Käppler](https://avatars2.githubusercontent.com/u/102802?s=80 "Matthias Käppler") | https://github.com/mttkay | http://mttkay.github.io/ | signpost
![Daniel Lew](https://avatars3.githubusercontent.com/u/514850?s=80 "Daniel Lew") | https://github.com/dlew | http://blog.danlew.net/ | Android Tips
![FaceBook](https://avatars0.githubusercontent.com/u/69631?v=3&s=80 "FaceBook") | https://github.com/facebook | https://code.facebook.com/mobile/ | buck
Code Zen | | http://arpitonline.com/ | iOS Android
![Styling Android](https://lh3.googleusercontent.com/-8MrsyY2gqwM/AAAAAAAAAAI/AAAAAAAAAC4/fhUUNvYEqqo/s80-c-k-no/photo.jpg) | [Google Plus](https://plus.google.com/101161883485148457960?prsrc=3) | https://blog.stylingandroid.com/ | A techical guide to to improving the UI and UX Android apps
