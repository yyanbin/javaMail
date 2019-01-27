package email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Created by 17988 on 2019/1/23.
 */
public class SendEmailHeadle {
    private  final  static Logger logger = LoggerFactory.getLogger(SendEmailHeadle.class);
    private final static String emailAcount = "";//发送邮件账户
    private final static String emailPwd = "";//发送邮件账户密码

    /**
     * 邮件发送
     * @param title 邮件标题
     * @param to 发送人
     * @param cc 抄送人
     * @param object 模板参数
     * @param templateName 模板名称
     * @throws Exception
     */
    public void sendEmailHtml(String title, String to, String cc, Object object,String templateName) throws Exception {
        logger.info("=====sendEmailHtml=start");
        //邮箱服务配置
        Properties prop = new Properties();
        //邮箱代理-qq邮箱
        prop.setProperty("mail.smtp.host", "smtp.exmail.qq.com");
        // SSL
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");

        prop.setProperty("mail.smtp.port", "465");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.setProperty("mail.smtp.starttls.required", "true");
        prop.setProperty("mail.transport.protocol", "smtp");
        logger.info("=====邮箱服务配置=end");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和密码连上邮件服务器
        ts.connect (emailAcount, emailPwd);
        //4、创建邮件
        Message message = createSimpleMail(session, title, to, cc, object,templateName);
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }


    /**
     *创建发送内容
     * @param session
     * @param title
     * @param to
     * @param cc
     * @param object
     * @param templateName
     * @return
     * @throws Exception
     */
    public static MimeMessage createSimpleMail(Session session, String title, String to, String cc, Object object, String templateName)
            throws Exception {
        logger.info("=====createSimpleMail=start");
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //邮件的发件人
        message.setFrom(new InternetAddress(emailAcount));
        //处理收件人
        Address[] internetAddressTo=null;
        if(StringUtils.isNotBlank(to)){
            internetAddressTo = InternetAddress.parse(to);
        }
        //处理抄送人
        Address[] internetAddressCc=null;
        if(StringUtils.isNotBlank(cc)){
            internetAddressCc = InternetAddress.parse(cc);
        }
        //邮件的收件人
        message.setRecipients(Message.RecipientType.TO, internetAddressTo);
        //邮件的抄送人
        message.setRecipients(Message.RecipientType.CC, internetAddressCc);
        //邮件的标题
        message.setSubject(title);
        //邮件的模板內容整合
        logger.info("=====getTemplatePath=="+getTemplatePath());
        Configuration configuration  = new Configuration();
        configuration.setDirectoryForTemplateLoading(new File(getTemplatePath()));
        configuration.setDefaultEncoding("UTF-8");
        Template template = configuration.getTemplate(templateName);
        template.setEncoding("UTF-8");
        logger.info("=====template="+template);
        StringWriter stringWriter = new StringWriter();
        template.process(object, stringWriter);
        message.setText(stringWriter.toString());
        return message;
    }



    /**
     * 匿名内部类获取路径
     * @return
     */
    public static String getTemplatePath() {
        String path = new Object() {
            public String getPath() {
                return this.getClass().getResource("/template").getPath();
            }
        }.getPath();
        return path;
    }
}
