<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api'
import { useAuth } from '../composables/useAuth'
import {
  empregosParaRequest,
  linhaEmpregoVazia,
  type EmpregoLinha,
} from '../utils/empregosForm'

const router = useRouter()
const auth = useAuth()

const form = reactive({
  nome: '',
  email: '',
  senha: '',
  rg: '',
  cpf: '',
  endereco: '',
  profissao: '',
})

const empregos = ref<EmpregoLinha[]>([linhaEmpregoVazia()])

const erro = ref('')
const carregando = ref(false)

function addEmprego() {
  if (empregos.value.length >= 3) return
  empregos.value.push(linhaEmpregoVazia())
}

function removeEmprego(i: number) {
  if (empregos.value.length <= 1) return
  empregos.value.splice(i, 1)
}

async function enviar() {
  erro.value = ''
  const lista = empregosParaRequest(empregos.value)
  if (lista.length === 0) {
    erro.value = 'Informe pelo menos um emprego com entidade e renda válida.'
    return
  }
  carregando.value = true
  try {
    await api.registrarCliente({
      ...form,
      empregos: lista,
    })
    const login = await api.login({ email: form.email, senha: form.senha })
    auth.setAuth(login)
    await router.push({ name: 'cliente-pedidos' })
  } catch (e) {
    erro.value = e instanceof Error ? e.message : 'Falha no cadastro.'
  } finally {
    carregando.value = false
  }
}
</script>

<template>
  <div class="page">
    <header class="page-head">
      <h1>Cadastro de cliente</h1>
      <p class="page-desc">
        Dados de identificação, profissão e até três vínculos empregatícios com rendimento.
      </p>
    </header>

    <section class="panel">
      <p v-if="erro" class="err">{{ erro }}</p>
      <form class="grid" @submit.prevent="enviar">
        <label class="field">
          <span>Nome</span>
          <input v-model="form.nome" required />
        </label>
        <label class="field">
          <span>E-mail</span>
          <input v-model="form.email" type="email" required />
        </label>
        <label class="field">
          <span>Senha</span>
          <input v-model="form.senha" type="password" minlength="6" required />
        </label>
        <label class="field">
          <span>RG</span>
          <input v-model="form.rg" required />
        </label>
        <label class="field">
          <span>CPF</span>
          <input v-model="form.cpf" required />
        </label>
        <label class="field full">
          <span>Endereço</span>
          <input v-model="form.endereco" required />
        </label>
        <label class="field">
          <span>Profissão</span>
          <input v-model="form.profissao" required />
        </label>

        <div class="full empregos-block">
          <h3>Emprego</h3>
          <div v-for="(e, i) in empregos" :key="i" class="emprego-row">
            <input v-model="e.entidadeEmpregadora" placeholder="Entidade empregadora" />
            <input v-model.number="e.renda" type="number" min="0.01" step="0.01" placeholder="Renda" />
            <button v-if="empregos.length > 1" type="button" class="btn-mini" @click="removeEmprego(i)">
              Remover
            </button>
          </div>
          <button
            v-if="empregos.length < 3"
            type="button"
            class="btn-secondary"
            @click="addEmprego"
          >
            + Adicionar emprego
          </button>
        </div>

        <div class="full actions">
          <button type="submit" class="btn btn-primary" :disabled="carregando">
            {{ carregando ? 'Salvando…' : 'Criar conta' }}
          </button>
        </div>
      </form>
      <p class="hint">
        Já tem conta? <RouterLink to="/login">Entrar</RouterLink>
      </p>
    </section>
  </div>
</template>

<style scoped>
.page {
  max-width: 720px;
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

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
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

.full {
  grid-column: 1 / -1;
}

.empregos-section-title {
  margin: 0.25rem 0 0.75rem;
  font-size: 0.78rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text-soft);
}

.emprego-grupo {
  margin-bottom: 1rem;
  padding: 1rem;
  border: 1px solid var(--border);
  border-radius: 10px;
  background: var(--surface-muted);
}

.emprego-grupo:last-of-type {
  margin-bottom: 0.75rem;
}

.emprego-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.75rem;
  margin-bottom: 0.85rem;
}

.emprego-numero {
  font-size: 0.78rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text-soft);
}

.btn-remover {
  font: inherit;
  font-size: 0.8rem;
  font-weight: 600;
  padding: 0.4rem 0.65rem;
  border-radius: 8px;
  border: 1px solid #fecaca;
  background: #fff;
  color: #b91c1c;
  cursor: pointer;
}

.btn-remover:hover {
  background: #fef2f2;
}

.emprego-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  align-items: end;
}

.btn-secondary {
  margin-top: 0.5rem;
  padding: 0.45rem 0.85rem;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: var(--surface-muted);
  cursor: pointer;
  font-weight: 600;
}

.actions {
  margin-top: 0.5rem;
}

.btn {
  font: inherit;
  cursor: pointer;
  border: none;
  border-radius: 8px;
  padding: 0.65rem 1.15rem;
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
}

.hint a {
  color: var(--accent);
  font-weight: 600;
  text-decoration: none;
}
</style>
