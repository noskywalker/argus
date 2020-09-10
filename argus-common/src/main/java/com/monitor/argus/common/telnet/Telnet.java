package com.monitor.argus.common.telnet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
 
 
/**
 * Telnet简单实现
 * 
 * @author xueliang 2010.11.6
 * 
 */
public class Telnet {
	private static final byte SB = (byte) 250;// 子选项开始
	private static final byte SE = (byte) 240;// 子选项结束
	private static final byte WILL = (byte) 251;// 选项协商
	private static final byte WONT = (byte) 252;// 选项协商
	private static final byte DO = (byte) 253;// 选项协商
	private static final byte DONT = (byte) 254;// 选项协商
	private static final byte IAC = (byte) 255;// 数据字节255
	private static final byte ECHO = (byte) 1;// 回显
	private static final byte IS = (byte) 0;// 是
	private static final byte SUPPRESS = (byte) 3;// 抑制继续进行
	private static final byte TT = (byte) 24;// 终端类型
	private InputStream is;
	private OutputStream os;
	private Socket client;
	private byte[] readBuffer = new byte[1024];
 
	/**
	 * 打开telnet连接
	 * @param ip
	 * @param port 23
	 * @return
	 * @throws CmdException 
	 */
	public Telnet(String ip){
		this(ip, 23);
	}
 
	/**
	 * 打开telnet连接
	 * @param ip
	 * @param port
	 * @return
	 * @throws CmdException 
	 */
	public Telnet(String ip, int port){
		try {
			client = new Socket(ip, port);
			client.setSoTimeout(5000);//设置is的read方法阻塞时间为5秒
			is = client.getInputStream();
			os = client.getOutputStream();
		} catch (Exception e) {
			this.close();
		} 
	}
	/**
	 * 读取回显，并进行telnet协商
	 * @return
	 * @throws IOException
	 */
	public String recieveEcho() throws IOException {
		int len = is.read(this.readBuffer);
		if(is.available() == 0)
			return "";
		ArrayList<Byte> bsList = new ArrayList<Byte>();
		ArrayList<Byte> cmdList = new ArrayList<Byte>();
		for (int i = 0; i < len; i++) {
			int b = this.readBuffer[i] & 0xff;//&0xff是为了防止byte的255溢出,java中byte的取值是-128~127
			if (b != 255) {
				if (b == '\n' || b == '\0') {// NVT中行结束符以'\r\n'表示，回车以'\r\0表示'
					continue;
				}
				bsList.add((byte) b);
				continue;
			}
			cmdList.add(IAC);
			switch (this.readBuffer[++i] & 0xff) {
			case 251:// 服务器想激活某选项
				if ((readBuffer[++i] & 0xff) == 1) {// 同意回显
					cmdList.add(DO);
					cmdList.add(ECHO);
				} else if ((readBuffer[i] & 0xff) == 3) {// 同意抑制继续执行
					cmdList.add(DO);
					cmdList.add(SUPPRESS);
					// cmdList.add(GA);
				} else {// 不同意其他类型协商
					cmdList.add(DONT);
					cmdList.add(readBuffer[i]);
				}
				break;
			case 253:// 服务器想让客户端发起激活某选项
				if ((readBuffer[++i] & 0xff) == 24) {// 终端类型
					cmdList.add(WONT);// 同意激活终端类型协商
					cmdList.add(TT);
				} else if ((readBuffer[i] & 0xff) == 1) {
					cmdList.add(WILL);
					cmdList.add(ECHO);
				} else {
					cmdList.add(WONT);// 不同意其他类型协商
					cmdList.add(readBuffer[i]);
				}
				break;
			case 250:// 子选项开始
				cmdList.add(SB);
				if ((readBuffer[++i] & 0xff) == 24
						&& (readBuffer[++i] & 0xff) == 1) {// 发送你的终端类型
					cmdList.add(TT);
					cmdList.add(IS);// 我的终端类型是
					cmdList.add((byte) 'V');
					cmdList.add((byte) 'T');
					cmdList.add((byte) '1');
					cmdList.add((byte) '0');
					cmdList.add((byte) '0');
				}
				break;
			case 240:// 子选项结束
				cmdList.add(SE);
				break;
			case 252:// 必须同意
				cmdList.add(DONT);
				cmdList.add(readBuffer[++i]);
				break;
			case 254:// 必须同意
				cmdList.add(WONT);
				cmdList.add(readBuffer[++i]);
				break;
			}
		}
		// 如果有协商则向服务端发送协商选项
		if (cmdList.size() > 0) {
			byte[] writeBuffer = new byte[cmdList.size()];
			for (int i = 0; i < cmdList.size(); i++) {
				writeBuffer[i] = cmdList.get(i);
			}
			os.write(writeBuffer);
		}
		// 组织回显字符
		int size = bsList.size();
		String str = "";
		if (size > 0) {
			byte[] bs = new byte[size];
			for (int i = 0; i < size; i++) {
				bs[i] = bsList.get(i).byteValue();
			}
			str = new String(bs, "gbk");
		}
		// 以下测试时打开
		// System.out.print("read===== ");
		// for (int i = 0; i < len; i++) {
		// System.out.print(readBuffer[i]&0xff);
		// System.out.print(" ");
		// }
		// System.out.println();
		// if(cmdList.size()>0){
		// System.out.print("write==== ");
		// for (int i = 0; i < cmdList.size(); i++) {
		// System.out.print(cmdList.get(i) & 0xff);
		// System.out.print(" ");
		// }
		// System.out.println();
		// }
		return str;
	}
 
	/**
	 * 直接发送命令，不发送回车键
	 * @param cmd
	 */
	public void sendWithoutCR(String cmd) {
		try {
			os.write(cmd.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 命令中不要包括回车、换行
	 * @param cmd
	 */
	public void sendCmd(String cmd) {
		cmd += "\r\0";
		try {
			os.write(cmd.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * 按字节发送命令，数组中不要包括回车、换行
	 * @param cmd
	 */
	public void sendCmd(List<Byte> cmdList) {
		if(cmdList == null)return;
		int size = cmdList.size();
		byte[] send = null;
		if(cmdList.get(size-1) == '\r'){
			send = new byte[size+1];
			send[size] = '\0';
		}else{
			send = new byte[size];
		}
		for (int i = 0; i < size; i++) {
			send[i] = cmdList.get(i);
		}
		try {
			os.write(send);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * 关闭telnet连接
	 */
	public void close() {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取期望值，使用默认超时时间15秒
	 * @param keyWords
	 * @return
	 */
	public String readKeyWords(String... keyWords){
		return this.readKeyWords(15000, keyWords);
	}
	/**
	 * 读取期望值
	 * @param timeOut 超时时间
	 * @param keyWords
	 * @return
	 * @throws CmdException 
	 */
	public String readKeyWords(long timeOut,String... keyWords){
		String rv = "";
		long nextTime = 0;
		long endTime = System.currentTimeMillis() + timeOut;
		do {
			try{
				String _rv = this.recieveEcho();
				rv += _rv;
			}catch(IOException e){
				nextTime = endTime - System.currentTimeMillis() ;
			}
		} while (!this.findKeyWord(keyWords, rv) && nextTime >= 0);
		if(nextTime < 0)
			System.err.println("Read TimeOut...Echo:\n"+rv);
		return rv;
	}
	
	/**
	 * 查找关键字
	 * @param keyWords
	 * @param str
	 * @return
	 */
	private boolean findKeyWord(String[] keyWords,String str){
		if(keyWords == null || str == null)return false;
		for (int i = 0; i < keyWords.length; i++) {
			if(str.indexOf(keyWords[i]) != -1)
				return true;
		}
		return false;
	}
	public static void main(String[] args) {
		Telnet telnet = new Telnet("10.110.111.193");
		String str = telnet.readKeyWords(":");
		System.out.println(str);
		telnet.sendCmd("boco");
		str = telnet.readKeyWords(":");
		telnet.sendCmd("Ab123456");
		str = telnet.readKeyWords("$");
		System.out.println(str);
		telnet.sendCmd("ls");
		str = telnet.readKeyWords(10000,"$");
		System.out.println(str);
		telnet.close();
	}
}