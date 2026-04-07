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

            // ── Flowers ──────────────────────────────────────────────────────
            Product.builder()
                .name("Nile Garden Bouquet")
                .description("A lush, hand-tied arrangement of garden roses, eucalyptus, and seasonal blooms — perfect for any occasion.")
                .price(85.00)
                .imageUrl("https://images.unsplash.com/photo-1464983308776-3c7215084895?w=800&q=80&auto=format&fit=crop")
                .category(Category.FLOWERS)
                .stock(12)
                .build(),

            Product.builder()
                .name("Rose Blush Arrangement")
                .description("Soft blush and ivory roses gathered with delicate greenery, wrapped in our signature kraft and ribbon.")
                .price(72.00)
                .imageUrl("https://images.unsplash.com/photo-1582794543139-8ac9cb0f7b11?w=800&q=80&auto=format&fit=crop")
                .category(Category.FLOWERS)
                .stock(8)
                .build(),

            Product.builder()
                .name("Wildflower Mix")
                .description("A free-spirited mix of seasonal wildflowers in a rustic wrap — cheerful, fragrant, and effortlessly beautiful.")
                .price(58.00)
                .imageUrl("https://images.unsplash.com/photo-1519378058457-4c29a0a2efac?w=800&q=80&auto=format&fit=crop")
                .category(Category.FLOWERS)
                .stock(15)
                .build(),

            Product.builder()
                .name("White Orchid Stem")
                .description("A single Phalaenopsis orchid stem, elegantly arranged in a clear bud vase — long-lasting and timeless.")
                .price(48.00)
                .imageUrl("https://images.unsplash.com/photo-1508610048659-a06b669e3321?w=800&q=80&auto=format&fit=crop")
                .category(Category.FLOWERS)
                .stock(20)
                .build(),

            Product.builder()
                .name("Lotus Centerpiece")
                .description("A statement arrangement of lotus blooms, magnolia leaves, and gilded branches for the modern home.")
                .price(120.00)
                .imageUrl("https://images.unsplash.com/photo-1490750967868-88df5691cc9b?w=800&q=80&auto=format&fit=crop")
                .category(Category.FLOWERS)
                .stock(6)
                .build(),

            // ── Bracelets ────────────────────────────────────────────────────
            Product.builder()
                .name("Gold Lotus Bracelet")
                .description("Delicate 18k gold-plated chain with a hand-set lotus charm — inspired by the waters of the Nile.")
                .price(48.00)
                .imageUrl("https://images.unsplash.com/photo-1611085583191-a3b181a88401?w=800&q=80&auto=format&fit=crop")
                .category(Category.BRACELETS)
                .stock(25)
                .build(),

            Product.builder()
                .name("Pearl Bloom Cuff")
                .description("A statement cuff adorned with freshwater pearls and floral enamel accents — feminine and bold.")
                .price(65.00)
                .imageUrl("https://images.unsplash.com/photo-1602173574767-37ac01994b2a?w=800&q=80&auto=format&fit=crop")
                .category(Category.BRACELETS)
                .stock(18)
                .build(),

            Product.builder()
                .name("Floral Charm Bracelet")
                .description("A dainty chain with seven interchangeable floral charms in rose gold — stack it or wear it alone.")
                .price(34.00)
                .imageUrl("https://images.unsplash.com/photo-1599751449128-eb7249c3d6b1?w=800&q=80&auto=format&fit=crop")
                .category(Category.BRACELETS)
                .stock(30)
                .build(),

            // ── Trinkets ─────────────────────────────────────────────────────
            Product.builder()
                .name("Nile Moon Pendant")
                .description("A crescent moon pendant in sterling silver with a pressed flower resin centre — wearable art.")
                .price(52.00)
                .imageUrl("https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=800&q=80&auto=format&fit=crop")
                .category(Category.TRINKETS)
                .stock(22)
                .build(),

            Product.builder()
                .name("Pressed Flower Frame")
                .description("A hand-pressed botanical arrangement sealed behind glass in a gilded frame — ready to hang or display.")
                .price(38.00)
                .imageUrl("https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&q=80&auto=format&fit=crop")
                .category(Category.TRINKETS)
                .stock(14)
                .build(),

            Product.builder()
                .name("Floral Wax Seal Set")
                .description("A set of three botanical wax seal stamps with gold sealing wax — perfect for letters and gift wrapping.")
                .price(28.00)
                .imageUrl("https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=80&auto=format&fit=crop")
                .category(Category.TRINKETS)
                .stock(40)
                .build(),

            // ── Seasonal ─────────────────────────────────────────────────────
            Product.builder()
                .name("Spring Garden Collection")
                .description("A curated seasonal bundle: a fresh arrangement, a floral charm, and a botanical candle — gift-wrapped with care.")
                .price(138.00)
                .imageUrl("https://images.unsplash.com/photo-1464983308776-3c7215084895?w=800&q=80&auto=format&fit=crop")
                .category(Category.SEASONAL)
                .stock(10)
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
