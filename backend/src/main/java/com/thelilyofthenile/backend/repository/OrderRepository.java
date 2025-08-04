public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
