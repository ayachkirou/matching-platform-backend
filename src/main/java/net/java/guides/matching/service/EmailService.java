package net.java.guides.matching.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Vérification de votre email - TalentMatch ");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "    <meta charset='UTF-8'>"
                    + "    <style>"
                    + "        body { "
                    + "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; "
                    + "            line-height: 1.6; "
                    + "            color: #333333; "
                    + "            margin: 0; "
                    + "            padding: 20px; "
                    + "            background-color: #f8fafc; "
                    + "        }"
                    + "        .container { "
                    + "            max-width: 600px; "
                    + "            margin: 0 auto; "
                    + "            background: white; "
                    + "            border-radius: 12px; "
                    + "            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); "
                    + "            overflow: hidden; "
                    + "        }"
                    + "        .header { "
                    + "            background: linear-gradient(135deg, #10b981 0%, #059669 100%); "
                    + "            padding: 30px; "
                    + "            text-align: center; "
                    + "            color: white; "
                    + "        }"
                    + "        .logo { "
                    + "            font-size: 28px; "
                    + "            font-weight: bold; "
                    + "            margin-bottom: 10px; "
                    + "        }"
                    + "        .content { "
                    + "            padding: 40px; "
                    + "        }"
                    + "        .code-container { "
                    + "            background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%); "
                    + "            border: 2px dashed #0ea5e9; "
                    + "            border-radius: 12px; "
                    + "            padding: 25px; "
                    + "            text-align: center; "
                    + "            margin: 30px 0; "
                    + "        }"
                    + "        .verification-code { "
                    + "            font-size: 32px; "
                    + "            font-weight: bold; "
                    + "            color: #0369a1; "
                    + "            letter-spacing: 3px; "
                    + "            text-shadow: 1px 1px 2px rgba(0,0,0,0.1); "
                    + "        }"
                    + "        .warning { "
                    + "            background: #fef3c7; "
                    + "            border-left: 4px solid #f59e0b; "
                    + "            padding: 15px; "
                    + "            margin: 20px 0; "
                    + "            border-radius: 6px; "
                    + "        }"
                    + "        .footer { "
                    + "            background: #f1f5f9; "
                    + "            padding: 25px; "
                    + "            text-align: center; "
                    + "            color: #64748b; "
                    + "            font-size: 14px; "
                    + "            border-top: 1px solid #e2e8f0; "
                    + "        }"
                    + "        .button { "
                    + "            display: inline-block; "
                    + "            padding: 12px 24px; "
                    + "            background: #10b981; "
                    + "            color: white; "
                    + "            text-decoration: none; "
                    + "            border-radius: 6px; "
                    + "            font-weight: 600; "
                    + "            margin: 10px 0; "
                    + "        }"
                    + "        .text-primary { color: #10b981; }"
                    + "        .text-secondary { color: #64748b; }"
                    + "        .text-center { text-align: center; }"
                    + "        .mb-20 { margin-bottom: 20px; }"
                    + "    </style>"
                    + "</head>"
                    + "<body>"
                    + "    <div class='container'>"
                    + "        <div class='header'>"
                    + "            <div class='logo'> TalentMatch</div>"
                    + "            <h2>Vérification de votre email</h2>"
                    + "        </div>"
                    + "        "
                    + "        <div class='content'>"
                    + "            <h2 class='text-center'>Bonjour </h2>"
                    + "            "
                    + "            <p>Nous avons reçu une demande de vérification pour votre adresse email. "
                    + "            Utilisez le code de vérification ci-dessous pour compléter votre inscription :</p>"
                    + "            "
                    + "            <div class='code-container'>"
                    + "                <p style='margin-top: 0; color: #475569;'>Votre code de vérification :</p>"
                    + "                <div class='verification-code'>" + code + "</div>"
                    + "                <p style='margin-bottom: 0; color: #475569; font-size: 14px;'>"
                    + "                     Valable pendant 15 minutes"
                    + "                </p>"
                    + "            </div>"
                    + "            "
                    + "            <div class='warning'>"
                    + "                <strong> Important :</strong>"
                    + "                <ul style='margin: 10px 0; padding-left: 20px;'>"
                    + "                    <li>Ne partagez jamais ce code avec personne</li>"
                    + "                    <li>TalentMatch ne vous demandera jamais votre code par téléphone</li>"
                    + "                    <li>Ce code expirera automatiquement dans 15 minutes</li>"
                    + "                </ul>"
                    + "            </div>"
                    + "            "
                    + "            <p>Si vous n'avez pas demandé cette vérification, veuillez ignorer cet email ou "
                    + "            <a href='mailto:support@talentmatch.com' style='color: #10b981;'>contacter notre support</a>.</p>"
                    + "            "
                    + "            <div class='text-center'>"
                    + "                <p class='text-secondary'>Besoin d'aide ? Consultez notre <a href='https://talentmatch.com/help' style='color: #10b981;'>centre d'aide</a></p>"
                    + "            </div>"
                    + "        </div>"
                    + "        "
                    + "        <div class='footer'>"
                    + "            <p>© 2025 TalentMatch. Tous droits réservés.</p>"
                    + "        </div>"
                    + "    </div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            verificationCodes.put(toEmail, new VerificationData(code));

        } catch (Exception e) {
            // Fallback vers SimpleMailMessage en cas d'erreur
            SimpleMailMessage fallbackMessage = new SimpleMailMessage();
            fallbackMessage.setTo(toEmail);
            fallbackMessage.setSubject("Vérification de votre email - TalentMatch");
            fallbackMessage.setText("Votre code de vérification est : " + code +
                    "\n\nCe code expirera dans 15 minutes." +
                    "\n\nSi vous n'avez pas demandé cette vérification, veuillez ignorer cet email.");

            mailSender.send(fallbackMessage);
            verificationCodes.put(toEmail, new VerificationData(code));
        }
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

    public void sendCompanyConfirmationEmail(String toEmail, String nomEntreprise) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Confirmation d'inscription - TalentMatch");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "    <meta charset='UTF-8'>"
                    + "    <style>"
                    + "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }"
                    + "        .container { max-width: 600px; margin: 0 auto; }"
                    + "        .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e5e7eb; color: #6b7280; font-size: 14px; }"
                    + "    </style>"
                    + "</head>"
                    + "<body>"
                    + "    <div class='container'>"
                    + "        <h2>Bienvenue sur TalentMatch !</h2>"
                    + "        <p>Cher(e) représentant(e) de <strong>" + nomEntreprise + "</strong>,</p>"
                    + "        <p>Votre inscription a été reçue avec succès.</p>"
                    + "        <p>Votre compte est en cours de vérification. Vous recevrez un email une fois votre compte approuvé.</p>"
                    + "        <p>Merci pour votre confiance.</p>"
                    + "        <div class='footer'>"
                    + "            <p>L'équipe TalentMatch</p>"
                    + "            <p>© 2025 TalentMatch. Tous droits réservés.</p>"
                    + "        </div>"
                    + "    </div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            // Log l'erreur mais ne pas la propager pour ne pas bloquer l'inscription
            System.out.println("Erreur lors de l'envoi de l'email de confirmation: " + e.getMessage());
        }
    }
}