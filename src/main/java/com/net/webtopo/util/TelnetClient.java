package com.net.webtopo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class TelnetClient {

    private String prompt = "#";        //结束标识字符串,Windows中是>,Linux中是#
    private char promptChar = '>';        //结束标识字符
    private org.apache.commons.net.telnet.TelnetClient telnet;
    private InputStream in;                // 输入流,接收返回信息
    private PrintStream out;        // 向服务器写入 命令

    /**
     * @param termtype        协议类型：VT1vv  00、VT52、VT220、VTNT、ANSI
     * @param prompt        结果结束标识
     */
    public TelnetClient(String termtype, String prompt){
        telnet = new org.apache.commons.net.telnet.TelnetClient(termtype);
        setPrompt(prompt);
    }

    public TelnetClient(String termtype){
        telnet = new org.apache.commons.net.telnet.TelnetClient(termtype);
    }

    public TelnetClient(){
        telnet = new org.apache.commons.net.telnet.TelnetClient();
    }

    /**
     * 登录到目标主机
     * @param ip
     * @param port
     * @param password
     */
    public Boolean login(String ip, int port,String password){
        try {
            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            readUntil("Password:");
            write(password);
            readUntil(">");
            write("enable");
            readUntil("Password:");
            write(password);
            if(readUntil("#")!=null){
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 读取分析结果
     *
     * @param pattern        匹配到该字符串时返回结果
     * @return
     */
    public String readUntil(String pattern) {
        StringBuffer sb = new StringBuffer();
        try {
            char lastChar = (char)-1;
            boolean flag = pattern!=null&&pattern.length()>0;
            if(flag)
                lastChar = pattern.charAt(pattern.length() - 1);
            char ch;
            int code = -1;
            while ((code = in.read()) != -1) {
                ch = (char)code;
                sb.append(ch);
                //匹配到结束标识时返回结果
                if (flag) {
                    if (ch == lastChar && sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }else{
                    //如果没指定结束标识,匹配到默认结束标识字符时返回结果
                    if(ch == promptChar)
                        return sb.toString();
                }
                //登录失败时返回结果
                if(sb.toString().contains("Login Failed")){
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    /**
     * 发送命令
     *
     * @param value
     */
    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送命令,返回执行结果
     *
     * @param command
     * @return
     */
    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        System.out.println(command);
//        return command;
    }
    /**
     * 关闭连接
     */
    public void distinct(){
        try {
            if(telnet!=null&&!telnet.isConnected())
                telnet.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrompt(String prompt) {
        if(prompt!=null){
            this.prompt = prompt;
            this.promptChar = prompt.charAt(prompt.length()-1);
        }
    }

    public static void main(String[] args) {
        TelnetClient telnet = new TelnetClient("VT220","#");        //Windows,用VT220,否则会乱码
        if(telnet.login("192.168.0.1", 23, "CISCO")){
            System.out.println(233);
            System.out.println("login");
            String rs = telnet.sendCommand("sh ip route");
            System.out.println(rs);
            try {
                rs = new String(rs.getBytes("ISO-8859-1"),"GBK");        //转一下编码
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(rs);
        }
    }
}

