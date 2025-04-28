import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './XpBar.css';

const XpBar = ({ userId }) => {
    console.log('XpBar component rendering with userId:', userId);
    const [userStats, setUserStats] = useState({
        xp: 0,
        level: 1,
        tasksCompleted: 0
    });
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserStats = async () => {
            console.log('Fetching user stats for userId:', userId);
            try {
                const response = await axios.get(`http://localhost:8080/api/todos/user/${userId}`);
                console.log('User stats response:', response.data);
                setUserStats(response.data);
                setError(null);
            } catch (error) {
                console.error('Error fetching user stats:', error);
                setError(error.message);
            }
        };

        if (userId) {
            fetchUserStats();
        }
    }, [userId]);

    const xpForNextLevel = (level) => {
        return Math.pow(level, 2) * 100;
    };

    const currentLevelXp = xpForNextLevel(userStats.level - 1);
    const nextLevelXp = xpForNextLevel(userStats.level);
    const xpProgress = ((userStats.xp - currentLevelXp) / (nextLevelXp - currentLevelXp)) * 100;

    console.log('Rendering XP bar with progress:', xpProgress);

    if (error) {
        return (
            <div className="xp-bar-container error">
                <div className="error-message">Error loading XP: {error}</div>
            </div>
        );
    }

    return (
        <div className="xp-bar-container">
            <div className="level-info">
                <span className="level">Level {userStats.level}</span>
                <span className="tasks-completed">Tasks Completed: {userStats.tasksCompleted}</span>
            </div>
            <div className="xp-bar">
                <div 
                    className="xp-progress" 
                    style={{ width: `${xpProgress}%` }}
                />
            </div>
            <div className="xp-info">
                <span>{userStats.xp} / {nextLevelXp} XP</span>
            </div>
        </div>
    );
};

export default XpBar; 