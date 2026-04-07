package com.thelilyofthenile.backend.config;

import com.thelilyofthenile.backend.model.Category;
import com.thelilyofthenile.backend.model.Customer;
import com.thelilyofthenile.backend.model.Product;
import com.thelilyofthenile.backend.repository.CustomerRepository;
import com.thelilyofthenile.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Value("${admin.email:admin@thelilyofthenile.com}")
    private String adminEmail;

    @Value("${admin.password:changeme-admin-password}")
    private String adminPassword;

    public DataSeeder(ProductRepository productRepo, CustomerRepository customerRepo) {
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedAdmin();
        if (productRepo.count() > 0) return;

        productRepo.saveAll(List.of(
            Product.builder()
                .name("Nile Lily Bouquet")
                .description("A lush arrangement of white agapanthus and soft greenery, evoking the serenity of the Nile.")
                .price(48.00)
                .imageUrl("https://images.unsplash.com/photo-1561848355-890d054dc55a?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                .category(Category.FLOWERS)
                .stock(10)
                .build(),
            Product.builder()
                .name("Blush Peony Bundle")
                .description("Soft blush peonies tied with ivory ribbon — timeless and romantic.")
                .price(55.00)
                .imageUrl("https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&q=80")
                .category(Category.FLOWERS)
                .stock(8)
                .build(),
            Product.builder()
                .name("Dried Wildflower Posy")
                .description("A hand-tied posy of dried lavender, wheat stalks, and cotton stems. Everlasting.")
                .price(32.00)
                .imageUrl("https://images.unsplash.com/photo-1471086569966-db3eebc25a59?w=600&q=80")
                .category(Category.FLOWERS)
                .stock(15)
                .build(),
            Product.builder()
                .name("Gold Lotus Bracelet")
                .description("Delicate gold-fill chain with a hand-stamped lotus charm. Adjustable fit.")
                .price(28.00)
                .imageUrl("https://images.unsplash.com/photo-1611591437281-460bfbe1220a?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                .category(Category.BRACELETS)
                .stock(20)
                .build(),
            Product.builder()
                .name("Pearl & Rose Quartz Bracelet")
                .description("Freshwater pearls and rose quartz on silk cord — elegant and wearable every day.")
                .price(34.00)
                .imageUrl("https://images.unsplash.com/photo-1611085583191-a3b181a88401?w=600&q=80")
                .category(Category.BRACELETS)
                .stock(12)
                .build(),
            Product.builder()
                .name("Pressed Flower Resin Pendant")
                .description("Real lily and fern pressed in clear resin. No two are exactly alike.")
                .price(18.00)
                .imageUrl("https://images.unsplash.com/photo-1602173574767-37ac01994b2a?w=600&q=80")
                .category(Category.TRINKETS)
                .stock(25)
                .build(),
            Product.builder()
                .name("Botanical Wax Seal Kit")
                .description("Wax seal stamps and rose-gold wax sticks, perfect for correspondence and gifting.")
                .price(22.00)
                .imageUrl("https://images.unsplash.com/photo-1585314062340-f1a5a7c9328d?w=600&q=80")
                .category(Category.TRINKETS)
                .stock(18)
                .build(),
            Product.builder()
                .name("Autumn Harvest Wreath")
                .description("A full wreath of dried amaranth, mini pumpkins, and eucalyptus for the season.")
                .price(68.00)
                .imageUrl("https://images.unsplash.com/photo-1508193638397-1c4234db14d8?w=600&q=80")
                .category(Category.SEASONAL)
                .stock(6)
                .build()
        ));
    }

    private void seedAdmin() {
        if (customerRepo.findByEmail(adminEmail).isEmpty()) {
            customerRepo.save(Customer.builder()
                    .username("admin")
                    .email(adminEmail)
                    .password(encoder.encode(adminPassword))
                    .role("ADMIN")
                    .build());
        }
    }
}
