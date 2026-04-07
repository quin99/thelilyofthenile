import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { AdminService } from '../../../core/services/admin.service';
import { Product, Category } from '../../../core/models/product.model';

type AdminView = 'list' | 'create' | 'edit';

const CATEGORIES: Category[] = ['FLOWERS', 'BRACELETS', 'TRINKETS', 'SEASONAL'];

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-page.html',
})
export class AdminPageComponent implements OnInit {
  private adminService = inject(AdminService);

  view = signal<AdminView>('list');
  products = signal<Product[]>([]);
  loading = signal(true);
  saving = signal(false);
  error = signal<string | null>(null);
  editingProduct = signal<Product | null>(null);

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
