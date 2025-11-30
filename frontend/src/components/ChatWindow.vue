<template>
  <div class="chat-window">
    <div class="messages" ref="messagesRef">
      <div v-for="(msg, idx) in messages" :key="idx" class="message" :class="msg.role">
        <div class="bubble">
          <pre>{{ msg.content }}</pre>
        </div>
      </div>
    </div>
    <form class="input-row" @submit.prevent="onSend">
      <input v-model="input" :placeholder="placeholder" />
      <button type="submit" :disabled="sending">发送</button>
    </form>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'

const props = defineProps({
  placeholder: { type: String, default: '请输入消息...' },
  onSendMessage: { type: Function, required: true }
})

const input = ref('')
const messages = defineModel('messages', { type: Array, default: () => [] })
const sending = ref(false)
const messagesRef = ref(null)

function scrollToBottom() {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

watch(messages, scrollToBottom, { deep: true })

async function onSend() {
  const content = input.value.trim()
  if (!content || sending.value) return
  sending.value = true
  try {
    await props.onSendMessage(content)
    input.value = ''
  } finally {
    sending.value = false
  }
}
</script>

<style scoped>
.chat-window { display: flex; flex-direction: column; height: calc(100vh - 120px); max-width: 1000px; margin: 0 auto; }
.messages { flex: 1; overflow: auto; padding: 12px; background: #fafafa; border: 1px solid #eee; border-radius: 8px; }
.message { display: flex; margin: 8px 0; }
.message.user { justify-content: flex-end; }
.message.ai { justify-content: flex-start; }
.bubble { max-width: 70%; white-space: pre-wrap; background: white; border: 1px solid #eee; border-radius: 8px; padding: 10px 12px; }
.message.user .bubble { background: #e6f7ff; border-color: #91d5ff; }
.input-row { display: flex; gap: 8px; margin-top: 12px; }
.input-row input { flex: 1; padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; }
.input-row button { padding: 0 16px; }
pre { margin: 0; font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace; font-size: 14px; }
</style>



