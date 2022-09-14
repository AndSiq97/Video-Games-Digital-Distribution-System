# Requirements and Clarifications

**TODO: Add Requirements and clarifications as specified in the README.md**

After discussions with our potential customers, we have decided to add additional transactions that must be included with your system. The following transactions need to be implemented:

removegame - remove a game from a user's inventory or from being sold
The front end will prompt for the game name and if necessary, the game's owner.
This information is saved to the daily transaction file
Constraints:
semi-privileged transaction - Non-admins can only remove their own games. In admin mode, this allows any game to be removed from any user.
game can be in the owner's inventory OR one of those the user has put up for sale
cannot remove a game that was purchased or put up to sale on the same day

gift - give a user a game from another user
The front end will prompt for the game name, the game's receiver, and if necessary, the game's owner.
This information is saved to the daily transaction file
Constraints:
semi-privileged transaction - Non-admins can only gift their own games to another. In admin mode, this allows any game to be gifted from any user to any other.
game must be in the owner's inventory OR one of those the user has put up for sale
if the game is one the owner had purchased, it is removed from their inventory when gifted
cannot gift a game that was purchased or put up to sale on the same day
cannot gift a game to a user who already owns a game with the same name

Lines in the daily transaction file appear like the following:

XX IIIIIIIIIIIIIIIIIII UUUUUUUUUUUUUU SSSSSSSSSSSSS

Where:

XX
is a two-digit transaction code: 08-removegame, 09-gift.

IIIIIIIIIIIIIIIIIII
is the game name

UUUUUUUUUUUUUU
is the owner's username

SSSSSSSSSSSSSS
is the receiver's username

- max price of a game is a float of 999.99
- max credit a user can have is 999,999.99
- a new 'day' occurs when backend is executed, not based on any actual date/time.
- Games are not unique to sellers, two sellers can sell the same game.
- Amount for refund does not need to match a past purchase. (sounds kinda weird but true according to piazza)
- If a user adds money to their account (or to an account in the case of admin), if the amount would bring the balance over the max. Max out the account (meaning bring it to the max possible), then print out a warning.
- If your balance is close to the max, selling a game or receiving a refund brings your balance to the max. If your account is at the max, your balance will not change from receiving a refund or from selling a game.
- Admin can do all transactions, including buy/sell.
- The normal 6 character sale price does not include the discount; this must be calculated separately based on whether a sale is on.
- addcredit is something anyone can do, but only admins can addcredit to other people's accounts; everyone else can only add to their own accounts.
- All accounts created in one session should be saved to so that they can be used in later sessions.
- front-end provides daily transactiono text file called daily.txt at the end of each day to our backend, we must read it and do stuff.
- we can have multiple design patterns.
- You can safely assume that the daily transaction file is sequential and won't have entries where someone logs in halfway through another user's transactions.
- The refund transaction is invalid if the seller's balance isn't high enough.
- A sell transaction introduces a new game.
- auctionsale has a 07 transaction code and uses the same format as login/logout/etc.
- A privileged transaction is typically something only an admin can do. A semi-privileged transaction is something that some accounts can do, but not others (e.g. a buyer user can't sell stuff).
- creating a file to store existing user accounts is a good idea.
- for fatal errors, you should continue to the next transaction. It would not be nice if because of one invalid transaction all subsequent transactions do not go through. same for constraint errors
- game names are case sensitive
- sellers have unlimited number of copies of games
- An admin may not necassarily be present
- an admin can refund themselves
- a person who bought a game cannot resell a game
- multiple sellers can sell the same game
- if a seller puts up a game for sale, he cannot take it down, he can only stop selling the game if his account is deleted
- usernames can contain special characters (eg. $%#@!~ ?)
- You can assume a username has more than just whitespace
- You can also assume a username will not end in a whitespace character
- The system should be functional even if there are no admins.
- If no admin available, buyyer cannot refund, and cannot delete and account or create an account.
- even if there is no admin, do not force create an admin
- no need to do login authentication, we just need to check for existing users

