@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.placeOrder(userDetails.getUsername());
    }

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getOrders(userDetails.getUsername());
    }
}
