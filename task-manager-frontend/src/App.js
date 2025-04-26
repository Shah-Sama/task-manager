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

  const fetchTodos = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/todos');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
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
    
    fetch('http://localhost:8080/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text: newTodo })
    }).then(res => res.json())
      .then(data => {
        setTodos([...todos, data]);
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

  const toggleComplete = (id) => {
    const todo = todos.find(t => t.id === id);
    if (!todo) return;

    const updatedTodo = {
      ...todo,
      completed: !todo.completed
    };

    fetch(`http://localhost:8080/api/todos/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(updatedTodo)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(updatedTodo => {
      setTodos(todos.map(t => t.id === id ? updatedTodo : t));
    })
    .catch(error => {
      console.error('Error updating todo:', error);
      // Revert the UI state if the update failed
      setTodos([...todos]);
    });
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static">
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              Task Manager
            </Typography>
          </Toolbar>
        </AppBar>
        
        <Container maxWidth="sm" sx={{ mt: 4 }}>
          <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
            <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
              <TextField
                fullWidth
                variant="outlined"
                label="Add a new task"
                value={newTodo}
                onChange={(e) => setNewTodo(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && addTodo()}
              />
              <Button
                variant="contained"
                color="primary"
                onClick={addTodo}
                startIcon={<AddIcon />}
                sx={{ minWidth: '100px' }}
              >
                Add
              </Button>
            </Box>
          </Paper>

          <Paper elevation={3}>
            <List>
              {todos.map(todo => (
                <ListItem
                  key={todo.id}
                  divider
                  sx={{
                    textDecoration: todo.completed ? 'line-through' : 'none',
                    opacity: todo.completed ? 0.7 : 1
                  }}
                >
                  <Checkbox
                    checked={todo.completed}
                    onChange={() => toggleComplete(todo.id)}
                  />
                  <ListItemText primary={todo.text} />
                  <ListItemSecondaryAction>
                    <IconButton
                      edge="end"
                      aria-label="delete"
                      onClick={() => deleteTodo(todo.id)}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </ListItemSecondaryAction>
                </ListItem>
              ))}
            </List>
          </Paper>
        </Container>
      </Box>
    </ThemeProvider>
  );
}

export default App;
