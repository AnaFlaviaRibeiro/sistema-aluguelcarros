<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { api } from '../api'
import { useFeedback } from '../composables/useFeedback'
import type { AtualizarClienteRequest } from '../types/api'
import {
  empregosParaRequest,
  linhaEmpregoVazia,
  linhasEmpregoDaApi,
  type EmpregoLinha,
} from '../utils/empregosForm'

const { show } = useFeedback()

const carregando = ref(true)
const perfil = reactive({
  nome: '',
  email: '',
  senha: '',
  rg: '',
  cpf: '',
  endereco: '',
  profissao: '',
})
const empregos = ref<EmpregoLinha[]>([])

async function carregar() {
  carregando.value = true
  try {
    const p = await api.clientePerfil()
    perfil.nome = p.nome
    perfil.email = p.email
    perfil.senha = ''
    perfil.rg = p.rg
    perfil.cpf = p.cpf
    perfil.endereco = p.endereco
    perfil.profissao = p.profissao
    empregos.value =
      p.empregos.length > 0 ? linhasEmpregoDaApi(p.empregos) : [linhaEmpregoVazia()]
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao carregar perfil.')
  } finally {
    carregando.value = false
  }
}

function addEmprego() {
  if (empregos.value.length >= 3) return
  empregos.value.push(linhaEmpregoVazia())
}

function removeEmprego(i: number) {
  if (empregos.value.length <= 1) return
  empregos.value.splice(i, 1)
}

async function salvar() {
  const lista = empregosParaRequest(empregos.value)
  if (lista.length === 0) {
    show('err', 'Informe pelo menos um emprego válido.')
    return
  }
  const body: AtualizarClienteRequest = {
    nome: perfil.nome,
    email: perfil.email,
    senha: perfil.senha,
    rg: perfil.rg,
    cpf: perfil.cpf,
    endereco: perfil.endereco,
    profissao: perfil.profissao,
    empregos: lista,
  }
  try {
    await api.clienteAtualizarPerfil(body)
    show('ok', 'Perfil atualizado.')
    perfil.senha = ''
    await carregar()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao salvar.')
  }
}

onMounted(() => {
  void carregar()
})
</script>

<template>
  <div class="page">
    <header class="page-head">
      <h1>Meu perfil</h1>
      <p class="page-desc">
        Dados de identificação, profissão e empregos (até 3) usados na análise financeira.
      </p>
    </header>

    <p v-if="carregando" class="muted">Carregando…</p>

    <section v-else class="panel">
      <form class="grid" @submit.prevent="salvar">
        <label class="field">
          <span>Nome</span>
          <input v-model="perfil.nome" required />
        </label>
        <label class="field">
          <span>E-mail</span>
          <input v-model="perfil.email" type="email" required />
        </label>
        <label class="field">
          <span>Nova senha</span>
          <input v-model="perfil.senha" type="password" placeholder="Deixe em branco para manter" />
        </label>
        <label class="field">
          <span>RG</span>
          <input v-model="perfil.rg" required />
        </label>
        <label class="field">
          <span>CPF</span>
          <input v-model="perfil.cpf" required />
        </label>
        <label class="field full">
          <span>Endereço</span>
          <input v-model="perfil.endereco" required />
        </label>
        <label class="field">
          <span>Profissão</span>
          <input v-model="perfil.profissao" required />
        </label>

        <div class="full empregos-block">
          <p class="empregos-section-title">Empregos (máx. 3)</p>
          <div
            v-for="(e, i) in empregos"
            :key="e.rowKey"
            class="emprego-grupo"
          >
            <div class="emprego-header">
              <span class="emprego-numero">Vínculo {{ i + 1 }}</span>
              <button
                v-if="empregos.length > 1"
                type="button"
                class="btn-remover"
                @click="removeEmprego(i)"
              >
                Remover
              </button>
            </div>
            <div class="emprego-grid">
              <label class="field">
                <span>Empregador (empresa ou órgão)</span>
                <input v-model="e.entidadeEmpregadora" placeholder="Ex.: Empresa S.A., PUC Minas…" />
              </label>
              <label class="field">
                <span>Renda mensal (R$)</span>
                <input
                  v-model.number="e.renda"
                  type="number"
                  min="0"
                  step="0.01"
                />
              </label>
            </div>
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

        <div class="full">
          <button type="submit" class="btn btn-primary">Salvar alterações</button>
        </div>
      </form>
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
}

.panel {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.5rem;
  box-shadow: var(--shadow-card);
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

.muted {
  color: var(--text-muted);
}
</style>
