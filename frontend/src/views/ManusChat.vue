<template>
  <div>
    <h2>AI 超级智能体</h2>
    <ChatWindow v-model:messages="messages" :onSendMessage="sendMessage" placeholder="和超级智能体聊天..." />
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue'
import ChatWindow from '../components/ChatWindow.vue'

const API_BASE = 'http://localhost:8123/api/ai'

const messages = ref([{ role: 'ai', content: '你好，我是超级智能体。可以开始你的问题。' }])
let es = null

async function sendMessage(text) {
  messages.value.push({ role: 'user', content: text })
  startSse(text)
}

function startSse(userMessage) {
  if (es) {
    es.close()
    es = null
  }
  const url = `${API_BASE}/manus/chat?message=${encodeURIComponent(userMessage)}`
  es = new EventSource(url)
  let aiBuffer = ''

  const pushBuffer = () => {
    if (aiBuffer.length > 0) {
      const last = messages.value[messages.value.length - 1]
      if (last && last.role === 'ai' && last.streaming) {
        last.content = aiBuffer
      } else {
        messages.value.push({ role: 'ai', content: aiBuffer, streaming: true })
      }
    }
  }

  es.onmessage = (e) => {
    if (!e.data) return
    if (e.data === '[DONE]') {
      pushBuffer()
      const last = messages.value[messages.value.length - 1]
      if (last && last.role === 'ai') last.streaming = false
      es.close()
      es = null
      return
    }
    aiBuffer += e.data
    pushBuffer()
  }

  es.onerror = () => {
    es && es.close()
    es = null
  }
}

onBeforeUnmount(() => { if (es) es.close() })
</script>

<style scoped>
</style>



