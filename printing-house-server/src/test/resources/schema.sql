DROP TABLE If EXISTS
    OrderEnobling,
    PaperOrderType,
    WorkflowStageStop,
    `Order`,
    WorkflowStageManager,
    WorkflowStage,
    Size,
    ImpositionType,
    Printer,
    PaperType,
    Colouring,
    BindingForm,
    Role,
    Salesman,
    Worker,
    EmploeeRole,
    Employee,
    IndividualClient,
    Company,
    Client,
    Email,
    Person,
    Bindery,
    Punch,
    UVVarnish,
    Enobling,
    PlatePrice,
    CalculationCard,
    PrintCost,
    URI;

-- size teble is like A2, A3 etc.
CREATE TABLE Size
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NULL UNIQUE,
    width DOUBLE NOT NULL,
    heigth DOUBLE NOT NULL,
    UNIQUE `formatSize`(`width`, `heigth`)
);

-- imposition is layout of pages on the sheet
CREATE TABLE ImpositionType
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL UNIQUE
);

-- printer to use by the printing house
CREATE TABLE Printer
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(100) NOT NULL UNIQUE,
    digest VARCHAR(4)   NOT NULL UNIQUE
);

-- it can be kreda, psapier błysk etc.
CREATE TABLE PaperType
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(200) NOT NULL UNIQUE,
    shortName VARCHAR(30)  NOT NULL UNIQUE
);

-- URI Type f. eg URL's
CREATE TABLE URI
(
    id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    uri TEXT NOT NULL
);

-- Some additional atribute to the final product
CREATE TABLE Enobling
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NULL
);

CREATE TABLE Punch
(
    enoblingId INT PRIMARY KEY,
    INDEX      punchEnoblingId_ind (enoblingId),
    FOREIGN KEY (enoblingId)
        REFERENCES Enobling (id)
        ON DELETE CASCADE
);

CREATE TABLE UVVarnish
(
    enoblingId INT PRIMARY KEY,
    INDEX      UVVarnEnoblingId_ind (enoblingId),
    FOREIGN KEY (enoblingId)
        REFERENCES Enobling (id)
        ON DELETE CASCADE
);

-- for example 4/0, 4/1
CREATE TABLE Colouring
(
    id          TINYINT PRIMARY KEY AUTO_INCREMENT,
    firstSide   TINYINT NOT NULL,
    secoundSide TINYINT NOT NULL,
    UNIQUE `bothSides`(`firstSide`, `secoundSide`)
);

-- Role of the employee for the data to access
-- this is spring boot roles to ensure the authrorisation
CREATE TABLE Role
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE
);

-- Shared pool of emails
CREATE TABLE Email
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(600) NOT NULL UNIQUE
);

-- Peoples
CREATE TABLE Person
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(200) NOT NULL,
    surname     VARCHAR(300) NOT NULL,
    pseudoPESEL CHAR(11)     NOT NULL UNIQUE -- PESEL is Polish personal ID number but it actually any string that can identify person
);

CREATE TABLE BindingForm
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE
);

-- Bindery
CREATE TABLE Bindery
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE
);

-- Employee
CREATE TABLE Employee
(
    personId      INT PRIMARY KEY,
    email         INT      NOT NULL UNIQUE,
    password      CHAR(68) NOT NULL,
    activeAccount BOOL     NOT NULL,
    employed      BOOL     NOT NULL,

    INDEX         empEmail_ind (email),
    FOREIGN KEY (email)
        REFERENCES Email (id)
        ON DELETE CASCADE,
    INDEX         empPersonId_ind (personId),
    FOREIGN KEY (personId)
        REFERENCES Person (id)
        ON DELETE CASCADE
);

-- Salesman
CREATE TABLE Salesman
(
    personId INT PRIMARY KEY,
    INDEX    slsmnPersonId_ind (personId),
    FOREIGN KEY (personId)
        REFERENCES Employee (personId)
        ON DELETE CASCADE
);

-- Client
CREATE TABLE Client
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    email       INT NULL UNIQUE,
    phoneNumber VARCHAR(15) NULL UNIQUE,
    INDEX       cliEmail_ind (email),
    FOREIGN KEY (email)
        REFERENCES Email (id)
        ON DELETE SET NULL
);

-- IndividualClient
CREATE TABLE IndividualClient
(
    personId INT PRIMARY KEY,
    clientId INT NOT NULL UNIQUE,
    INDEX    indCliId_ind (clientId),
    FOREIGN KEY (clientId)
        REFERENCES Client (id)
        ON DELETE CASCADE,
    INDEX    inCliPerson_ind (personId),
    FOREIGN KEY (personId)
        REFERENCES Person (id)
        ON DELETE CASCADE
);

-- Company
CREATE TABLE Company
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(500) NOT NULL,
    NIP      CHAR(10)     NOT NULL UNIQUE,
    clientId int NULL UNIQUE,
    INDEX    compCliId_ind (clientId),
    FOREIGN KEY (clientId)
        REFERENCES Client (id)
        ON DELETE SET NULL
);

-- Order is equivalent for the calculation card and work card combined
CREATE TABLE `Order`
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    name                  VARCHAR(200) NOT NULL,
    netSize               INT          NOT NULL,
    pages                 INT          NOT NULL,
    supervisor            INT          NOT NULL,
    client                INT          NOT NULL,
    creationDate          TIMESTAMP    NOT NULL,
    realizationDate       TIMESTAMP    NOT NULL,
    bindingForm           INT          NOT NULL,
    bindery               INT          NOT NULL,
    folding               BOOL         NOT NULL,
    towerCut              BOOL         NOT NULL,
    imageURL              BIGINT NULL,
    imageComment          TEXT NULL,
    checked               BOOL         NOT NULL,
    designsNumberForSheet INT          NOT NULL,
    completionDate        TIMESTAMP NULL,
    withdrawalDate        TIMESTAMP NULL,
    comment               TEXT NULL,

    INDEX                 netSize_ind (netSize),
    FOREIGN KEY (netSize)
        REFERENCES Size (id)
        ON DELETE RESTRICT,
    INDEX                 supervisor_ind (supervisor),
    FOREIGN KEY (supervisor)
        REFERENCES Salesman (personId)
        ON DELETE NO ACTION,
    INDEX                 ordCli_ind (client),
    FOREIGN KEY (client)
        REFERENCES Client (id)
        ON DELETE NO ACTION,

    INDEX                 ordBindForm_ind (bindingForm),
    FOREIGN KEY (bindingForm)
        REFERENCES BindingForm (id),

    INDEX                 ordBindery_ind (bindery),
    FOREIGN KEY (bindery)
        REFERENCES Bindery (id),
    INDEX                 ordURLImg_ind (imageURL),
    FOREIGN KEY (imageURL)
        REFERENCES URI (id)
);


CREATE TABLE OrderEnobling
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    enobling   INT NOT NULL,
    `order`    INT NOT NULL,
    bindery    INT NOT NULL,
    annotation TEXT NULL,

    INDEX      ordEnEn_ind (enobling),
    FOREIGN KEY (enobling)
        REFERENCES Enobling (id),
    INDEX      ordEnOrd_ind (`order`),
    FOREIGN KEY (`order`)
        REFERENCES `Order` (id)
        ON DELETE CASCADE,
    INDEX      ordEnBindery_ind (bindery),
    FOREIGN KEY (bindery)
        REFERENCES Bindery (id)
);

-- PaperOrderType paper type specific to order
CREATE TABLE PaperOrderType
(
    id                       INT PRIMARY KEY AUTO_INCREMENT,
    typeId                   INT     NOT NULL,
    orderId                  INT     NOT NULL,
    grammage DOUBLE NOT NULL,
    colours                  TINYINT NOT NULL,
    circulation              INT     NOT NULL,
    stockCirculation         INT     NOT NULL,
    sheetNumber              INT     NOT NULL,
    comment                  VARCHAR(1000) NULL,
    printer                  INT     NOT NULL,
    platesQuantityForPrinter INT     NOT NULL,
    imposition               int     NOT NULL,
    size                     int     NOT NULL,
    productionSize           int     NOT NULL,


    INDEX                    potPaperType_ind (typeId),
    FOREIGN KEY (typeId)
        REFERENCES PaperType (id),
    INDEX                    potOrder_ind (orderId),
    FOREIGN KEY (orderId)
        REFERENCES `Order` (id)
        ON DELETE CASCADE,
    INDEX                    potColours_ind (colours),
    FOREIGN KEY (colours)
        REFERENCES Colouring (id),
    INDEX                    potPrinter_ind (printer),
    FOREIGN KEY (printer)
        REFERENCES Printer (id),
    INDEX                    potImpositionType_ind (imposition),
    FOREIGN KEY (imposition)
        REFERENCES ImpositionType (id),
    INDEX                    potSize_ind ( size),
    FOREIGN KEY (size)
        REFERENCES Size (id),
    INDEX                    potProdSize_ind (productionSize),
    FOREIGN KEY (productionSize)
        REFERENCES Size (id)
);

-- worker does the work and annotate it with done
CREATE TABLE Worker
(
    personId INT PRIMARY KEY,
    INDEX    wkrPrsnId_ind (personId),
    FOREIGN KEY (personId)
        REFERENCES Employee (personId)
        ON DELETE CASCADE
);

-- asossiate employees with their role
CREATE TABLE EmploeeRole
(
    empId  INT,
    roleId INT,
    PRIMARY KEY (empId, roleId),
    FOREIGN KEY (empId)
        REFERENCES Employee (personId)
        ON DELETE CASCADE,
    FOREIGN KEY (roleId)
        REFERENCES Role (id)
        ON DELETE CASCADE,
    UNIQUE `empRoleUnique`(`empId`, `roleId`)
);

CREATE TABLE WorkflowStage
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    roleId INT          NOT NULL, -- dodać tabelkę
    name   VARCHAR(100) NOT NULL UNIQUE,
    INDEX  wsRoleId_ind (roleId),
    FOREIGN KEY (roleId)
        REFERENCES Role (id)
        ON DELETE CASCADE
);

CREATE TABLE WorkflowStageManager
(
    workflowStage INT,
    employeeId    INT,

    FOREIGN KEY (workflowStage)
        REFERENCES WorkflowStage (id)
        ON DELETE CASCADE,
    FOREIGN KEY (employeeId)
        REFERENCES Worker (personId)
        ON DELETE CASCADE,
    PRIMARY KEY (workflowStage, employeeId),
    UNIQUE `workflowStageMgrUnique`(`workflowStage`, `employeeId`)
);

-- WorkflowStageStop is
CREATE TABLE WorkflowStageStop
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    comment           VARCHAR(500) NULL,
    createTime        TIMESTAMP NOT NULL,
    assignTime        TIMESTAMP NULL,
    worker            INT NULL,
    `order`           INT       NOT NULL,
    workflowStage     INT       NOT NULL,
    lastWorkflowStage BOOL      NOT NULL,

    INDEX             wssWorker_ind (worker),
    FOREIGN KEY (worker)
        REFERENCES Worker (personId)
        ON DELETE NO ACTION,
    INDEX             wssOrder_ind (`order`),
    FOREIGN KEY (`order`)
        REFERENCES `Order` (id)
        ON DELETE CASCADE,
    INDEX             wssWorkStage_ind (workflowStage),
    FOREIGN KEY (workflowStage)
        REFERENCES WorkflowStage (id)
);

-- PlatePrice prices for disks to specific printer
CREATE TABLE PlatePrice
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    date    TIMESTAMP  NOT NULL,
    price   DEC(63, 2) NOT NULL,
    printer INT        NOT NULL,

    INDEX   dpPrinter_ind (printer),
    FOREIGN KEY (printer)
        REFERENCES Printer (id)
        ON DELETE CASCADE
);

-- CalculationCard this is part of Order which is calculated by the Salesman
CREATE TABLE CalculationCard
(
    orderId     INT PRIMARY KEY,
    bindingCost DEC(63, 2) NOT NULL,
    enobling    DEC(63, 2) NOT NULL,
    otherCosts  DEC(63, 2) NOT NULL,
    transport   DEC(63, 2) NOT NULL,

    INDEX       ccOrderId_ind (orderId),
    FOREIGN KEY (orderId)
        REFERENCES `Order` (id)
        ON DELETE CASCADE
);

-- is the individual cost of printing in the CalculationCard
CREATE TABLE PrintCost
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    orderId    INT        NOT NULL,
    printer    INT        NOT NULL,
    printCost  DEC(63, 2) NOT NULL,
    matrixCost DEC(63, 2) NOT NULL,
    INDEX      printCOrderId_ind (orderId),
    FOREIGN KEY (orderId)
        REFERENCES CalculationCard (orderId)
        ON DELETE CASCADE,
    INDEX      printCPrinter_ind (printer),
    FOREIGN KEY (printer)
        REFERENCES Printer (id)
        ON DELETE NO ACTION
);