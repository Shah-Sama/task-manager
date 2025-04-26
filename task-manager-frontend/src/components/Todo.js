import React from 'react';
import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({
  todoItem: {
    display: 'flex',
    alignItems: 'center',
    padding: '10px',
    margin: '5px 0',
    backgroundColor: '#f1f8e9',
    borderRadius: '4px',
    '&:hover': {
      backgroundColor: '#e8f5e9',
    },
  },
  completed: {
    textDecoration: 'line-through',
    color: '#81c784',
  },
  checkbox: {
    color: '#81c784',
    '&.Mui-checked': {
      color: '#519657',
    },
  },
  deleteButton: {
    color: '#81c784',
    '&:hover': {
      color: '#519657',
    },
  },
});

const Todo = ({ todo, onDelete, onToggle }) => {
  const classes = useStyles();

  if (!todo || typeof todo !== 'object') {
    console.error('Invalid todo data:', todo);
    return null;
  }

  return (
    <div className={classes.todoItem}>
      <input
        type="checkbox"
        checked={todo.completed || false}
        onChange={() => onToggle(todo.id)}
        className={classes.checkbox}
      />
      <span className={todo.completed ? classes.completed : ''}>
        {todo.text || 'No text'}
      </span>
      <button 
        onClick={() => onDelete(todo.id)}
        className={classes.deleteButton}
      >
        Delete
      </button>
    </div>
  );
};

export default Todo; 