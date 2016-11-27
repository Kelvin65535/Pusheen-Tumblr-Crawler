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
	//����Ŀ¼
	public String DIR = "D:\\pusheen";
	//��ַ��ҳ���
	public static int URLIndex = 1;
	//��ַ���һҳ��ҳ���
	public static int URLLastPageIndex = 44;
	//URL
	public String URL = "http://www.pusheen.com/page/" + URLIndex;
	//����ͼƬ�����
	public static int imageIndex = 0;
	
	
	//��������Ŀ¼
	public void initDir(){
		File dirFile = new File(DIR);
		dirFile.mkdir();
	}
	
	//����jsoup
	public void parseURL() throws IOException{
		Document doc = Jsoup.connect(URL).timeout(10000).get();
		Elements element = doc.getElementsByAttributeValue("class","photo");
		
		//��������Ŀ¼
		initDir();
		
		//��Elements����ȡͼƬ��ַ
		getLink(element);
		
		//ִ����Ϻ���ת����һҳ
		URLIndex++;
		URL = "http://www.pusheen.com/page/" + URLIndex;
	}
	
	//��Elements����ȡͼƬ��ַ
	public void getLink(Elements elements) throws IOException {
		for(Element e : elements){
			String imagePathString = e.getElementsByTag("img").attr("src");
			//����ͼƬ
			downloadImg(imagePathString);
			
			//ʹ���߳�˯��2��
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void downloadImg(String imageURL) throws IOException{
		//�ļ���չ��
		int index = imageURL.lastIndexOf('.');
		String fileType = imageURL.substring(index);
		
		//�����ļ���
		imageIndex++;
		String fileName = imageIndex + fileType;
		
		
		//��������·��
		String filePath = DIR + "\\" + fileName;
		System.out.println(filePath);
		
		//�½��ļ�
		File file = new File(filePath);
		//�ж��ļ��Ƿ����
		if (file.exists()){
			System.out.println(filePath + " �Ѵ���");
			return;
		}
		
		//����ͼƬ
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
