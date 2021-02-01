# Blockbased CSG Tool

## About
This is a simple proof of concept tool to show the possibility of combining a block based user interface with modeling. This tool enables you to create simple models using constructive solid geometry by clicking and dragging blocks around. 

The blocks can only connect in a certain way, indicated by their connection points. Furthermore they grow dynamically in size to accomodate the increasing nesting.

![Simple CSG Example](assets/example.png "Simple CSG Example")

## Installation

### Requirements
- Gradle
- Java 8

### Building
Clone the repository using git. (Or download the source code manually and extract it.)

`` git clone  https://git.haw-hamburg.de/aci876/blockcsg.git``

Move into the directory of the cloned repository.

``cd blockcsg/``

Now start Gradle and build the project.

``gradle build``


So far, this tool only works with Java 8.

## Usage

