# Java-Pokemon-game

![image](https://github.com/chauyiu1994/Java-Pokemon-game/blob/master/image1.jpg)

An individual project developed under the JAVA language, JAVA SDK is required to open the program. 

A simple 2D platformer game, control the player to move around using the arrow keys, catch the pokemons using the pokeballs supplied by   the pokestations (represented by the ball icon).

![image](https://github.com/chauyiu1994/Java-Pokemon-game/blob/master/image2.jpg)

This project is heavily based on the skill of multi-thread processing, various threads are set so that the pokemon can move around and  disappear randomly in random time interval without colliding with the environmental obstacle/colliding with other pokemons, as well as handling the case when the player does not have enough pokeballs. The pokestation will also reappear in the map in random position after supplying, it is also facilitated by threads. When the Pause button is pressed, all the threads will stop running to make sure the game run smoothly.

The UI function is used to set up the whole platform and buttons, such as Vbox for the text, button listener, the imageview. Various hashmaps is set up to store the pokemon and corresponding threads. The map setting can be changed by a specific txt file input pattern. Also, Exception is used for debug purpose.

![image](https://github.com/chauyiu1994/Java-Pokemon-game/blob/master/image3.jpg)


