import { UserOutlined } from '@ant-design/icons';
import { Flex, Layout } from 'antd';
import { Bubble, Sender } from '@ant-design/x';
import { useState } from 'react';
import './Chat.less';
import { getMessages, sendMessage } from '@/api/apiService.ts';
import { useEffectAsync } from '@/utils/utils.ts';

// 定义允许匿名对象扩展的Message类型
type ChatMessage = {
  content?: string;
  sender?: string;
};

export default function HomePage() {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [value, setValue] = useState<string>('');

  const chatId = 'default';

  useEffectAsync(async () => {
    const backendMessages = await getMessages(chatId);
    // 将后端消息格式映射为前端所需的格式
    const frontendMessages = backendMessages.map((msg) => ({
      content: msg.text,
      sender: msg.messageType === 'USER' ? 'user' : 'assistant',
    }) satisfies ChatMessage);
    setMessages((prevMessages: ChatMessage[]) => [
      ...prevMessages,
      ...frontendMessages
    ]);
  }, []);

  const handleSendMessage = async () => {
    if (!value.trim()) return;

    // 添加用户消息到聊天记录
    setMessages((prevMessages) => [
      ...prevMessages,
      { content: value, sender: 'user' },
    ]);

    setValue('');

    try {
      let accumulatedResponse = '';
      await sendMessage(chatId, value, (chunk) => {
        accumulatedResponse += chunk;
        console.log(chunk);

        setMessages((prevMessages) => {
          if (prevMessages[prevMessages.length - 1]?.sender === 'assistant') {
            return [
              ...prevMessages.slice(0, -1),
              { content: accumulatedResponse, sender: 'assistant' },
            ];
          }
          return [...prevMessages, { content: accumulatedResponse, sender: 'assistant' }];
        });
      });
    } catch (error) {
      console.error('Failed to send message:', error);
    }
  };

  return (
    <Layout style={{ height: '90vh' }}>
      <Flex gap="middle" vertical>
        {messages.map((msg, index) => (
          <Bubble
            key={index}
            placement={msg.sender === 'user' ? 'end' : 'start'}
            content={msg.content}
            avatar={{ icon: <UserOutlined /> }}
          />
        ))}
      </Flex>
      <div className="sender-container">
        <Sender
          value={value}
          onChange={(v) => {
            setValue(v);
          }}
          onSubmit={handleSendMessage}
          autoSize={{ minRows: 2, maxRows: 6 }}
        />
      </div>
    </Layout>
  );
}