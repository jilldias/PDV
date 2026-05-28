// Global state
const state = {
  cart: [],
  products: [],
  selectedPayment: 'DINHEIRO',
  currentSection: 'vendas'
};

// API endpoints
const API = {
  products: '/api/produtos',
  sales: '/api/vendas',
  reports: '/api/relatorios',
  cash: '/api/caixa'
};

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
  initMenu();
  loadProducts();
  setupEventListeners();
  showSection('vendas');
});

// ============================================
// Menu Navigation
// ============================================
function initMenu() {
  const menuItems = document.querySelectorAll('.menu-item');
  menuItems.forEach(item => {
    item.addEventListener('click', (e) => {
      const sectionId = e.target.dataset.section;
      showSection(sectionId);
    });
  });
}

function showSection(sectionId) {
  state.currentSection = sectionId;
  
  // Hide all sections
  document.querySelectorAll('.section').forEach(section => {
    section.classList.remove('active');
  });
  
  // Update menu active state
  document.querySelectorAll('.menu-item').forEach(item => {
    item.classList.remove('active');
  });
  
  // Show selected section
  const section = document.getElementById(sectionId);
  if (section) {
    section.classList.add('active');
  }
  
  // Update active menu item
  const activeItem = document.querySelector(`[data-section="${sectionId}"]`);
  if (activeItem) {
    activeItem.classList.add('active');
  }
  
  // Load section-specific data
  if (sectionId === 'relatorios') {
    loadReports();
  } else if (sectionId === 'produtos') {
    loadProductsManagement();
  } else if (sectionId === 'caixa') {
    loadCashRegister();
  }
}

// ============================================
// Products (Vendas Section)
// ============================================
function loadProducts() {
  fetch(API.products)
    .then(res => res.json())
    .then(data => {
      state.products = data;
      renderProducts();
    })
    .catch(err => showNotification('Erro ao carregar produtos', 'error'));
}

function renderProducts() {
  const grid = document.getElementById('productsGrid');
  if (!grid) return;
  
  grid.innerHTML = state.products.map(product => `
    <div class="product-card" onclick="addToCart({
      id: ${product.id},
      nome: '${product.nome.replace(/'/g, "\\'")}',
      preco: ${product.preco}
    })">
      <div class="product-name">${product.nome}</div>
      <div class="product-price">R$ ${product.preco.toFixed(2)}</div>
      <div class="product-stock">Estoque: ${product.estoque}</div>
      <button class="btn-add">Adicionar</button>
    </div>
  `).join('');
}

function searchProducts() {
  const query = document.getElementById('searchInput').value.toLowerCase();
  const filtered = state.products.filter(p => 
    p.nome.toLowerCase().includes(query)
  );
  
  const grid = document.getElementById('productsGrid');
  if (grid) {
    grid.innerHTML = filtered.map(product => `
      <div class="product-card" onclick="addToCart({
        id: ${product.id},
        nome: '${product.nome.replace(/'/g, "\\'")}',
        preco: ${product.preco}
      })">
        <div class="product-name">${product.nome}</div>
        <div class="product-price">R$ ${product.preco.toFixed(2)}</div>
        <div class="product-stock">Estoque: ${product.estoque}</div>
        <button class="btn-add">Adicionar</button>
      </div>
    `).join('');
  }
}

// ============================================
// Shopping Cart
// ============================================
function addToCart(product) {
  const existing = state.cart.find(item => item.id === product.id);
  
  if (existing) {
    existing.quantidade++;
  } else {
    state.cart.push({
      ...product,
      quantidade: 1
    });
  }
  
  renderCart();
  showNotification('Produto adicionado ao carrinho', 'success');
}

function removeFromCart(index) {
  state.cart.splice(index, 1);
  renderCart();
}

function updateQuantity(index, newQty) {
  if (newQty <= 0) {
    removeFromCart(index);
  } else {
    state.cart[index].quantidade = newQty;
    renderCart();
  }
}

function renderCart() {
  const cartItems = document.getElementById('cartItems');
  if (!cartItems) return;
  
  cartItems.innerHTML = state.cart.map((item, idx) => `
    <div class="cart-item">
      <div class="cart-item-name">${item.nome}</div>
      <div class="cart-item-price">R$ ${item.preco.toFixed(2)}</div>
      <div class="cart-item-controls">
        <button class="qty-btn" onclick="updateQuantity(${idx}, ${item.quantidade - 1})">-</button>
        <span class="qty-display">${item.quantidade}</span>
        <button class="qty-btn" onclick="updateQuantity(${idx}, ${item.quantidade + 1})">+</button>
        <button class="btn-remove" onclick="removeFromCart(${idx})">×</button>
      </div>
    </div>
  `).join('');
  
  updateCartResume();
}

function updateCartResume() {
  const subtotal = state.cart.reduce((sum, item) => sum + (item.preco * item.quantidade), 0);
  const tax = subtotal * 0.10;
  const total = subtotal + tax;
  
  document.getElementById('subtotal').textContent = `R$ ${subtotal.toFixed(2)}`;
  document.getElementById('tax').textContent = `R$ ${tax.toFixed(2)}`;
  document.getElementById('total').textContent = `R$ ${total.toFixed(2)}`;
}

function clearCart() {
  if (confirm('Deseja limpar o carrinho?')) {
    state.cart = [];
    renderCart();
  }
}

// ============================================
// Payment
// ============================================
function selectPayment(method) {
  state.selectedPayment = method;
  document.querySelectorAll('.payment-option').forEach(opt => {
    opt.classList.remove('selected');
  });
  document.querySelector(`[data-payment="${method}"]`).classList.add('selected');
}

// ============================================
// Finalize Sale
// ============================================
function finalizeSale() {
  if (state.cart.length === 0) {
    showNotification('Carrinho vazio', 'warning');
    return;
  }
  
  const customerName = document.getElementById('customerName').value || 'Cliente Genérico';
  const customerDoc = document.getElementById('customerDoc').value || '';
  
  const total = state.cart.reduce((sum, item) => sum + (item.preco * item.quantidade), 0);
  const tax = total * 0.10;
  
  const saleData = {
    cliente: customerName,
    documento: customerDoc,
    itens: state.cart.map(item => ({
      produtoId: item.id,
      quantidade: item.quantidade,
      preco: item.preco
    })),
    subtotal: total,
    imposto: tax,
    total: total + tax,
    pagamento: state.selectedPayment
  };
  
  fetch(API.sales, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(saleData)
  })
    .then(res => {
      if (res.ok) {
        showNotification('Venda finalizada com sucesso!', 'success');
        state.cart = [];
        renderCart();
        document.getElementById('customerName').value = '';
        document.getElementById('customerDoc').value = '';
        state.selectedPayment = 'DINHEIRO';
        document.querySelectorAll('.payment-option').forEach(opt => opt.classList.remove('selected'));
        document.querySelector('[data-payment="DINHEIRO"]').classList.add('selected');
      } else {
        showNotification('Erro ao finalizar venda', 'error');
      }
    })
    .catch(err => showNotification('Erro ao finalizar venda', 'error'));
}

// ============================================
// Product Management
// ============================================
function loadProductsManagement() {
  const container = document.getElementById('productsManagementList');
  if (!container) return;
  
  container.innerHTML = `
    <table>
      <thead>
        <tr>
          <th>Nome</th>
          <th>Preço</th>
          <th>Estoque</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        ${state.products.map((p, idx) => `
          <tr>
            <td>${p.nome}</td>
            <td>R$ ${p.preco.toFixed(2)}</td>
            <td>${p.estoque}</td>
            <td>
              <button class="btn btn-secondary" onclick="editProduct(${p.id})">Editar</button>
              <button class="btn btn-danger" onclick="deleteProduct(${p.id})">Excluir</button>
            </td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;
}

function toggleProductForm() {
  const form = document.getElementById('productForm');
  if (form) {
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
  }
}

function addProduct() {
  const nome = document.getElementById('productName').value;
  const descricao = document.getElementById('productDesc').value;
  const preco = parseFloat(document.getElementById('productPrice').value);
  const estoque = parseInt(document.getElementById('productStock').value);
  const categoria = document.getElementById('productCategory').value;
  
  if (!nome || !preco || !estoque) {
    showNotification('Preencha todos os campos', 'warning');
    return;
  }
  
  fetch(API.products, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ nome, descricao, preco, estoque, categoria })
  })
    .then(res => {
      if (res.ok) {
        showNotification('Produto adicionado!', 'success');
        document.getElementById('productName').value = '';
        document.getElementById('productDesc').value = '';
        document.getElementById('productPrice').value = '';
        document.getElementById('productStock').value = '';
        loadProducts();
        loadProductsManagement();
        toggleProductForm();
      }
    })
    .catch(err => showNotification('Erro ao adicionar produto', 'error'));
}

function editProduct(id) {
  showNotification('Função de editar em desenvolvimento', 'info');
}

function deleteProduct(id) {
  if (confirm('Deseja excluir este produto?')) {
    fetch(`${API.products}/${id}`, { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          showNotification('Produto excluído!', 'success');
          loadProducts();
          loadProductsManagement();
        }
      })
      .catch(err => showNotification('Erro ao excluir produto', 'error'));
  }
}

// ============================================
// Reports
// ============================================
function loadReports() {
  const container = document.getElementById('reportsTable');
  if (!container) return;
  
  fetch(API.reports)
    .then(res => res.json())
    .then(data => {
      renderReports(data);
    })
    .catch(err => showNotification('Erro ao carregar relatórios', 'error'));
}

function renderReports(reports) {
  const container = document.getElementById('reportsTable');
  if (!container) return;
  
  container.innerHTML = `
    <table>
      <thead>
        <tr>
          <th>ID Venda</th>
          <th>Data</th>
          <th>Cliente</th>
          <th>Valor</th>
          <th>Pagamento</th>
        </tr>
      </thead>
      <tbody>
        ${reports.map(report => `
          <tr>
            <td>#${report.id}</td>
            <td>${new Date(report.data).toLocaleDateString()}</td>
            <td>${report.cliente}</td>
            <td>R$ ${report.total.toFixed(2)}</td>
            <td>${report.pagamento}</td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;
}

function generateReport() {
  const startDate = document.getElementById('startDate').value;
  const endDate = document.getElementById('endDate').value;
  
  const params = new URLSearchParams();
  if (startDate) params.append('inicio', startDate);
  if (endDate) params.append('fim', endDate);
  
  fetch(`${API.reports}?${params}`)
    .then(res => res.json())
    .then(data => renderReports(data))
    .catch(err => showNotification('Erro ao gerar relatório', 'error'));
}

function exportPDF() {
  const startDate = document.getElementById('startDate').value;
  const endDate = document.getElementById('endDate').value;
  
  const params = new URLSearchParams();
  if (startDate) params.append('inicio', startDate);
  if (endDate) params.append('fim', endDate);
  
  window.open(`${API.reports}/pdf?${params}`, '_blank');
}

// ============================================
// Cash Register
// ============================================
function loadCashRegister() {
  fetch(API.cash)
    .then(res => res.json())
    .then(data => {
      document.getElementById('caixaStatus').textContent = data.status || 'FECHADO';
      document.getElementById('valorInicial').textContent = `R$ ${(data.valorInicial || 0).toFixed(2)}`;
      document.getElementById('valorFinal').textContent = `R$ ${(data.valorFinal || 0).toFixed(2)}`;
    })
    .catch(err => showNotification('Erro ao carregar caixa', 'error'));
}

function openCashRegister() {
  const initialAmount = prompt('Digite o valor inicial do caixa:');
  if (initialAmount) {
    fetch(API.cash, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ acao: 'abrir', valorInicial: parseFloat(initialAmount) })
    })
      .then(res => {
        if (res.ok) {
          showNotification('Caixa aberto!', 'success');
          loadCashRegister();
        }
      })
      .catch(err => showNotification('Erro ao abrir caixa', 'error'));
  }
}

function closeCashRegister() {
  const finalAmount = prompt('Digite o valor final do caixa:');
  if (finalAmount) {
    fetch(API.cash, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ acao: 'fechar', valorFinal: parseFloat(finalAmount) })
    })
      .then(res => {
        if (res.ok) {
          showNotification('Caixa fechado!', 'success');
          loadCashRegister();
        }
      })
      .catch(err => showNotification('Erro ao fechar caixa', 'error'));
  }
}

// ============================================
// Modal & Notifications
// ============================================
function showModal(title, message, onConfirm) {
  const modal = document.getElementById('confirmModal');
  if (modal) {
    modal.querySelector('.modal-header').textContent = title;
    modal.querySelector('.modal-body').textContent = message;
    modal.classList.add('active');
    
    modal.querySelector('.btn-primary').onclick = () => {
      modal.classList.remove('active');
      if (onConfirm) onConfirm();
    };
    
    modal.querySelector('.btn-secondary').onclick = () => {
      modal.classList.remove('active');
    };
  }
}

function showNotification(message, type = 'info') {
  const container = document.body;
  const notification = document.createElement('div');
  notification.className = `notification ${type}`;
  notification.textContent = message;
  container.appendChild(notification);
  
  setTimeout(() => {
    notification.remove();
  }, 4000);
}

// ============================================
// Event Listeners
// ============================================
function setupEventListeners() {
  // Search input
  const searchInput = document.getElementById('searchInput');
  if (searchInput) {
    searchInput.addEventListener('input', searchProducts);
  }
  
  // Payment methods
  document.querySelectorAll('.payment-option').forEach(opt => {
    opt.addEventListener('click', (e) => {
      selectPayment(e.target.dataset.payment);
    });
  });
  
  // Buttons
  const btnClear = document.getElementById('btnClear');
  if (btnClear) btnClear.addEventListener('click', clearCart);
  
  const btnFinalize = document.getElementById('btnFinalize');
  if (btnFinalize) btnFinalize.addEventListener('click', finalizeSale);
  
  // Modal close button
  const modal = document.getElementById('confirmModal');
  if (modal) {
    modal.addEventListener('click', (e) => {
      if (e.target === modal) {
        modal.classList.remove('active');
      }
    });
  }
}

// Initialize payment selection on load
window.addEventListener('load', () => {
  const paymentOpt = document.querySelector('[data-payment="DINHEIRO"]');
  if (paymentOpt) {
    paymentOpt.classList.add('selected');
  }
});
