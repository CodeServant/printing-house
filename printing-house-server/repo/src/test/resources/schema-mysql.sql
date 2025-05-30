DROP TABLE If EXISTS
    OrderEnobling,
    PaperOrderType,
    WorkflowDirEdge,
    WorkflowDirGraph,
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
    Image;

-- size teble is like A2, A3 etc.
CREATE TABLE Size
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(50) NOT NULL UNIQUE,
    width  DOUBLE      NOT NULL,
    heigth DOUBLE      NOT NULL,
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

-- Image is the url pointing to image
CREATE TABLE Image
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    url          TEXT          NOT NULL,
    imageComment VARCHAR(1000) NULL
);

-- Some additional atribute to the final product
CREATE TABLE Enobling
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL UNIQUE,
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
-- todo create trigger on insert, update to check id fistSide>secondSide
CREATE TABLE Colouring
(
    id         TINYINT PRIMARY KEY AUTO_INCREMENT,
    firstSide  TINYINT NOT NULL,
    secondSide TINYINT NOT NULL,
    UNIQUE `bothSides`(`firstSide`, `secondSide`)
);

-- Role of the employee for the data to access
-- this is spring boot roles to ensure the authorization
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
    email       INT         NULL UNIQUE,
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
    NIP      CHAR(10)     NOT NULL UNIQUE, -- pl.wikipedia.org/wiki/Numer_identyfikacji_podatkowej
    clientId int          NULL UNIQUE,
    INDEX    compCliId_ind (clientId),
    FOREIGN KEY (clientId)
        REFERENCES Client (id)
        ON DELETE SET NULL
);

-- Order is equivalent for the calculation card and work card combined
-- todo Create trigger on insert or update Order to check if creationDate is lower than realizationDate or completionDate
CREATE TABLE `Order`
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    name                  VARCHAR(200) NOT NULL,
    netSize               INT          NULL,
    netSizeWidth          DOUBLE       NULL,
    netSizeHeight         DOUBLE       NULL,
    pages                 INT          NOT NULL,
    supervisor            INT          NOT NULL,
    client                INT          NOT NULL,
    creationDate          TIMESTAMP    NOT NULL,
    realizationDate       TIMESTAMP    NOT NULL, -- is the deadline date that we wish to serve the order
    bindingForm           INT          NOT NULL,
    bindery               INT          NOT NULL,
    folding               BOOL         NOT NULL,
    towerCut              BOOL         NOT NULL,
    imageURL              BIGINT       NULL,
    checked               BOOL         NOT NULL,
    designsNumberForSheet INT          NOT NULL,
    completionDate        TIMESTAMP    NULL,     -- this is completion date of the last WorkflowStageStop
    withdrawalDate        TIMESTAMP    NULL,
    comment               TEXT         NULL,

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
        REFERENCES Image (id)
);


CREATE TABLE OrderEnobling
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    enobling   INT  NOT NULL,
    `order`    INT  NOT NULL,
    bindery    INT  NOT NULL,
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
    typeId                   INT           NOT NULL,
    orderId                  INT           NOT NULL,
    grammage                 DOUBLE        NOT NULL,
    colours                  TINYINT       NOT NULL,
    circulation              INT           NOT NULL,
    stockCirculation         INT           NOT NULL,
    sheetNumber              INT           NOT NULL,
    comment                  VARCHAR(1000) NULL,
    printer                  INT           NOT NULL,
    platesQuantityForPrinter INT           NOT NULL,
    imposition               INT           NOT NULL,
    size                     INT           NULL,
    sizeWidth                DOUBLE        NULL,
    sizeHeight               DOUBLE        NULL,
    productionSize           INT           NULL,
    productionSizeWidth      DOUBLE        NULL,
    productionSizeHeight     DOUBLE        NULL,


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

-- WorkflowStage with id 1 is the last workflow stage and completes the Graph edges.
CREATE TABLE WorkflowStage
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
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

CREATE TABLE WorkflowDirGraph
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    creationTime TIMESTAMP    NOT NULL,
    changedTime  TIMESTAMP    NULL,
    name         VARCHAR(300) NOT NULL UNIQUE,
    comment      VARCHAR(500) NULL
);

CREATE TABLE WorkflowDirEdge
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    V1      INT NOT NULL,
    V2      INT NOT NULL,
    graphId INT NOT NULL,

    INDEX   v1WorkFlowSt_ind (V1),
    FOREIGN KEY (V1)
        REFERENCES WorkflowStage (id)
        ON DELETE CASCADE,
    INDEX   v2WorkFlowSt_ind (V2),
    FOREIGN KEY (V2)
        REFERENCES WorkflowStage (id)
        ON DELETE CASCADE,
    INDEX   graphId_ind (graphId),
    FOREIGN KEY (graphId)
        REFERENCES WorkflowDirGraph (id)
        ON DELETE CASCADE
);

-- WorkflowStageStop is kind of sequential task that is going to be completed by worker
CREATE TABLE WorkflowStageStop
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    comment        VARCHAR(500) NULL,
    createTime     TIMESTAMP    NOT NULL,
    assignTime     TIMESTAMP    NULL,
    completionTime TIMESTAMP    NULL,
    worker         INT          NULL,
    `order`        INT          NOT NULL,
    workflowEdgeId INT          NOT NULL,

    INDEX          wssWorker_ind (worker),
    FOREIGN KEY (worker)
        REFERENCES Worker (personId)
        ON DELETE SET NULL,
    INDEX          wssOrder_ind (`order`),
    FOREIGN KEY (`order`)
        REFERENCES `Order` (id)
        ON DELETE CASCADE,
    INDEX          wssWorkDEdge_ind (workflowEdgeId),
    FOREIGN KEY (workflowEdgeId)
        REFERENCES WorkflowDirEdge (id)
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