import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Typography, 
  TextField, 
  Button, 
  List, 
  ListItem, 
  ListItemText, 
  ListItemSecondaryAction,
  IconButton,
  Paper,
  Box,
  AppBar,
  Toolbar,
  Checkbox,
  CssBaseline,
  ThemeProvider,
  createTheme
} from '@mui/material';
import { Delete as DeleteIcon, Add as AddIcon } from '@mui/icons-material';

// Create a theme instance with light green colors
const theme = createTheme({
  palette: {
    primary: {
      main: '#81c784', // Light green
      light: '#b2fab4',
      dark: '#519657',
    },
    secondary: {
      main: '#a5d6a7', // Lighter green
      light: '#d7ffd9',
      dark: '#75a478',
    },
    background: {
      default: '#f1f8e9', // Very light green
      paper: '#ffffff',
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          backgroundColor: '#81c784',
          '&:hover': {
            backgroundColor: '#519657',
          },
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundColor: '#f1f8e9',
        },
      },
    },
  },
});

function App() {
  const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState('');
  const [newDescription, setNewDescription] = useState('');

  const fetchTodos = async () => {
    try {
      console.log('Fetching todos...');
      const response = await fetch('http://localhost:8080/api/todos');
      console.log('Response status:', response.status);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      console.log('Received data:', data);
      if (Array.isArray(data)) {
        setTodos(data);
      } else {
        console.error('Received data is not an array:', data);
        setTodos([]);
      }
    } catch (error) {
      console.error('Error fetching todos:', error);
      setTodos([]);
    }
  };

  useEffect(() => {
    fetchTodos();
  }, []);

  const addTodo = () => {
    if (newTodo.trim() === '') return;
    
    console.log('Adding new todo:', { title: newTodo, description: newDescription });
    
    fetch('http://localhost:8080/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ 
        title: newTodo,
        description: newDescription,
        completed: false
      })
    })
    .then(res => {
      console.log('Add todo response status:', res.status);
      return res.json();
    })
    .then(data => {
      console.log('Add todo response data:', data);
      setTodos([...todos, data]);
      setNewTodo('');
      setNewDescription('');
    })
    .catch(error => {
      console.error('Error adding todo:', error);
    });
  };

  const deleteTodo = (id) => {
    console.log('Deleting todo:', id);
    fetch(`http://localhost:8080/api/todos/${id}`, {
      method: 'DELETE'
    }).then(() => {
      setTodos(todos.filter(todo => todo.id !== id));
    });
  };

  const toggleComplete = (id) => {
    console.log('Toggling todo:', id);
    fetch(`http://localhost:8080/api/todos/${id}/toggle`, {
      method: 'PUT'
    }).then(res => res.json())
      .then(updatedTodo => {
        setTodos(todos.map(todo => 
          todo.id === id ? updatedTodo : todo
        ));
      });
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Task Manager
          </Typography>
        </Toolbar>
      </AppBar>
      <Container maxWidth="sm" sx={{ mt: 4 }}>
        <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="New Task"
              variant="outlined"
              value={newTodo}
              onChange={(e) => setNewTodo(e.target.value)}
              fullWidth
            />
            <TextField
              label="Description"
              variant="outlined"
              value={newDescription}
              onChange={(e) => setNewDescription(e.target.value)}
              fullWidth
              multiline
              rows={2}
            />
            <Button
              variant="contained"
              onClick={addTodo}
              startIcon={<AddIcon />}
            >
              Add Task
            </Button>
          </Box>
        </Paper>
        <List>
          {todos.map(todo => (
            <ListItem
              key={todo.id}
              sx={{
                mb: 1,
                bgcolor: 'background.paper',
                borderRadius: 1,
                '&:hover': {
                  bgcolor: 'action.hover',
                },
              }}
            >
              <Checkbox
                checked={todo.completed}
                onChange={() => toggleComplete(todo.id)}
              />
              <ListItemText
                primary={todo.title}
                secondary={todo.description}
                sx={{
                  textDecoration: todo.completed ? 'line-through' : 'none',
                  color: todo.completed ? 'text.secondary' : 'text.primary',
                }}
              />
              <ListItemSecondaryAction>
                <IconButton
                  edge="end"
                  aria-label="delete"
                  onClick={() => deleteTodo(todo.id)}
                >
                  <DeleteIcon />
                </IconButton>
              </ListItemSecondaryAction>
            </ListItem>
          ))}
        </List>
      </Container>
    </ThemeProvider>
  );
}

export default App;
