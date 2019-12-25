insert into Customer (customer_id, address, name, phone, verified) values (1, 'address11','tony', '+491181818', TRUE);
insert into Customer (customer_id, address, name, phone, verified) values (2, 'address12','jim', '+4918181812', TRUE);
insert into Customer (customer_id, address, name, phone, verified) values (3, 'address13','ana', '+4918181843', TRUE);
insert into Customer (customer_id, address, name, phone, verified) values (4, 'address14','mia', '+4918181334', TRUE);

insert into Account (account_id, sourceIBAN, assignedIBAN, customer_id) values (1001, 'DE10010010001001','UK10010010001001', 1);
insert into Account (account_id, sourceIBAN, assignedIBAN, customer_id) values (1002, 'DE10010010002002','UK10010010002002', 2);
insert into Account (account_id, sourceIBAN, assignedIBAN, customer_id) values (1003, 'DE10010010003003','UK10010010003003', 3);
insert into Account (account_id, sourceIBAN, assignedIBAN, customer_id) values (1004, 'DE10010010004004','UK10010010004004', 4);

insert into Balance (balance_id, currency, amount, etag, account_id) values (1,'EUR',100,  12345654321, 1001);
insert into Balance (balance_id, currency, amount, etag, account_id) values (2,'GBP',100, 82734923487, 1001);
insert into Balance (balance_id, currency, amount, etag, account_id) values (3,'GBP',10, 88234827473, 1002);

