# Java-Pokemon-game

![image](https://github.com/chauyiu1994/Java-Pokemon-game/blob/master/image1.jpg)

An individual project developed under the JAVA language, JAVA SDK is required to open the program. 

A simple 2D platformer game, control the player to move around using the arrow keys, catch the pokemons using the pokeballs supplied by   the pokestations (represented by the ball icon).

![image](https://github.com/chauyiu1994/Java-Pokemon-game/blob/master/image2.jpg)

This project is heavily based on the skill of multi-thread processing, One thread is set for each pokemon using the technique of lock so that it can move around randomly in random time interval without colliding with the environmental obstacle/colliding with other pokemons. When the player does not have enough pokeballs, another thread will be started so that the pokemon disappear for a while and appear again random on the map without overlapping of position with another pokemons. When a pokemon is caught, it will not appear again and its thread is permanently removed. The pokestation will also reappear in the map in random position after supplying, this is also facilitated by threads. When the Pause and Resume button are pressed, all the threads will stop running and resume running correctly. 

The UI function is used to set up the whole platform and buttons, such as Vbox for the text, button listener, the imageview. Various hashmaps is set up to store the pokemon and corresponding threads. The map setting can be changed by a specific txt file input pattern. Also, Exception is used for debug purpose.

![image](https://github.com/chauyiu1994/Java-Pokemon-game/blob/master/image3.jpg)


