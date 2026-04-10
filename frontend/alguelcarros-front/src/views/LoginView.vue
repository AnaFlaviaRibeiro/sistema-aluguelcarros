<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api'
import { useAuth } from '../composables/useAuth'

const router = useRouter()
const auth = useAuth()

const email = ref('')
const senha = ref('')
const erro = ref('')
const carregando = ref(false)

async function enviar() {
  erro.value = ''
  carregando.value = true
  try {
    const res = await api.login({ email: email.value, senha: senha.value })
    auth.setAuth(res)
    if (res.tipo === 'CLIENTE') {
      await router.push({ name: 'cliente-pedidos' })
    } else {
      await router.push({ name: 'agente-pedidos' })
    }
  } catch (e) {
    erro.value = e instanceof Error ? e.message : 'Falha no login.'
  } finally {
    carregando.value = false
  }
}
</script>

<template>
  <div class="page">
    <header class="page-head">
      <h1>Entrar</h1>
      <p class="page-desc">
        Use o cadastro de cliente ou as credenciais de agente fornecidas pelo sistema.
      </p>
    </header>

    <section class="panel">
      <p v-if="erro" class="err">{{ erro }}</p>
      <form class="stack" @submit.prevent="enviar">
        <label class="field">
          <span>E-mail</span>
          <input v-model="email" type="email" autocomplete="username" required />
        </label>
        <label class="field">
          <span>Senha</span>
          <input v-model="senha" type="password" autocomplete="current-password" required />
        </label>
        <button type="submit" class="btn btn-primary" :disabled="carregando">
          {{ carregando ? 'Entrando…' : 'Entrar' }}
        </button>
      </form>
      <p class="hint">
        Sem conta?
        <RouterLink to="/registro">Cadastre-se como cliente</RouterLink>
      </p>
      <p class="hint muted">
        Agentes demo: <code>empresa@demo.com</code> ou <code>banco@demo.com</code> · senha
        <code>agente123</code>
      </p>
    </section>
  </div>
</template>

<style scoped>
.page {
  max-width: 420px;
  margin: 0 auto;
}

.page-head h1 {
  margin: 0 0 0.35rem;
  font-size: 1.6rem;
  font-weight: 700;
  color: var(--text);
}

.page-desc {
  margin: 0 0 1.25rem;
  color: var(--text-muted);
  font-size: 0.95rem;
  line-height: 1.5;
}

.panel {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.5rem;
  box-shadow: var(--shadow-card);
}

.err {
  margin: 0 0 1rem;
  padding: 0.6rem 0.75rem;
  border-radius: 8px;
  background: #fef2f2;
  color: #991b1b;
  font-size: 0.9rem;
}

.stack {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.field span {
  font-size: 0.78rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text-soft);
}

.field input {
  padding: 0.55rem 0.65rem;
  border: 1px solid var(--border-input);
  border-radius: 8px;
  font: inherit;
}

.btn {
  font: inherit;
  cursor: pointer;
  border: none;
  border-radius: 8px;
  padding: 0.65rem 1rem;
  font-weight: 600;
}

.btn-primary {
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-dark) 100%);
  color: #fff;
}

.btn-primary:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.hint {
  margin: 1.25rem 0 0;
  font-size: 0.9rem;
  color: var(--text-muted);
}

.hint a {
  color: var(--accent);
  font-weight: 600;
  text-decoration: none;
}

.hint code {
  font-size: 0.82em;
  background: var(--surface-muted);
  padding: 0.1rem 0.35rem;
  border-radius: 4px;
}

.muted {
  margin-top: 0.75rem;
  font-size: 0.82rem;
}
</style>
