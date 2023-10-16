INSERT INTO Person (name, surname, pseudoPESEL)
VALUES ('Jan', 'Kowalski-Salesman', '451254785'),
       ('Marian', 'Rokita-IntroligManager', '548746687'),
       ('Jiliusz', 'Szymański-Introligatornia', '911234144'),
       ('Robert', 'Makłowisz-NaśwManager', '42389285u'),
       ('Brad', 'PittIndividualCli', '013503592'),
       ('Rick', 'SanchesIndividualCli', '913582395'),
       ('Anna', 'Nadstawna-Naświetlarnia', '423523523');
INSERT INTO Bindery (name)
VALUES ('A1'),
       ('A2'),
       ('A3'),
       ('A4'),
       ('A5');
INSERT INTO Image (url, imageComment)
VALUES ('https://viewer.diagrams.net/?tags=%7B%7D&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Diagram%20bez%20tytu%C5%82u.drawio#RzZXRjqMgFIafhstNFGrrXk51djoXk0zSSfaayhklRTCIo%2FbpFyrWWneSnWST6ZXwn8MPfAcBkaTsnjStihfFQCAcsA6RFGEcrsjKfpzSD8omxoOQa8580iTs%2BQm8GHi14QzqWaJRShhezcVMSQmZmWlUa9XO096VmM9a0RwWwj6jYqn%2B5swUgxrjzaTvgOfFOHO4%2FjlESjom%2B53UBWWqvZLIIyKJVsoMrbJLQDh4I5dh3K9PopeFaZDmXwa8vqVV%2F3J4o1H6%2FBC0h0x0ux%2Fe5YOKxm%2FYL9b0IwGtGsnAmQSIbNuCG9hXNHPR1tbcaoUphe2FtuntQBvoPl1neNm9PTagSjC6tynjgNgD8ycGb3y%2FnfiHI9Tiiv3aa9SXPL9YT1Rsw4P5AiR8f5BIeG%2BQyP1Buj1JZPXdkFb3B%2Bn2JH0%2FpGgBqdKn%2FoiSCMXY3aFu9bo%2FNRKOC3wWhJkzooLn0rYzCwS0FRwubm%2F4Bx8oOWNu%2BFZDzU%2F0cLZy8CvFpTnvLtqiKHVejVH18EY569podYRECWV9U6mkc3nnQtxI%2F6FMmETzHz6Il2UifykT%2BXqZbHd6ls6xq8edPP4B',
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
INSERT INTO `Role` (name)
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
       ('evilcorp@example.com'),
       ('jankowa@wp.pl'),
       ('julek@wp.pl'),
       ('maklowicz@wp.pl'),
       ('marianmieszka@wp.pl'),
       ('rick@gmail.com');
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
       (7, 7, '{bcrypt}$2a$12$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.', 1, 1);
INSERT INTO EmploeeRole (empId, roleId)
VALUES (1, 1),
       (2, 3),
       (3, 4),
       (2, 10),
       (4, 10);
INSERT INTO IndividualClient (personId, clientId)
VALUES (6, 1),
       (5, 2);
INSERT INTO Punch (enoblingId)
VALUES (4),
       (5);
INSERT INTO Salesman (personId)
VALUES (1);
INSERT INTO `Size` (name, width, heigth)
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
INSERT INTO WorkflowStage (roleId, name)
VALUES (2, 'Introligatornia'),
       (5, 'Naświetlarnia'),
       (1, 'Handlowiec'),
       (3, 'Krajalnia');
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
VALUES ('2023-10-01 10:11:00', '2023-10-01 11:00:00', 'typical flow', NULL);
INSERT INTO WorkflowDirEdge (V1, V2, graphId)
VALUES (3, 1, 1),
       (3, 2, 1),
       (1, 4, 1),
       (2, 4, 1);
INSERT INTO WorkflowStageStop (comment, createTime, assignTime, worker, `order`, workflowEdgeId)
VALUES ('coś zrobiłam i nawet dobrze', '2022-10-21 15:00:00', '2022-10-21 15:00:00', 7, 1, 1),
       (NULL, '2022-10-21 16:00:00', NULL, NULL, 1, 2);