package com.continuum.cucumber.utils;

import com.continuum.cucumber.shell.Application;
import com.continuum.cucumber.shell.Shell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
public class HtmlEmailSender {
    static String absolutePath = new File("").getAbsolutePath();

    public static void sendEmail(final String userName, final String password, String receiver, String subject, String message, File report) {
        try {
            Session session = Session.getInstance(System.getProperties(), new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(userName));
            msg.setRecipients(RecipientType.TO, InternetAddress.parse(receiver));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(message);
            addReportToMailBody(msg, report);
            Transport.send(msg);
            log.info("********Sending report mail**********");
        } catch (MessagingException var9) {
            log.error("****************Unable to Send Email", var9);
        }
    }

    public static void addReportToMailBody(Message msg, File report) throws MessagingException {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(new FileReader(report), writer);
            msg.setContent(writer.toString(), "text/html");
        } catch (IOException var4) {
            log.error("Not able to retrieve cucumber report file", var4);
        }
    }

    public static void sendReport() {
        final Application.Mail mail = Shell.Application.getMail();
        if (!mail.isReportMail())
            return;
        final String name = Shell.Application.getName();

        String subject = "Automation Report for " + name, message = subject;
        JSONObject buildNumberInfo = Artifactory.getLatestBuildNumberOfRepository();
        if (buildNumberInfo != null && !buildNumberInfo.isEmpty())
            subject += " on " + Artifactory.formTheBuildVersionsForReporting(Artifactory.getLatestBuildNumberOfRepository());
        if (name.equalsIgnoreCase("Platform-Alerting"))
            message += "\n For more details visit:  http://qedashboard.continuum.net/qemetrics/status.php";

        Path cucumberReport = Paths.get(absolutePath, Shell.Application.getReportOutputDirectory(), "cucumber-html-reports/overview-features.html");
        sendEmail(mail.getReportUser(), mail.getReportPassword(), mail.getReportReceiver(), subject, message, cucumberReport.toFile());
    }
}