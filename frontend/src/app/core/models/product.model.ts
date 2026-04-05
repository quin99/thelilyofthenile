export type Category = 'FLOWERS' | 'BRACELETS' | 'TRINKETS' | 'SEASONAL';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string | null;
  category: Category;
  stock: number;
}
