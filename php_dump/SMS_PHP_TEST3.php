<?php

$str = ser_version();
echo "Version: $str"; 
ser_open( "COM5", 9600, 8, "None", "1", "None" ); 
if (ser_isopen()) echo "Port is open!";
ser_setDTR( True ); 
ser_write("AT+CMGF=1\r\n"); 
sleep(1); // wait a while
$str = ser_read();
echo $str; 
ser_write("AT+CMGL=\"ALL\"\r\n"); 
sleep(2); // wait a while, if list is long we must wait longer
$str = ser_read();
echo $str; 

?>