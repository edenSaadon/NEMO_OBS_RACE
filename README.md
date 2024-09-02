# Nemo Obstacle Race Game

## Overview
The Nemo Obstacle Race Game is a fun and engaging mobile game where the player controls Nemo, navigating through a series of obstacles (sharks) to avoid collisions. The game features a three-lane road setup, and Nemo can move left or right to avoid the obstacles. The player has three lives, and the game continues endlessly until all lives are lost.

## Demo
Watch the demo of the app here: [Link](https://drive.google.com/file/d/1_LzTEF1j3DdKnTuybK2aGVpDXrcalZKD/view?usp=sharing)

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
![image](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/23927117-f7bb-4b81-9414-8b653bc753b4)
### Home Screen
![image](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/999b4cce-c81b-4403-8d1a-9181202abe24)

### Game with Buttons
![image](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/01bdc9fa-7b0c-4863-9ec6-b8b7a8a8fbe2)

### Game with Sensors
![image](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/4391d454-2141-4b85-baf4-dbd0088feff0)

### High Scores Screen
![image](https://github.com/edenSaadon/NEMO_OBS_RACE/assets/97795061/90b8e6db-a96f-4066-9f01-93bfeb260b8f)



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
