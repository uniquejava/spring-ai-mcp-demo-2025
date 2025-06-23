import type { Message } from '@/api/apiModel.ts';

export const sendMessage = async (id: string, message: string, onChunk: (chunk: string) => void): Promise<void> => {
  const response = await fetch('http://localhost:8080/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ id: id, query: message }),
  });

  if (!response.ok || !response.body) {
    throw new Error(`请求失败：${response.status}`);
  }

  const reader = response.body.getReader();
  const decoder = new TextDecoder('utf-8');

  try {
    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      const chunk = decoder.decode(value, { stream: true });
      onChunk(chunk);
    }
  } catch (error) {
    console.error('流式读取错误：', error);
  } finally {
    reader.releaseLock();
  }
};

export const getMessages = async (id: string): Promise<Message[]> => {
  const response = await fetch('http://localhost:8080/messages?conversation_id' + id);
  const json = await response.json();
  return json;
};
