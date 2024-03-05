CREATE TABLE Size
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(50) NULL UNIQUE,
    width  DOUBLE      NOT NULL,
    heigth DOUBLE      NOT NULL,
    constraint formatSize
        unique (width, heigth)
);

CREATE TABLE ImpositionType
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE Printer
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(100) NOT NULL UNIQUE,
    digest VARCHAR(4)   NOT NULL UNIQUE
);

CREATE TABLE PaperType
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(200) NOT NULL UNIQUE,
    shortName VARCHAR(30)  NOT NULL UNIQUE
);

CREATE TABLE Image
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    url          TEXT          NOT NULL,
    imageComment VARCHAR(1000) NULL
);

CREATE TABLE Enobling
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NULL
);

CREATE TABLE Punch
(
    enoblingId INT PRIMARY KEY,
    FOREIGN KEY (enoblingId)
        REFERENCES Enobling (id)
        ON DELETE CASCADE
);

create index punchEnoblingId_ind
    on Punch (enoblingId);

CREATE TABLE UVVarnish
(
    enoblingId INT PRIMARY KEY,
    FOREIGN KEY (enoblingId)
        REFERENCES Enobling (id)
        ON DELETE CASCADE
);

create index UVVarnEnoblingId_ind
    on UVVarnish (enoblingId);

CREATE TABLE Colouring
(
    id         TINYINT PRIMARY KEY AUTO_INCREMENT,
    firstSide  TINYINT NOT NULL,
    secondSide TINYINT NOT NULL,
    constraint bothSides
        unique (firstSide, secondSide)
);

CREATE TABLE Role
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE Email
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(600) NOT NULL UNIQUE
);

CREATE TABLE Person
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(200) NOT NULL,
    surname     VARCHAR(300) NOT NULL,
    pseudoPESEL CHAR(11)     NOT NULL UNIQUE
);

CREATE TABLE BindingForm
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE Bindery
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE Employee
(
    personId      INT PRIMARY KEY,
    email         INT      NOT NULL UNIQUE,
    password      CHAR(68) NOT NULL,
    activeAccount BOOL     NOT NULL,
    employed      BOOL     NOT NULL,

    FOREIGN KEY (email)
        REFERENCES Email (id)
        ON DELETE CASCADE,
    FOREIGN KEY (personId)
        REFERENCES Person (id)
        ON DELETE CASCADE
);

create index empEmail_ind
    on Employee (email);

create index empPersonId_ind
    on Employee (personId);

CREATE TABLE Salesman
(
    personId INT PRIMARY KEY,
    FOREIGN KEY (personId)
        REFERENCES Employee (personId)
        ON DELETE CASCADE
);

create index slsmnPersonId_ind
    on Salesman (personId);

CREATE TABLE Client
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    email       INT         NULL UNIQUE,
    phoneNumber VARCHAR(15) NULL UNIQUE,
    FOREIGN KEY (email)
        REFERENCES Email (id)
        ON DELETE SET NULL
);

create index cliEmail_ind
    on Client (email);

CREATE TABLE IndividualClient
(
    personId INT PRIMARY KEY,
    clientId INT NOT NULL UNIQUE,
    FOREIGN KEY (clientId)
        REFERENCES Client (id)
        ON DELETE CASCADE,
    FOREIGN KEY (personId)
        REFERENCES Person (id)
        ON DELETE CASCADE
);

create index indCliId_ind
    on IndividualClient (clientId);

create index inCliPerson_ind
    on IndividualClient (personId);

CREATE TABLE Company
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(500) NOT NULL,
    NIP      CHAR(10)     NOT NULL UNIQUE,
    clientId int          NULL UNIQUE,
    FOREIGN KEY (clientId)
        REFERENCES Client (id)
        ON DELETE SET NULL
);

create index compCliId_ind
    on Company (clientId);

CREATE TABLE `Order`
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    name                  VARCHAR(200) NOT NULL,
    netSize               INT          NOT NULL,
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
    FOREIGN KEY (netSize)
        REFERENCES Size (id)
        ON DELETE RESTRICT,
    FOREIGN KEY (supervisor)
        REFERENCES Salesman (personId)
        ON DELETE NO ACTION,
    FOREIGN KEY (client)
        REFERENCES Client (id)
        ON DELETE NO ACTION,
    FOREIGN KEY (bindingForm)
        REFERENCES BindingForm (id),
    FOREIGN KEY (bindery)
        REFERENCES Bindery (id),
    FOREIGN KEY (imageURL)
        REFERENCES Image (id)
);
create index netSize_ind
    on `Order` (netSize);
create index supervisor_ind
    on `Order` (supervisor);
create index ordCli_ind
    on `Order` (client);
create index ordBindForm_ind
    on `Order` (bindingForm);
create index ordBindery_ind
    on `Order` (bindery);
create index ordURLImg_ind
    on `Order` (imageURL);

CREATE TABLE OrderEnobling
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    enobling   INT  NOT NULL,
    `order`    INT  NOT NULL,
    bindery    INT  NOT NULL,
    annotation TEXT NULL,
    FOREIGN KEY (enobling)
        REFERENCES Enobling (id),
    FOREIGN KEY (`order`)
        REFERENCES `Order` (id)
        ON DELETE CASCADE,
    FOREIGN KEY (bindery)
        REFERENCES Bindery (id)
);

create index ordEnEn_ind
    on OrderEnobling (enobling);
create index ordEnOrd_ind
    on OrderEnobling (`order`);
create index ordEnBindery_ind
    on OrderEnobling (bindery);

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
    imposition               int           NOT NULL,
    size                     int           NOT NULL,
    productionSize           int           NOT NULL,
    FOREIGN KEY (typeId)
        REFERENCES PaperType (id),

    FOREIGN KEY (orderId)
        REFERENCES `Order` (id)
        ON DELETE CASCADE,
    FOREIGN KEY (colours)
        REFERENCES Colouring (id),
    FOREIGN KEY (printer)
        REFERENCES Printer (id),
    FOREIGN KEY (imposition)
        REFERENCES ImpositionType (id),
    FOREIGN KEY (size)
        REFERENCES Size (id),
    FOREIGN KEY (productionSize)
        REFERENCES Size (id)
);

create index potPaperType_ind
    on PaperOrderType (typeId);
create index potOrder_ind
    on PaperOrderType (orderId);
create index potColours_ind
    on PaperOrderType (colours);
create index potPrinter_ind
    on PaperOrderType (printer);
create index potImpositionType_ind
    on PaperOrderType (imposition);
create index potSize_ind
    on PaperOrderType (size);
create index potProdSize_ind
    on PaperOrderType (productionSize);

CREATE TABLE Worker
(
    personId INT PRIMARY KEY,
    FOREIGN KEY (personId)
        REFERENCES Employee (personId)
        ON DELETE CASCADE
);

create index wkrPrsnId_ind
    on Worker (personId);

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
    constraint empRoleUnique
        unique (empId, roleId)
);

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
    constraint workflowStageMgrUnique
        unique (workflowStage, employeeId)
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


    FOREIGN KEY (V1)
        REFERENCES WorkflowStage (id)
        ON DELETE CASCADE,

    FOREIGN KEY (V2)
        REFERENCES WorkflowStage (id)
        ON DELETE CASCADE,

    FOREIGN KEY (graphId)
        REFERENCES WorkflowDirGraph (id)
        ON DELETE CASCADE
);

create index v1WorkFlowSt_ind
    on WorkflowDirEdge (V1);
create index v2WorkFlowSt_ind
    on WorkflowDirEdge (V2);
create index graphId_ind
    on WorkflowDirEdge (graphId);

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
    FOREIGN KEY (worker)
        REFERENCES Worker (personId)
        ON DELETE SET NULL,
    FOREIGN KEY (`order`)
        REFERENCES `Order` (id)
        ON DELETE CASCADE,
    FOREIGN KEY (workflowEdgeId)
        REFERENCES WorkflowDirEdge (id)
);

create index wssWorker_ind
    on WorkflowStageStop (worker);
create index wssOrder_ind
    on WorkflowStageStop (`order`);
create index wssWorkDEdge_ind
    on WorkflowStageStop (workflowEdgeId);

CREATE TABLE PlatePrice
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    date    TIMESTAMP  NOT NULL,
    price   DEC(63, 2) NOT NULL,
    printer INT        NOT NULL,
    FOREIGN KEY (printer)
        REFERENCES Printer (id)
        ON DELETE CASCADE
);

create index dpPrinter_ind
    on PlatePrice (printer);

CREATE TABLE CalculationCard
(
    orderId     INT PRIMARY KEY,
    bindingCost DEC(63, 2) NOT NULL,
    enobling    DEC(63, 2) NOT NULL,
    otherCosts  DEC(63, 2) NOT NULL,
    transport   DEC(63, 2) NOT NULL,
    FOREIGN KEY (orderId)
        REFERENCES `Order` (id)
        ON DELETE CASCADE
);

create index ccOrderId_ind
    on CalculationCard (orderId);

CREATE TABLE PrintCost
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    orderId    INT        NOT NULL,
    printer    INT        NOT NULL,
    printCost  DEC(63, 2) NOT NULL,
    matrixCost DEC(63, 2) NOT NULL,
    FOREIGN KEY (orderId)
        REFERENCES CalculationCard (orderId)
        ON DELETE CASCADE,
    FOREIGN KEY (printer)
        REFERENCES Printer (id)
        ON DELETE NO ACTION
);

create index printCOrderId_ind
    on PrintCost (orderId);
create index printCPrinter_ind
    on PrintCost (printer);
