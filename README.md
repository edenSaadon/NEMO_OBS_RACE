# Nemo Obstacle Race Game

## Overview
The Nemo Obstacle Race Game is a fun and engaging mobile game where the player controls Nemo, navigating through a series of obstacles (sharks) to avoid collisions. The game features a three-lane road setup, and Nemo can move left or right to avoid the obstacles. The player has three lives, and the game continues endlessly until all lives are lost.

## Features
- App icon featuring Nemo
- Five-lane road with extended length
- Nemo character controlled by buttons or sensors (tilting the device)
- Obstacles (sharks) moving towards the player
- Collectible coins on the road
- Collision detection with vibration feedback and crash sound
- Three lives represented by hearts
- Endless gameplay with a game over notification
- Odometer tracking the distance covered
- Main menu with options to choose between buttons, sensors, or view high scores
- High scores screen with a table of top scores and a map displaying the location of each score

## How to Play
1. Use the left and right buttons or tilt the device to move Nemo left or right.
2. Avoid the shark obstacles and collect coins to increase your score.
3. The game ends when all three lives are lost.
4. Check your high scores and locations on the high scores screen.

## Screenshots
### App Icon
![Screenshot_20240703_212401ICON](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/27013bac-0d0d-4d4f-b696-34f8b6f3ebaf)
### Home Screen
![Screenshot_20240703_212432Open](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/b8295e6c-4a9d-4990-9db1-1ae52a643f87)

### Game with Buttons
![Screenshot_20240703_212514BTN](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/50e63fc7-11d1-44b8-9757-12eecddc6717)

### High Scores Screen
![Screenshot_20240703_212549Score](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/ba60a799-74a6-4e19-befa-efb826201dd4)

### Game with Sensors
![Screenshot_20240703_212636Sn](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/c552e3c7-4fc3-4275-a298-941ed42fe747)

## Code Structure
- **MainActivity.java**: The main activity managing the menu and navigation.
- **ButtonsActivity.java**: The activity where the game logic is implemented using buttons.
- **SensorActivity.java**: The activity where the game logic is implemented using sensors.
- **GameManager.java**: Manages the game state, including lives, score, and game over conditions.
- **HighScoresActivity.java**: The activity managing the high scores and map display.
- **HighScoresFragment.java**: Fragment displaying the top 10 scores.
- **MapFragment.java**: Fragment displaying the map with locations of high scores.
- **HighScoresManager.java**: Manages the high scores, saving and retrieving them from shared preferences.
- **HighScore.java**: The data model for a high score, including user ID, score, latitude, longitude, and distance.
- **activity_main.xml**: The layout file defining the UI components for the main activity.
- **activity_buttons.xml**: The layout file defining the UI components for the buttons activity.
- **activity_sensors.xml**: The layout file defining the UI components for the sensors activity.
- **fragment_high_scores.xml**: The layout file for the high scores fragment.
- **fragment_map.xml**: The layout file for the map fragment.

## Dependencies
- [Glide](https://github.com/bumptech/glide): Used for loading and displaying images.
- [OSMDroid](https://github.com/osmdroid/osmdroid): Used for displaying maps and tracking locations.
