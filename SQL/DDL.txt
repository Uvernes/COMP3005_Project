-- CREATING TABLES --

create table book
(ISBN varchar(20),
 title varchar(50),
 genre varchar(30),
 publication_year integer,
 page_count integer,
 price numeric(5,2),
 stock integer,
 primary key (ISBN)
);

create table author
(ID serial,
 first_name varchar(30),
 last_name varchar(30),
 primary key (ID)
);

create table writes
(author_id integer,
 ISBN varchar(20),
 foreign key (author_id) references author,
 foreign key (ISBN) references book
);

create table user_account
(username varchar(30),
 password varchar(30),
 first_name varchar(30),
 last_name varchar(30),
 email varchar(30),
 primary key (username)
);

create table owner
(username varchar(30),
 salary numeric(10,2),
 primary key (username),
 foreign key (username) references user_account
);

create table area
(postal_code varchar(10),
 city varchar(50),
 province varchar(50),
 country varchar(50),
 primary key (postal_code)
);

create table address
(postal_code varchar(10),
 street_address varchar (100),
 primary key (postal_code, street_address),
 foreign key (postal_code) references area
);

create table basket_item
(ISBN varchar(20),
 username varchar(30),
 quantity integer,
 primary key (ISBN, username),
 foreign key (ISBN) references book,
 foreign key (username) references user_account
);

create table publisher
(name varchar(50),
 email varchar(50),
 phone_number varchar(20),
 bank_account_number varchar(30),
 postal_code varchar(10),
 street_address varchar(100),
 primary key (name),
 foreign key (postal_code, street_address) references address
);

create table publishes
(name varchar(30),
 ISBN varchar(20),
 sales_percentage numeric(5,2),
 primary key (name, ISBN),
 foreign key (name) references publisher,
 foreign key (ISBN) references book
);

create table customer
(username varchar(30),
 credit_card_number varchar(30),
 billing_postal_code varchar(10),
 billing_street_address varchar(100),
 shipping_postal_code varchar(10),
 shipping_street_address varchar(100),
 primary key (username),
 foreign key (username) references user_account,
 foreign key (billing_postal_code, billing_street_address) references address(postal_code, street_address),
 foreign key (shipping_postal_code, shipping_street_address) references address(postal_code, street_address)
);

create table order_info
(order_number serial,
 customer varchar(30),
 shipping_postal_code varchar(10),
 shipping_street_address varchar(100),
 current_postal_code varchar(10),
 current_street_address varchar(100),
 order_status varchar(20),
 date_of_sale varchar(50),
 warehouse varchar(50), 
 primary key (order_number),
 foreign key (customer) references customer(username),
 foreign key (shipping_postal_code, shipping_street_address) references address(postal_code, street_address),
 foreign key (current_postal_code, current_street_address) references address(postal_code, street_address)
);

create table book_order
(order_number integer,
 ISBN varchar(20),
 quantity integer,
 primary key(order_number, ISBN),
 foreign key (order_number) references order_info,
 foreign key (ISBN) references book
);
