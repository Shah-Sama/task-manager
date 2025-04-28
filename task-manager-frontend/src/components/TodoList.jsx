import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './TodoList.css';

const TodoList = ({ userId }) => {
  const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState({ title: '', description: '' });

  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/todos');
      setTodos(response.data);
    } catch (error) {
      console.error('Error fetching todos:', error);
    }
  };

  const createTodo = async () => {
    try {
      await axios.post('http://localhost:8080/api/todos', newTodo, {
        params: { userId }
      });
      setNewTodo({ title: '', description: '' });
      fetchTodos();
    } catch (error) {
      console.error('Error creating todo:', error);
    }
  };

  const toggleTodo = async (id) => {
    try {
      await axios.put(`http://localhost:8080/api/todos/${id}/toggle`, null, {
        params: { userId }
      });
      fetchTodos();
    } catch (error) {
      console.error('Error toggling todo:', error);
    }
  };

  const deleteTodo = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/todos/${id}`);
      fetchTodos();
    } catch (error) {
      console.error('Error deleting todo:', error);
    }
  };

  return (
    <div className="todo-list">
      <div className="todo-form">
        <input
          type="text"
          placeholder="Title"
          value={newTodo.title}
          onChange={(e) => setNewTodo({ ...newTodo, title: e.target.value })}
        />
        <input
          type="text"
          placeholder="Description"
          value={newTodo.description}
          onChange={(e) => setNewTodo({ ...newTodo, description: e.target.value })}
        />
        <button onClick={createTodo}>Add Todo</button>
      </div>
      <ul>
        {todos.map((todo) => (
          <li key={todo.id} className={todo.completed ? 'completed' : ''}>
            <div className="todo-content">
              <h3>{todo.title}</h3>
              <p>{todo.description}</p>
            </div>
            <div className="todo-actions">
              <button onClick={() => toggleTodo(todo.id)}>
                {todo.completed ? 'Undo' : 'Complete'}
              </button>
              <button onClick={() => deleteTodo(todo.id)}>Delete</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TodoList; 