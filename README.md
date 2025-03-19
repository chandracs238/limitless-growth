# Limitless Growth REST API Documentation

Overview

The Limitless Growth API is inspired by the Solo Leveling anime, offering a gamified task management system where users can complete daily missions and track personal goals. The system optimizes data storage using a task template mechanism, ensuring that all users receive the same missions without redundant entries.

Key Features

Daily Missions: A predefined 1-100 day mission template assigned to every user.

Personal Goals: Users can add their own custom goals alongside the daily missions.

Gamification: Users earn experience points (XP) upon completing missions, motivating continuous progress.

Optimized Storage: Reduces redundant task storage by ~80% through a shared mission template.

Authentication

The API uses JWT-based authentication to ensure secure access to resources. Users must include a valid token in the Authorization header for protected endpoints.

API Endpoints



