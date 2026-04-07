package com.thelilyofthenile.backend.service;

import com.thelilyofthenile.backend.model.Order;
import com.thelilyofthenile.backend.model.OrderItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${resend.api-key:}")
    private String resendApiKey;

    @Value("${mail.from:}")
    private String fromAddress;

    public void sendOrderConfirmation(Order order) {
        if (resendApiKey == null || resendApiKey.isBlank()) {
            System.out.println("Resend API key not configured — skipping order confirmation for order #" + order.getId());
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        String from = (fromAddress == null || fromAddress.isBlank()) ? "onboarding@resend.dev" : fromAddress;

        Map<String, Object> body = Map.of(
                "from", from,
                "to", order.getCustomer().getEmail(),
                "subject", "Your order is confirmed — The Lily of the Nile",
                "html", buildHtml(order)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity("https://api.resend.com/emails", request, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
        }
    }

    private String buildHtml(Order order) {
        StringBuilder rows = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            rows.append(String.format("""
                <tr>
                  <td style="padding:12px 0;border-bottom:1px solid #e8ddd5;color:#4a2e2e;font-family:'Georgia',serif;font-size:14px;">%s</td>
                  <td style="padding:12px 0;border-bottom:1px solid #e8ddd5;color:#8a6a5a;font-size:13px;text-align:center;">%d</td>
                  <td style="padding:12px 0;border-bottom:1px solid #e8ddd5;color:#4a2e2e;font-size:13px;text-align:right;">$%.2f</td>
                </tr>
                """,
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPriceAtPurchase() * item.getQuantity()
            ));
        }

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="margin:0;padding:0;background:#fdf9f5;font-family:Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="background:#fdf9f5;padding:48px 0;">
                <tr>
                  <td align="center">
                    <table width="560" cellpadding="0" cellspacing="0" style="background:#ffffff;border:1px solid #e8ddd5;">

                      <!-- Header -->
                      <tr>
                        <td style="background:#1a0f0f;padding:32px 40px;text-align:center;">
                          <p style="margin:0;font-family:'Georgia',serif;font-size:11px;letter-spacing:.25em;text-transform:uppercase;color:#c9a96e;">The Lily of the Nile</p>
                        </td>
                      </tr>

                      <!-- Body -->
                      <tr>
                        <td style="padding:40px;">
                          <p style="margin:0 0 8px;font-family:'Georgia',serif;font-size:22px;font-weight:normal;color:#1a0f0f;">Order Confirmed</p>
                          <p style="margin:0 0 32px;font-size:13px;color:#8a6a5a;line-height:1.6;">
                            Thank you, %s. Your order <strong>#%d</strong> has been received and will be prepared with care.
                          </p>

                          <!-- Items -->
                          <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:24px;">
                            <tr>
                              <th style="text-align:left;font-size:9px;letter-spacing:.2em;text-transform:uppercase;color:#8a6a5a;padding-bottom:12px;border-bottom:1px solid #1a0f0f;">Item</th>
                              <th style="text-align:center;font-size:9px;letter-spacing:.2em;text-transform:uppercase;color:#8a6a5a;padding-bottom:12px;border-bottom:1px solid #1a0f0f;">Qty</th>
                              <th style="text-align:right;font-size:9px;letter-spacing:.2em;text-transform:uppercase;color:#8a6a5a;padding-bottom:12px;border-bottom:1px solid #1a0f0f;">Price</th>
                            </tr>
                            %s
                          </table>

                          <!-- Total -->
                          <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:32px;">
                            <tr>
                              <td style="font-size:13px;color:#4a2e2e;">Total</td>
                              <td style="text-align:right;font-family:'Georgia',serif;font-size:18px;color:#1a0f0f;">$%.2f</td>
                            </tr>
                          </table>

                          <p style="margin:0;font-size:13px;color:#8a6a5a;line-height:1.7;">
                            We'll be in touch once your arrangement is ready. If you have any questions, simply reply to this email.
                          </p>
                        </td>
                      </tr>

                      <!-- Footer -->
                      <tr>
                        <td style="background:#fdf5ed;padding:24px 40px;text-align:center;border-top:1px solid #e8ddd5;">
                          <p style="margin:0;font-size:11px;letter-spacing:.15em;text-transform:uppercase;color:#b8a89a;">The Lily of the Nile</p>
                        </td>
                      </tr>

                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """,
                order.getCustomer().getUsername(),
                order.getId(),
                rows.toString(),
                order.getTotalAmount()
        );
    }
}
