# Module printing-house-server

The server app that manages business information
for printing house. Clients can interact with it via REST API.

All events are saved in a directed acyclic graph. WorkflowStage is a vertex and WorkflowStageStop is meant to be weighted edge.
User have to create template route first with WorkflowDirGraph and then choose it for the specific Order.