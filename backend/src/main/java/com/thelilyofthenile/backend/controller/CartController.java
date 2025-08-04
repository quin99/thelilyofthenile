@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class CartController {

    @Autowired private CartService cartService;

    @GetMapping
    public List<CartItem> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return cartService.getCart(userDetails.getUsername());
    }

    @PostMapping("/add")
    public CartItem addToCart(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());
        return cartService.addItem(userDetails.getUsername(), productId, quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public void removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long productId) {
        cartService.removeItem(userDetails.getUsername(), productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
    }
}
