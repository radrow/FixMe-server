package services;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class SenderTool {
    public static void sendEmail(final String message, final String email) {
        new Thread(() -> {
            try {
                GMailSender sender = new GMailSender("usuwacz.awarii@onet.pl",
                        "mimuwFTW123");
                sender.sendMail("Zgłoszenie o awarii", message,
                        "usuwacz.awarii@onet.pl", email);
                //lk385775
                System.out.println("Wysłano wiadomość: " + message + " do użytkownika: " + email);
            } catch (Exception e) {
//                    System.err.println(e.toString());
                e.printStackTrace();
            }
        }).start();

    }
}