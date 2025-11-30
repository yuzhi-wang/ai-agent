# AI 前端（Vue 3 + Vite）

一个包含 3 个页面的前端项目：主页、AI 恋爱大师、AI 超级智能体。采用 SSE（Server-Sent Events）实时流式对话展示。

## 技术栈
- Vue 3 + Vite
- Vue Router
- Axios（备用，后续可扩展普通 HTTP 请求）

## 目录结构
```
frontend/
  ├─ index.html
  ├─ package.json
  ├─ vite.config.js
  └─ src/
      ├─ main.js
      ├─ App.vue
      ├─ styles.css
      ├─ router/
      │   └─ index.js
      ├─ components/
      │   └─ ChatWindow.vue
      └─ views/
          ├─ Home.vue
          ├─ LoveChat.vue
          └─ ManusChat.vue
```

## 页面说明
- 主页：在不同应用之间进行切换。
- 恋爱大师：聊天室界面，进入后自动生成 `chatId`，通过 SSE 调用后端接口实时显示 AI 回复。
- 超级智能体：同样为聊天室界面，通过 SSE 调用后端接口实时显示 AI 回复。

## 后端接口
- 基础前缀：`http://localhost:8123/api`
- 恋爱大师 SSE：`GET /api/ai/love_app/chat/sse?message=...&chatId=...`
- 超级智能体 SSE：`GET /api/ai/manus/chat?message=...`

> 注：SSE 为单向流，前端使用 `EventSource` 订阅后端推送；当收到 `[DONE]` 时结束本轮消息。

## 本地运行（Windows PowerShell）
```powershell
cd frontend
npm install
npm run dev
```

默认启动端口：`5173`，浏览器将自动打开。

## 生产构建
```powershell
cd frontend
npm run build
npm run preview
```

## 自定义
- 若后端地址变更，可在 `LoveChat.vue` 与 `ManusChat.vue` 内修改 `API_BASE`。
- 样式位于 `src/styles.css`，聊天气泡布局在 `components/ChatWindow.vue` 中可调整。

