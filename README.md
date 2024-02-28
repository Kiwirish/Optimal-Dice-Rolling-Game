# Etude 2 - Rollin Rollin Rollin


## Strategy

Steps:
1. Find all the possible sets of three that we can make with the existing numbers.
    - For each set calculate the numbers that could be used in swapping to immediately make a set out of the remaining numbers.
    - Then also use the roll to figure out if we could get higher odds of a set by swapping it with one of the three numbers.
    - record the score and swap choice -1, 0-5
2. Sort options by score then return that options choice as the return value.