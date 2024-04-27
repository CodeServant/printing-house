INSERT INTO Person (name, surname, pseudoPESEL)
VALUES ('Jan', 'Kowalski-Salesman', '45125478527'),
       ('Marian', 'Rokita-IntroligManager', '54874668723'),
       ('Jiliusz', 'Szymański-Introligatornia', '91123414471'),
       ('Robert', 'Makłowisz-NaśwManager', '42389285u78'),
       ('Brad', 'PittIndividualCli', '01350359272'),
       ('Rick', 'SanchesIndividualCli', '91357482395'),
       ('Anna', 'Nadstawna-Naświetlarnia', '43823523523'),
       ('Arthur', 'Boss-Manager', '43823523xxx');
INSERT INTO Bindery (name)
VALUES ('A1'),
       ('A2'),
       ('A3'),
       ('A4'),
       ('A5');
INSERT INTO Image (url, imageComment)
VALUES ('https://diagrams.net',
        'draw.io to fajna strona do rysowania diagramów, tego linka można potem odtworzyć i podejrzeć a nawet pozmieniać obrazek');
INSERT INTO Enobling (name, description)
VALUES ('farba kolorowa', 'to jest farba kolorowa'),
       ('karton aksamitny', NULL),
       ('papier błysk', NULL),
       ('szalony wykrojnik', NULL),
       ('wykrojnik zwyczajny', 'taki sobie oto wykrojnik zwyczajny');
INSERT INTO ImpositionType (name)
VALUES ('f/f'),
       ('f/o'),
       ('f/g');
INSERT INTO Role (name)
VALUES ('SALESMAN'),
       ('introligatornia'),
       ('krajalnia'),
       ('MANAGER'),
       ('naświetlarnia'),
       ('ADMIN'),
       ('EMPLOYEE'),
       ('OWNER'),
       ('WORKER'),
       ('WORKFLOW_STAGE_MANAGER');
INSERT INTO BindingForm (name)
VALUES ('Papier'),
       ('Folia'),
       ('Karton'),
       ('Płyta gipsowa');
INSERT INTO PaperType (name, shortName)
VALUES ('Papier Błysk', 'Pap Błysk'),
       ('Papier Offsetowy', 'Offsetowy'),
       ('papier Kredowy', 'kreda');
INSERT INTO Printer (name, digest)
VALUES ('duża komori', 'DK'),
       ('mała komori', 'MK');
INSERT INTO Email (email)
VALUES ('anna@wp.pl'),
       ('brat@wp.pl'),
       ('jan@example.com'),
       ('jankowa@wp.pl'),
       ('marian@example.com'),
       ('maklowicz@wp.pl'),
       ('marianmieszka@wp.pl'),
       ('robert@example.com'),
       ('manager@example.com');
INSERT INTO Client (email, phoneNumber)
VALUES (5, '984324654'),
       (4, '254236643'),
       (6, '152358752');
INSERT INTO Colouring (firstSide, secondSide)
VALUES (4, 0),
       (2, 0),
       (2, 2),
       (1, 1);
INSERT INTO Company (name, NIP, clientId)
VALUES ('evil corp inc.', '1413135634', 3);
INSERT INTO PlatePrice (`date`, price, printer)
VALUES ('2022-10-21 10:58:49', 10.00, 1),
       ('2022-10-21 08:56:52', 17.00, 2);
INSERT INTO Employee (personId, email, password, activeAccount, employed)
VALUES (1, 3, '{bcrypt}$2a$12$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.', 1, 1),
       (2, 5, '{bcrypt}$2a$12$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.', 1, 1),
       (3, 4, '{bcrypt}$2a$12$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.', 1, 1),
       (4, 8, '{bcrypt}$2a$12$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.', 1, 1),
       (7, 7, '{bcrypt}$2a$12$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.', 1, 1),
       (8, 9, '{bcrypt}$2a$12$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.', 1, 1);
INSERT INTO EmploeeRole (empId, roleId)
VALUES (1, 1),
       (1, 7),
       (2, 3),
       (2, 9),
       (2, 7),
       (3, 7),
       (3, 9),
       (2, 10),
       (4, 10),
       (4, 7),
       (4, 9),
       (7, 7),
       (7, 9),
       (8, 4),
       (8, 7);
INSERT INTO IndividualClient (personId, clientId)
VALUES (6, 1),
       (5, 2);
INSERT INTO Punch (enoblingId)
VALUES (4),
       (5);
INSERT INTO Salesman (personId)
VALUES (1);
INSERT INTO Size (name, width, heigth)
VALUES ('A0', 841.0, 1189.0),
       ('A1', 594.0, 841.0),
       ('A2', 420.0, 594.0),
       ('A3', 297.0, 420.0),
       ('A4', 210.0, 297.0),
       (NULL, 123.0, 123.0),
       (NULL, 205.0, 295.0),
       (NULL, 430.0, 610.0);
INSERT INTO UVVarnish (enoblingId)
VALUES (1);
INSERT INTO Worker (personId)
VALUES (2),
       (3),
       (4),
       (7);
INSERT INTO WorkflowStage (name)
VALUES ('Introligatornia'),
       ('Naświetlarnia'),
       ('Handlowiec'),
       ('Krajalnia');
INSERT INTO WorkflowStageManager (workflowStage, employeeId)
VALUES (1, 2),
       (2, 4);
INSERT INTO `Order` (name, netSize, pages, supervisor, client, creationDate, realizationDate, bindingForm, bindery,
                     folding, towerCut, imageURL, checked, designsNumberForSheet, completionDate,
                     withdrawalDate, comment)
VALUES ('gorzelska', 7, 8, 1, 3, '2022-10-21 00:00:00', '2022-10-26 00:00:00', 3, 3, 0, 1, 1,
        1, 4, NULL, NULL, NULL);
INSERT INTO CalculationCard (orderId, bindingCost, enobling, otherCosts, transport)
VALUES (1, 1000.00, 1245.00, 1251.00, 1516.00);
INSERT INTO OrderEnobling (enobling, `order`, bindery, annotation)
VALUES (4, 1, 2, NULL),
       (1, 1, 3, 'zróbcie jeszcze szpagat');
INSERT INTO PaperOrderType (typeId, orderId, grammage, colours, circulation, stockCirculation, sheetNumber, comment,
                            printer, platesQuantityForPrinter, imposition, `size`, productionSize)
VALUES (2, 1, 70.0, 4, 20000, 70, 1, NULL, 1, 1, 1, 3, 8),
       (3, 1, 90.0, 4, 2000, 40, 0, NULL, 1, 0, 1, 3, 8);
INSERT INTO PrintCost (orderId, printer, printCost, matrixCost)
VALUES (1, 1, 12.00, 13.00),
       (1, 1, 13.00, 14.00),
       (1, 2, 1.00, 2.00);
INSERT INTO WorkflowDirGraph (creationTime, changedTime, name, comment)
VALUES ('2023-10-01 10:11:00', '2023-10-01 11:00:00', 'typical flow', NULL),
       ('2023-10-01 10:11:00', '2023-10-01 11:00:00', 'simple flow', NULL);
INSERT INTO WorkflowDirEdge (V1, V2, graphId)
VALUES (3, 1, 1),
       (3, 2, 1),
       (1, 4, 1),
       (2, 4, 1),
       (1, 2, 2),
       (2, 3, 2);
INSERT INTO WorkflowStageStop (comment, createTime, assignTime, worker, `order`, workflowEdgeId, completionTime)
VALUES ('coś zrobiłam i nawet dobrze', '2022-10-21 15:00:00', '2022-10-21 15:00:00', 7, 1, 1, '2022-10-21 16:00:00'),
       (NULL, '2022-10-21 16:00:00', NULL, NULL, 1, 2, null);