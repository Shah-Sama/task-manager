import React from 'react';
import { Box, LinearProgress, Typography, Paper } from '@mui/material';

const XPBar = ({ xp, level, tasksCompleted }) => {
  // Calculate progress to next level (100 XP per level)
  const xpForCurrentLevel = (level - 1) * 100;
  const progress = ((xp - xpForCurrentLevel) / 100) * 100;

  return (
    <Paper elevation={3} sx={{ p: 2, mb: 3, bgcolor: 'background.paper' }}>
      <Box sx={{ width: '100%', mb: 2 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
          <Typography variant="h6" color="primary">
            Level {level}
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Tasks Completed: {tasksCompleted}
          </Typography>
        </Box>
        <LinearProgress 
          variant="determinate" 
          value={progress} 
          sx={{ 
            height: 10, 
            borderRadius: 5,
            backgroundColor: '#e8f5e9',
            '& .MuiLinearProgress-bar': {
              backgroundColor: '#81c784'
            }
          }} 
        />
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1 }}>
          <Typography variant="body2" color="text.secondary">
            XP: {xp}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Next Level: {(level) * 100} XP
          </Typography>
        </Box>
      </Box>
    </Paper>
  );
};

export default XPBar; 