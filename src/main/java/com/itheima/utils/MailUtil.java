package com.itheima.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * 邮件发送的工具类
 * @author HUAWEI
 *
 */
public class MailUtil {

    /**
     * 创建简单的文本邮件
     * @param session
     * @param mailfrom 邮件发送方地址  eg.xx@qq.com
     * @param mailTo   邮件接收方地址，eg.xx@qq.com
     * @param mailTittle 邮件标题
     * @param mailText   邮件文本主体
     * @return
     * @throws Exception
     */
    public static MimeMessage createSimpleMail(Session session, String mailfrom, String mailTo, String mailTittle,
                                               String mailText) throws Exception {
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明邮件的发件人
        message.setFrom(new InternetAddress(mailfrom));
        // 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        // 邮件的标题
        message.setSubject(mailTittle);
        // 邮件的文本内容
        message.setContent(mailText, "text/html;charset=UTF-8");
        // 返回创建好的邮件对象
        return message;
    }

    /**
     * 创建图文邮件
     * @param session
     * @param mailMap 邮件的所有内容的包装
     * @param hasfujian 若为true，mailMap中必须包含相应的信息
     * @return
     * @throws MessagingException
     */
    public static MimeMessage imageMail(Session session,Map<String,Object> mailMap,boolean hasfujian) throws MessagingException {
        //消息的固定信息
        MimeMessage mimeMessage = new MimeMessage(session);
        //邮件发送人
        mimeMessage.setFrom(new InternetAddress(mailMap.get("from").toString()));
        //邮件接收人，可以同时发送给很多人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailMap.get("to").toString()));
        mimeMessage.setSubject(mailMap.get("title").toString()); //邮件主题

        //图片
        MimeBodyPart body1 = new MimeBodyPart();
        body1.setDataHandler(new DataHandler(new FileDataSource(mailMap.get("datasrc").toString())));
        body1.setContentID(mailMap.get("contentid").toString()); //图片设置ID

        //文本
        MimeBodyPart body2 = new MimeBodyPart();
        body2.setContent(mailMap.get("content"),"text/html;charset=utf-8");

        if(hasfujian){
            List<Map<String,String>> listmap = (List<Map<String, String>>) mailMap.get("fujian");
            List<MimeBodyPart> fjlist = new ArrayList<MimeBodyPart>();
            for(Map<String, String> item:listmap){
                //附件
                MimeBodyPart body = new MimeBodyPart();
                body.setDataHandler(new DataHandler(new FileDataSource(item.get("filesrc").toString())));
                body.setFileName(item.get("filename")); //附件设置名字
                fjlist.add(body);
                //拼装邮件正文内容
                MimeMultipart multipart1 = new MimeMultipart();
                multipart1.addBodyPart(body1);
                multipart1.addBodyPart(body2);
                multipart1.setSubType("related"); //文本和图片内嵌成功！

                //new MimeBodyPart().setContent(multipart1); //将拼装好的正文内容设置为主体
                MimeBodyPart contentText =  new MimeBodyPart();
                contentText.setContent(multipart1);

                //拼接附件
                MimeMultipart allFile =new MimeMultipart();
                for(MimeBodyPart mbp:fjlist){
                    allFile.addBodyPart(mbp); //附件
                }
                allFile.addBodyPart(contentText);//正文
                allFile.setSubType("mixed"); //正文和附件都存在邮件中，所有类型设置为mixed；

                //放到Message消息中
                mimeMessage.setContent(allFile);
                mimeMessage.saveChanges();//保存修改
            }
        }else{
            //拼装邮件正文内容
            MimeMultipart multipart1 = new MimeMultipart();
            multipart1.addBodyPart(body1);
            multipart1.addBodyPart(body2);
            multipart1.setSubType("related"); //1.txt.文本和图片内嵌成功！

            //new MimeBodyPart().setContent(multipart1); //将拼装好的正文内容设置为主体
            MimeBodyPart contentText =  new MimeBodyPart();
            contentText.setContent(multipart1);

            //拼接附件
            MimeMultipart allFile =new MimeMultipart();
            allFile.addBodyPart(contentText);//正文
            allFile.setSubType("mixed"); //正文和附件都存在邮件中，所有类型设置为mixed；

            //放到Message消息中
            mimeMessage.setContent(allFile);
            mimeMessage.saveChanges();//保存修改
        }
        return mimeMessage;
    }

    /**
     * 发送邮件方法封装
     * @param contentMap 邮件配置的map
     * @param imgmial 是否为带图邮件
     * @param hasfujian 是否有附件
     * @throws Exception
     */
    public static void sendMail(final Map<String, Object> contentMap,boolean imgmial,boolean hasfujian) throws Exception{
        Properties prop = new Properties();
        //设置QQ邮件服务器
        prop.setProperty("mail.host", contentMap.get("host").toString());
        //邮件发送协议
        prop.setProperty("mail.transport.protocol", "smtp");
        //需要验证用户名密码
        prop.setProperty("mail.smtp.auth", "true");

        //关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
         prop.put("mail.smtp.ssl.socketFactory", sf);

        //使用JavaMail发送邮件的5个步骤
        //1.txt、创建定义整个应用程序所需的环境信息的Session对象
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication(contentMap.get("from").toString(),
                        contentMap.get("password").toString());
            }
        });
        //可以通过session开启Dubug模式，查看所有的过程
        session.setDebug(true);

        //2.获取连接对象，通过session对象获得Transport，需要捕获或者抛出异常；
        Transport tp = session.getTransport();
        //3.连接服务器,需要抛出异常；
        tp.connect(contentMap.get("host").toString(),contentMap.get("from").toString(),contentMap.get("password").toString());
        MimeMessage mimeMessage = null;
        //4.连接上之后我们需要发送邮件；
        if(imgmial){
            mimeMessage = imageMail(session,contentMap,hasfujian);
        }else{
            mimeMessage = createSimpleMail(session, contentMap.get("from").toString(), contentMap.get("to").toString(), contentMap.get("title").toString(), contentMap.get("content").toString());
        }
        //5.发送邮件
        tp.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        //6.关闭连接
        tp.close();
    }
}