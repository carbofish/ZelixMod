# ZelixMod
Zelix Group

# 启动配置
请构建完成后配置VM参数加入<br/>
	-Dfml.coremods.load=zelix.cc.injection.MixinLoader

# 构建操作
如果你的IDE是Idea那么就请运行ideaSetupWorkspace.bat<br/>
如果是Eclipse就运行eclipseSetupWorkspace.bat

#其他
如果你觉得构建太慢了,想要使用代理那么请用文本编辑器打开gradle.properties文件<br/>
	systemProp.http.proxyHost=你的代理IP<br/>
	systemProp.http.proxyPort=代理端口<br/>
	systemProp.https.proxyHost=你的代理IP<br/>
	systemProp.https.proxyPort=代理端口<br/>
加入以上内容
