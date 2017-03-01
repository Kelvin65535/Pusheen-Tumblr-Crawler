#Pusheen-Tumblr-Crawler

##介绍

自动下载Pusheen官网（pusheen.com）上的图片。

##项目结构

项目源码位于`Pusheen/`，可以使用Eclipse导入该项目并运行。

在`release/`中有一个附带默认参数的可执行java文件，默认设置为自动抓取网站中第1-44页的图片，并将导出的图片保存到`D:\pusheen`文件夹。可通过双击`Run.bat`执行，或执行`java -jar Pusheen.jar`运行程序。

##操作说明

在src/Main.java中自定义爬取图片所需的参数，在Pusheen类中修改。

参数说明：

`DIR`：图片保存的目录，默认为`D:\pusheen`，需要将斜杠`\`用双斜杠`\\`替换

`URLIndex`：要抓取的网址首页页码，默认为1

`URLLastPageIndex`：要抓取的网页最后一页页码，默认为44



