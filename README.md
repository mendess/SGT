# SGT
Shift Management System. Project for Software Systems Development course (DSS)

This course requires de creation of models in UML that describe the structure of the aplication, which in turn is developed in Java.

All models can be seen in screenshots saved in the [Report/modelacao](/Report/modelacao) directory. The Visual Paradigm file with them is in the [Report/VPP](/Report/VPP) folder.

Due to the fact that I'm from Portugal, most things are in portuguese. At the end of this file will be a dictionary of commonly used words.

The software developed is a shift management system where students can enroll in Courses so that later the Admin (*Diretor de Curso*) can assign them a shift. Shift assignment is done automatically, at the DC's command.

## Features:
 - Teachers can mark students as present or absent
 - The teacher responsible for each course can see which students have a absence rate over 25%
 - The teacher responsible can add or remove students from a shift
 - Students can make shift exchange requests and accept these same requests
 - Some students (designated: Special status students) can change shift without trading with another student
 - The Admin can disable shift exchanges
 - etc..

The whole system is supported by Relational Database, developed with MySQL.

-----------------

## Dictionary

| Portuguese | English |
| ---------- | ------- |
| Utlizador | User |
| Aluno | Student |
| Aluno Especial<br/>Aluno com estatuto especial | Special Status Student |
| Docente | Teacher |
| Coordenador | Teacher responsible for one course |
| Diretor de Curso | Admin |
| Turno | Shift |
| Aula | Class |
| UC | Course<br/>Curricular Unit |
| Pedido | Request |
| Troca | Exchange |
| Dia | Day |
