# Features

**TODO: Add features of the program**

**Databases**

The program stores the results of all transactions occuring at the end of the day, using the database files `AuctionDatabase`, `ForSaleDatabase`, `GameDatabase`, and `UserDatabase`. The following files are used to store specific information:
- *Users.txt*: Stores info regarding each User in the following format: `username, user type, credit balance`.
- *Games.txt*: Stores info regarding each Game in the following format: `game title, owners of game (one or more)`.
- *ForSale.txt*: Stores info regarding Games that were on sale in the following format: `game title, owner of game, listed price, listed discount`.
- *AuctionStatus.txt*: Stores info regarding the status of the auction sale in the following format: `true (if auction sale was active)` or `false (if auction sale was inactive)`

At the beginning of a new day, the program loads the information stored in databases to the Transactions class using the respective database's `.getUsers()`/`getGames()`/`getStatus()` method. 
