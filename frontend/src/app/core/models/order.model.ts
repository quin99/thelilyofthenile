export interface OrderItem {
  productId: number;
  productName: string;
  quantity: number;
  priceAtPurchase: number;
}

export interface Order {
  id: number;
  status: string;
  totalAmount: number;
  paymentIntentId: string | null;
  createdAt: string;
  items: OrderItem[];
  customerName?: string;
  customerEmail?: string;
}
