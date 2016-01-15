@Echo Off

if /i "%1"=="" goto :default
if /i "%1"=="i" goto :install
if /i "%1"=="u" goto :uninstall

::执行实际的命令
goto :raw

::无参数情况下的默认执行命令
:default
gradlew iD
goto :eof

::实际命令
:raw
gradlew %1
goto :eof

::安装所有Debug版本的APK
:install
gradlew iD
goto :eof

::卸载所有版本的APK
:uninstall
gradlew uA
goto :eof