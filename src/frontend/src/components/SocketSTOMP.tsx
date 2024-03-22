import React, { useState } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const SocketSTOMP: React.FC = () => {
  const [socket, setSocket] = useState<any>(null);
  const [stompClient, setStompClient] = useState<any>(null);
  const [status, setStatus] = useState<string>('Disconnected');

  const connect = () => {
    const newSocket = new SockJS(process.env.NEXT_PUBLIC_URL+'/esp');
    const newStompClient = Stomp.over(newSocket);

    newStompClient.connect({}, (frame: any) => {
      console.log('Connected: ' + frame);
      setSocket(newSocket);
      setStompClient(newStompClient);
      setStatus('Connected');


      newStompClient.subscribe('/topic/readers', (message: any) => {
        console.log('Message: ' + message.body);
        // handle incoming messages here
      });
    });

  };

  const disconnect = () => {
    if (stompClient !== null) {
      stompClient.disconnect();
    }
    setStatus('Disconnected');
  };

  const sendPing = () => {
    // Replace with actual readerId
    const readerId = 'some-reader-id';
    fetch(process.env.NEXT_PUBLIC_URL+'/readers', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: 'readerId=' + encodeURIComponent(readerId),
    }).then(response => {
      console.log('Ping sent. Status: ' + response.status);
    });
  };

  return (
    <div>
      <h1>WebSocket Client</h1>
      <button onClick={connect}>Connect</button>
      <button onClick={disconnect}>Disconnect</button>
      <button onClick={sendPing}>Send Ping</button>
      <div id="status">{status}</div>
    </div>
  );
};

export default SocketSTOMP;
