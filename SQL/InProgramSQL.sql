-- ** Note: A question mark means the value is variable. 
-- That is, in a prepared statement, the value is represented as a question mark. 
-- This generalizes the query, so it spans several different, possible queries.

—----- Queries in MainMenu —----

-- Finds some customer’s password
select password from user_account where username = ? and username in (select username from customer);

-- Finds some owner’s password
select password from user_account where username = "some_username" and username in (select username from owner);

--Note: many inserts / deletes are self-explanatory 

insert into area values (?,?,?, ?);

insert into address values (?,?);

insert into user_account values (?,?,?,?,?);

insert into customer values (?,?,?,?,?,?);


-- Queries in UserMenu —-

--This query is used to load in the books table from the database, and then create book objects
select * from book;

--For each book written, it gets more information regarding the author(s)
select * from author, writes where author.id = writes.author_id;

------ Queries in OwnerMenu ------

delete from writes where ISBN = ?;

delete from publishes where ISBN = ?;

delete from book_order where ISBN = ?;

delete from book where ISBN = ?;

insert into table book values (?,?,?,?,?,?,?);

insert into publishes values (?,?,?);

insert into author values (?,?,?);

insert into writes values (?,?);

------ Queries in CustomerMenu ------

-- Query to update stock when quantity is added to cart
update book set stock = ? where ISBN = ?;

-- Query to add new book to cart
insert into basket_item values (?,?,?);

-- Query to update quantity of book if more copies are added to cart
update basket_item set quantity = ? where ISBN = ? and username = ?;

-- Query to get order menu 
select * from order_info where customer = ? ;


— Queries for Cart —--

--first value is default since order_info has order_number as a serial type
insert into order_info values (default,?,?,?,?,?,?,?,?);

--Used to delete all the contents in a customer’s cart
delete from basket_item where username = ?;

--Gets customer’s shipping address
select shipping_postal_code, shipping_street_address from customer where username=?;

--Gets customer’s billing address
select billing_postal_code, billing_street_address from customer where username=?;
