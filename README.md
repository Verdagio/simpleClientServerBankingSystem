# simpleClientServerBankingSystem
3rd year under-grad project

### 12/01/17 - Daniel Verdejo

Operating Systems.
#Simple client server communication bank account system 

In this project I did not take an Object Oriented approach as it was not required (Excuse the mess). The client side does all the majority of the GUI, and handles messages going to & from the server once a connection to the server side has been established. The user wil be prompted to use either localhost (pre-entered) or to enter a custom server which would be an actual server(Amazon Web Services in this case). An IP and port will be required to connect.

The server side will handle all the logic required to carry out whatever it is that the user wants to do. The operations which a user can carry out on their account are as follows:

	1 - Make a lodgement
	2 - Make a withdrawal
	3 - Veiw Transaction history
	4 - Quit

    1 - Lodging will add the user input amount onto the current balance of the account. A record of the transaction will be saved to a linked   list of Transaction objects.
    2 - Withdrawing will subtract the user input amount from the account on the condition that the users current balance is greater or equal to the amount requested. A record of the transaction will be saved to a linked list of Transaction objects.
    3 - The transaction history will print out each transaction made stating the account number, transaction type W - witdraw or L - Lodgement , and the amount of the transaction.
    4 - Quit will close the streams and connection.

The server side has Account, & Transaction classes which are used in LinkedLists to keep record of accounts and transactions. (Note: text files with lists of accounts & transactions would be required to save this data for future use, but this was not stated within' the project spec therefore there is currently no such files or file handling).
