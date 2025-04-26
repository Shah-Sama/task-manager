import React, { useState, useEffect } from 'react';

function App() {
  const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/todos')
      .then(res => res.json())
      .then(data => setTodos(data));
  }, []);

  const addTodo = () => {
    fetch('http://localhost:8080/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text: newTodo })
    }).then(() => {
      setTodos([...todos, { text: newTodo }]);
      setNewTodo('');
    });
  };

  const deleteTodo = (id) => {
    fetch(`http://localhost:8080/api/todos/${id}`, {
      method: 'DELETE'
    }).then(() => {
      setTodos(todos.filter(todo => todo.id !== id));
    });
  };

  return (
    <div style={{ padding: 20 }}>
      <h1>To-Do List</h1>
      <input
        value={newTodo}
        onChange={e => setNewTodo(e.target.value)}
        placeholder="Enter a task"
      />
      <button onClick={addTodo}>Add</button>
      <ul>
        {todos.map(todo => (
          <li key={todo.id}>
            {todo.text} <button onClick={() => deleteTodo(todo.id)}>❌</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
