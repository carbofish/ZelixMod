# ZelixMod
Zelix Group

# 启动配置
请构建完成后配置VM参数加入
	-Dfml.coremods.load=zelix.cc.injection.MixinLoader

# 构建操作
如果你的IDE是Idea那么就请运行ideaSetupWorkspace.bat
如果是Eclipse就运行eclipseSetupWorkspace.bat

#其他
如果你觉得构建太慢了,想要使用代理那么请用文本编辑器打开gradle.properties文件
  systemProp.http.proxyHost=你的代理IP
  systemProp.http.proxyPort=代理端口
  systemProp.https.proxyHost=你的代理IP
  systemProp.https.proxyPort=代理端口
加入以上内容
