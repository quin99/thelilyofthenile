import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { firstValueFrom } from 'rxjs';
import { AdminService } from '../../../core/services/admin.service';
import { SiteImageService } from '../../../core/services/site-image.service';
import { OrderService } from '../../../core/services/order.service';
import { Product, Category } from '../../../core/models/product.model';
import { Order } from '../../../core/models/order.model';

type AdminView = 'list' | 'create' | 'edit';
type AdminTab = 'products' | 'orders' | 'site-images';

const CATEGORIES: Category[] = ['FLOWERS', 'BRACELETS', 'TRINKETS', 'SEASONAL'];

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, DatePipe],
  templateUrl: './admin-page.html',
})
export class AdminPageComponent implements OnInit {
  private adminService = inject(AdminService);
  private siteImageService = inject(SiteImageService);
  private orderService = inject(OrderService);

  tab = signal<AdminTab>('products');
  view = signal<AdminView>('list');
  products = signal<Product[]>([]);
  loading = signal(true);
  saving = signal(false);
  error = signal<string | null>(null);
  editingProduct = signal<Product | null>(null);

  siteImageSlots = [
    { slot: 'hero_1',      label: 'Hero Card 1 — Nile Bouquet' },
    { slot: 'hero_2',      label: 'Hero Card 2 — Rose Blush' },
    { slot: 'hero_3',      label: 'Hero Card 3 — Petal Set' },
    { slot: 'editorial',   label: 'Editorial Section — Our Philosophy' },
    { slot: 'accessory_1', label: 'Accessory 1 — Gold Lotus Bracelet' },
    { slot: 'accessory_2', label: 'Accessory 2 — Pearl Bloom Cuff' },
    { slot: 'accessory_3', label: 'Accessory 3 — Floral Charm Set' },
    { slot: 'accessory_4', label: 'Accessory 4 — Nile Moon Pendant' },
  ];
  siteImages = signal<Record<string, string>>({});
  siteImageUploading = signal<string | null>(null);
  siteImageError = signal<string | null>(null);

  orders = signal<Order[]>([]);
  ordersLoading = signal(false);
  readonly ORDER_STATUSES = ['PENDING', 'PAID', 'FULFILLED', 'SHIPPED'];

  categories = CATEGORIES;

  // Form fields
  name = '';
  description = '';
  price = 0;
  category: Category = 'FLOWERS';
  stock = 0;
  selectedFile: File | null = null;
  imagePreview: string | null = null;

  async ngOnInit() {
    await this.loadProducts();
    this.loadSiteImages();
    this.loadOrders();
  }

  loadOrders() {
    this.ordersLoading.set(true);
    this.orderService.getAllOrders().subscribe({
      next: orders => { this.orders.set(orders); this.ordersLoading.set(false); },
      error: () => this.ordersLoading.set(false),
    });
  }

  async updateOrderStatus(order: Order, status: string) {
    await firstValueFrom(this.orderService.updateStatus(order.id, status));
    this.loadOrders();
  }

  loadSiteImages() {
    this.siteImageService.getAll().subscribe(images => this.siteImages.set(images));
  }

  async onSiteImageSelected(slot: string, event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.siteImageUploading.set(slot);
    this.siteImageError.set(null);
    try {
      await firstValueFrom(this.siteImageService.upload(slot, file));
      this.loadSiteImages();
    } catch {
      this.siteImageError.set(`Failed to upload image for ${slot}.`);
    } finally {
      this.siteImageUploading.set(null);
      input.value = '';
    }
  }

  async loadProducts() {
    this.loading.set(true);
    try {
      const products = await firstValueFrom(this.adminService.getProducts());
      this.products.set(products);
    } catch (e) {
      this.error.set('Failed to load products.');
    } finally {
      this.loading.set(false);
    }
  }

  openCreate() {
    this.resetForm();
    this.view.set('create');
  }

  openEdit(product: Product) {
    this.editingProduct.set(product);
    this.name = product.name;
    this.description = product.description;
    this.price = product.price;
    this.category = product.category;
    this.stock = product.stock;
    this.selectedFile = null;
    this.imagePreview = product.imageUrl;
    this.error.set(null);
    this.view.set('edit');
  }

  cancelForm() {
    this.resetForm();
    this.view.set('list');
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    this.selectedFile = file;
    const reader = new FileReader();
    reader.onload = () => { this.imagePreview = reader.result as string; };
    reader.readAsDataURL(file);
  }

  async save() {
    if (!this.name.trim() || !this.description.trim() || this.price <= 0) {
      this.error.set('Please fill in all required fields.');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const productPayload = {
      name: this.name,
      description: this.description,
      price: this.price,
      category: this.category,
      stock: this.stock,
      existingImageUrl: !this.selectedFile ? this.imagePreview : null
    };

    const formData = new FormData();
    formData.append('product', new Blob([JSON.stringify(productPayload)], { type: 'application/json' }));
    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    try {
      if (this.view() === 'create') {
        await firstValueFrom(this.adminService.createProduct(formData));
      } else {
        const product = this.editingProduct();
        if (!product) return;
        await firstValueFrom(this.adminService.updateProduct(product.id, formData));
      }
      await this.loadProducts();
      this.view.set('list');
      this.resetForm();
    } catch (e) {
      this.error.set('Failed to save product.');
    } finally {
      this.saving.set(false);
    }
  }

  async delete(product: Product) {
    if (!confirm(`Delete "${product.name}"?`)) return;
    try {
      await firstValueFrom(this.adminService.deleteProduct(product.id));
      await this.loadProducts();
    } catch (e) {
      this.error.set('Failed to delete product.');
    }
  }

  private resetForm() {
    this.name = '';
    this.description = '';
    this.price = 0;
    this.category = 'FLOWERS';
    this.stock = 0;
    this.selectedFile = null;
    this.imagePreview = null;
    this.editingProduct.set(null);
    this.error.set(null);
  }
}
