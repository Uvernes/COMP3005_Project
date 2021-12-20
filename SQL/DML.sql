-- Inserting values --

insert into user_account
values
('uvito5', 'abcde', 'Uvernes', 'Somarriba', 'uv@gmail.com'),
('majd01', '12345', 'Majd', 'Taweel', 'majd@gmail,com'),
('leo10', 'goat', 'Lionel', 'Messi', 'messi@gmail.com'),
('bookworm101', 'read101', 'Billy', 'Jean', 'billyjean@gmail.com');

insert into owner
values
('uvito5', 125000),
('majd01', 125000);

insert into area
values
('T2A 1C8', 'Calgary', 'Alberta', 'Canada'),
('P0R 1L0', 'Thessalon', 'Ontario', 'Canada');

insert into address
values
('T2A 1C8', '3536 40th Street'),
('P0R 1L0', '4846 Nelson Street');

insert into customer
values
('leo10', '4716293822574750', 'T2A 1C8', '3536 40th Street', 'T2A 1C8', '3536 40th Street'),
('bookworm101', '4929865875977775', 'P0R 1L0', '4846 Nelson Street', 'P0R 1L0', '4846 Nelson Street');

insert into book 
values
('9780812968255', 'Meditations', 'Philosophy', 1641, 256, 10.41, 15),
('9781387779833', 'The Enchiridion of Epictetus', 'Philosophy', 1683, 64, 4.99, 20),
('9780140442106', 'Letters from a Stoic', 'Philosophy', 1969, 256, 19.13, 10),
('1501142976', 'IT', 'Horror', 2016, 1168, 19.83, 15),
('1501156705', 'Pet Sematary', 'Horror', 2017, 560, 17.50, 10),
('0060853980', 'Good Omens', 'Comedy', 2006, 512, 8.99, 15);

insert into author
values
(101, 'Marcus', 'Aurelius'),
(102, 'E.', 'Epictetus'),
(103, 'Lucius', 'Seneca'),
(104, 'Stephen', 'King'),
(105, 'Neil', 'Gaiman'),
(106, 'Terry', 'Pratchett');

insert into writes
values
(101, '9780812968255'),
(102, '9781387779833'),
(103, '9780140442106'),
(104, '1501142976'),
(104, '1501156705'),
(105, '0060853980'),
(106, '0060853980');

insert into area 
values
('K7L 4A6', 'Calgary' , 'Alberta','Canada'),
('L1H 9N2', 'Ottawa', 'Ontario', 'Canada'),
('K2S 3Q7', 'Vancouver', 'British Columbia', 'Canada'),
('L2H 9N2', 'Thessalon', 'Ontario', 'Canada');

insert into address
values
('K7L 4A6', '180 Ludwig Way'),
('L1H 9N2', '52 Castanza Avenue'),
('K2S 3Q7', '42 Sadie Road'),
('L2H 9N2', '327 Murdock Road');

insert into publisher
values 
('Cheesecake Books', 'cheesecake@gmail.com', '55575333', '4716393866582811', 'K7L 4A6', '180 Ludwig Way'),
('Ponder Inc' , 'ponder@gmail.com', '44474828', '4826275042590670', 'L1H 9N2', '52 Castanza Avenue'),
('Velma Publishing' , 'velma@gmail.com', '55590440', '4236870342552916', 'K2S 3Q7', '42 Sadie Road');

insert into publishes 
values
('Cheesecake Books', '0060853980', 0.20),
('Ponder Inc', '9780812968255', 0.35),
('Ponder Inc', '9781387779833', 0.33),
('Ponder Inc', '9780140442106', 0.40),
('Velma Publishing', '1501142976', 0.12),
('Cheesecake Books', '1501156705', 0.20);

insert into order_info
values
(11000027, 'bookworm101', 'P0R 1L0', '4846 Nelson Street', 'L2H 9N2', '327 Murdock Road', 'Processing', '12/12/2021', 'Mississauga'),
(11000078, 'leo10','T2A 1C8', '3536 40th Street', 'T2A 1C8', '3536 40th Street', 'Delivered', '11/11/2021', 'Mississauga');

insert into book_order
values 
(11000027, '9780812968255', 2),
(11000027, '9780140442106', 18),
(11000027, '9781387779833', 1),
(11000078, '0060853980', 12);
