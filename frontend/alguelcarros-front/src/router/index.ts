import { createRouter, createWebHistory } from 'vue-router'
import { useAuth } from '../composables/useAuth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'inicio',
      component: () => import('../views/HomeView.vue'),
      meta: { title: 'Início' },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { title: 'Entrar', guestOnly: true },
    },
    {
      path: '/registro',
      name: 'registro',
      component: () => import('../views/RegistroView.vue'),
      meta: { title: 'Cadastro', guestOnly: true },
    },
    {
      path: '/cliente/perfil',
      name: 'cliente-perfil',
      component: () => import('../views/ClientePerfilView.vue'),
      meta: { title: 'Meu perfil', role: 'CLIENTE' as const },
    },
    {
      path: '/cliente/pedidos',
      name: 'cliente-pedidos',
      component: () => import('../views/ClientePedidosView.vue'),
      meta: { title: 'Meus pedidos', role: 'CLIENTE' as const },
    },
    {
      path: '/agente/pedidos',
      name: 'agente-pedidos',
      component: () => import('../views/AgentePedidosView.vue'),
      meta: { title: 'Pedidos (agente)', role: 'AGENTE' as const },
    },
  ],
  scrollBehavior() {
    return { top: 0, behavior: 'smooth' }
  },
})

router.beforeEach((to) => {
  const auth = useAuth()
  if (to.meta.guestOnly && auth.isAuthenticated.value) {
    return auth.tipo.value === 'CLIENTE'
      ? { name: 'cliente-pedidos' }
      : { name: 'agente-pedidos' }
  }
  if (to.meta.role) {
    if (!auth.isAuthenticated.value) {
      return { name: 'login', query: { redirect: to.fullPath } }
    }
    if (to.meta.role === 'CLIENTE' && auth.tipo.value !== 'CLIENTE') {
      return { name: 'agente-pedidos' }
    }
    if (to.meta.role === 'AGENTE' && auth.tipo.value !== 'AGENTE') {
      return { name: 'cliente-pedidos' }
    }
  }
  return true
})

router.afterEach((to) => {
  const base = 'Aluguel de Carros'
  document.title =
    typeof to.meta.title === 'string' ? `${to.meta.title} · ${base}` : base
})

export default router
