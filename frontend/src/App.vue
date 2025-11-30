<template>
  <div class="app-shell">
    <header class="app-header">
      <h1>AI 应用集合</h1>
      <nav class="nav">
        <RouterLink to="/">主页</RouterLink>
        <RouterLink to="/love">恋爱大师</RouterLink>
        <RouterLink to="/manus">超级智能体</RouterLink>
      </nav>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
  </div>
  
</template>

<script setup>
</script>

<style scoped>
.app-shell { display: flex; flex-direction: column; min-height: 100vh; }
.app-header { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; border-bottom: 1px solid #eee; }
.nav { display: flex; gap: 12px; }
.nav a { color: #2c3e50; text-decoration: none; }
.nav a.router-link-active { font-weight: 700; }
.app-main { flex: 1; padding: 16px; }
</style>



