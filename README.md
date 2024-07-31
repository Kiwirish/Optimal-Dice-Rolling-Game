# Optimal Dice Rolling 

This project is a simple game that uses ordinary six-sided dice. The game starts with six dice in a random configuration. On each turn, you roll one more die and then have the option to exchange it for any one of your current dice. The objective of the game is to form two sets of three dice as quickly as possible. A valid set consists of either three dice showing the same number (e.g., 444) or three dice in sequence (e.g., 123 or 345).

## Strategy

Steps:
1. Find all the possible sets of three that we can make with the existing numbers.
    - For each set calculate the numbers that could be used in swapping to immediately make a set out of the remaining numbers.
    - Then also use the roll to figure out if we could get higher odds of a set by swapping it with one of the three numbers.
    - record the score and swap choice -1, 0-5
2. Sort options by score then return that options choice as the return value.
