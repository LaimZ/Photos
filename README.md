
Photos
=========

Java, jdbc OracleSQL, ApacheCLI

This is program is photo storege, wich allows you add photos (in .jpg format) into OracleSQL Database, link them with users, albums, note them with descriptions and tags. Program uses command line interface (see below).

Command Line Interface
----------------------

Just example:
```
getusers
setuser user=Lena
getalbums
setalbum album="Lena's_Summer"
getphotos
download

upload file="1.jpg" name="Lena_Morning" tags="Lena, Morning"
```

Some notes:
-----------

Library ojdbc14_g.jar for DAO can be found at C:\oracle10\jdbc\lib
Other libraries can be fount at lib/ folder
