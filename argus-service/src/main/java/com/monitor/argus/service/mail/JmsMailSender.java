package com.monitor.argus.service.mail;

import com.monitor.email.ws.client.EmailClient;
import com.monitor.email.ws.client.MailRequest;
import com.monitor.email.ws.client.Receiver;
import com.monitor.email.ws.client.SendResult;
import com.monitor.argus.bean.MailEntity;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class JmsMailSender {
    private Logger log = LoggerFactory.getLogger(JmsMailSender.class);


    /** json转换器 */
    private ObjectMapper jsonMapper = new ObjectMapper();

    public EmailClient getMailClient() {
        return mailClient;
    }

    public void setMailClient(EmailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Autowired
    private EmailClient mailClient;

    /** 邮件发送地址 */
    private String MAIL_FROM="noreply@monitor.com";
    /** 邮件附件访问协议 */
    private static String MAIL_ATTACH_PREFIX;

//    @Value("${email.attach.prefix}")
//    private void setMAIL_ATTACH_PREFIX(String mail_attach_prefix) {
//        MAIL_ATTACH_PREFIX = mail_attach_prefix;
//    }


    /**
     * 调用邮件服务平台发送邮件
     * 
     * @param mqMail
     */
    private boolean sendEmail(Email mqMail) {
        boolean flag = false;
        try {
                log.info("发送邮件。邮件对象:(" + jsonMapper.writeValueAsString(mqMail) + ")");
            flag = sendMailToMailService(mqMail);
        } catch (Exception e) {
            try {
                log.error("发送邮件出错。邮件对象:(" + jsonMapper.writeValueAsString(mqMail) + ")", e);
            } catch (Exception e1) {
                log.error("发送邮件出错。to:" + mqMail.getAddressee() + " subject:" + mqMail.getSubject(), e1);
            }
        }
        return flag;
    }


    public static void main(String[] args) {
        Email email=new Email();
        email.setAddressee("feixue8@monitor.cn");
        email.setContent("test email");
        email.setSubject("test mail for subject");
        JmsMailSender sender=new JmsMailSender();
        Map<String,Object> mailType=new HashMap<>();
        mailType.put("EMAIL_TYPE","110110110");
        email.setModel(mailType);
        sender.setMailClient(new EmailClient());
        sender.sendEmail(email);
    }

    public void sendAsyc(final MailEntity mailEntity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(mailEntity);
            }
        }).start();
    }
   public void send(MailEntity mailEntity){
       Email email=new Email();
       email.setFrom("noreply@monitor.cn");
       email.setAddressee(mailEntity.getReceivers());
       email.setContent("alarm mail");
       email.setSubject(mailEntity.getTitle());
       email.setModel(mailEntity.getMailExtInfo());
       sendEmail(email);

   }

   public boolean sendCommonMail(Email email) {
       return sendMailToMailService(email);
   }
    /**
     * 调用邮件平台Webservice服务，将邮件发送给邮件平台
     *
     * @param email 邮件对象Bean
     */
    @Deprecated
    private boolean sendMailToMailService(Email email) {
        log.info("sendMailByMailService" + "Start...");
        ResourceBundle bundle = PropertyResourceBundle.getBundle("resourcesconfig");
        log.info(bundle.getString("emailServerUrl"));
        // 邮件发送状态记录标识,false:发送失败，true：发送成功
        boolean sendFlag = false;
        // 验证传输给邮件平台的数据类型为非Java基本数据类型，自定义数据类型Bean不允许进行数据传输。
        // Map<String, Object> datas = email.getModel();
        // for(Object o : datas.values()) {
        // if (!o.getClass().isPrimitive() &&
        // !o.getClass().isInstance(String.class)) {
        // logger.error("o.getClass()" + o.getClass());
        // logger.error(logger.getName() + "传输数据为非基本数据类型，请修改Model内的Value数据类型");
        // return sendFlag;
        // }
        // }
        MailRequest request = createMailRequest(email);
        // 业务系统ID
        request.setOrgNo("02");
        // 邮件模板ID
        // 模板文件类型转换
        request.setMailTemplateCode(email.getModel().get("EMAIL_TYPE").toString());
        SendResult result = null;
        try {
            result = mailClient.sendMailPackage(request,
                    EmailType.getSendFlag(email.getModel().get("EMAIL_TYPE").toString()));
            log.info("邮件平台返回的结果:" + "发送状态：" + result.isSuccess() + "异步方式返回的批次号：batchCode" + result.getBatchCode());
        } catch (Exception e) {
            log.error("发送邮件出错,连接邮件平台出错或者网络异常", e);
        } finally {
            if (null == result) {
                log.info("发送邮件失败");
            } else {
                // 打印发送邮件的基本情况
                log.info(result.isSuccess() + " " + result.getExceptionType() + " " + result.getBatchCode());
                // 邮件成功发送重置发送状态， result的isSuccess状态获取的是邮件平台的发送状态,如果失败请
                sendFlag = result.isSuccess();
                if (sendFlag) {
                    log.info("发送邮件成功");
                } else {
                    log.info("发送邮件失败，请联系邮件平台，邮件已经发送到邮件平台，发送失败原因请查看邮件平台信息");
                }
            }
        }
        log.info("sendMailByMailService" + "End...");
        return sendFlag;
    }

    /**
     * 将Email内容翻译给邮件服务平台
     * 
     * @param mail
     * @return
     */
    private static MailRequest createMailRequest(Email mail) {
        MailRequest request = new MailRequest();
        // 初始化邮件接收者
        List<Receiver> rs = new ArrayList<Receiver>();
        Receiver r = null;
        // 初始化邮件抄送人，但是一般不要设置抄送人，不想让邮件接收者知道的情况下，使用BCC秘抄的方式,目前邮件平台还不支持秘抄
        // List<String> ccs = new ArrayList<String>();
        // ccs.add("danzhao3@monitor.cn");
        // r.setCc(ccs);
        // 获取发送地址，并翻译邮件内容转化有邮件平台格式，转发给邮件平台。
        String[] toAddress = mail.getAddress();
        Map<String, String> inline = new HashMap<String, String>();
        if (MapUtils.isNotEmpty(mail.getInLineFile())) {
            for (Iterator<Map.Entry<String, String>> it = mail.getInLineFile().entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                inline.put(entry.getKey(), new File(entry.getValue()).getAbsolutePath());
            }
        }
        Map<String, String> attach = new HashMap<String, String>();
        if (MapUtils.isNotEmpty(mail.getAttachmentFiles())) {
            for (Iterator<Map.Entry<String, String>> it = mail.getAttachmentFiles().entrySet().iterator(); it
                    .hasNext();) {
                Map.Entry<String, String> entry = it.next();
                attach.put(entry.getKey(), MAIL_ATTACH_PREFIX + entry.getValue());
            }
        }
        for (String to : toAddress) {
            r = new Receiver();
            r.setEmailAddress(to);
            r.setDatas(mail.getModel());
            r.setAttachUrls(attach);
            r.setInlineUrls(inline);
            rs.add(r);
        }
        request.setReceivers(rs);
        return request;
    }
}
