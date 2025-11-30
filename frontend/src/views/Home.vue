<template>
  <div class="home">
    <h2>主页</h2>
    <div class="cards">
      <RouterLink class="card" to="/love">
        <h3>AI 恋爱大师</h3>
        <p>实时聊天，获取恋爱建议与情感支持。</p>
      </RouterLink>
      <RouterLink class="card" to="/manus">
        <h3>AI 超级智能体</h3>
        <p>多工具协作的智能体对话。</p>
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 16px; }
.card { display: block; border: 1px solid #eee; padding: 16px; border-radius: 8px; color: inherit; text-decoration: none; transition: box-shadow .2s ease; }
.card:hover { box-shadow: 0 6px 20px rgba(0,0,0,0.08); }
</style>



