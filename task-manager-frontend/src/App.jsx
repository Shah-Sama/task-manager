import React, { useState } from 'react';
import TodoList from './components/TodoList';
import XpBar from './components/XpBar';
import './App.css';

function App() {
  console.log('App component rendering');
  const [userId] = useState('user-123'); // In a real app, this would come from authentication

  return (
    <div className="App">
      <header className="App-header">
        <h1>Task Manager</h1>
        <XpBar userId={userId} />
      </header>
      <main>
        <TodoList userId={userId} />
      </main>
    </div>
  );
}

export default App; 