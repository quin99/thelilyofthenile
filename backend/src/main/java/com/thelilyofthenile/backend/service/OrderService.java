@Service
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private CartItemRepository cartRepo;
    @Autowired private UserRepository userRepo;

    public Order placeOrder(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        List<CartItem> cartItems = cartRepo.findByUser(user);

        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        double total = 0;

        for (CartItem cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(cart.getProduct());
            item.setQuantity(cart.getQuantity());
            item.setPriceAtPurchase(cart.getProduct().getPrice());

            total += item.getQuantity() * item.getPriceAtPurchase();
            order.getItems().add(item);
        }

        order.setTotalAmount(total);

        cartRepo.deleteAll(cartItems); // Clear cart
        return orderRepo.save(order);
    }

    public List<Order> getOrders(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return orderRepo.findByUser(user);
    }
}
