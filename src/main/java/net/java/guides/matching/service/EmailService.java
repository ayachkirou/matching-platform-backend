package net.java.guides.matching.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, VerificationData> verificationCodes = new HashMap<>();

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Classe interne pour stocker le code et le timestamp
    private static class VerificationData {
        String code;
        long timestamp;

        VerificationData(String code) {
            this.code = code;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return (System.currentTimeMillis() - timestamp) > 15 * 60 * 1000; // 15 minutes
        }
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Vérification de votre email - TalentMatch");
        message.setText("Votre code de vérification est : " + code +
                "\n\nCe code expirera dans 15 minutes." +
                "\n\nSi vous n'avez pas demandé cette vérification, veuillez ignorer cet email.");

        mailSender.send(message);
        verificationCodes.put(toEmail, new VerificationData(code));
    }

    public boolean verifyCode(String email, String code) {
        VerificationData data = verificationCodes.get(email);
        if (data == null || data.isExpired()) {
            verificationCodes.remove(email);
            return false;
        }
        return data.code.equals(code);
    }

    public void removeCode(String email) {
        verificationCodes.remove(email);
    }
}