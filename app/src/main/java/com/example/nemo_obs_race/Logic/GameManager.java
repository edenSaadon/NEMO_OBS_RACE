package com.example.nemo_obs_race.Logic;

public class GameManager {
        private int lifeCount=3;

        public GameManager() {
            this.lifeCount=3;
        }

        public GameManager(int lifeCount) {
            this.lifeCount = lifeCount;
        }

        public int getLifeCount() {
            return lifeCount;
        }
        public void decreaseLives() {
            if (lifeCount > 0) {
                lifeCount--;
            }
        }

        public boolean isGameEnded(){//the end of questions
            return lifeCount==0;
        }


    }


