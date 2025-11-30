<template>
  <div>
    <h2>AI 恋爱大师</h2>
    <p class="meta">聊天室 ID：{{ chatId }}</p>
    <ChatWindow v-model:messages="messages" :onSendMessage="sendMessage" placeholder="和恋爱大师聊天..." />
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue'
import ChatWindow from '../components/ChatWindow.vue'
import axios from 'axios'

const API_BASE = 'http://localhost:8123/api/ai'

const messages = ref([{ role: 'ai', content: '你好，我是恋爱大师。有什么想聊的吗？' }])
const chatId = ref(generateChatId())
let es = null

function generateChatId() {
  return 'chat_' + Math.random().toString(36).slice(2) + Date.now().toString(36)
}

async function sendMessage(text) {
  messages.value.push({ role: 'user', content: text })
  // Start SSE stream for AI response
  startSse(text)
}

function startSse(userMessage) {
  // Close previous stream if any
  if (es) {
    es.close()
    es = null
  }
  const url = `${API_BASE}/love_app/chat/sse?message=${encodeURIComponent(userMessage)}&chatId=${encodeURIComponent(chatId.value)}`
  es = new EventSource(url)
  let aiBuffer = ''

  const pushBuffer = () => {
    if (aiBuffer.length > 0) {
      // if last message is ai and streaming, update it; else push new
      const last = messages.value[messages.value.length - 1]
      if (last && last.role === 'ai' && last.streaming) {
        last.content = aiBuffer
      } else {
        messages.value.push({ role: 'ai', content: aiBuffer, streaming: true })
      }
    }
  }

  es.onmessage = (e) => {
    // Some backends may send [DONE] or empty heartbeats
    if (!e.data) return
    if (e.data === '[DONE]') {
      pushBuffer()
      // mark last ai as finished
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

onBeforeUnmount(() => {
  if (es) es.close()
})
</script>

<style scoped>
.meta { color: #888; margin-bottom: 8px; }
</style>



