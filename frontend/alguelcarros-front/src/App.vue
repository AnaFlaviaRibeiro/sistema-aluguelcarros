<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { useRentalsApp } from './composables/useRentalsApp'

const { feedback, loading } = useRentalsApp()
</script>

<template>
  <div class="shell">
    <header class="topbar">
      <div class="topbar-inner">
        <RouterLink to="/" class="brand">
          <span class="brand-mark" aria-hidden="true" />
          <span class="brand-text">Aluguel Carros</span>
        </RouterLink>
        <nav class="nav" aria-label="Principal">
          <RouterLink to="/" class="nav-link">Início</RouterLink>
          <RouterLink to="/clientes" class="nav-link">Clientes</RouterLink>
          <RouterLink to="/pedidos" class="nav-link">Pedidos</RouterLink>
        </nav>
      </div>
    </header>

    <p v-if="feedback" :class="['toast', feedback.kind]" role="status">
      {{ feedback.text }}
    </p>
    <div v-if="loading" class="loading-bar" aria-hidden="true" />

    <main class="main">
      <RouterView v-slot="{ Component }">
        <Transition name="view" mode="out-in">
          <component :is="Component" />
        </Transition>
      </RouterView>
    </main>

    <footer class="footer">
      <p>Frontend Vue · API via proxy <code>/api</code></p>
    </footer>
  </div>
</template>

<style scoped>
.shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: 'DM Sans', system-ui, sans-serif;
  color: var(--text);
  background:
    radial-gradient(ellipse 80% 50% at 50% -20%, rgb(99 102 241 / 12%), transparent),
    radial-gradient(ellipse 60% 40% at 100% 0%, rgb(37 99 235 / 8%), transparent),
    var(--bg);
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 40;
  border-bottom: 1px solid var(--border);
  background: rgb(255 255 255 / 85%);
  backdrop-filter: blur(10px);
}

.topbar-inner {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0.75rem 1.25rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  flex-wrap: wrap;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 0.6rem;
  text-decoration: none;
  color: inherit;
  font-weight: 700;
  font-size: 1.05rem;
  letter-spacing: -0.02em;
}

.brand-mark {
  width: 2rem;
  height: 2rem;
  border-radius: 9px;
  background: linear-gradient(135deg, var(--accent) 0%, #4f46e5 100%);
  box-shadow: 0 4px 12px rgb(37 99 235 / 35%);
}

.brand:hover .brand-mark {
  transform: scale(1.02);
  transition: transform 0.15s ease;
}

.nav {
  display: flex;
  flex-wrap: wrap;
  gap: 0.35rem;
}

.nav-link {
  padding: 0.45rem 0.85rem;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-muted);
  text-decoration: none;
  transition:
    background 0.15s ease,
    color 0.15s ease;
}

.nav-link:hover {
  color: var(--text);
  background: var(--surface-muted);
}

.nav-link.router-link-active {
  color: var(--accent);
  background: rgb(37 99 235 / 10%);
}

.toast {
  max-width: 1100px;
  margin: 0.75rem auto 0;
  padding: 0.65rem 1rem;
  border-radius: 10px;
  font-size: 0.92rem;
  font-weight: 500;
  margin-left: 1rem;
  margin-right: 1rem;
}

.toast.ok {
  background: #ecfdf5;
  color: #065f46;
  border: 1px solid #6ee7b7;
}

.toast.err {
  background: #fef2f2;
  color: #991b1b;
  border: 1px solid #fca5a5;
}

.loading-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  z-index: 50;
  background: linear-gradient(
    90deg,
    transparent,
    var(--accent),
    transparent
  );
  animation: shimmer 1s ease-in-out infinite;
}

@keyframes shimmer {
  0% {
    opacity: 0.35;
    transform: scaleX(0.3);
    transform-origin: left;
  }
  50% {
    opacity: 1;
    transform: scaleX(1);
    transform-origin: center;
  }
  100% {
    opacity: 0.35;
    transform: scaleX(0.3);
    transform-origin: right;
  }
}

.main {
  flex: 1;
  max-width: 1100px;
  width: 100%;
  margin: 0 auto;
  padding: 1.25rem 1.25rem 2.5rem;
}

.footer {
  border-top: 1px solid var(--border);
  padding: 1rem 1.25rem;
  text-align: center;
  font-size: 0.82rem;
  color: var(--text-soft);
  background: var(--surface);
}

.footer p {
  margin: 0;
}

.footer code {
  font-size: 0.9em;
  background: var(--surface-muted);
  padding: 0.1rem 0.35rem;
  border-radius: 4px;
}

.view-enter-active,
.view-leave-active {
  transition:
    opacity 0.2s ease,
    transform 0.2s ease;
}

.view-enter-from,
.view-leave-to {
  opacity: 0;
  transform: translateY(6px);
}
</style>
