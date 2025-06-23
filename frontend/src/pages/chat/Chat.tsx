import { UserOutlined } from '@ant-design/icons';
import { Flex, Layout } from 'antd';
import { Bubble, Sender } from '@ant-design/x';
import { useState } from 'react';
import './Chat.less';
import { sendMessage } from '@/api/apiService.ts';

interface Message {
  content: string;
  placement: 'start' | 'end';
}

export default function HomePage() {
  const [messages, setMessages] = useState<Message[]>([{ content: '你好，我是你的智能助手！', placement: 'start' }]);
  const [value, setValue] = useState<string>('');

  const chatId = '123';

  const handleSendMessage = async () => {
    if (!value.trim()) return;

    setMessages((prevMessages) => [...prevMessages, { content: value, placement: 'end' }]);

    setValue('');

    try {
      let accumulatedResponse = '';
      await sendMessage(chatId, value, (chunk) => {
        accumulatedResponse += chunk;
        console.log(chunk);

        setMessages((prevMessages) => {
          if (prevMessages[prevMessages.length - 1].placement === 'end') {
            return [...prevMessages, { content: accumulatedResponse, placement: 'start' }];
          }
          return [...prevMessages.slice(0, -1), { content: accumulatedResponse, placement: 'start' }];
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
          <Bubble key={index} placement={msg.placement} content={msg.content} avatar={{ icon: <UserOutlined /> }} />
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
