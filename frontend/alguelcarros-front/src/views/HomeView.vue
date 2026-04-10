<script setup lang="ts">
import { RouterLink } from 'vue-router'
import heroImg from '../assets/carros.jpeg'
</script>

<template>
  <div class="home">
    <section class="hero">
      <div class="hero-copy">
        <p class="eyebrow">Gestão de locação</p>
        <h1>Aluguel de carros com fluxo claro e rápido</h1>
        <p class="lead">
          O acesso exige cadastro prévio. Clientes gerenciam seus pedidos; agentes (empresas e bancos)
          avaliam financeiramente e registram contratos — com opção de vínculo a crédito concedido por
          banco agente.
        </p>
        <div class="cta-row">
          <RouterLink class="btn btn-primary" to="/registro">
            Cadastrar como cliente
          </RouterLink>
          <RouterLink class="btn btn-ghost" to="/login">
            Entrar
          </RouterLink>
        </div>
      </div>
      <div class="hero-visual" aria-hidden="true">
        <div class="hero-glow" />
        <img :src="heroImg" alt="" class="hero-img" width="520" height="360" />
      </div>
    </section>

    <section class="features">
      <article class="feature-card">
        <span class="feature-icon" aria-hidden="true">◎</span>
        <h2>Cliente</h2>
        <p>
          RG, CPF, endereço, profissão e até três empregos com renda. Cria, altera, consulta e cancela
          pedidos conforme as regras de status.
        </p>
        <RouterLink to="/registro" class="feature-link">Criar conta →</RouterLink>
      </article>
      <article class="feature-card">
        <span class="feature-icon" aria-hidden="true">◇</span>
        <h2>Agente</h2>
        <p>
          Coloca pedidos em análise financeira, aprova ou reprova e, após parecer positivo, registra o
          contrato de aluguel (com ou sem crédito bancário).
        </p>
        <RouterLink to="/login" class="feature-link">Acesso agente →</RouterLink>
      </article>
      <article class="feature-card">
        <span class="feature-icon" aria-hidden="true">✓</span>
        <h2>Veículo e propriedade</h2>
        <p>
          Matrícula, ano, marca, modelo e placa; proprietário pode ser cliente, empresa ou banco, conforme
          o tipo de contrato.
        </p>
      </article>
    </section>
  </div>
</template>

<style scoped>
.home {
  max-width: 1100px;
  margin: 0 auto;
}

.hero {
  display: grid;
  gap: 2rem;
  align-items: center;
  padding: 2.5rem 0 3rem;
}

@media (min-width: 900px) {
  .hero {
    grid-template-columns: 1fr 1fr;
    gap: 3rem;
    padding: 3rem 0 4rem;
  }
}

.eyebrow {
  margin: 0 0 0.5rem;
  font-size: 0.8rem;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--accent);
}

.hero-copy h1 {
  margin: 0 0 1rem;
  font-size: clamp(1.85rem, 4vw, 2.45rem);
  font-weight: 700;
  line-height: 1.15;
  color: var(--text);
  letter-spacing: -0.02em;
}

.lead {
  margin: 0 0 1.75rem;
  font-size: 1.05rem;
  line-height: 1.6;
  color: var(--text-muted);
  max-width: 34rem;
}

.cta-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.65rem 1.25rem;
  border-radius: 10px;
  font-weight: 600;
  font-size: 0.95rem;
  text-decoration: none;
  transition:
    background 0.15s ease,
    color 0.15s ease,
    box-shadow 0.15s ease,
    transform 0.15s ease;
}

.btn-primary {
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-dark) 100%);
  color: #fff;
  box-shadow: 0 4px 14px rgb(37 99 235 / 35%);
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgb(37 99 235 / 40%);
}

.btn-ghost {
  background: var(--surface);
  color: var(--text);
  border: 1px solid var(--border);
}

.btn-ghost:hover {
  border-color: var(--accent);
  color: var(--accent);
}

.hero-visual {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 240px;
}

.hero-glow {
  position: absolute;
  inset: 10% 5%;
  background: radial-gradient(
    ellipse at center,
    rgb(99 102 241 / 18%) 0%,
    transparent 70%
  );
  border-radius: 50%;
  filter: blur(8px);
}

.hero-img {
  position: relative;
  width: 100%;
  max-width: 480px;
  height: auto;
  border-radius: 16px;
  box-shadow:
    0 24px 48px rgb(15 23 42 / 12%),
    0 0 0 1px rgb(15 23 42 / 6%);
}

.features {
  display: grid;
  gap: 1.25rem;
  padding-bottom: 2rem;
}

@media (min-width: 720px) {
  .features {
    grid-template-columns: repeat(3, 1fr);
  }
}

.feature-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.35rem 1.5rem;
  box-shadow: var(--shadow-card);
  transition:
    border-color 0.15s ease,
    box-shadow 0.15s ease;
}

.feature-card:hover {
  border-color: rgb(37 99 235 / 25%);
  box-shadow: var(--shadow-card-hover);
}

.feature-icon {
  display: block;
  font-size: 1.25rem;
  margin-bottom: 0.65rem;
  opacity: 0.85;
  color: var(--accent);
}

.feature-card h2 {
  margin: 0 0 0.5rem;
  font-size: 1.05rem;
  color: var(--text);
}

.feature-card p {
  margin: 0 0 0.85rem;
  font-size: 0.9rem;
  line-height: 1.5;
  color: var(--text-muted);
}

.feature-card code {
  font-size: 0.82em;
  background: var(--surface-muted);
  padding: 0.15rem 0.4rem;
  border-radius: 6px;
}

.feature-link {
  font-size: 0.88rem;
  font-weight: 600;
  color: var(--accent);
  text-decoration: none;
}

.feature-link:hover {
  text-decoration: underline;
}
</style>
