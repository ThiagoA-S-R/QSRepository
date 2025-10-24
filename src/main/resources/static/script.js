const API = '/api/users';
let editingId = null;

const userForm = document.getElementById('userForm');
const submitBtn = document.getElementById('submitBtn');

async function fetchUsers() {
  const res = await fetch(API);
  const data = await res.json();
  const tbody = document.querySelector('#usersTable tbody');
  tbody.innerHTML = '';

  data.forEach(u => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${u.name}</td>
      <td>${u.email}</td>
      <td>${u.age ?? ''}</td>
      <td>
        <button class="edit" data-id="${u.id}">Editar</button>
        <button class="delete" data-id="${u.id}">Excluir</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

userForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const name = document.getElementById('name').value;
  const email = document.getElementById('email').value;
  const ageVal = document.getElementById('age').value;
  const age = ageVal ? parseInt(ageVal, 10) : null;

  if (editingId) {
    
    const res = await fetch(`${API}/${editingId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, age })
    });
    if (res.ok) {
      editingId = null;
      userForm.reset();
      submitBtn.textContent = 'Adicionar';
      fetchUsers();
    } else {
      alert('Erro ao atualizar usuário');
    }
  } else {
    
    const res = await fetch(API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, age })
    });
    if (res.ok) {
      userForm.reset();
      fetchUsers();
    } else {
      const txt = await res.text();
      alert('Erro: ' + txt);
    }
  }
});

document.querySelector('#usersTable').addEventListener('click', async (e) => {
  const id = e.target.dataset.id;

  if (e.target.matches('button.delete')) {
    const res = await fetch(`${API}/${id}`, { method: 'DELETE' });
    if (res.status === 204) fetchUsers();
    else alert('Falha ao excluir');
  }

  if (e.target.matches('button.edit')) {
    const res = await fetch(`${API}/${id}`);
    if (res.ok) {
      const data = await res.json();
      document.getElementById('name').value = data.name;
      document.getElementById('email').value = data.email;
      document.getElementById('age').value = data.age ?? '';
      editingId = id;
      submitBtn.textContent = 'Salvar Edição';
    } else {
      alert('Erro ao buscar usuário');
    }
  }
});

fetchUsers();
