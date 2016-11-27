import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Main {

	public static void main(String[] args) throws IOException {
		Pusheen pusheen = new Pusheen();
		
		while (pusheen.URLIndex <= pusheen.URLLastPageIndex){
			pusheen.parseURL();
		}
	}
}

class Pusheen{
	//保存目录
	public String DIR = "D:\\pusheen";
	//网址的页码号
	public static int URLIndex = 1;
	//网址最后一页的页码号
	public static int URLLastPageIndex = 44;
	//URL
	public String URL = "http://www.pusheen.com/page/" + URLIndex;
	//下载图片的序号
	public static int imageIndex = 0;
	
	
	//创建保存目录
	public void initDir(){
		File dirFile = new File(DIR);
		dirFile.mkdir();
	}
	
	//调用jsoup
	public void parseURL() throws IOException{
		Document doc = Jsoup.connect(URL).timeout(10000).get();
		Elements element = doc.getElementsByAttributeValue("class","photo");
		
		//创建保存目录
		initDir();
		
		//从Elements中提取图片网址
		getLink(element);
		
		//执行完毕后跳转到下一页
		URLIndex++;
		URL = "http://www.pusheen.com/page/" + URLIndex;
	}
	
	//从Elements中提取图片网址
	public void getLink(Elements elements) throws IOException {
		for(Element e : elements){
			String imagePathString = e.getElementsByTag("img").attr("src");
			//下载图片
			downloadImg(imagePathString);
			
			//使用线程睡眠2秒
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void downloadImg(String imageURL) throws IOException{
		//文件扩展名
		int index = imageURL.lastIndexOf('.');
		String fileType = imageURL.substring(index);
		
		//构建文件名
		imageIndex++;
		String fileName = imageIndex + fileType;
		
		
		//构建下载路径
		String filePath = DIR + "\\" + fileName;
		System.out.println(filePath);
		
		//新建文件
		File file = new File(filePath);
		//判断文件是否存在
		if (file.exists()){
			System.out.println(filePath + " 已存在");
			return;
		}
		
		//下载图片
		URL url = new URL(imageURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setReadTimeout(60000);
		if(con.getResponseCode() >= 200 && con.getResponseCode() < 300){
			InputStream inputStream = con.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = inputStream.read(buffer)) != -1){
				outputStream.write(buffer, 0, len);
			}
			outputStream.close();
			inputStream.close();
			byte[] data = outputStream.toByteArray();
			fileOutputStream.write(data);
			fileOutputStream.close();
		}
		else{
			System.out.println("Error response code: " + con.getResponseCode());
			System.exit(0);
		}
	}
	
}
